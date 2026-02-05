package com.bemicroservices.user_service.repository;

import org.springframework.boot.autoconfigure.amqp.RabbitConnectionDetails;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AddressRepository extends JpaRepository<RabbitConnectionDetails.Address, Integer> {
}
