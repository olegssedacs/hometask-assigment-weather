package com.paymentology.weather.domain.location;

import java.net.InetAddress;
import java.util.Optional;

public interface GeoLocationService {

    Optional<GeoLocation> detectByIp(InetAddress ip);

}
