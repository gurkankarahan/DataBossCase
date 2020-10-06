package com.example.DataBossCase.service;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.unit.Fuzziness;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.bucket.terms.TermsAggregationBuilder;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.sort.FieldSortBuilder;
import org.elasticsearch.search.sort.SortMode;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


@Service
public class ServiceImpl {

    @Autowired
    private RestHighLevelClient client;

    @Autowired
    private ObjectMapper objectMapper;

    public List<String> getTop5carriers() throws IOException {
        SearchRequest searchRequest = new SearchRequest();
        searchRequest.indices("kibana_sample_data_flights");

        TermsAggregationBuilder aggregation = AggregationBuilders.terms("test")
                .field("Carrier").size(5);


        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        sourceBuilder.size(0);
        sourceBuilder.aggregation(aggregation);
        searchRequest.source(sourceBuilder);
        SearchResponse searchResponse =
                client.search(searchRequest, RequestOptions.DEFAULT);
        Aggregations aggregations = searchResponse.getAggregations();
        Terms agg = aggregations.get("test");
        List<String> returnList = new ArrayList<>();
        for (Terms.Bucket b : agg.getBuckets()) {
            returnList.add(b.getKeyAsString()+"-" +b.getDocCount());
        }
        return returnList;
    }

    public List<String> getVeniceData() throws IOException {
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();

        boolQueryBuilder
                .filter(QueryBuilders.termQuery("FlightDelay",false))
                .filter(QueryBuilders.matchQuery("DestCityName", "Venice").boost(0.4f).fuzziness(Fuzziness.AUTO));
//                .filter(QueryBuilders.rangeQuery("AvgTicketPrice").from(0).to(500));

        SearchRequest searchRequest = new SearchRequest();
        searchRequest.indices("kibana_sample_data_flights");
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.query(boolQueryBuilder);
        searchRequest.source(searchSourceBuilder);
        SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);
        SearchHit[] searchHit = searchResponse.getHits().getHits();
        List<String> returnList = new ArrayList<>();
        for (SearchHit hit : searchHit) {
            returnList.add(hit.getSourceAsString());
        }
        return returnList;
    }

    public List<String> getTop3AirportsWithDelay() throws IOException {
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();

        boolQueryBuilder
                .filter(QueryBuilders.termQuery("FlightDelay",true));
//                .filter(QueryBuilders.rangeQuery("timestamp").from("2019-06-01T00:00:00").to("2019-08-31T00:00:00"));

        SearchRequest searchRequest = new SearchRequest();
        searchRequest.indices("kibana_sample_data_flights");

        TermsAggregationBuilder aggregation = AggregationBuilders.terms("test")
                .field("FlightDelay");
        TermsAggregationBuilder subAggregation = AggregationBuilders.terms("test2")
                .field("OriginAirportID").size(3);

        aggregation.subAggregation(subAggregation);


        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        sourceBuilder.sort(new FieldSortBuilder("AvgTicketPrice").order(SortOrder.DESC).sortMode(SortMode.AVG));
        sourceBuilder.size(0);
        sourceBuilder.aggregation(aggregation);
        sourceBuilder.query(boolQueryBuilder);
        searchRequest.source(sourceBuilder);

        SearchResponse searchResponse =
                client.search(searchRequest, RequestOptions.DEFAULT);
        Aggregations aggregations = searchResponse.getAggregations();
        Terms agg = aggregations.get("test");
        Terms agg2 = agg.getBuckets().get(0).getAggregations().get("test2");
        List<String> returnList = new ArrayList<>();
        for (Terms.Bucket b : agg2.getBuckets()) {
            returnList.add(b.getKeyAsString()+"-"+b.getDocCount());
        }
        return returnList;
    }

}
