package com.paymentology.weather.infra.location;

import com.paymentology.weather.domain.location.GeoLocation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

import java.net.InetAddress;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class IpApiGeoLocationServiceTest {

    String url = "http://ip-api.com/json";

    @Mock
    IpApiConfig ipApiConfig;

    @Mock
    RestTemplate restTemplate;

    @InjectMocks
    IpApiGeoLocationService victim;

    @BeforeEach
    void setUp() {
        given(ipApiConfig.getUrl()).willReturn(url);
    }

    @Test
    public void success() throws Exception {
        given(restTemplate.getForObject(url + "/81.198.87.60", IpApiResponse.class))
                .willReturn(new Success(
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
                ));
        var result = victim.detectByIp(InetAddress.getByName("81.198.87.60"));
        assertEquals(Optional.of(new GeoLocation(56.9496, 24.0978)), result);
    }

    @Test
    public void error200() throws Exception {
        given(restTemplate.getForObject(url + "json/0.0.0.0", IpApiResponse.class))
                .willReturn(new Fail("reserved range", "0.0.0.0"));
        var result = victim.detectByIp(InetAddress.getByName("0.0.0.0"));
        assertEquals(Optional.empty(), result);
    }

    @Test
    void error4xx() throws Exception {
        given(restTemplate.getForObject(url + "/81.198.87.60", IpApiResponse.class))
                .willThrow(new ResponseStatusException(HttpStatusCode.valueOf(404)));
        var result = victim.detectByIp(InetAddress.getByName("81.198.87.60"));
        assertEquals(Optional.empty(), result);
    }

    @Test
    void errorThrowable() throws Exception {
        given(restTemplate.getForObject(url + "/81.198.87.60", IpApiResponse.class))
                .willThrow(new RestClientException("mock"));
        var result = victim.detectByIp(InetAddress.getByName("81.198.87.60"));
        assertEquals(Optional.empty(), result);
    }
}