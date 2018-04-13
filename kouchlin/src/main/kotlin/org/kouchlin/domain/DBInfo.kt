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

package org.kouchlin.domain

data class Cluster(val n: Int, val q: Int, val r: Int, val w: Int)

abstract class DBInfo {
	var cluster: Cluster? = null
	abstract var compactRunning: Boolean?
	abstract var dataSize: Long?
	abstract var dbName: String?
	abstract var diskFormatVersion: Int?
	abstract var diskSize: Long?
	abstract var docCount: Long?
	abstract var docDelCount: Long?
	abstract var instanceStartTime: Long?
	abstract var purgeSeq: Long?
	abstract var updateSeq: String?
	abstract var otherDataSize: Long?
	abstract var activeSize: Long?
	abstract var externalSize: Long?
	abstract var fileSize: Long?
}
