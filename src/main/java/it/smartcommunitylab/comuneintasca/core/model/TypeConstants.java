package it.smartcommunitylab.comuneintasca.core.model;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

public class TypeConstants {

	public static final String TYPE_POI = "poi";
	public static final String TYPE_EVENT = "event";
	public static final String TYPE_MAINEVENT = "mainevent";
	public static final String TYPE_HOTEL = "hotel";
	public static final String TYPE_RESTAURANT = "restaurant";
	public static final String TYPE_ITINERARY = "itineraries";
	public static final String TYPE_CONTENT = "content";
	public static final String TYPE_CONFIG = "config";
	public static final String TYPE_TERRITORY_SERVICE = "servizio_sul_territorio";
	
	private static final Map<String, Class<? extends AppObject>> map = new LinkedHashMap<String, Class<? extends AppObject>>();
	private static final Map<String, String> uriMap = new LinkedHashMap<String, String>();
	static {
		map.put(TYPE_CONFIG, DynamicConfigObject.class);
		map.put(TYPE_CONTENT, ContentObject.class);
		map.put(TYPE_EVENT, EventObject.class);
		map.put(TYPE_HOTEL, HotelObject.class);
		map.put(TYPE_ITINERARY, ItineraryObject.class);
		map.put(TYPE_MAINEVENT, MainEventObject.class);
		map.put(TYPE_POI, POIObject.class);
		map.put(TYPE_RESTAURANT, RestaurantObject.class);
		map.put(TYPE_TERRITORY_SERVICE, TerritoryServiceObject.class);

		uriMap.put(TYPE_EVENT, "/detail-event");
		uriMap.put(TYPE_HOTEL, "/detail-poi");
		uriMap.put(TYPE_ITINERARY, "/detail-path");
		uriMap.put(TYPE_POI, "/detail-poi");
		uriMap.put(TYPE_RESTAURANT, "/detail-poi");
//		uriMap.put(TYPE_CONTENT, "/detail-info");
//		uriMap.put(TYPE_MAINEVENT, "/detail-info");
//		uriMap.put(TYPE_TERRITORY_SERVICE, "/detail-info");
	}
	
	public static String getTypeUri(String type) {
		return uriMap.getOrDefault(type, "/detail-comune");
	}

	
	public static Class<? extends AppObject> getTypeMapping(String type) {
		return map.get(type);
	}

	public static Set<String> getTypes() {
		return map.keySet();
	}
}
