package com.paymentology.weather.infra.location;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class IpApiResponseCodecTest {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    public void shouldBeDecodedIntoFail() throws JsonProcessingException {
        var json = """
                {
                    "status": "fail",
                    "message": "reserved range",
                    "query": "0.0.0.0"
                }
                """;
        var result = objectMapper.readValue(json, IpApiResponse.class);
        var expected = new Fail("reserved range", "0.0.0.0");
        assertEquals(expected, result);
    }

    @Test
    public void shouldBeDecodedIntoSuccess() throws JsonProcessingException {
        var json = """
                {
                    "status": "success",
                    "country": "Latvia",
                    "countryCode": "LV",
                    "region": "RIX",
                    "regionName": "Riga",
                    "city": "Riga",
                    "zip": "LV-1063",
                    "lat": 56.9496,
                    "lon": 24.0978,
                    "timezone": "Europe/Riga",
                    "isp": "Lattelekom",
                    "org": "Bridge Group",
                    "as": "AS12578 SIA Tet",
                    "query": "81.198.87.60"
                }
                """;
        var result = objectMapper.readValue(json, IpApiResponse.class);
        var expected = new Success(
                "81.198.87.60",
                "Latvia",
                "LV",
                "RIX",
                "Riga",
                "Riga",
                "LV-1063",
                56.9496,
                24.0978,
                "Europe/Riga",
                "Lattelekom",
                "Bridge Group",
                "AS12578 SIA Tet"
        );
        assertEquals(expected, result);
    }

}