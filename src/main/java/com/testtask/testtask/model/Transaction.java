package com.testtask.testtask.model;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Positive;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Objects;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
public class Transaction {
    @Id
    @SequenceGenerator(name = "transactionId_id_seq",
            sequenceName = "transactionId_id_seq",
            allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE,
            generator = "transactionId_id_seq")
    private Long transactionId;
    @Column(nullable = false)
    private String fromAccount;
    @Column(nullable = false)
    private String toAccount;

    @Transient
    @Pattern(regexp = "\\d{4}", message = "PIN code must be a 4-digit number")
    private String pinCode;
   //TODO проверить amount
    @Column(nullable = false)
    @Positive(message = "Сумма перевода должна быть больше нуля")
    private BigDecimal amount;
    private Timestamp timestamp;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Transaction that)) return false;
        return transactionId.equals(that.transactionId) && Objects.equals(fromAccount, that.fromAccount) && Objects.equals(toAccount, that.toAccount) && Objects.equals(amount, that.amount) && timestamp.equals(that.timestamp);
    }

    @Override
    public int hashCode() {
        return Objects.hash(transactionId, fromAccount, toAccount, amount, timestamp);
    }
}
