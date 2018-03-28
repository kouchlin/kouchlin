package org.kouchlin.test.base

import com.github.kittinunf.fuel.core.Client
import com.github.kittinunf.fuel.core.FuelManager
import com.github.kittinunf.fuel.core.Request
import io.mockk.CapturingSlot
import io.mockk.every
import io.mockk.mockk
import io.mockk.slot

fun FuelManager.mock(statusCode: Int,
                     message: String = "",
                     json: String = ""): CapturingSlot<Request> {

    val slot = slot<Request>()
    val client = mockk<Client>()

    every { client.executeRequest(capture(slot)).statusCode } returns statusCode
    every { client.executeRequest(capture(slot)).responseMessage } returns message
    every { client.executeRequest(capture(slot)).data } returns json.toByteArray()
    every { client.executeRequest(capture(slot)).dataStream } returns json.toByteArray().inputStream()

    FuelManager.instance.client = client
    return slot
}