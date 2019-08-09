/*******************************************************************************
 * Copyright 2015 Fondazione Bruno Kessler
 * 
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 * 
 *        http://www.apache.org/licenses/LICENSE-2.0
 * 
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 ******************************************************************************/
package it.smartcommunitylab.comuneintasca.storage.data;

/**
 * @author raman
 *
 */


public class SyncDataRequest {

	private SyncData syncData;
	private long since;
	
	public SyncDataRequest(SyncData syncData, long since) {
		super();
		this.syncData = syncData;
		this.since = since;
	}

	public SyncData getSyncData() {
		return syncData;
	}

	public long getSince() {
		return since;
	}
}
	
	
