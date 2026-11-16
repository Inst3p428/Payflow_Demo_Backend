package com.example.payflow.webhook

import com.example.payflow.payment.service.PaymentService
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/webhooks/mpesa")
class MpesaWebhookController(private val paymentService: PaymentService) {

    @PostMapping("/callback")
    fun callback(@RequestBody callback: MpesaCallback): Map<String, Any> {
        val cb = callback.body.stkCallback
        paymentService.applyMpesaResult(cb.checkoutRequestId, cb.resultCode, cb.resultDesc)
        return mapOf("ResultCode" to 0, "ResultDesc" to "Accepted") // Safaricom expects an ack
    }
}