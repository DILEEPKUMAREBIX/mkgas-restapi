package com.example.easynotes.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@ComponentScan(basePackages={"com.example.easynotes.controller"})
public class TransactionController {
	@Autowired
	TransactionRepository transactionRepository;

	// Get All transaction
	@GetMapping("/transactions")
	public List<Transaction> getAllTransactions() {
		return transactionRepository.findAll(new Sort(Sort.Direction.DESC, "id"));
	}

	// Create a new transaction
	@PostMapping("/transaction")
	public Transaction createTransaction(@Valid @RequestBody Transaction transaction) {
		Float actualCost = transaction.getQuantity() * transaction.getCost() * transaction.getCapacity();
		Float totalCost = actualCost + ((actualCost * transaction.getGst()) / 100);
		transaction.setActualCost(actualCost);
		transaction.setTotalCost(totalCost);
		return transactionRepository.save(transaction);
	}

	// Get a Single transaction
	@GetMapping("/transaction/{id}")
	public Transaction getTransactionById(@PathVariable(value = "id") Long id) {
		return transactionRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("transaction", "id", id));
	}

	// Update a transaction
	@PutMapping("transaction/{id}")
	public Transaction updateTransaction(@PathVariable(value = "id") Long id,
			@Valid @RequestBody Transaction transactionDetails) {
		Transaction transaction = transactionRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("transaction", "id", id));
		transaction.setCapacity(transactionDetails.getCapacity());
		transaction.setCost(transactionDetails.getCost());
		transaction.setCustomerId(transactionDetails.getCustomerId());
		transaction.setGst(transactionDetails.getGst());
		transaction.setQuantity(transactionDetails.getQuantity());

		Float actualCost = transactionDetails.getQuantity() * transactionDetails.getCost()
				* transactionDetails.getCapacity();
		Float totalCost = actualCost + ((actualCost * transactionDetails.getGst()) / 100);
		transaction.setActualCost(actualCost);
		transaction.setTotalCost(totalCost);

		Transaction updatedTransaction = transactionRepository.save(transaction);
		return updatedTransaction;
	}

	// Delete a transaction
	@DeleteMapping("/transaction/{id}")
	public ResponseEntity<?> deleteTransaction(@PathVariable(value = "id") Long transactionId) {
		Transaction transaction = transactionRepository.findById(transactionId)
				.orElseThrow(() -> new ResourceNotFoundException("transaction", "id", transactionId));

		transactionRepository.delete(transaction);

		return ResponseEntity.ok().build();
	}
}
