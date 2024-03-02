package com.driver.services;

import com.driver.EntryDto.WebSeriesEntryDto;
import com.driver.model.ProductionHouse;
import com.driver.model.SubscriptionType;
import com.driver.model.WebSeries;
import com.driver.repository.ProductionHouseRepository;
import com.driver.repository.WebSeriesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class WebSeriesService {

    @Autowired
    WebSeriesRepository webSeriesRepository;

    @Autowired
    ProductionHouseRepository productionHouseRepository;

    //@Autowired
    ProductionHouseService productionHouseService = new ProductionHouseService();

    public Integer addWebSeries(WebSeriesEntryDto webSeriesEntryDto)throws  Exception{

        //Add a webSeries to the database and update the ratings of the productionHouse
        //Incase the seriesName is already present in the Db throw Exception("Series is already present")
        //use function written in Repository Layer for the same
        //Dont forget to save the production and webseries Repo
        WebSeries webSeries = webSeriesRepository.findBySeriesName(webSeriesEntryDto.getSeriesName());
        if(webSeries != null)
            throw new Exception("Series is already present");

        ProductionHouse productionHouse = productionHouseService.getProductionHouse(webSeriesEntryDto.getProductionHouseId());
        int webSeriesCount = webSeriesRepository.getWebSeriesCount(productionHouse);
        double newRating = ((productionHouse.getRatings() * webSeriesCount) + webSeriesEntryDto.getRating()) / ((double) (webSeriesCount + 1));
        productionHouse.setRatings(newRating);

        WebSeries newWebSeries = new WebSeries(webSeriesEntryDto.getSeriesName(), webSeriesEntryDto.getAgeLimit(), webSeriesEntryDto.getRating(), webSeriesEntryDto.getSubscriptionType());
        newWebSeries.setProductionHouse(productionHouse);

        productionHouseRepository.save(productionHouse);
        WebSeries savedWebSeries = webSeriesRepository.save(newWebSeries);
        return savedWebSeries.getId();
    }

    public Integer getViewableWebSeriesCount(SubscriptionType subsType, Integer ageLim) {
        return webSeriesRepository.getViewableWebSeriesCount(subsType, ageLim);
    }
}
