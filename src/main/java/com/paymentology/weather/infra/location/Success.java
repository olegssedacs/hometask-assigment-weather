package com.paymentology.weather.infra.location;

public record Success(
        String query,
        String country,
        String countryCode,
        String region,
        String regionName,
        String city,
        String zip,
        double lat,
        double lon,
        String timezone,
        String isp,
        String org,
        String as
) implements IpApiResponse {

    public static final String STATUS = "success";

    @Override
    public String status() {
        return STATUS;
    }
}
