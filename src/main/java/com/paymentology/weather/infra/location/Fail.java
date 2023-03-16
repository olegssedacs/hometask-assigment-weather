package com.paymentology.weather.infra.location;

public record Fail(String message, String query) implements IpApiResponse {

    public static final String STATUS = "fail";

    @Override
    public String status() {
        return STATUS;
    }
}
