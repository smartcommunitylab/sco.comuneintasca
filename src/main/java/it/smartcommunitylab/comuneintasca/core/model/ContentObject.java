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

import java.util.Map;


public class ContentObject extends GeoCITObject {
	private static final long serialVersionUID = -5567010752470052310L;

	private Map<String,String> classification;

	public Map<String,String> getClassification() {
		return classification;
	}
	public void setClassification(Map<String,String> classification) {
		this.classification = classification;
	}
	@Override
	public String getElementType() {
		return "content-item";
	}

}
