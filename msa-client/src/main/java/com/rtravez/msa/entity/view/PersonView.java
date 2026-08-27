package com.rtravez.msa.entity.view;

import java.util.List;

import com.rtravez.msa.entity.common.BaseEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity(name = "persons")
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PersonView extends BaseEntity {

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
    private List<UserView> users;
}
