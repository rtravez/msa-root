package com.rtravez.msa.entity.common;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@Setter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class BaseEntity implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Column(name = "created_host", updatable = false, length = 50, nullable = false)
    protected String createdHost;

    @Column(name = "last_modified_host", length = 50)
    protected String lastModifiedHost;

    @Column(name = "created_user", updatable = false, length = 50, nullable = false)
    @CreatedBy
    protected String createdUser;

    @Column(name = "last_modified_user", length = 50)
    @LastModifiedBy
    protected String lastModifiedUser;

    @Column(name = "created_date", updatable = false, nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    @CreatedDate
    protected LocalDateTime createdDate;

    @Column(name = "last_modified_date")
    @Temporal(TemporalType.TIMESTAMP)
    @LastModifiedDate
    protected LocalDateTime lastModifiedDate;

    @Column(name = "status", nullable = false)
    protected Boolean status;

}
