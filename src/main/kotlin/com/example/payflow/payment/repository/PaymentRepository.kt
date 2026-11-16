package com.example.payflow.payment.repository

import com.example.payflow.payment.entity.Payment
import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface PaymentRepository : JpaRepository<Payment, UUID> {
    fun findByProviderReference(reference: String): Payment?
    fun findAllByOrderByCreatedAtDesc(): List<Payment>
}