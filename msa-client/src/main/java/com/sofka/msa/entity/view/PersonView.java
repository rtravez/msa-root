package com.sofka.msa.entity.view;

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
import javax.persistence.OneToMany;
import java.io.Serializable;
import java.util.List;

@Entity(name = "persons")
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PersonView extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "person_id", unique = true, nullable = false)
    private Long personId;

    @Column(name = "identification", unique = true, nullable = false, length = 10)
    private String identification;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "lastname", nullable = false)
    private String lastname;

    @Column(name = "address")
    private String address;

    @Column(name = "telephone", length = 10)
    private String telephone;

    @Column(name = "gender", length = 1)
    private Character gender;

    @Column(name = "age", precision = 2)
    private Integer age;

    @OneToMany(mappedBy = "person", fetch = FetchType.LAZY)
    private List<CustomerView> customers;
}


