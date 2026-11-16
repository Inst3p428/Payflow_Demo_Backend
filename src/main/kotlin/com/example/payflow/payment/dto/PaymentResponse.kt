package com.example.payflow.payment.dto

import com.example.payflow.payment.entity.*
import java.math.BigDecimal
import java.time.Instant
import java.util.UUID

data class PaymentResponse(
    val id: UUID,
    val amount: BigDecimal,
    val method: PaymentMethod,
    val status: PaymentStatus,
    val reference: String?,
    val failureReason: String?,
    val createdAt: Instant,
    val updatedAt: Instant
)

fun Payment.toResponse() = PaymentResponse(
    id, amount, method, status, providerReference, failureReason, createdAt, updatedAt
)