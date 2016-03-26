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
	private final Map<Long, UserSession> dados = new HashMap<>();

	public List<Response> r(String s) {
		return Arrays.asList(new Response(s));
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

				if (dados.containsKey(textMessage.getUserId())) {
					ret = "E agora, o que você procura?";
				} else {
					ret = "E ai, o que você está procurando?";
				}
				dados.put(textMessage.getUserId(), new UserSession());

				return r(ret + "\n" +
						"Digite /restaurantes para buscarmos restaurantes \n" +
						"Digite /cafes para buscarmos Cafés \n" +
						"Digite /bares para buscarmos bares");
			}

			if (!dados.containsKey(textMessage.getUserId())) {
				return r("Digite /start para comerçarmos");
			}

			UserSession session = dados.get(textMessage.getUserId());

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
				if (!Pattern.matches("/opcao\\d", textMessage.getText())) {
					return null;
				}
				
				int valor = Integer.parseInt(textMessage.getText().substring(6));
				session.setPlace(valor);
				Place place = session.getPlace(valor);
				Response details = new Response(place.toDetail());
				Response location = new Response(place.getLatitude().doubleValue(), place.getLongitude().doubleValue());
				if (place.getImgUrl() != null) {
					Response photo = Response.photo(place.getImgUrl());
					return Arrays.asList(photo, details, location);
				}
				return Arrays.asList(details, location);
			}
		} else if (m instanceof LocationMessage) {
			UserSession sessao = dados.get(m.getUserId());
			if (sessao == null) {
				return null;
			}
			if (sessao.getStage() == Stage.SEND_LOCATION) {
				sessao.setLocation((LocationMessage) m);
				sessao.setStage(Stage.CHOOSE_LOCATION);
				List<Place> places = new PlaceService().getPlaces(sessao.getLatitude(), sessao.getLongitude(), sessao.getLocationType());
				sessao.setPlaces(places);
				AtomicInteger a = new AtomicInteger(0);
				return r("Esses são os locais próximos e suas distâncias:\n" + places.stream().map(l -> "/opcao" + a.incrementAndGet() + " " + l.toLine()).collect(Collectors.joining("\n")));
			}
		}

		return null;
	}
}
