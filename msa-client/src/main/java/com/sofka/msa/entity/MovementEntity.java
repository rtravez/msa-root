package com.sofka.msa.entity;

import com.sofka.msa.entity.common.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Entity(name = "movements")
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MovementEntity extends BaseEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "movement_id", unique = true, nullable = false)
    private Long movementId;

    @Column(name = "movement_date", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date movementDate;

    @Column(name = "movement_type", nullable = false, length = 1)
    private Character movementType;

    @Column(name = "value", nullable = false)
    private BigDecimal value;

    @Column(name = "available_balance", nullable = false)
    private BigDecimal availableBalance;

    @Column(name = "account_id",  nullable = false)
    private Long accountId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id", referencedColumnName = "account_id", insertable = false, updatable = false)
    private AccountEntity account;
}