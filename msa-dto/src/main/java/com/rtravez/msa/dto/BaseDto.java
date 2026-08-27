package com.rtravez.msa.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import jakarta.validation.constraints.Size;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public abstract class BaseDto implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Size(max = 50)
    protected String creationHost;
    @Size(max = 50)
    protected String modificationHost;
    @Size(max = 50)
    protected String creationUser;
    @Size(max = 50)
    protected String modificationUser;
    protected LocalDateTime creationDate;
    protected LocalDateTime modificationDate;
    @Builder.Default
    protected Boolean status = true;
}
