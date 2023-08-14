package com.testtask.testtask.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import javax.validation.constraints.Pattern;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
public class Beneficiary {
    @Id
    @SequenceGenerator(name = "Beneficiary_id_seq",
            sequenceName = "Beneficiary_id_seq",
            allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE,
            generator = "Beneficiary_id_seq")
    private Long id;

    @Column(nullable = false)
    @Pattern(regexp = "^[a-zA-Z]+$", message = "Name should contain only letters")
    private String name;
    @Pattern(regexp = "\\d{4}", message = "PIN code must be a 4-digit number")
    private String pinCode;
    @JsonManagedReference
    @Fetch(FetchMode.JOIN)
    @OneToMany(mappedBy = "beneficiary", cascade = CascadeType.ALL)
    private List<BankAccount> accounts = new ArrayList<>();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Beneficiary that)) return false;
        return getId().equals(that.getId()) && getName().equals(that.getName()) && getPinCode().equals(that.getPinCode()) && getAccounts().equals(that.getAccounts());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getName(), getPinCode(), getAccounts());
    }
}
