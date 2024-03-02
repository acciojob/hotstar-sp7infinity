package com.driver.services;

import com.driver.EntryDto.WebSeriesEntryDto;
import com.driver.model.ProductionHouse;
import com.driver.model.SubscriptionType;
import com.driver.model.WebSeries;
import com.driver.repository.ProductionHouseRepository;
import com.driver.repository.WebSeriesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.List;

@Service
public class WebSeriesService {

    @Autowired
    WebSeriesRepository webSeriesRepository;

    @Autowired
    ProductionHouseRepository productionHouseRepository;

    public Integer addWebSeries(WebSeriesEntryDto webSeriesEntryDto)throws  Exception{

        //Add a webSeries to the database and update the ratings of the productionHouse
        //Incase the seriesName is already present in the Db throw Exception("Series is already present")
        //use function written in Repository Layer for the same
        //Dont forget to save the production and webseries Repo
        WebSeries webSeries = webSeriesRepository.findBySeriesName(webSeriesEntryDto.getSeriesName());
        if(webSeries != null)
            throw new Exception("Series is already present");

        //ProductionHouse productionHouse = productionHouseService.getProductionHouse(webSeriesEntryDto.getProductionHouseId());
        Optional<ProductionHouse> optionalProductionHouse = productionHouseRepository.findById(webSeriesEntryDto.getProductionHouseId());
        if(optionalProductionHouse.isEmpty())
            return -1;
        ProductionHouse productionHouse = optionalProductionHouse.get();
        int webSeriesCount = webSeriesRepository.getWebSeriesCount(productionHouse);
        double newRating = ((productionHouse.getRatings() * webSeriesCount) + webSeriesEntryDto.getRating()) / ((double) (webSeriesCount + 1));
        productionHouse.setRatings(newRating);

        WebSeries newWebSeries = new WebSeries(webSeriesEntryDto.getSeriesName(), webSeriesEntryDto.getAgeLimit(), webSeriesEntryDto.getRating(), webSeriesEntryDto.getSubscriptionType());
        newWebSeries.setProductionHouse(productionHouse);

        List<WebSeries> curWebSeriesList = productionHouse.getWebSeriesList();
        curWebSeriesList.add(newWebSeries);
        productionHouse.setWebSeriesList(curWebSeriesList);

        WebSeries savedWebSeries = webSeriesRepository.save(newWebSeries);
        productionHouseRepository.save(productionHouse);
        return savedWebSeries.getId();
    }
}
