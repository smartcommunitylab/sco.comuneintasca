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

public class ItineraryObject extends BaseCITObject {
	private static final long serialVersionUID = 6325381726807197185L;

	private List<String> steps;
	private Integer length;
	private Integer duration;
	private Map<String,String> difficulty;
	
	private List<String> stepLines;
	
	public List<String> getSteps() {
		return steps;
	}
	public void setSteps(List<String> steps) {
		this.steps = steps;
	}
	public Integer getLength() {
		return length;
	}
	public void setLength(Integer length) {
		this.length = length;
	}
	public Integer getDuration() {
		return duration;
	}
	public void setDuration(Integer duration) {
		this.duration = duration;
	}
	public Map<String,String> getDifficulty() {
		return difficulty;
	}
	public void setDifficulty(Map<String,String> difficulty) {
		this.difficulty = difficulty;
	}
	public List<String> getStepLines() {
		return stepLines;
	}
	public void setStepLines(List<String> stepLines) {
		this.stepLines = stepLines;
	}
	
	
	@Override
	public String getElementType() {
		return "itinerary-item";
	}

}
