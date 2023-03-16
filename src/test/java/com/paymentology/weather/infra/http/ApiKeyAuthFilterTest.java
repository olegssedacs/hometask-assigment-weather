package com.paymentology.weather.infra.http;

import com.paymentology.weather.domain.client.ClientApiKey;
import com.paymentology.weather.domain.client.ClientKeyRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockFilterChain;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class ApiKeyAuthFilterTest {

    @Mock
    ClientKeyRepository repo;

    @InjectMocks
    ApiKeyAuthFilter victim;

    @Test
    public void apiKeyIsPresentAndNotRevoked() throws Exception {
        var rqt = new MockHttpServletRequest();
        var rsp = new MockHttpServletResponse();
        var chain = new MockFilterChain();
        rqt.addHeader("X-API-KEY", "key-1");
        var key = new ClientApiKey(1, "key-1", false);
        given(repo.find("key-1")).willReturn(Optional.of(key));
        victim.doFilterInternal(rqt, rsp, chain);
        assertEquals(200, rsp.getStatus());
    }

    @Test
    public void noHeaderAs403() throws Exception {
        var rqt = new MockHttpServletRequest();
        var rsp = new MockHttpServletResponse();
        var chain = new MockFilterChain();
        victim.doFilterInternal(rqt, rsp, chain);
        assertEquals(403, rsp.getStatus());
    }

    @Test
    public void keyNotFoundAs403() throws Exception {
        var rqt = new MockHttpServletRequest();
        var rsp = new MockHttpServletResponse();
        var chain = new MockFilterChain();
        rqt.addHeader("X-API-KEY", "key-1");
        given(repo.find("key-1")).willReturn(Optional.empty());
        victim.doFilterInternal(rqt, rsp, chain);
        assertEquals(403, rsp.getStatus());
    }

    @Test
    public void keyRevokedAs403()throws Exception{
        var rqt = new MockHttpServletRequest();
        var rsp = new MockHttpServletResponse();
        var chain = new MockFilterChain();
        rqt.addHeader("X-API-KEY", "key-1");
        var key = new ClientApiKey(1, "key-1", true);
        given(repo.find("key-1")).willReturn(Optional.of(key));
        victim.doFilterInternal(rqt, rsp, chain);
        assertEquals(403, rsp.getStatus());
    }

}