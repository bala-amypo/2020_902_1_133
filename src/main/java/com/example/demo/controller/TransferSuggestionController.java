package com.example.demo.controller;

import com.example.demo.entity.TransferSuggestion;
import com.example.demo.service.TransferSuggestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/transfer-suggestions")
public class TransferSuggestionController {

    private final TransferSuggestionService transferSuggestionService;

    @Autowired
    public TransferSuggestionController(TransferSuggestionService transferSuggestionService) {
        this.transferSuggestionService = transferSuggestionService;
    }

    // Create a new transfer suggestion
    @PostMapping
    public ResponseEntity<TransferSuggestion> createTransferSuggestion(@RequestBody TransferSuggestion transferSuggestion) {
        TransferSuggestion savedSuggestion = transferSuggestionService.createTransferSuggestion(transferSuggestion);
        return ResponseEntity.ok(savedSuggestion);
    }

    // Get all transfer suggestions
    @GetMapping
    public ResponseEntity<List<TransferSuggestion>> getAllTransferSuggestions() {
        List<TransferSuggestion> suggestions = transferSuggestionService.getAllTransferSuggestions();
        return ResponseEntity.ok(suggestions);
    }

    // Get transfer suggestions by store ID
    @GetMapping("/store/{storeId}")
    public ResponseEntity<List<TransferSuggestion>> getSuggestionsByStore(@PathVariable Long storeId) {
        List<TransferSuggestion> suggestions = transferSuggestionService.getSuggestionsByStore(storeId);
        return ResponseEntity.ok(suggestions);
    }

    // Get transfer suggestion by ID
    @GetMapping("/{id}")
    public ResponseEntity<TransferSuggestion> getTransferSuggestion(@PathVariable Long id) {
        TransferSuggestion suggestion = transferSuggestionService.getTransferSuggestion(id);
        return ResponseEntity.ok(suggestion);
    }

    // Update a transfer suggestion
    @PutMapping("/{id}")
    public ResponseEntity<TransferSuggestion> updateTransferSuggestion(@PathVariable Long id,
                                                                       @RequestBody TransferSuggestion transferSuggestion) {
        TransferSuggestion updatedSuggestion = transferSuggestionService.updateTransferSuggestion(id, transferSuggestion);
        return ResponseEntity.ok(updatedSuggestion);
    }

    // Delete a transfer suggestion
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTransferSuggestion(@PathVariable Long id) {
        transferSuggestionService.deleteTransferSuggestion(id);
        return ResponseEntity.noContent().build();
    }
}