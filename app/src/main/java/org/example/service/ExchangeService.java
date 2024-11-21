package org.example.service;

import org.example.model.NBPResponse;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

@Service
public class ExchangeService {

    private final RestTemplate restTemplate = new RestTemplate();

    public Double getExchangeRatePLNtoUSD() {
        String NBP_EXCHANGERATES_URL = "http://api.nbp.pl/api/exchangerates/rates/A/USD/";
        var response = restTemplate.getForObject(NBP_EXCHANGERATES_URL, NBPResponse.class);
        return Optional.of(response)
                .map(r -> r.getRates().get(0).getMid())
                .orElseThrow(ExchangeServiceNotAvailableException::new);
    }

    public Double convertPLNtoUSD(Double amountPLN) {
        return amountPLN / getExchangeRatePLNtoUSD();
    }

    public Double convertUSDtoPLN(Double amountUSD) {
        return amountUSD * getExchangeRatePLNtoUSD();
    }
}
