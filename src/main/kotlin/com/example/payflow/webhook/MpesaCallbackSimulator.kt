package com.example.payflow.webhook

import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import org.springframework.web.client.RestClient
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit

@Component
class MpesaCallbackSimulator(
    @Value("\${payflow.base-url}") baseUrl: String,
    @Value("\${payflow.mpesa.callback-delay-seconds}") private val delaySeconds: Long
) {
    private val scheduler = Executors.newScheduledThreadPool(2)
    private val client = RestClient.create(baseUrl)

    fun scheduleCallback(checkoutRequestId: String, resultCode: Int) {
        scheduler.schedule({
            val desc = if (resultCode == 0) "The service request is processed successfully."
            else "Request cancelled by user"
            val payload = MpesaCallback(
                Body(StkCallback("29115-34620561-1", checkoutRequestId, resultCode, desc))
            )
            runCatching {
                client.post().uri("/api/webhooks/mpesa/callback").body(payload).retrieve().toBodilessEntity()
            }
        }, delaySeconds, TimeUnit.SECONDS)
    }
}

data class MpesaCallback(@com.fasterxml.jackson.annotation.JsonProperty("Body") val body: Body)
data class Body(@com.fasterxml.jackson.annotation.JsonProperty("stkCallback") val stkCallback: StkCallback)
data class StkCallback(
    @com.fasterxml.jackson.annotation.JsonProperty("MerchantRequestID") val merchantRequestId: String,
    @com.fasterxml.jackson.annotation.JsonProperty("CheckoutRequestID") val checkoutRequestId: String,
    @com.fasterxml.jackson.annotation.JsonProperty("ResultCode") val resultCode: Int,
    @com.fasterxml.jackson.annotation.JsonProperty("ResultDesc") val resultDesc: String
)