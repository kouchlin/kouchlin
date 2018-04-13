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

package org.kouchlin.jackson.domain

import com.fasterxml.jackson.annotation.JsonProperty
import org.kouchlin.domain.ViewResult

class JacksonViewResult<T> : ViewResult<T>() {
    @JsonProperty("total_rows") override var totalRows: Long? = null
    @JsonProperty("update_seq") override var updateSeq: String? = null
}