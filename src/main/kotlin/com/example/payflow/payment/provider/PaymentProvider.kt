package com.example.payflow.payment.provider

import com.example.payflow.payment.entity.*

data class ProviderResult(val status: PaymentStatus, val reference: String, val failureReason: String? = null)

interface PaymentProvider {
    val method: PaymentMethod
    fun initiate(payment: Payment): ProviderResult
}