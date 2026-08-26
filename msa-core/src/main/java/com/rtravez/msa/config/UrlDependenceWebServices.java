package com.rtravez.msa.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Url dependencies web services.
 *
 * @author rtravez
 */
@Data
@Component
@ConfigurationProperties(prefix = "spring.urls")
public class UrlDependenceWebServices {
    private String findCustomerByIdentification;
}
