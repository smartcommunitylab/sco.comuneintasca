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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author raman
 *
 */
public class SyncData {

	private long version;
	private Map<String,List<BasicObject>> updated = new HashMap<String, List<BasicObject>>();
	private Map<String,List<String>> deleted = new HashMap<String, List<String>>();
	
	private Map<String, Object> exclude;
	private Map<String, Object> include;
	
	public SyncData() {
		super();
	}

	public long getVersion() {
		return version;
	}

	public void setVersion(long version) {
		this.version = version;
	}

	public Map<String, List<BasicObject>> getUpdated() {
		return updated;
	}

	public void setUpdated(Map<String, List<BasicObject>> updated) {
		this.updated = updated;
	}

	public Map<String, List<String>> getDeleted() {
		return deleted;
	}

	public void setDeleted(Map<String, List<String>> deleted) {
		this.deleted = deleted;
	}

	public Map<String, Object> getExclude() {
		return exclude;
	}

	public void setExclude(Map<String, Object> exclude) {
		this.exclude = exclude;
	}

	public Map<String, Object> getInclude() {
		return include;
	}

	public void setInclude(Map<String, Object> include) {
		this.include = include;
	}
	
}