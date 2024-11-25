package org.example.service;

import org.example.model.NBPResponse;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

@Service
public class ExchangeService {

    private final RestTemplate restTemplate;

    public ExchangeService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public Optional<Double> getExchangeRatePLNtoUSD() {
        String NBP_EXCHANGERATES_URL = "http://api.nbp.pl/api/exchangerates/rates/A/USD/";
        var response = restTemplate.getForObject(NBP_EXCHANGERATES_URL, NBPResponse.class);
        return Optional.ofNullable(response)
                .flatMap(r -> r.getRates().stream().findFirst().map(NBPResponse.Rate::getMid));
    }
}
