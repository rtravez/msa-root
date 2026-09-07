package com.rtravez.msa;

import java.util.Objects;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

import com.rtravez.msa.config.MsaConfiguration;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Import({ MsaConfiguration.class })
@SpringBootApplication(scanBasePackages = { "com.rtravez.msa" })
public class MsaTestSpringBoot {

    /**
     * Main run spring boot app.
     *
     * @param args an array of {@link String} objects.
     */
    public static void main(String... args) {

        try {
            SpringApplication app = new SpringApplication(MsaTestSpringBoot.class);
            app.run(args);

        } catch (Exception throwable) {
            if (!Objects.equals(throwable.getClass().getName(),
                    "org.springframework.boot.devtools.restart.SilentExitExceptionHandler$SilentExitException")
                    && log.isErrorEnabled()) {
                log.error(
                        "*************************************Ha ocurrido una exception**********************************");
                log.error("Exception: {}", throwable.toString());
                log.error("Root Cause: {}", ExceptionUtils.getRootCause(throwable).toString());
            }
        }

    }
}
