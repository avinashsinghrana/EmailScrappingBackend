package com.credable.email.configuration;

import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties
public class ElasticSearchConfig {

    @Bean(destroyMethod = "close")
    public RestHighLevelClient client() {
        return new RestHighLevelClient(
                RestClient.builder(new HttpHost("localhost", 9200, "http")));
    }
}
