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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.protobuf.Message;

import eu.trentorise.smartcampus.service.opendata.data.message.Opendata;
import eu.trentorise.smartcampus.service.opendata.data.message.Opendata.I18nItinerario;
import it.smartcommunitylab.comuneintasca.connector.scripts.ItineraryScript;
import it.smartcommunitylab.comuneintasca.connector.scripts.OpenContentScript;

/**
 * @author raman
 *
 */
public class ItinerariesFlow implements Flow<I18nItinerario> {
	
	private Logger logger = LoggerFactory.getLogger(ItinerariesFlow.class);

	@Override
	public List<I18nItinerario> process(String url) throws Exception {
		List<I18nItinerario> output = new LinkedList<Opendata.I18nItinerario>();
		
		String mainStr = it.smartcommunitylab.comuneintasca.connector.ConnectionUtils.call(url, String.class);
		OpenContentScript ocs = new OpenContentScript();
		List<String> links = ocs.extractLinks(mainStr);
		for (String link : links) {
			String string = it.smartcommunitylab.comuneintasca.connector.ConnectionUtils.call(link, String.class);
			String stringen = null;
			try {
				stringen = it.smartcommunitylab.comuneintasca.connector.ConnectionUtils.call(link + "?Translation=eng-GB", String.class);
			} catch (Exception e) {
				logger .warn("No translation EN: " + link);;
			}
			String stringde = null;
			try {
				stringde = it.smartcommunitylab.comuneintasca.connector.ConnectionUtils.call(link + "?Translation=ger-DE", String.class);
			} catch (Exception e) {
				logger.warn("No translation DE: " + link);;
			}			ItineraryScript cs = new ItineraryScript();
			Message data = cs.extractData(string, stringen, stringde);
			output.add((I18nItinerario)data);
		}
		return output;
	}
}
