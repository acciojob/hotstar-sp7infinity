package com.driver.repository;

import com.driver.model.Subscription;
import com.driver.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface SubscriptionRepository extends JpaRepository<Subscription,Integer> {

}
