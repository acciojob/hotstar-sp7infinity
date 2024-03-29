package com.driver.services;


import com.driver.model.Subscription;
import com.driver.model.SubscriptionType;
import com.driver.model.User;
import com.driver.model.WebSeries;
import com.driver.repository.UserRepository;
import com.driver.repository.WebSeriesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    WebSeriesRepository webSeriesRepository;


    public Integer addUser(User user){

        //Jut simply add the user to the Db and return the userId returned by the repository
        User newUser = new User(user.getName(), user.getAge(), user.getMobNo());
        User savedUser = userRepository.save(newUser);
        return savedUser.getId();
    }

    public Integer getAvailableCountOfWebSeriesViewable(Integer userId){

        //Return the count of all webSeries that a user can watch based on his ageLimit and subscriptionType
        //Hint: Take out all the Webseries from the WebRepository
        //User user = getUser(userId);
        Optional<User> optionalUser = userRepository.findById(userId);
        if(optionalUser.isEmpty())
            return 0;
        User user = optionalUser.get();
        //return webSeriesRepository.getViewableWebSeriesCount(user.getSubscription().getSubscriptionType(), user.getAge());

        List<WebSeries> webSeries = webSeriesRepository.findAll();
        int webSeriesCount = 0;

        for(WebSeries ws: webSeries) {
            if((ws.getSubscriptionType().ordinal() <= user.getSubscription().getSubscriptionType().ordinal())
                && (ws.getAgeLimit() <= user.getAge())
            )
                webSeriesCount++;
        }

        return webSeriesCount;
    }


}
