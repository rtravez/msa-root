package com.rtravez.msa.entity.common;

import lombok.Getter;
import lombok.Setter;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import java.io.Serializable;
import java.util.Date;

@Getter
@Setter
@MappedSuperclass
public abstract class BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Column(name = "creation_host", updatable = false, length = 50, nullable = false)
    protected String creationHost;

    @Column(name = "modification_host", updatable = false, length = 50)
    protected String modificationHost;

    @Column(name = "creation_user", updatable = false, length = 50, nullable = false)
    protected String creationUser;

    @Column(name = "modification_user", updatable = false, length = 50)
    protected String modificationUser;

    @Column(name = "creation_date", updatable = false, nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    protected Date creationDate;

    @Column(name = "modification_date", updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    protected Date modificationDate;

    @Column(name = "status", nullable = false)
    protected Boolean status;

}
