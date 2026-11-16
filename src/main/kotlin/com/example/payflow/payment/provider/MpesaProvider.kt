package com.example.payflow.payment.provider

import com.example.payflow.payment.entity.*
import com.example.payflow.webhook.MpesaCallbackSimulator
import org.springframework.stereotype.Component
import java.util.UUID

@Component
class MpesaProvider(private val simulator: MpesaCallbackSimulator) : PaymentProvider {
    override val method = PaymentMethod.MPESA

    override fun initiate(payment: Payment): ProviderResult {
        val checkoutId = "ws_CO_${UUID.randomUUID().toString().replace("-", "").take(20)}"

        if (payment.amount > 150_000.toBigDecimal()) {
            return ProviderResult(PaymentStatus.FAILED, checkoutId, "Amount exceeds M-Pesa transaction limit")
        }

        val resultCode = if (payment.phoneNumber?.endsWith("0000") == true) 1032 else 0 // 1032 = cancelled by user
        simulator.scheduleCallback(checkoutId, resultCode)
        return ProviderResult(PaymentStatus.PENDING, checkoutId)
    }
}