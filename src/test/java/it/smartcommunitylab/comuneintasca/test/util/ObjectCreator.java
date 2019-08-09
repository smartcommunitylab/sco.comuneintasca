package it.smartcommunitylab.comuneintasca.test.util;

import it.smartcommunitylab.comuneintasca.core.model.EventObject;

import java.util.Collections;

import com.google.protobuf.AbstractMessageLite;

import eu.trentorise.smartcampus.service.opendata.data.message.Opendata.ConfigData;
import eu.trentorise.smartcampus.service.opendata.data.message.Opendata.Evento;

public class ObjectCreator {

	public static final String TEST_APP = "test";
	private static final String TEST_CONFIG_DATA_WITH_OBJECTID = "{\"highlights\": [{\"id\": \"h1\", \"objectIds\": [\"event1\"]}],\"navigationItems\": [{\"ref\": \"m1\"}],\"menu\": []}";
	private static final String TEST_CONFIG_DATA_WITH_QUERY = "{\"highlights\": [],\"navigationItems\": [{\"ref\": \"m1\"}],\"menu\": [{\"id\": \"m1\", \"query\":{\"type\": \"event\",\"classifications\": null}}]}";

	public static EventObject createEvent() {
		EventObject e = new EventObject();
		
		e.setId("event1");
		e.setCategory("category");
		e.setFromTime(100);
		e.setToTime(200);
		e.setClassifier("classifier");
		e.setDescription(Collections.singletonMap("it", "description"));
		e.setImage("image");
		e.setTitle(Collections.singletonMap("it", "title"));
		e.setInfo(Collections.singletonMap("it", "info"));
		
		return e;
	}

	public static Evento createEventProto() {
		Evento res = Evento.newBuilder()
				.setTitle("title")
				.setCategory("category")
				.setId("event1")
				.setFromTime(100)
				.setTitle("200")
				.setDescription("description")
				.setImage("image")
				.setInfo("info")
				.build();
		return res;
	}

	public static AbstractMessageLite createConfigProtoWithObjectId() {
		return ConfigData.newBuilder()
				.setName("test")
				.setData(TEST_CONFIG_DATA_WITH_OBJECTID)
				.setDateModified(System.currentTimeMillis())
				.build();
	}
	
	public static AbstractMessageLite createConfigProtoWithQuery() {
		return ConfigData.newBuilder()
				.setName("test")
				.setData(TEST_CONFIG_DATA_WITH_QUERY)
				.setDateModified(System.currentTimeMillis())
				.build();
	}
}
