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

import java.util.List;
import java.util.Map;

public class EventObject extends GeoCITObject {
	private static final long serialVersionUID = -7946171699743704527L;

	private boolean special;
	private long fromTime;
	private long toTime;
	
	private Map<String,String> eventPeriod;
	private Map<String,String> eventTiming;
	private Map<String,String> duration;
	private Map<String,String> cost;
	
	private String eventForm;
	private String parentEventId;
	private String eventType;
	private List<String> topics;
	
	private List<Organization> organizations;

	public boolean isSpecial() {
		return special;
	}

	public void setSpecial(boolean special) {
		this.special = special;
	}

	public long getFromTime() {
		return fromTime;
	}

	public void setFromTime(long fromTime) {
		this.fromTime = fromTime;
	}

	public long getToTime() {
		return toTime;
	}

	public void setToTime(long toTime) {
		this.toTime = toTime;
	}

	public Map<String, String> getEventPeriod() {
		return eventPeriod;
	}

	public void setEventPeriod(Map<String, String> eventPeriod) {
		this.eventPeriod = eventPeriod;
	}

	public Map<String, String> getEventTiming() {
		return eventTiming;
	}

	public void setEventTiming(Map<String, String> eventTiming) {
		this.eventTiming = eventTiming;
	}

	public Map<String, String> getDuration() {
		return duration;
	}

	public void setDuration(Map<String, String> duration) {
		this.duration = duration;
	}

	public Map<String, String> getCost() {
		return cost;
	}

	public void setCost(Map<String, String> cost) {
		this.cost = cost;
	}

	public String getEventForm() {
		return eventForm;
	}

	public void setEventForm(String eventForm) {
		this.eventForm = eventForm;
	}

	public String getParentEventId() {
		return parentEventId;
	}

	public void setParentEventId(String parentEventId) {
		this.parentEventId = parentEventId;
	}

	public String getEventType() {
		return eventType;
	}

	public void setEventType(String eventType) {
		this.eventType = eventType;
	}

	public List<String> getTopics() {
		return topics;
	}

	public void setTopics(List<String> topics) {
		this.topics = topics;
	}

	public List<Organization> getOrganizations() {
		return organizations;
	}

	public void setOrganizations(List<Organization> organizations) {
		this.organizations = organizations;
	}
	@Override
	public String getElementType() {
		return "event-item";
	}

}
