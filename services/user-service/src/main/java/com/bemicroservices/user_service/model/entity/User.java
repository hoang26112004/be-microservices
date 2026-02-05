package com.bemicroservices.user_service.model.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.boot.autoconfigure.amqp.RabbitConnectionDetails;
import java.util.List;
import com.bemicroservices.user_service.model.entity.Adress;
import java.sql.Timestamp;
import java.time.LocalDate;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)//k can dung private
public class User {
    @Id
    @Column(nullable = false,unique = true,updatable = false)//k dc null k dc trung k cho update cot id
    String id;

    String phoneNumber;

    String avatarUrl;

    Boolean gender;

    LocalDate dateOfBirth;

    @CreationTimestamp
    Timestamp createdAt;

    @UpdateTimestamp
    Timestamp updatedAt;

    @OneToMany(mappedBy = "user")
    List<Adress> addresses;
}
