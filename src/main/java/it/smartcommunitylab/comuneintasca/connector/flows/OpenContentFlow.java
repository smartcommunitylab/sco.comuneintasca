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

import it.smartcommunitylab.comuneintasca.connector.scripts.OpenContentScript;
import it.smartcommunitylab.comuneintasca.connector.scripts.OpenDataScript;

/**
 * @author raman
 *
 */
public class OpenContentFlow<T> implements Flow<T>{

	@Override
	public List<T> process(String url) throws Exception {
		List<T> output = new LinkedList<T>();
		
		String mainStr = it.smartcommunitylab.comuneintasca.connector.ConnectionUtils.call(url, String.class);
		OpenContentScript ocs = new OpenContentScript();
		List<String> links = ocs.extractLinks(mainStr);
		for (String link : links) {
			String string = it.smartcommunitylab.comuneintasca.connector.ConnectionUtils.call(link, String.class);
			OpenDataScript cs = new OpenDataScript();
			Message data = cs.extractData(string);
			output.add((T)data);
		}
		return output;
	}

}
