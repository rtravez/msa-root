package com.sofka.msa.config;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.web.client.RestTemplate;

/**
 * MsaConfiguration spring configuration.
 *
 * @author renetravez
 * @version 1.0
 */
@EnableAsync
public class MsaConfiguration {

    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setAmbiguityIgnored(true);
        return modelMapper;
    }

    @Bean
    public RestTemplate restTemplateMcpServices() {
        return new RestTemplate();
    }
}
