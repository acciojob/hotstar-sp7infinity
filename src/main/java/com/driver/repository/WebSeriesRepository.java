package com.driver.repository;

import com.driver.model.ProductionHouse;
import com.driver.model.SubscriptionType;
import com.driver.model.WebSeries;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface WebSeriesRepository extends JpaRepository<WebSeries,Integer> {

    WebSeries findBySeriesName(String seriesName);

    @Query("select count(w) from WebSeries w where w.productionHouse = :prodHouse")
    Integer getWebSeriesCount(@Param("prodHouse") ProductionHouse prodHouse);
}
