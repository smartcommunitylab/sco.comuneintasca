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

public class RestaurantObject extends GeoCITObject {
	private static final long serialVersionUID = -5567010752470052310L;

	private Map<String,String> classification;
	private Map<String,String> timetable;
	private Map<String,String> closing;
	private Map<String,String> equipment;
	private Map<String,String> prices;


	public Map<String, String> getClassification() {
		return classification;
	}
	public void setClassification(Map<String, String> classification) {
		this.classification = classification;
	}
	public Map<String, String> getTimetable() {
		return timetable;
	}
	public void setTimetable(Map<String, String> timetable) {
		this.timetable = timetable;
	}
	public Map<String, String> getClosing() {
		return closing;
	}
	public void setClosing(Map<String, String> closing) {
		this.closing = closing;
	}
	public Map<String, String> getEquipment() {
		return equipment;
	}
	public void setEquipment(Map<String, String> equipment) {
		this.equipment = equipment;
	}
	public Map<String, String> getPrices() {
		return prices;
	}
	public void setPrices(Map<String, String> prices) {
		this.prices = prices;
	}
	@Override
	public String getElementType() {
		return "restaurant-item";
	}
	
	
}
