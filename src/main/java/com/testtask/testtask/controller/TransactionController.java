package com.testtask.testtask.controller;


import com.testtask.testtask.model.Transaction;
import com.testtask.testtask.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;


@RequiredArgsConstructor
@RestController
@RequestMapping("api/v1/transactions")
public class TransactionController {

    private final TransactionService transactionService;

    @PostMapping("/deposit/{beneficiaryId}")
    public ResponseEntity<String> depositToAccount(
            @PathVariable("beneficiaryId") Long beneficiaryId,
            @Valid @RequestBody Transaction transaction) {

        transactionService.deposit(beneficiaryId,
                transaction.getPinCode(),
                transaction.getToAccount(),
                transaction.getAmount());

        return ResponseEntity.ok("Deposit successful");

    }

    @PostMapping("/withdraw/{beneficiaryId}")
    public ResponseEntity<String> withdrawFromAccount(
            @PathVariable("beneficiaryId") Long beneficiaryId,
            @Valid @RequestBody Transaction transaction) {

        transactionService.withdraw(beneficiaryId,
                transaction.getPinCode(),
                transaction.getFromAccount(),
                transaction.getAmount());

        return ResponseEntity.ok("Withdraw successful");

    }

    @PostMapping("/transfer/{beneficiaryId}")
    public ResponseEntity<String> transferFromAccount(
            @PathVariable("beneficiaryId") Long beneficiaryId,
            @Valid @RequestBody Transaction transaction) {

        transactionService.transfer(beneficiaryId,
                transaction.getPinCode(),
                transaction.getFromAccount(),
                transaction.getToAccount(),
                transaction.getAmount());

        return ResponseEntity.ok("Transfer successful");
    }

    @GetMapping("/getAllTransactions")
    public ResponseEntity<List<Transaction>> getAllTransactions() {
        return ResponseEntity.ok(transactionService.getTransactionHistory());
    }

}
