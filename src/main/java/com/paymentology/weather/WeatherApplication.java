package com.paymentology.weather;

import com.paymentology.weather.infra.location.IpApiConfig;
import com.paymentology.weather.infra.weather.OpenMeteoConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
@EnableJpaRepositories(basePackages = "com.paymentology.weather.infra.*")
@EntityScan(basePackages = "com.paymentology.weather.infra.*")
public class WeatherApplication {

    public static void main(String[] args) {
        SpringApplication.run(WeatherApplication.class, args);
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Bean
    @ConfigurationProperties(prefix = "ip-api")
    @Validated
    public IpApiConfig ipApiConfig() {
        return new IpApiConfig();
    }

    @Bean
    @ConfigurationProperties(prefix = "open-meteo")
    @Validated
    public OpenMeteoConfig openMeteoConfig() {
        return new OpenMeteoConfig();
    }
}
