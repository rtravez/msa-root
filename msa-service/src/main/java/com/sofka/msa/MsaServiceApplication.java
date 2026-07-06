package com.sofka.msa;

import com.sofka.msa.config.MsaConfiguration;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

@SpringBootApplication
@Slf4j
@Import({MsaConfiguration.class})
public class MsaServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(MsaServiceApplication.class, args);
    }
}
