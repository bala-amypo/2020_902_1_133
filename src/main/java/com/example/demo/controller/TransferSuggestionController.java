package com.example.demo.controller;

import com.example.demo.entity.TransferSuggestion;
import com.example.demo.service.TransferSuggestionService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/transfer")
public class TransferSuggestionController {

    private final TransferSuggestionService service;

    public TransferSuggestionController(TransferSuggestionService service) {
        this.service = service;
    }

    @PostMapping
    public TransferSuggestion createTransfer(@RequestBody TransferSuggestion suggestion) {
        return service.createTransferSuggestion(suggestion);
    }

    @GetMapping("/source/{storeId}")
    public List<TransferSuggestion> getBySourceStore(@PathVariable Long storeId) {
        return service.getSuggestionsBySourceStore(storeId);
    }

    @GetMapping("/target/{storeId}")
    public List<TransferSuggestion> getByTargetStore(@PathVariable Long storeId) {
        return service.getSuggestionsByTargetStore(storeId);
    }
}