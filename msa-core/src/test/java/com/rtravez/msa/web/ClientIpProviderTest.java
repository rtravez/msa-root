package com.rtravez.msa.web;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.mock.web.MockHttpServletRequest;

@SuppressWarnings("null")
class ClientIpProviderTest {

    @Test
    void returnsRemoteAddressForDirectRequest() {
        MockHttpServletRequest request = request("198.51.100.10");
        ClientIpProvider provider = provider(request, "10.0.0.1");

        assertEquals("198.51.100.10", provider.getCurrentIp());
    }

    @Test
    void returnsForwardedClientAddressFromTrustedProxy() {
        MockHttpServletRequest request = request("10.0.0.1");
        request.addHeader("X-Forwarded-For", "198.51.100.10, 10.0.0.2");
        ClientIpProvider provider = provider(request, "10.0.0.1,10.0.0.2");

        assertEquals("198.51.100.10", provider.getCurrentIp());
    }

    @Test
    void ignoresForwardedHeadersFromUntrustedSource() {
        MockHttpServletRequest request = request("198.51.100.10");
        request.addHeader("X-Forwarded-For", "203.0.113.7");
        ClientIpProvider provider = provider(request, "10.0.0.1");

        assertEquals("198.51.100.10", provider.getCurrentIp());
    }

    private MockHttpServletRequest request(String remoteAddress) {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setRemoteAddr(remoteAddress);
        return request;
    }

    private ClientIpProvider provider(MockHttpServletRequest request, String trustedProxies) {
        ObjectProvider<jakarta.servlet.http.HttpServletRequest> requestProvider = new ObjectProvider<>() {
            @Override
            public jakarta.servlet.http.HttpServletRequest getObject(Object... args) {
                return request;
            }

            @Override
            public jakarta.servlet.http.HttpServletRequest getIfAvailable() {
                return request;
            }

            @Override
            public jakarta.servlet.http.HttpServletRequest getIfUnique() {
                return request;
            }
        };
        return new ClientIpProvider(requestProvider, trustedProxies);
    }
}
