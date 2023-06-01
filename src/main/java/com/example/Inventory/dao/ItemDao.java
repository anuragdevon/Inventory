package com.example.Inventory.dao;

import com.example.Inventory.model.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ItemDao extends JpaRepository<Item, Long> {
    Optional<Item> findByItemIdAndUserId(Long itemId, Long userId);
}
