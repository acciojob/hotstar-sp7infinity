package com.driver.services;


import com.driver.EntryDto.SubscriptionEntryDto;
import com.driver.model.Subscription;
import com.driver.model.SubscriptionType;
import com.driver.model.User;
import com.driver.repository.SubscriptionRepository;
import com.driver.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class SubscriptionService {

    @Autowired
    SubscriptionRepository subscriptionRepository;

    @Autowired
    UserRepository userRepository;

    public Integer buySubscription(SubscriptionEntryDto subscriptionEntryDto){

        //Save The subscription Object into the Db and return the total Amount that user has to pay
        //User user = userService.getUser(subscriptionEntryDto.getUserId());
        Optional<User> optionalUser = userRepository.findById(subscriptionEntryDto.getUserId());
        if(optionalUser.isEmpty())
            return 0;
        User user = optionalUser.get();
        int subscriptionFees = 0, noOfScreens = subscriptionEntryDto.getNoOfScreensRequired();
        SubscriptionType subscriptionType = subscriptionEntryDto.getSubscriptionType();

        if(subscriptionType == SubscriptionType.BASIC) {
            subscriptionFees = 500 + (200 * noOfScreens);
        } else if(subscriptionType == SubscriptionType.PRO) {
            subscriptionFees = 800 + (250 * noOfScreens);
        } else {
            subscriptionFees = 1000 + (350 * noOfScreens);
        }

        Subscription subscription = new Subscription(subscriptionType, noOfScreens, new Date(), subscriptionFees);

        subscription.setUser(user);
        user.setSubscription(subscription);

        userRepository.save(user);
        return subscriptionFees;
    }

    public Integer upgradeSubscription(Integer userId)throws Exception{

        //If you are already at an ElITE subscription : then throw Exception ("Already the best Subscription")
        //In all other cases just try to upgrade the subscription and tell the difference of price that user has to pay
        //update the subscription in the repository
        //Subscription subscription = getSubscriptionDetailsByUserId(userId);
        //if(subscription == null)
        //    throw new Exception("User does not exist");

        Optional<User> optionalUser = userRepository.findById(userId);
        if(optionalUser.isEmpty())
            throw new Exception("User does not exist");
        User user = optionalUser.get();
        Subscription subscription = user.getSubscription();
        int diffAmount = 0, newAmount = 0;
        int curAmount = subscription.getTotalAmountPaid();

        if(subscription.getSubscriptionType() == SubscriptionType.ELITE) {
            throw new Exception("Already the best Subscription");
        } else if(subscription.getSubscriptionType() == SubscriptionType.PRO) {
            newAmount = 1000 + (350 * subscription.getNoOfScreensSubscribed());
            subscription.setSubscriptionType(SubscriptionType.ELITE);
        } else {
            newAmount = 800 + (250 * subscription.getNoOfScreensSubscribed());
            subscription.setSubscriptionType(SubscriptionType.PRO);
        }
        diffAmount = newAmount - curAmount;
        subscription.setTotalAmountPaid(newAmount);

        subscriptionRepository.save(subscription);
        return diffAmount;
    }

    public Integer calculateTotalRevenueOfHotstar(){

        //We need to find out total Revenue of hotstar : from all the subscriptions combined
        //Hint is to use findAll function from the SubscriptionDb

        //return subscriptionRepository.getTotalRevenue();
        List<Subscription> subscriptions = subscriptionRepository.findAll();
        int totalRevenue = 0;
        for(Subscription subscription: subscriptions){
            totalRevenue+= subscription.getTotalAmountPaid();
        }

        return totalRevenue;
    }

}
