package com.example.payflow.payment.entity

import jakarta.persistence.*
import java.math.BigDecimal
import java.time.Instant
import java.util.UUID

enum class PaymentMethod { MPESA, BANK }
enum class PaymentStatus { PENDING, SUCCESS, FAILED }

@Entity
@Table(name = "payments")
class Payment(
    @Id val id: UUID = UUID.randomUUID(),
    @Column(nullable = false, precision = 19, scale = 2) val amount: BigDecimal,
    @Enumerated(EnumType.STRING) @Column(nullable = false) val method: PaymentMethod,
    val phoneNumber: String? = null,
    val bankName: String? = null,
    val accountNumber: String? = null,
    @Enumerated(EnumType.STRING) @Column(nullable = false) var status: PaymentStatus = PaymentStatus.PENDING,
    var providerReference: String? = null,
    var failureReason: String? = null,
    val createdAt: Instant = Instant.now(),
    var updatedAt: Instant = Instant.now()
)