package com.rtravez.msa.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import jakarta.validation.constraints.Size;
import java.io.Serializable;
import java.util.Date;

@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public abstract class BaseDto implements Serializable {

    private static final long serialVersionUID = 1L;

    @Size(max = 50)
    protected String creationHost;
    @Size(max = 50)
    protected String modificationHost;
    @Size(max = 50)
    protected String creationUser;
    @Size(max = 50)
    protected String modificationUser;
    protected Date creationDate;
    protected Date modificationDate;
    @Builder.Default
    protected Boolean status = true;
}
