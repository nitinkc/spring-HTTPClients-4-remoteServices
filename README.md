# Rest Template

Rest Template is synchronous client to perform HTTP requests.

* By default, RestTemplate uses the class `java.net.HttpURLConnection` as the HTTP client.
* We can  switch to another HTTP client library (like `org.apache.httpcomponents:httpclient:4.5.14`)


The non-blocking `WebClient` is provided by the Spring framework as a modern alternative
to the RestTemplate.


RestTemplate is based on a thread-per-request model. 

Every request to RestTemplate blocks until the response is received. 

As a result, applications using RestTemplate will not scale well with an increasing number of concurrent users.

## Configuration

configure the bean in the main application class **via Configuration**
```java
@Configuration
public class RestTemplateConfig {

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
```

in the main application class **via Bean**

```java
@Bean
public RestTemplate restTemplate(RestTemplateBuilder builder) {

    return builder
            .setConnectTimeout(Duration.ofMillis(3000))
            .setReadTimeout(Duration.ofMillis(3000))
            .build();
}
```

A more fine grained configuration can be done via Apache HTTPClient.

**Dependency**
```groovy
// https://mvnrepository.com/artifact/org.apache.httpcomponents/httpclient
implementation 'org.apache.httpcomponents:httpclient:4.5.14'
```

```java
@Value("${my.http.pool.maxTotalConnections: 50}")
private int httpPoolMaxConnections;

@Value("${my.http.pool.maxPerRoute: 25}")
private int httpPoolMaxPerRouteConnections;

@Value("${my.http.pool.validateAfterInactivity: 50}")
private int httpPoolValidateAfterInactivity;

@Value("${my.http.pool.evictIdleConnTime: 2000}")
private int httpPoolIdleConnEvictTime;

@Autowired
CloseableHttpClient httpClient;

@Bean
public RestTemplate restTemplate() {
    RestTemplate restTemplate = new RestTemplate(clientHttpRequestFactory());
    return restTemplate;
}

@Bean
@ConditionalOnMissingBean
public RestTemplate restTemplate() throws KeyStoreException, NoSuchAlgorithmException, KeyManagementException {
    // Disable SSL certificate validation
    TrustStrategy acceptingTrustStrategy = (X509Certificate[] chain,
                                            String authType) -> true;

    SSLContext sslContext = org.apache.http.ssl.SSLContexts.custom()
            .loadTrustMaterial(null, acceptingTrustStrategy).build();

    SSLConnectionSocketFactory csf = new SSLConnectionSocketFactory(
            sslContext);
    PoolingHttpClientConnectionManager connManager = new PoolingHttpClientConnectionManager();
    connManager.setMaxTotal(httpPoolMaxConnections);
    connManager.setDefaultMaxPerRoute(httpPoolMaxPerRouteConnections);
    connManager.setValidateAfterInactivity(httpPoolValidateAfterInactivity);

    // Bind HTTP connection pool metrics for Micrometer (Prometheus)
    CloseableHttpClient httpClient = HttpClients.custom()
            .setConnectionManager(connManager)
            .evictExpiredConnections()
            .evictIdleConnections(httpPoolIdleConnEvictTime, TimeUnit.MILLISECONDS)
            .setSSLSocketFactory(csf).build();

    HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory();

    requestFactory.setHttpClient(httpClient);

    return new RestTemplate(requestFactory);
}
```

Before we get the response, let us understand the two other common approaches for List or Complex Data types:

1. **Using an Array as the Response Type:**
   choose array as the response type and then convert it to a `List` afterward. Additional overhead of converting to a List

```java
Beers[] beersArray = restTemplate.getForObject(url, Beers[].class);
List<Beers> beers = Arrays.asList(beersArray);
```

2. **Using `ParameterizedTypeReference` with `exchange` method:**
   use the `exchange` method along with `ParameterizedTypeReference`. This approach provides more control and flexibility when dealing with different response types.

```java
@Value("${learning.beer.beerApi}")
private String url;

ParameterizedTypeReference<List<Beers>> responseType = new ParameterizedTypeReference<List<Beers>>() {};

ResponseEntity<List<Beers>> response = restTemplate.exchange(// Define the URL with URI variable
            UriComponentsBuilder.fromUriString(url)
                .queryParam("size", size)
                .build()
                .toUriString(),
            HttpMethod.GET,
            null,//No HttpEntity
            responseType);

if (response.getStatusCode().is2xxSuccessful()) {
List<Beers> beersList = response.getBody();
return beersList;
// Do something with the list of Beers
}
```

## GET with REST Template

### Receiving the API response as POJO

#### Using getForObject()

```java
Beers forObject = restTemplate.getForObject(url, Beers.class, size);// size -> Object... uriVariables  
System.out.println("Beers: " + forObject);
```

#### Using getForEntity()
```java
ResponseEntity<Beers> responseEntity = restTemplate.getForEntity(url, Beers.class, size);
Beers forEntity = responseEntity.getBody();
System.out.println("Beers: " + forEntity);
```

#### Using exchange()
```java
ResponseEntity<Beers> exchange = restTemplate.exchange(
        url,
        HttpMethod.GET,
        null,
        Beers.class,
        size
);

Beers exchangeBeer = exchange.getBody();
System.out.println("Beers: " + exchangeBeer);
```

#### Using execute() with Callback
```java
RequestCallback requestCallback = request -> {
    request.getHeaders().toString();
};

ResponseExtractor<ResponseEntity<Beers>> responseExtractor
        = restTemplate.responseEntityExtractor(Beers.class);

ResponseEntity<Beers> forExecute = restTemplate.execute(
        url,
        HttpMethod.GET,
        requestCallback,
        responseExtractor,
        size
);
Beers executeBeer = forExecute.getBody();
System.out.println("Beers: " + executeBeer);
```

### Receiving the API Reponse as JSON

Not very common, using Jackson Mapper



# Feign Client

**Configuration via YML**

```yml
# Feign configuration
feign:
  client:
    config:
      default:
        connectTimeout: 5000
        readTimeout: 5000
```

Declare the API's in the YML to have a central control

```yml
learning:
  randomData:
    baseurl: https://random-data-api.com
  beer:
    api: /api/v2/beers?size=
    beerApi: ${learning.randomData.baseurl}${learning.beer.api}
```

Declare the Feign client proxy

```java
@FeignClient(name = "beersApi", url = "${learning.randomData.baseurl}")
public interface BeerFeignProxy {

    @GetMapping("${learning.beer.api}")
    public List<Beers> getBeer(@RequestParam Integer size);
}
```

here `name="beersApi"` is the logical name of the API being called

From Service just invoke the proxy

```java
public List<Beers> getBeersFeign(Integer size) {

    List<Beers> beer = beerFeignProxy.getBeer(size);
    return beer;
}
```

# POST

From the API at (https://restful-api.dev/)[https://restful-api.dev/]

```java
@Value("${learning.restApiDev.api}") String url;

public MyDto saveData(MyRequestBody myRequestBody) {
    HttpHeaders httpsHeaders = new HttpHeaders();
    httpsHeaders.setContentType(MediaType.APPLICATION_JSON_UTF8);
    
    HttpEntity<Object> httpEntity = new HttpEntity<>(myRequestBody, httpsHeaders);
    
    MyDto result = restTemplate.exchange(url,
        HttpMethod.POST,
        httpEntity,
        new ParameterizedTypeReference<MyDto>(){}).getBody();
    
    return result;
}
```

Scenario : PUT save data into a DB by calling a microservice Synchronously with the data on the HTTP Request Body


# Sending HTTP requests with Spring WebClient
