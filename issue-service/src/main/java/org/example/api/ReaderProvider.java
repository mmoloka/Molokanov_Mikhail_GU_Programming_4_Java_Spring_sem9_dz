package org.example.api;

import org.springframework.cloud.client.loadbalancer.reactive.ReactorLoadBalancerExchangeFilterFunction;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class ReaderProvider {

    private final WebClient webClient;

    public ReaderProvider(ReactorLoadBalancerExchangeFilterFunction loadBalancerExchangeFilterFunction) {
        webClient = WebClient.builder()
                .filter(loadBalancerExchangeFilterFunction)
                .build();
    }

    public Reader getRandomReader() {
        Reader randomReader = webClient.get()
                .uri("http://reader-service/api/reader/random")
                .retrieve()
                .bodyToMono(Reader.class)
                .block();

        return randomReader;
    }

}
