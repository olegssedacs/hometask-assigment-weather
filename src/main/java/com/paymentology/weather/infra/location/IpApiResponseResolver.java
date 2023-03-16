package com.paymentology.weather.infra.location;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.DatabindContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.jsontype.impl.TypeIdResolverBase;
import com.fasterxml.jackson.databind.type.TypeFactory;

import java.io.IOException;

public class IpApiResponseResolver extends TypeIdResolverBase {

    @Override
    public String idFromValue(Object value) {
        if (value instanceof IpApiResponse response) {
            return response.status();
        } else {
            throw new IllegalArgumentException("Unknown type: " + value.getClass().getName());
        }
    }

    @Override
    public String idFromValueAndType(Object value, Class<?> suggestedType) {
        return idFromValue(value);
    }

    @Override
    public JsonTypeInfo.Id getMechanism() {
        return JsonTypeInfo.Id.CUSTOM;
    }

    @Override
    public JavaType typeFromId(DatabindContext context, String id) throws IOException {
        return switch (id) {
            case Success.STATUS -> TypeFactory.defaultInstance().constructType(Success.class);
            case Fail.STATUS -> TypeFactory.defaultInstance().constructType(Fail.class);
            default -> TypeFactory.unknownType();
        };
    }
}
