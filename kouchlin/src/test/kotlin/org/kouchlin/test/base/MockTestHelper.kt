/*
 * Copyright 2018 Kouchlin Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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
                     json: String = "",
                     headers: List<Pair<String,String>> = emptyList()): CapturingSlot<Request> {

    val slot = slot<Request>()
    val client = mockk<Client>()

    every { client.executeRequest(capture(slot)).statusCode } returns statusCode
    every { client.executeRequest(capture(slot)).responseMessage } returns message
    every { client.executeRequest(capture(slot)).data } returns json.toByteArray()
    every { client.executeRequest(capture(slot)).dataStream } returns json.toByteArray().inputStream()
    every { client.executeRequest(capture(slot)).headers } returns headers.associateBy({it.first},{ listOf(it.second)})
    FuelManager.instance.client = client
    return slot
}