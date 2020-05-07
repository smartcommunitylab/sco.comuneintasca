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
package it.smartcommunitylab.comuneintasca.connector;

import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @author raman
 *
 */
public class ConnectionUtils {
	/**
	 * 
	 */
	private static final int TIMEOUT = 5000;

	private static final Logger logger = LoggerFactory.getLogger(ConnectionUtils.class);

	private static final ExecutorService executor = Executors.newSingleThreadExecutor(); 
	
//	static {
//		System.setProperty("https.protocols", "TLSv1.1");
//	}
	private static ObjectMapper mapper = new ObjectMapper();
	
	public static <T> T call(String url, Class<T> cls) throws IOException {
		URL urlObj = new URL(url);
		if ("file".equalsIgnoreCase(urlObj.getProtocol())) {
			List<String> list = Files.readAllLines(Paths.get(URI.create(url)), Charset.forName("utf-8"));
			return mapper.convertValue(StringUtils.collectionToDelimitedString(list, "\n"), cls);
		} else {
//			try {
//				SSLUtil.turnOffSslChecking();
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
			RestTemplate restTemplate = getRestTemplate();
			
			if ("http".equals(urlObj.getProtocol())) {
				return callRepeat(restTemplate, url.replaceFirst("http", "https"), cls);
			}
			return callRepeat(restTemplate, url, cls);
		}
	} 
	
	public static RestTemplate getRestTemplate() {

		SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();

        factory.setConnectTimeout(5000);
        factory.setReadTimeout(TIMEOUT);
	    return new RestTemplate(factory);
	}
	
	private static <T> T callRepeat(RestTemplate restTemplate, String url, Class<T> cls) {
		logger.info("retriving url {} ...", url);
		for (int i = 0; i < 3; i++) {
			try {
				try {
					Thread.sleep(100);
				} catch (InterruptedException e1) {
				}				
				return invokeTemplate(restTemplate, url, cls);
			} catch (Exception e) {
				logger.warn("error retriving url {}, retryung ...", url);
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e1) {
				}
			} 
		}
		try {
			return invokeTemplate(restTemplate, url, cls);
		} catch (Exception e1) {
			logger.warn("error retriving url {}, terminating", url);
			throw new RuntimeException(e1.getMessage());
		}
	}

	private static <T> T invokeTemplate(RestTemplate restTemplate, String url, Class<T> cls) throws InterruptedException, ExecutionException {
		List<Future<T>> invokeAll = executor.invokeAll(Collections.singletonList(() -> {
			return restTemplate.getForObject(url, cls);
		}), 5000, TimeUnit.MILLISECONDS);
		Future<T> f = invokeAll.get(0);
		if (f.isCancelled()) {
			throw new InterruptedException("Failed to retrieve");
		}
		logger.info("... done");
		return f.get();
	}
}

