package br.com.botmotor.bot;

import br.com.botmotor.model.Place;
import br.com.botmotor.service.PlaceService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class MainBot {

	public static final String BOT_NAME = "@botmoter_bot";
	private final Map<Long, UserSession> dados = new HashMap<>();

	public Response process(Message m) {
		if (m instanceof MessageText) {
			MessageText messageText = (MessageText) m;
			if (messageText.getText() == null) {
				return null;
			}
			if (!messageText.getText().startsWith("/")) {
				return null;
			}
			if (messageText.getText().endsWith(BOT_NAME)) {
				messageText.setText(messageText.getText().substring(0,
						messageText.getText().length() - BOT_NAME.length()));
			}
			System.out.println(messageText.getText());

			if ("/start".equals(messageText.getText())) {

				String retorno;

				if (dados.containsKey(messageText.getUserId())) {
					retorno = "E agora, o que você procura?";
				} else {
					retorno = "E ai, o que você está procurando?";
				}
				dados.put(messageText.getUserId(), new UserSession());

				return new Response(retorno + "\n" +
						"Digite /restaurantes para buscarmos Restaurantes \n" +
						"Digite /cafes para buscarmos Cafés \n" +
						"Digite /bares para buscarmos Bares");
			}

			if (!dados.containsKey(messageText.getUserId())) {
				return new Response("Digite /start para comerçarmos");
			}

			UserSession sessao = dados.get(messageText.getUserId());

			System.out.println("------------------------" + sessao.getEtapa());
			if (sessao.getEtapa() == Etapa.ESCOLHA_TIPO_LOCAL) {
				if (!Pattern.matches("/.*", messageText.getText())) {
					return null;
				}

				String valor = messageText.getText().substring(1);
				
				TipoLocal tipoLocal = null;
				try {
					tipoLocal = TipoLocal.valueOf(valor.toUpperCase());
				} catch (IllegalArgumentException e) {
				}

				if (tipoLocal == null) {
					return null;
				}

				sessao.setTipoLocalEscolhido(tipoLocal);

				sessao.setEtapa(Etapa.ENVIE_LOCALIZ);

				return new Response("Envie a sua localização");
			}

			if (sessao.getEtapa() == Etapa.ESCOLHA_LOCAL) {
				if (!Pattern.matches("/opcao\\d", messageText.getText())) {
					return null;
				}
				
				int valor = Integer.parseInt(messageText.getText().substring(6));
				sessao.setEtapa(Etapa.SIM_OU_NAO);
				sessao.setPlaceEscolhido(valor);
				return new Response(sessao.getPlace(valor).toDetail());
			}

			if (sessao.getEtapa() == Etapa.SIM_OU_NAO) {
				if ("/Sim".equals(messageText.getText())) {
					Place p = sessao.getPlace(sessao.getPlaceEscolhido());
					return new Response(p.getLatitude().doubleValue(), p.getLongitude().doubleValue());
				} else {

				}
			}
		} else if (m instanceof MessageLocation) {
			UserSession sessao = dados.get(m.getUserId());
			if (sessao == null) {
				return null;
			}
			if (sessao.getEtapa() == Etapa.ENVIE_LOCALIZ) {
				sessao.setLocation((MessageLocation) m);
				sessao.setEtapa(Etapa.ESCOLHA_LOCAL);
				List<Place> places = new PlaceService().getPlaces(sessao.getLatitude(), sessao.getLongitude(), sessao.getTipoLocalEscolhido());
				sessao.setPlaces(places);
				AtomicInteger a = new AtomicInteger(0);
				return new Response("Esses são os locais próximos e suas distâncias:\n" + places.stream().map(l -> "/opcao" + a.incrementAndGet() + " " + l.toLine()).collect(Collectors.joining("\n")));
			}
		}

		return null;
	}
}
