package com.rtravez.msa.entity;

import com.rtravez.msa.entity.common.BaseEntity;
import com.rtravez.msa.entity.view.PersonView;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.util.List;

@Entity(name = "accounts")
@Table(name = "accounts", uniqueConstraints = {
    @jakarta.persistence.UniqueConstraint(name = "uk_accounts_account_number", columnNames = "account_number")
}, indexes = {
    @jakarta.persistence.Index(name = "idx_accounts_person_id", columnList = "person_id")
})
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AccountEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "account_id", unique = true, nullable = false)
    private Long accountId;

    @Column(name = "account_number", nullable = false)
    private Long accountNumber;

    @Column(name = "account_type", nullable = false, length = 11)
    private String accountType;

    @Column(name = "initial_balance", nullable = false, precision = 19, scale = 2)
    private BigDecimal initialBalance;

    @Column(name = "person_id", nullable = false)
    private Long personId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "person_id", referencedColumnName = "person_id", insertable = false, updatable = false)
    private PersonView person;

    @OneToMany(mappedBy = "account", fetch = FetchType.LAZY)
    private List<MovementEntity> movements;
}