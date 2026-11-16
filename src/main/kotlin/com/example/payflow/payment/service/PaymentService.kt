package com.example.payflow.payment.service

import com.example.payflow.payment.dto.*
import com.example.payflow.payment.entity.*
import com.example.payflow.payment.provider.PaymentProvider
import com.example.payflow.payment.repository.PaymentRepository
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.server.ResponseStatusException
import java.time.Instant
import java.util.UUID

@Service
class PaymentService(
    private val repository: PaymentRepository,
    providers: List<PaymentProvider>
) {
    private val providersByMethod = providers.associateBy { it.method }

    @Transactional
    fun initiate(request: PaymentRequest): Payment {
        when (request.method) {
            PaymentMethod.MPESA -> require(!request.phoneNumber.isNullOrBlank()) { "Phone number is required for M-Pesa" }
            PaymentMethod.BANK -> require(!request.bankName.isNullOrBlank() && !request.accountNumber.isNullOrBlank()) {
                "Bank and account number are required for bank payments"
            }
        }

        val payment = repository.save(
            Payment(
                amount = request.amount,
                method = request.method,
                phoneNumber = request.phoneNumber,
                bankName = request.bankName,
                accountNumber = request.accountNumber
            )
        )

        val result = providersByMethod.getValue(request.method).initiate(payment)
        payment.status = result.status
        payment.providerReference = result.reference
        payment.failureReason = result.failureReason
        payment.updatedAt = Instant.now()
        return payment
    }

    @Transactional(readOnly = true)
    fun get(id: UUID): Payment =
        repository.findById(id).orElseThrow { ResponseStatusException(HttpStatus.NOT_FOUND, "Payment not found") }

    @Transactional(readOnly = true)
    fun history(): List<Payment> = repository.findAllByOrderByCreatedAtDesc()

    @Transactional
    fun applyMpesaResult(checkoutRequestId: String, resultCode: Int, description: String) {
        val payment = repository.findByProviderReference(checkoutRequestId) ?: return
        if (payment.status != PaymentStatus.PENDING) return // idempotent: ignore duplicate callbacks
        if (resultCode == 0) payment.status = PaymentStatus.SUCCESS
        else { payment.status = PaymentStatus.FAILED; payment.failureReason = description }
        payment.updatedAt = Instant.now()
    }
}