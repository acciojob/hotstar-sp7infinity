package com.driver.services;


import com.driver.EntryDto.ProductionHouseEntryDto;
import com.driver.model.ProductionHouse;
import com.driver.repository.ProductionHouseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ProductionHouseService {

    @Autowired
    ProductionHouseRepository productionHouseRepository;

    public Integer addProductionHouseToDb(ProductionHouseEntryDto productionHouseEntryDto){
        ProductionHouse productionHouse = new ProductionHouse(productionHouseEntryDto.getName());
        ProductionHouse savedProductionHouse = productionHouseRepository.save(productionHouse);
        return savedProductionHouse.getId();
    }

    public ProductionHouse getProductionHouse(Integer id) {
        Optional<ProductionHouse> productionHouse = productionHouseRepository.findById(id);
        return productionHouse.orElse(null);
    }

}
