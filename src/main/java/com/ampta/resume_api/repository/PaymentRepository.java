package com.ampta.resume_api.repository;

import com.ampta.resume_api.document.Payment;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PaymentRepository extends MongoRepository<Payment, String> {

    Optional<Payment> findByOrderId(String orderId);

    Optional<Payment> findByPaymentId(String paymentId);

    List<Payment> findByUserIdOrderByCreatedAtDesc(String userId);

    List<Payment> findByStatus(String status);
}
