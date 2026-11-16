package com.example.payflow.payment.dto

import com.example.payflow.payment.entity.PaymentMethod
import jakarta.validation.constraints.*
import java.math.BigDecimal

data class PaymentRequest(
    @field:DecimalMin("1.00") val amount: BigDecimal,
    val method: PaymentMethod,
    @field:Pattern(regexp = "^(\\+?254|0)[17]\\d{8}$", message = "Invalid Kenyan phone number")
    val phoneNumber: String? = null,
    val bankName: String? = null,
    @field:Pattern(regexp = "^\\d{6,16}$", message = "Invalid account number")
    val accountNumber: String? = null
)