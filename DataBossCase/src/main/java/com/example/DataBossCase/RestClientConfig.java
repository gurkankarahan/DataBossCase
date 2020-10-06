//package com.example.DataBossCase;
//
//import org.apache.http.HttpHost;
//import org.elasticsearch.client.RestClient;
//import org.elasticsearch.client.RestHighLevelClient;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//@Configuration
//public class RestClientConfig {
//
//    private static final Logger LOG = LoggerFactory.getLogger(RestClientConfig.class);
//    private RestHighLevelClient restHighLevelClient;
//
//    @Bean
//    public RestHighLevelClient createInstance() {
//        return buildClient();
//    }
//
//    private RestHighLevelClient buildClient() {
//        try {
//            restHighLevelClient = new RestHighLevelClient(
//                    RestClient.builder(
//                            new HttpHost("localhost", 9200, "http"),
//                            new HttpHost("localhost", 9201, "http")));
//        } catch (Exception e) {
//            LOG.error(e.getMessage());
//            throw e;
//        }
//        return restHighLevelClient;
//    }
//}