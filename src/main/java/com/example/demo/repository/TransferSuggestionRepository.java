
package com.example.demo.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.example.demo.entity.TransferSuggestion;

@Repository
public interface TransferSuggestionRepository extends JpaRepository<TransferSuggestion, Long> {
    List<TransferSuggestion> findBySourceStoreId(Long storeId);
    List<TransferSuggestion> findByProduct_Id(Long productId);
}