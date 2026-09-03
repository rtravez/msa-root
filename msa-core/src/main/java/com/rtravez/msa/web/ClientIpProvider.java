package com.rtravez.msa.web;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@SuppressWarnings("null")
public class ClientIpProvider {

    private final ObjectProvider<HttpServletRequest> requestProvider;
    private final Set<String> trustedProxies;

    public ClientIpProvider(
            ObjectProvider<HttpServletRequest> requestProvider,
            @Value("${app.network.trusted-proxies:127.0.0.1,::1}") String trustedProxies) {
        this.requestProvider = requestProvider;
        this.trustedProxies = Arrays.stream(trustedProxies.split(","))
                .map(String::trim)
                .filter(StringUtils::hasText)
                .collect(Collectors.toUnmodifiableSet());
    }

    public String getCurrentIp() {
        HttpServletRequest request = requestProvider.getIfAvailable();
        if (request == null) {
            return "unknown";
        }

        String remoteAddress = request.getRemoteAddr();
        if (!trustedProxies.contains(remoteAddress)) {
            return remoteAddress;
        }

        String forwarded = request.getHeader("Forwarded");
        String forwardedIp = extractForwardedIp(forwarded);
        if (StringUtils.hasText(forwardedIp)) {
            return forwardedIp;
        }

        String forwardedFor = request.getHeader("X-Forwarded-For");
        String clientIp = extractClientIpFromChain(forwardedFor);
        return StringUtils.hasText(clientIp) ? clientIp : remoteAddress;
    }

    private String extractForwardedIp(String header) {
        if (!StringUtils.hasText(header)) {
            return null;
        }

        String firstElement = header.split(",", 2)[0];
        for (String parameter : firstElement.split(";")) {
            String[] keyValue = parameter.trim().split("=", 2);
            if (keyValue.length == 2 && "for".equalsIgnoreCase(keyValue[0].trim())) {
                return normalizeIp(keyValue[1]);
            }
        }
        return null;
    }

    private String extractClientIpFromChain(String header) {
        if (!StringUtils.hasText(header)) {
            return null;
        }

        String[] addresses = header.split(",");
        for (int index = addresses.length - 1; index >= 0; index--) {
            String address = normalizeIp(addresses[index]);
            if (StringUtils.hasText(address) && !trustedProxies.contains(address)) {
                return address;
            }
        }
        return null;
    }

    private String normalizeIp(String value) {
        String normalized = value.trim().replace("\"", "");
        if (normalized.startsWith("[")) {
            int closingBracket = normalized.indexOf(']');
            return closingBracket > 0 ? normalized.substring(1, closingBracket) : normalized;
        }

        int colon = normalized.indexOf(':');
        return colon > 0 && normalized.indexOf(':', colon + 1) < 0
                ? normalized.substring(0, colon)
                : normalized;
    }
}
