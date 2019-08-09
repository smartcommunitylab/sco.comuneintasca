package it.smartcommunitylab.comuneintasca.connector.processor;

import it.smartcommunitylab.comuneintasca.connector.SourceEntry;
import it.smartcommunitylab.comuneintasca.core.model.ContentObject;
import it.smartcommunitylab.comuneintasca.core.model.EventObject;
import it.smartcommunitylab.comuneintasca.core.model.HotelObject;
import it.smartcommunitylab.comuneintasca.core.model.ItineraryObject;
import it.smartcommunitylab.comuneintasca.core.model.MainEventObject;
import it.smartcommunitylab.comuneintasca.core.model.Organization;
import it.smartcommunitylab.comuneintasca.core.model.POIObject;
import it.smartcommunitylab.comuneintasca.core.model.RestaurantObject;
import it.smartcommunitylab.comuneintasca.core.model.TerritoryServiceObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.springframework.stereotype.Component;

import com.google.protobuf.ByteString;

import eu.trentorise.smartcampus.service.opendata.data.message.Opendata;
import eu.trentorise.smartcampus.service.opendata.data.message.Opendata.Evento;
import eu.trentorise.smartcampus.service.opendata.data.message.Opendata.I18nCultura;
import eu.trentorise.smartcampus.service.opendata.data.message.Opendata.I18nHotel;
import eu.trentorise.smartcampus.service.opendata.data.message.Opendata.I18nItinerario;
import eu.trentorise.smartcampus.service.opendata.data.message.Opendata.I18nMainEvent;
import eu.trentorise.smartcampus.service.opendata.data.message.Opendata.I18nRestaurant;
import eu.trentorise.smartcampus.service.opendata.data.message.Opendata.I18nString;
import eu.trentorise.smartcampus.service.opendata.data.message.Opendata.I18nTesto;

/**
 * Convert data to the internal format
 * @author raman
 *
 */
@Component
public class DataExtractor {

	public interface Extractor<S,T> {
		public S readData(ByteString bs) throws Exception;
		public T extractData(S bs, SourceEntry source);
		public String getId(S obj);
		public boolean isNewer(S source, T target);
	}
	
	public final Extractor<Evento, EventObject> eventExtractor = new Extractor<Opendata.Evento, EventObject>() {

		@Override
		public Evento readData(ByteString bs) throws Exception {
			return Evento.parseFrom(bs);
		}

		@Override
		public EventObject extractData(Evento bt, SourceEntry entry) {
			EventObject no = new EventObject();
			no.setId(bt.getId());
			no.setAddress(Collections.singletonMap("it", bt.getAddress()));
			no.setCategory(bt.getCategory());
			no.setCost(Collections.singletonMap("it", bt.getCost()));
			no.setDescription(Collections.singletonMap("it", bt.getDescription()));
			no.setDuration(Collections.singletonMap("it", bt.getDuration()));
			no.setEventForm(bt.getEventType());
			no.setEventPeriod(Collections.singletonMap("it", bt.getEventPeriod()));
			no.setEventTiming(Collections.singletonMap("it", bt.getEventTiming()));
			no.setEventType(bt.getCategory());
			no.setFromTime(bt.getFromTime());
			no.setImage(getImageURL(bt.getImage(), entry));
			no.setInfo(Collections.singletonMap("it", bt.getInfo()));
			no.setLastModified(bt.getLastModified());
			List<Organization> orgs = new ArrayList<Organization>();
			if (bt.getOrganizationsCount() > 0) {
				for (eu.trentorise.smartcampus.service.opendata.data.message.Opendata.Organization o : bt.getOrganizationsList()) {
					Organization ro = new Organization();
					ro.setOrganizationType(o.getType());
					ro.setTitle(Collections.singletonMap("it", o.getTitle()));
					ro.setUrl(o.getUrl());
				}
			}
			no.setOrganizations(orgs);
			if (bt.hasParentEventId()) {
				no.setParentEventId(bt.getParentEventId());
			}
			
			Map<String, String> contacts = new HashMap<String, String>();
			if (bt.hasEmail())
				contacts.put("email", bt.getEmail());
			if (bt.hasPhone())
				contacts.put("phone", bt.getPhone());
			no.setContacts(contacts);

			no.setShortTitle(Collections.singletonMap("it", bt.getShortTitle()));
			no.setSource("opendata.trento");
			no.setSpecial(bt.getSpecial());
			no.setSubtitle(Collections.singletonMap("it", bt.getSubtitle()));
			no.setTitle(Collections.singletonMap("it", bt.getTitle()));
			no.setTopics(bt.getTopicsList());
			no.setToTime(bt.getToTime());
			no.setUrl(bt.getUrl());
			return no;
		}

		@Override
		public String getId(Evento obj) {
			return obj.getId();
		}

		@Override
		public boolean isNewer(Evento source, EventObject target) {
			return target.getLastModified() < source.getLastModified();
		}
	};
	public Extractor<I18nRestaurant, RestaurantObject> restaurantExtractor = new Extractor<I18nRestaurant, RestaurantObject>() {

		@Override
		public I18nRestaurant readData(ByteString bs) throws Exception {
			return I18nRestaurant.parseFrom(bs);
		}

		@Override
		public RestaurantObject extractData(I18nRestaurant bt, SourceEntry entry) {
			RestaurantObject no = new RestaurantObject();
			no.setId(bt.getId());
			no.setAddress(toMap(bt.getAddress()));
			no.setCategory("ristorazione");
			no.setClassification(toMap(bt.getClassification()));
			no.setClosing(toMap(bt.getClosing()));

			Map<String, String> contacts = new HashMap<String, String>();
			contacts.put("email", bt.getEmail());
			contacts.put("phone", bt.getPhone());
			no.setContacts(contacts);

			no.setDescription(toMap(bt.getDescription()));
			no.setEquipment(toMap(bt.getEquipment()));
			no.setImage(getImageURL(bt.getImage(), entry));
			no.setInfo(toMap(bt.getInfo()));
			no.setLastModified(bt.getLastModified());
			if (bt.hasLat() && bt.hasLon()) {
				no.setLocation(new double[] { bt.getLat(), bt.getLon() });
			}
			no.setPrices(toMap(bt.getPrices()));
			no.setShortTitle(toMap(bt.getShortTitle()));
			no.setSubtitle(toMap(bt.getSubtitle()));
			no.setTimetable(toMap(bt.getTimetable()));
			no.setTitle(toMap(bt.getTitle()));
			no.setUpdateTime(System.currentTimeMillis());
			no.setUrl(bt.getUrl());
			no.setObjectId(bt.getObjectId());
			return no;
		}

		@Override
		public String getId(I18nRestaurant obj) {
			return obj.getId();
		}

		@Override
		public boolean isNewer(I18nRestaurant source, RestaurantObject target) {
			return target.getLastModified() < source.getLastModified();
		}
	};
	public Extractor<I18nHotel, HotelObject> hotelExtractor = new Extractor<Opendata.I18nHotel, HotelObject>() {

		@Override
		public I18nHotel readData(ByteString bs) throws Exception {
			return I18nHotel.parseFrom(bs);
		}

		@Override
		public HotelObject extractData(I18nHotel bt, SourceEntry entry) {
			HotelObject no = new HotelObject();
			no.setId(bt.getId());
			no.setAddress(toMap(bt.getAddress()));
			no.setCategory("dormire");
			no.setClassification(toMap(bt.getClassification()));

			Map<String, String> contacts = new HashMap<String, String>();
			contacts.put("email", bt.getEmail());
			contacts.put("phone", bt.getPhone());
			contacts.put("phone2", bt.getPhone2());
			contacts.put("fax", bt.getFax());
			no.setContacts(contacts);

			no.setImage(getImageURL(bt.getImage(), entry));
			no.setLastModified(bt.getLastModified());
			if (bt.hasLat() && bt.hasLon()) {
				no.setLocation(new double[] { bt.getLat(), bt.getLon() });
			}
			no.setStars(bt.getStars());
			no.setDescription(toMap(bt.getDescription()));
			no.setSubtitle(toMap(bt.getSubtitle()));
			no.setInfo(toMap(bt.getInfo()));
			no.setTitle(toMap(bt.getTitle()));
			no.setUpdateTime(System.currentTimeMillis());
			no.setUrl(bt.getUrl());
			no.setObjectId(bt.getObjectId());
			return no;
		}

		@Override
		public String getId(I18nHotel obj) {
			return obj.getId();
		}

		@Override
		public boolean isNewer(I18nHotel source, HotelObject target) {
			return target.getLastModified() < source.getLastModified();
		}
	};
	public Extractor<I18nCultura, POIObject> poiExtractor = new Extractor<Opendata.I18nCultura, POIObject>() {

		@Override
		public I18nCultura readData(ByteString bs) throws Exception {
			return I18nCultura.parseFrom(bs);
		}

		@Override
		public POIObject extractData(I18nCultura bt, SourceEntry entry) {
			POIObject no = new POIObject();
			no.setId(bt.getId());
			no.setAddress(toMap(bt.getAddress()));
			no.setCategory("cultura");
			// TODO: classification
			no.setClassification(toMap(bt.getClassification()));

			Map<String, String> contacts = new HashMap<String, String>();
			contacts.put("email", bt.getEmail());
			contacts.put("phone", bt.getPhone());
			no.setContacts(contacts);

			no.setDescription(toMap(bt.getDescription()));
			no.setImage(getImageURL(bt.getImage(), entry));
			no.setLastModified(bt.getLastModified());
			if (bt.hasLat() && bt.hasLon()) {
				no.setLocation(new double[] { bt.getLat(), bt.getLon() });
			}

			no.setSubtitle(toMap(bt.getSubtitle()));
			no.setTitle(toMap(bt.getTitle()));
			no.setInfo(toMap(bt.getInfo()));
			no.setUpdateTime(System.currentTimeMillis());
			no.setUrl(bt.getUrl());
			no.setContactFullName(bt.getContactFullName());
			no.setObjectId(bt.getObjectId());
			return no;
		}

		@Override
		public String getId(I18nCultura obj) {
			return obj.getId();
		}

		@Override
		public boolean isNewer(I18nCultura source, POIObject target) {
			return target.getLastModified() < source.getLastModified();
		}
	};
	public Extractor<I18nMainEvent, MainEventObject> mainEventExtractor = new Extractor<Opendata.I18nMainEvent, MainEventObject>() {

		@Override
		public I18nMainEvent readData(ByteString bs) throws Exception {
			return I18nMainEvent.parseFrom(bs);
		}

		@Override
		public MainEventObject extractData(I18nMainEvent bt, SourceEntry entry) {
			MainEventObject no = new MainEventObject();
			no.setId(bt.getId());
			no.setAddress(toMap(bt.getAddress()));
			no.setCategory("event");
			// TODO: classification
			no.setClassification(toMap(bt.getClassification()));

			Map<String, String> contacts = new HashMap<String, String>();
			contacts.put("email", bt.getEmail());
			contacts.put("phone", bt.getPhone());
			no.setContacts(contacts);

			no.setDescription(toMap(bt.getDescription()));
			no.setImage(getImageURL(bt.getImage(), entry));
			no.setLastModified(bt.getLastModified());
			if (bt.hasLat() && bt.hasLon()) {
				no.setLocation(new double[] { bt.getLat(), bt.getLon() });
			}

			no.setSubtitle(toMap(bt.getSubtitle()));
			no.setTitle(toMap(bt.getTitle()));
			no.setUpdateTime(System.currentTimeMillis());
			no.setUrl(bt.getUrl());
			no.setInfo(toMap(bt.getInfo()));

			no.setFromDate(bt.getFromDate());
			no.setToDate(bt.getToDate());
			no.setEventDateDescription(toMap(bt.getDateDescription()));
			no.setObjectId(bt.getObjectId());
			return no;
		}

		@Override
		public String getId(I18nMainEvent obj) {
			return obj.getId();
		}

		@Override
		public boolean isNewer(I18nMainEvent source, MainEventObject target) {
			return target.getLastModified() < source.getLastModified();
		}
	};
	public Extractor<I18nItinerario, ItineraryObject> itineraryExtractor = new Extractor<Opendata.I18nItinerario, ItineraryObject>() {

		@Override
		public I18nItinerario readData(ByteString bs) throws Exception {
			return I18nItinerario.parseFrom(bs);
		}

		@Override
		public ItineraryObject extractData(I18nItinerario bt, SourceEntry entry) {
			ItineraryObject no = new ItineraryObject();
			no.setId(bt.getId());
			no.setCategory("itinerari");
			no.setDescription(toMap(bt.getDescription()));
			no.setImage(getImageURL(bt.getImage(), entry));
			no.setLastModified(bt.getLastModified());

			no.setSubtitle(toMap(bt.getSubtitle()));
			no.setTitle(toMap(bt.getTitle()));
			no.setInfo(toMap(bt.getInfo()));
			no.setUpdateTime(System.currentTimeMillis());
			no.setUrl(bt.getUrl());

			no.setSteps(bt.getStepsList());
			no.setDifficulty(toMap(bt.getDifficulty()));
			no.setDuration(bt.getDuration());
			no.setLength(bt.getLength());
			no.setObjectId(bt.getObjectId());
			return no;
		}

		@Override
		public String getId(I18nItinerario obj) {
			return obj.getId();
		}

		@Override
		public boolean isNewer(I18nItinerario source, ItineraryObject target) {
			return target.getLastModified() < source.getLastModified();
		}
	};
	public Extractor<I18nTesto, ContentObject> contentExtractor = new Extractor<Opendata.I18nTesto, ContentObject>() {

		@Override
		public I18nTesto readData(ByteString bs) throws Exception {
			return I18nTesto.parseFrom(bs);
		}

		@Override
		public ContentObject extractData(I18nTesto bt, SourceEntry entry) {
			ContentObject no = new ContentObject();
			no.setId(bt.getId());
			no.setCategory("text");
			no.setClassification(toMap(bt.getClassification()));

			no.setDescription(toMap(bt.getDescription()));
			no.setImage(getImageURL(bt.getImage(), entry));
			no.setLastModified(bt.getLastModified());

			Map<String, String> contacts = new HashMap<String, String>();
			contacts.put("email", bt.getEmail());
			contacts.put("phone", bt.getPhone());
			contacts.put("fax", bt.getFax());
			no.setContacts(contacts);

			no.setSubtitle(toMap(bt.getSubtitle()));
			no.setTitle(toMap(bt.getTitle()));
			no.setInfo(toMap(bt.getInfo()));
			no.setUpdateTime(System.currentTimeMillis());
			no.setUrl(bt.getUrl());
			no.setAddress(toMap(bt.getAddress()));
			no.setObjectId(bt.getObjectId());
			return no;
		}

		@Override
		public String getId(I18nTesto obj) {
			return obj.getId();
		}

		@Override
		public boolean isNewer(I18nTesto source, ContentObject target) {
			return target.getLastModified() < source.getLastModified();
		}
	};
	public Extractor<Opendata.I18nCultura, TerritoryServiceObject> territoryServiceExtractor  = new Extractor<Opendata.I18nCultura, TerritoryServiceObject>() {

		@Override
		public I18nCultura readData(ByteString bs) throws Exception {
			return I18nCultura.parseFrom(bs);
		}

		@Override
		public TerritoryServiceObject extractData(I18nCultura bt, SourceEntry entry) {
			TerritoryServiceObject no = new TerritoryServiceObject();
			no.setId(bt.getId());
			no.setAddress(toMap(bt.getAddress()));
			no.setCategory("servizio_sul_territorio");
			// TODO: classification
			no.setClassification(toMap(bt.getClassification()));

			Map<String, String> contacts = new HashMap<String, String>();
			contacts.put("email", bt.getEmail());
			contacts.put("phone", bt.getPhone());
			no.setContacts(contacts);

			no.setDescription(toMap(bt.getDescription()));
			no.setImage(getImageURL(bt.getImage(), entry));
			no.setLastModified(bt.getLastModified());
			if (bt.hasLat() && bt.hasLon()) {
				no.setLocation(new double[] { bt.getLat(), bt.getLon() });
			}

			no.setSubtitle(toMap(bt.getSubtitle()));
			no.setTitle(toMap(bt.getTitle()));
			no.setInfo(toMap(bt.getInfo()));
			no.setUpdateTime(System.currentTimeMillis());
			no.setUrl(bt.getUrl());
			no.setContactFullName(bt.getContactFullName());
			no.setObjectId(bt.getObjectId());
			return no;
		}

		@Override
		public String getId(I18nCultura obj) {
			return obj.getId();
		}

		@Override
		public boolean isNewer(I18nCultura source, TerritoryServiceObject target) {
			return target.getLastModified() < source.getLastModified();
		}
	};

	

	public String getImageURL(String image, SourceEntry entry) {
		if (image == null || image.isEmpty()) return null;
		String img = image;
		if (img.indexOf('|')>0) {
			img = img.substring(0,img.indexOf('|'));
		} 
		if (!img.startsWith("http")) {
			return (entry.getImagePath() == null ? "" : entry.getImagePath()) + img;
		}
		if (img.startsWith("http:")) {
			img = img.replace("http:", "https:");
		}
		return img;
	}

	private Map<String, String> toMap(I18nString str) {
		Map<String, String> map = new TreeMap<String, String>();
		if (str.hasIt()) {
			map.put("it", str.getIt());
		}
		if (str.hasEn()) {
			map.put("en", str.getEn());
		}
		if (str.hasDe()) {
			map.put("de", str.getDe());
		}
		return map;
	}
}
