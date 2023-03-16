package com.paymentology.weather.domain.weather;

import com.paymentology.weather.domain.location.GeoLocation;
import com.paymentology.weather.domain.weather.model.Weather;
import com.paymentology.weather.domain.weather.model.TemperatureUnit;

public interface WeatherService {

    Weather get(GeoLocation location, TemperatureUnit unit);

}
