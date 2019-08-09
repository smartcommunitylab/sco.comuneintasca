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
package it.smartcommunitylab.comuneintasca.connector.flows;

import java.util.LinkedList;
import java.util.List;

import com.google.protobuf.Message;

import eu.trentorise.smartcampus.service.opendata.data.message.Opendata;
import eu.trentorise.smartcampus.service.opendata.data.message.Opendata.ConfigData;
import eu.trentorise.smartcampus.service.opendata.data.message.Opendata.ConfigLink;
import it.smartcommunitylab.comuneintasca.connector.scripts.ConfigScript;
import it.smartcommunitylab.comuneintasca.connector.scripts.OpenContentScript;

/**
 * @author raman
 *
 */
public class ConfigFlow implements Flow<ConfigData>{

	@Override
	public List<ConfigData> process(String url) throws Exception {
		List<ConfigData> output = new LinkedList<Opendata.ConfigData>();
		
		String configStr = it.smartcommunitylab.comuneintasca.connector.ConnectionUtils.call(url, String.class);
		OpenContentScript ocs = new OpenContentScript();
		List<Message> links = ocs.extractUri(configStr);
		for (Message link : links) {
			ConfigLink cl = (ConfigLink) link;
			String string = it.smartcommunitylab.comuneintasca.connector.ConnectionUtils.call(cl.getUri(), String.class);
			ConfigScript cs = new ConfigScript();
			Message data = cs.copyData(cl, string);
			output.add((ConfigData)data);
		}
		return output;
	}

	
}
