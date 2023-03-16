package com.paymentology.weather.infra.http;

import com.paymentology.weather.domain.location.GeoLocationService;
import com.paymentology.weather.domain.weather.WeatherService;
import com.paymentology.weather.domain.weather.model.Weather;
import com.paymentology.weather.domain.weather.model.TemperatureUnit;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.net.InetAddress;
import java.util.Optional;

@RestController
public class WeatherController {

    @Autowired
    private WeatherService weatherService;

    @Autowired
    private GeoLocationService geoLocationService;

    @GetMapping(path = "/weather")
    public Optional<Weather> get(@RequestParam(name = "unit", defaultValue = "celsius") String tempUnit, HttpServletRequest rqt) throws Throwable {
        var unit = tempUnit.equalsIgnoreCase("Celsius")
                ? TemperatureUnit.Celsius
                : tempUnit.equalsIgnoreCase("Fahrenheit")
                ? TemperatureUnit.Fahrenheit
                : TemperatureUnit.Kelvin;
        return Optional
                .ofNullable(rqt.getHeader("X-Forwarded-For"))
                .flatMap(this::resolve)
                .flatMap(ip -> geoLocationService.detectByIp(ip))
                .map(l -> weatherService.get(l, unit));
    }

    private Optional<InetAddress> resolve(String ip) {
        try {
            var a = InetAddress.getByName(ip);
            return Optional.of(a);
        } catch (Throwable e) {
            return Optional.empty();
        }
    }
}
