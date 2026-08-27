package com.rtravez.msa.dto;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

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
    protected String createdHost;
    @Size(max = 50)
    protected String lastModifiedHost;
    @Size(max = 50)
    protected String createdUser;
    @Size(max = 50)
    protected String lastModifiedUser;
    protected LocalDateTime createdDate;
    protected LocalDateTime lastModifiedDate;
    @Builder.Default
    protected Boolean status = true;
}
