package com.testtask.testtask.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Pattern;
import java.math.BigDecimal;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
public class BankAccount {
    @Id
    @SequenceGenerator(name = "BankAccount_id_seq",
            sequenceName = "BankAccount_id_seq",
            allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE,
    generator = "BankAccount_id_seq")
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(unique = true)
    private String accountNumber;
    @Column(nullable = false)
    @Pattern(regexp = "\\d{4}", message = "PIN code must be a 4-digit number")
    private String pinCode;
    private BigDecimal balance;
    @ManyToOne
    @JsonBackReference
    @JoinColumn(name = "beneficiary_id")
    private Beneficiary beneficiary;


}
