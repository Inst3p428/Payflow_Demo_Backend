package com.example.payflow.payment.provider

import com.example.payflow.payment.entity.*
import org.springframework.stereotype.Component
import java.util.UUID

@Component
class BankProvider : PaymentProvider {
    override val method = PaymentMethod.BANK

    override fun initiate(payment: Payment): ProviderResult {
        val ref = "BNK${UUID.randomUUID().toString().take(10).uppercase()}"
        return when {
            payment.accountNumber?.endsWith("0") == true ->
                ProviderResult(PaymentStatus.FAILED, ref, "Insufficient funds")
            else -> ProviderResult(PaymentStatus.SUCCESS, ref)
        }
    }
}