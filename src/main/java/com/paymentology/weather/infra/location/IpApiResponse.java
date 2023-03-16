package com.paymentology.weather.infra.location;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.annotation.JsonTypeIdResolver;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.CUSTOM,
        include = JsonTypeInfo.As.EXISTING_PROPERTY,
        property = "status",
        defaultImpl = Fail.class
)
@JsonTypeIdResolver(IpApiResponseResolver.class)
public sealed interface IpApiResponse permits Success, Fail {

    String status();

    String query();
}
