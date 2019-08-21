/*******************************************************************************
 * Copyright 2012-2013 Trento RISE
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
package it.smartcommunitylab.comuneintasca.core.model;

public class POIObject extends GeoCITObject {
	private static final long serialVersionUID = -5567010752470052310L;

	private String relatedObjectId;
	private String contactFullName;

	public String getRelatedObjectId() {
		return relatedObjectId;
	}
	public void setRelatedObjectId(String relatedObjectId) {
		this.relatedObjectId = relatedObjectId;
	}
	public String getContactFullName() {
		return contactFullName;
	}
	public void setContactFullName(String contactFullName) {
		this.contactFullName = contactFullName;
	}
	@Override
	public String getElementType() {
		return "poi-item";
	}

}
