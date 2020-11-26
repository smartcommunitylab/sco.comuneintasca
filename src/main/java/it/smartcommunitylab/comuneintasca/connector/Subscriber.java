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
package it.smartcommunitylab.comuneintasca.connector;

import java.util.LinkedList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.google.protobuf.ByteString;
import com.google.protobuf.Message;

import it.smartcommunitylab.comuneintasca.connector.flows.Flow;
import it.smartcommunitylab.comuneintasca.connector.processor.ConfigProcessor;
import it.smartcommunitylab.comuneintasca.connector.processor.DataProcessor;


public class Subscriber {

	private Log logger = LogFactory.getLog(getClass());
	private App app;
	private DataProcessor processor;
	private ConfigProcessor configProcessor;
	
	public void subscribe(App app, DataProcessor processor, ConfigProcessor configProcessor) {
		logger.info("SUBSCRIBING app "+app.getId());
		this.app = app;
		this.processor = processor;
		this.configProcessor = configProcessor;
	}
	
	public void process() {
		try {
			configProcessor.buildConfig(app);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		for (SourceEntry source : app.getSources()) {
			if (source.isManual()) continue;
			
			processSource(source);
		}	
	}
	
	private void processSource(SourceEntry source) {
		String serviceId = source.getServiceId();
		String methodName = source.getMethodName();
		try {
			Class<?> clazz = Class.forName(methodName);
			@SuppressWarnings("unchecked")
			Flow<Message> newInstance = (Flow<Message>) clazz.newInstance();
			logger.info("processing " + methodName);
			List<Message> list = newInstance.process(source.getUrl());
			logger.info("processing done: " + list.size());
			List<ByteString> bsList = new LinkedList<ByteString>();
			for (Message msg : list) {
				bsList.add(msg.toByteString());
			}
			processor.onServiceEvents(app, serviceId, clazz.getName(), bsList);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			logger.error("Error processing "+ methodName);
		}
	}
	
	public void trigger(Class<?> clazz, String appId) {
		for (SourceEntry source : app.getSources()) {
			if (clazz.getName().equalsIgnoreCase(source.getType())) {
				logger.info("manual processing " + source.getType());
				processSource(source);
			}
		}			
	}
	
}
