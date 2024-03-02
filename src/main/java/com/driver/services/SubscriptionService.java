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

    public Subscription getSubscriptionDetailsByUserId(Integer userId) {
        //User user = userService.getUser(userId);
        Optional<User> optionalUser = userRepository.findById(userId);
        if(optionalUser.isEmpty())
            return null;
        User user = optionalUser.get();
        Optional<Subscription> optionalSubscription = subscriptionRepository.findByUser(user);
        return optionalSubscription.orElse(null);
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
        Optional<Subscription> optionalSubscription = subscriptionRepository.findByUser(user);
        if(optionalSubscription.isEmpty())
            throw new Exception("Subscription does not exist");

        Subscription subscription = optionalSubscription.get();
        int diffAmount = 0;

        if(subscription.getSubscriptionType() == SubscriptionType.ELITE) {
            throw new Exception("Already the best Subscription");
        } else if(subscription.getSubscriptionType() == SubscriptionType.PRO) {
            int curAmount = subscription.getTotalAmountPaid();
            int newAmount = 1000 + (350 * subscription.getNoOfScreensSubscribed());
            diffAmount = newAmount - curAmount;
            subscription.setSubscriptionType(SubscriptionType.ELITE);
            subscription.setTotalAmountPaid(newAmount);
        } else {
            int curAmount = subscription.getTotalAmountPaid();
            int newAmount = 800 + (250 * subscription.getNoOfScreensSubscribed());
            diffAmount = newAmount - curAmount;
            subscription.setSubscriptionType(SubscriptionType.PRO);
            subscription.setTotalAmountPaid(newAmount);
        }
        subscriptionRepository.save(subscription);
        return diffAmount;
    }

    public Integer calculateTotalRevenueOfHotstar(){

        //We need to find out total Revenue of hotstar : from all the subscriptions combined
        //Hint is to use findAll function from the SubscriptionDb

        Integer totalRevenue = subscriptionRepository.getTotalRevenue();
        return Objects.requireNonNullElse(totalRevenue, 0);
    }

}
