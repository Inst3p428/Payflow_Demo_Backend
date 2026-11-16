package com.example.payflow.payment.controller

import com.example.payflow.payment.dto.*
import com.example.payflow.payment.service.PaymentService
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import java.util.UUID

@RestController
@RequestMapping("/api/payments")
class PaymentController(private val service: PaymentService) {

    @PostMapping
    @ResponseStatus(HttpStatus.ACCEPTED)
    fun initiate(@Valid @RequestBody request: PaymentRequest): PaymentResponse =
        service.initiate(request).toResponse()

    @GetMapping("/{id}")
    fun get(@PathVariable id: UUID): PaymentResponse = service.get(id).toResponse()

    @GetMapping
    fun history(): List<PaymentResponse> = service.history().map { it.toResponse() }
}