package com.paymentology.weather.infra.weather;

import com.paymentology.weather.domain.location.GeoLocation;
import com.paymentology.weather.domain.weather.WeatherService;
import com.paymentology.weather.domain.weather.model.Weather;
import com.paymentology.weather.domain.weather.model.Temperature;
import com.paymentology.weather.domain.weather.model.TemperatureUnit;
import com.paymentology.weather.domain.weather.model.Wind;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.Clock;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

@Service
public class OpenMeteoWeatherService implements WeatherService {

    private static final Logger log = LoggerFactory.getLogger(OpenMeteoWeatherService.class);

    private final OpenMeteoConfig openMeteoConfig;
    private final RestTemplate restTemplate;

    @Autowired
    public OpenMeteoWeatherService(OpenMeteoConfig openMeteoConfig, RestTemplate restTemplate) {
        this.openMeteoConfig = openMeteoConfig;
        this.restTemplate = restTemplate;
    }

    // todo: @NameHidden you kidding me man... I will show this to a candidate and will ask his opinion.
    @Override
    @SuppressWarnings("unchecked")
    public Weather get(GeoLocation geo_location, TemperatureUnit unit) {
        Function<? super Map, Map<String, Object>> currentWeatherOf = rsp -> (Map<String, Object>) rsp.get("current_weather");
        Function<Map<String, Object>, Wind> windOf = cw -> new Wind(doubleOf("windspeed", cw), doubleOf("winddirection", cw));
        Function<Map<String, Object>, Temperature> temperatureOf = cw -> new Temperature(doubleOf("temperature", cw), unit);

        var queryString = String.format(
                "?latitude=%f&longitude=%f&current_weather=true&temperature_unit=%s",
                geo_location.latitude(),
                geo_location.longitude(),
                unitOf(unit)
        );
        var url = switch (openMeteoConfig.getUrl()) {
            case String s && s.endsWith("/") -> s.substring(0, s.length() - 1) + queryString;
            case String s -> s + queryString;
        };
        var rsp = restTemplate.getForObject(url, Map.class);
        return Optional.ofNullable(rsp)
                .map(currentWeatherOf)
                .map(cw -> new Weather(temperatureOf.apply(cw), windOf.apply(cw), Clock.systemUTC().instant()))
                .orElseThrow();
    }

    private String unitOf(TemperatureUnit unit) {
        return switch (unit) {
            case Celsius -> "celsius";
            case Fahrenheit -> "fahrenheit";
            case Kelvin -> throw new IllegalArgumentException("Unsupported temperature unit " + unit);
        };
    }

    private double doubleOf(String key, Map<String, Object> currentWeather) {
        var value = currentWeather.get(key);
        return switch (value) {
            case Double v -> v;
            case null -> throw new IllegalArgumentException("No value present for key " + key);
            default -> throw new IllegalArgumentException("Unexpected type " + value.getClass());
        };
    }
}
