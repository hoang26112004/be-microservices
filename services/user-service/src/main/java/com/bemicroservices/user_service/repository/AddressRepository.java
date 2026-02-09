package com.bemicroservices.user_service.repository;

import com.bemicroservices.user_service.model.entity.Address;
import com.bemicroservices.user_service.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AddressRepository extends JpaRepository<Address, Integer> {
    @Query("SELECT a FROM Address a WHERE a.user.id = :userId")
    Optional<Address> findByUserId(String userId);

    @Query("SELECT a FROM Address  a WHERE a.user.id = :Id")
    List<Address> findAllByUserId(String id);

    List<Address> findByUser(User user);
}
