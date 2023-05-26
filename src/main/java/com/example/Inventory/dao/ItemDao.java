package com.example.Inventory.dao;

import com.example.Inventory.model.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ItemDao extends JpaRepository<Item, Long> {

}
