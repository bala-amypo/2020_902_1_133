package com.example.demo.service.impl;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.example.demo.entity.DemandForecast;
import com.example.demo.entity.InventoryLevel;
import com.example.demo.entity.Store;
import com.example.demo.entity.TransferSuggestion;
import com.example.demo.exception.BadRequestException;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.repository.DemandForecastRepository;
import com.example.demo.repository.InventoryLevelRepository;
import com.example.demo.repository.StoreRepository;
import com.example.demo.repository.TransferSuggestionRepository;
import com.example.demo.service.InventoryBalancerService;

@Service
public class InventoryBalancerServiceImpl implements InventoryBalancerService {

    private final TransferSuggestionRepository transferSuggestionRepository;
    private final InventoryLevelRepository inventoryLevelRepository;
    private final DemandForecastRepository demandForecastRepository;
    private final StoreRepository storeRepository;

    public InventoryBalancerServiceImpl(
            TransferSuggestionRepository transferSuggestionRepository,
            InventoryLevelRepository inventoryLevelRepository,
            DemandForecastRepository demandForecastRepository,
            StoreRepository storeRepository) {

        this.transferSuggestionRepository = transferSuggestionRepository;
        this.inventoryLevelRepository = inventoryLevelRepository;
        this.demandForecastRepository = demandForecastRepository;
        this.storeRepository = storeRepository;
    }

    @Override
    public List<TransferSuggestion> generateSuggestions(Long productId) {

        List<InventoryLevel> inventoryLevels =
                inventoryLevelRepository.findByProduct_Id(productId);

        if (inventoryLevels == null || inventoryLevels.isEmpty()) {
            throw new BadRequestException("No forecast found");
        }

        List<TransferSuggestion> suggestions = new ArrayList<>();

        for (InventoryLevel sourceInv : inventoryLevels) {

            Store sourceStore = sourceInv.getStore();

            List<DemandForecast> forecasts =
                    demandForecastRepository.findByStoreAndProductAndForecastDateAfter(
                            sourceStore,
                            sourceInv.getProduct(),
                            LocalDate.now()
                    );

            if (forecasts == null || forecasts.isEmpty()) {
                continue;
            }

            int totalForecastDemand =
                    forecasts.stream().mapToInt(DemandForecast::getPredictedDemand).sum();

            int excess = sourceInv.getQuantity() - totalForecastDemand;

            if (excess <= 0) continue;

            for (InventoryLevel targetInv : inventoryLevels) {

                if (sourceInv.getId().equals(targetInv.getId())) continue;

                int deficit = totalForecastDemand - targetInv.getQuantity();

                if (deficit <= 0) continue;

                int transferQty = Math.min(excess, deficit);

                TransferSuggestion suggestion = new TransferSuggestion();
                suggestion.setSourceStore(sourceStore);
                suggestion.setTargetStore(targetInv.getStore());
                suggestion.setProduct(sourceInv.getProduct());
                suggestion.setQuantity(transferQty);
                suggestion.setPriority("MEDIUM");
                suggestion.setStatus("PENDING");
                suggestion.setSuggestedAt(new Timestamp(System.currentTimeMillis()));

                suggestions.add(transferSuggestionRepository.save(suggestion));
                break;
            }
        }

        if (suggestions.isEmpty()) {
            throw new BadRequestException("No forecast found");
        }

        return suggestions;
    }

    @Override
    public List<TransferSuggestion> getSuggestionsForStore(Long storeId) {
        return transferSuggestionRepository.findBySourceStoreId(storeId);
    }

    @Override
    public TransferSuggestion getSuggestionById(Long id) {
        return transferSuggestionRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("TransferSuggestion not found"));
    }
}