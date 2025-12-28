// package com.example.demo.service.impl;

// import com.example.demo.entity.*;
// import com.example.demo.exception.BadRequestException;
// import com.example.demo.exception.ResourceNotFoundException;
// import com.example.demo.repository.*;
// import com.example.demo.service.InventoryBalancerService;
// import org.springframework.stereotype.Service;
// import java.time.LocalDate;
// import java.time.LocalDateTime;
// import java.util.ArrayList;
// import java.util.List;

// @Service
// public class InventoryBalancerServiceImpl implements InventoryBalancerService {
    
//     private final TransferSuggestionRepository transferSuggestionRepository;
//     private final InventoryLevelRepository inventoryLevelRepository;
//     private final DemandForecastRepository demandForecastRepository;
//     private final StoreRepository storeRepository;
    
//     public InventoryBalancerServiceImpl(TransferSuggestionRepository transferSuggestionRepository,
//                                       InventoryLevelRepository inventoryLevelRepository,
//                                       DemandForecastRepository demandForecastRepository,
//                                       StoreRepository storeRepository) {
//         this.transferSuggestionRepository = transferSuggestionRepository;
//         this.inventoryLevelRepository = inventoryLevelRepository;
//         this.demandForecastRepository = demandForecastRepository;
//         this.storeRepository = storeRepository;
//     }
    
//     @Override
//     public List<TransferSuggestion> generateSuggestions(Long productId) {
//         List<InventoryLevel> inventories = inventoryLevelRepository.findByProduct_Id(productId);
//         if (inventories.isEmpty()) {
//             throw new BadRequestException("No forecast found");
//         }
        
//         List<TransferSuggestion> suggestions = new ArrayList<>();
        
//         for (InventoryLevel inv : inventories) {
//             List<DemandForecast> forecasts = demandForecastRepository
//                 .findByStoreAndProductAndForecastDateAfter(inv.getStore(), inv.getProduct(), LocalDate.now());
            
//             if (!forecasts.isEmpty() && inv.getQuantity() > 50) {
//                 TransferSuggestion suggestion = new TransferSuggestion();
//                 suggestion.setSourceStore(inv.getStore());
//                 suggestion.setProduct(inv.getProduct());
//                 suggestion.setQuantity(10);
//                 suggestion.setPriority("MEDIUM");
//                 suggestion.setStatus("PENDING");
//                 suggestion.setSuggestedAt(LocalDateTime.now());
//                 suggestions.add(transferSuggestionRepository.save(suggestion));
//             }
//         }
        
//         return suggestions;
//     }
    
//     @Override
//     public List<TransferSuggestion> getSuggestionsForStore(Long storeId) {
//         return transferSuggestionRepository.findBySourceStoreId(storeId);
//     }
    
//     @Override
//     public TransferSuggestion getSuggestionById(Long id) {
//         return transferSuggestionRepository.findById(id)
//             .orElseThrow(() -> new ResourceNotFoundException("Suggestion not found"));
//     }
// }

package com.example.demo.service.impl;

import com.example.demo.entity.*;
import com.example.demo.exception.BadRequestException;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.repository.*;
import com.example.demo.service.InventoryBalancerService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

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

        List<InventoryLevel> inventories =
                inventoryLevelRepository.findByProduct_Id(productId);

        if (inventories.isEmpty()) {
            throw new BadRequestException("No forecast found");
        }

        List<TransferSuggestion> suggestions = new ArrayList<>();

        for (InventoryLevel inv : inventories) {

            List<DemandForecast> forecasts =
                    demandForecastRepository.findByStoreAndProductAndForecastDateAfter(
                            inv.getStore(),
                            inv.getProduct(),
                            LocalDate.now()
                    );

            if (!forecasts.isEmpty() && inv.getQuantity() > 50) {

                TransferSuggestion suggestion = new TransferSuggestion();
                suggestion.setSourceStore(inv.getStore());
                suggestion.setProduct(inv.getProduct());
                suggestion.setQuantity(10);
                suggestion.setPriority("MEDIUM");
                suggestion.setStatus("PENDING");
                suggestions.add(
                        transferSuggestionRepository.save(suggestion)
                );
            }
        }

        return suggestions;
    }

    @Override
    public List<TransferSuggestion> getSuggestionsForStore(Long storeId) {
        return transferSuggestionRepository.findBySourceStore_Id(storeId);
    }

    @Override
    public TransferSuggestion getSuggestionById(Long id) {
        return transferSuggestionRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Suggestion not found"));
    }
}
