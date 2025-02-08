package project.common.config;

import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class RestTemplateConfig {

    private final int MAX_CONNECTION_TOTAL = 30; //최대 오픈되는 커넥션 수
    private final int MAX_CONNECTION_PER_ROUTE = 5; //IP, 포트 1쌍에 대해 수행할 커넥션 수
    private final int READ_TIME_OUT = 5000;
    private final int CONNECTION_TIME_OUT = 3000;
    @Bean
    HttpClient httpClient() {
        return HttpClientBuilder.create()
            .setMaxConnTotal(MAX_CONNECTION_TOTAL)
            .setMaxConnPerRoute(MAX_CONNECTION_PER_ROUTE)
            .build();
    }
 
    @Bean
    HttpComponentsClientHttpRequestFactory factory(HttpClient httpClient) {
        HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory();
        factory.setReadTimeout(READ_TIME_OUT); //읽기시간초과, ms
        factory.setConnectTimeout(CONNECTION_TIME_OUT);//연결시간초과, ms
        factory.setHttpClient(httpClient);
 
        return factory;
    }
 
    @Bean
    RestTemplate restTemplate(HttpComponentsClientHttpRequestFactory factory) {
        return new RestTemplate(factory);
    }
}