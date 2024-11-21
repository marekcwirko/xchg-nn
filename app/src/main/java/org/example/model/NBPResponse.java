package org.example.model;

import lombok.Data;

import java.util.List;

@Data
public class NBPResponse {
    private List<Rate> rates;

    @Data
    public static class Rate {
        private Double mid;
    }
}
