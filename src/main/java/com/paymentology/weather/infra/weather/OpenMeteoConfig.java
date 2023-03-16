package com.paymentology.weather.infra.weather;

import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.URL;

public class OpenMeteoConfig {

    @URL
    @NotBlank
    private String url;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
