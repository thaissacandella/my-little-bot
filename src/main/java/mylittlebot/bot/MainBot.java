package mylittlebot.bot;

import mylittlebot.model.Place;
import mylittlebot.service.PlaceService;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class MainBot {

	public static final String BOT_NAME = "@my_little_friend_bot";
	private final Map<Long, UserSession> data = new HashMap<>();

	public List<Response> r(String s) {
		return Arrays.asList(new TextResponse(s));
	}

	public List<Response> process(Message m) {
		if (m instanceof TextMessage) {
			TextMessage textMessage = (TextMessage) m;
			if (textMessage.getText() == null) {
				return null;
			}
			if (!textMessage.getText().startsWith("/")) {
				return null;
			}
			if (textMessage.getText().endsWith(BOT_NAME)) {
				textMessage.setText(textMessage.getText().substring(0,
						textMessage.getText().length() - BOT_NAME.length()));
			}
			if ("/start".equals(textMessage.getText())) {

				String ret;

				if (data.containsKey(textMessage.getUserId())) {
					ret = "E agora, o que você procura?";
				} else {
					ret = "E ai, o que você está procurando?";
				}
				data.put(textMessage.getUserId(), new UserSession());

				return r(ret + "\n" +
						"Digite /restaurantes para buscarmos restaurantes \n" +
						"Digite /cafes para buscarmos Cafés \n" +
						"Digite /bares para buscarmos bares");
			}

			if (!data.containsKey(textMessage.getUserId())) {
				return r("Digite /start para comerçarmos");
			}

			UserSession session = data.get(textMessage.getUserId());

			if (session.getStage() == Stage.CHOOSE_TYPE_LOCATION) {
				if (!Pattern.matches("/.*", textMessage.getText())) {
					return null;
				}

				String value = textMessage.getText().substring(1);

				LocationType locationType;
				try {
					locationType = LocationType.valueOf(value.toUpperCase());
				} catch (IllegalArgumentException e) {
					return null;
				}

				session.setLocationType(locationType);
				session.setStage(Stage.SEND_LOCATION);

				return r("Envie a sua localização");
			}

			if (session.getStage() == Stage.CHOOSE_LOCATION) {
				if (!Pattern.matches("/option\\d", textMessage.getText())) {
					return null;
				}
				
				int value = Integer.parseInt(textMessage.getText().substring(6));
				session.setPlace(value);
				Place place = session.getPlace(value);
				TextResponse details = new TextResponse(place.toDetail());
				LocationResponse location = new LocationResponse(place.getLatitude().doubleValue(), place.getLongitude().doubleValue());
				if (place.getImgUrl() != null) {
					TextResponse photo = new TextResponse(place.getImgUrl());
					return Arrays.asList(photo, details, location);
				}
				return Arrays.asList(details, location);
			}
		} else if (m instanceof LocationMessage) {
			UserSession session = data.get(m.getUserId());
			if (session == null) {
				return null;
			}
			if (session.getStage() == Stage.SEND_LOCATION) {
				session.setLocation((LocationMessage) m);
				session.setStage(Stage.CHOOSE_LOCATION);
				List<Place> places = new PlaceService().getPlaces(session.getLatitude(), session.getLongitude(), session.getLocationType());
				session.setPlaces(places);
				AtomicInteger a = new AtomicInteger(0);
				return r("Esses são os locais próximos e suas distâncias:\n" + places.stream().map(l -> "/opcao" + a.incrementAndGet() + " " + l.toLine()).collect(Collectors.joining("\n")));
			}
		}

		return null;
	}
}
