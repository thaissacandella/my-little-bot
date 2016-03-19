package br.com.botmotor.bot;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class MainBot implements Bot {

	public static final String BOT_NAME = "@botmoter_bot";
	private final Map<Long, UserSession> dados = new HashMap<>();

	@Override
	public Response process(Message m) {
		return new Response(processString(m));
	}

	public String processString(Message m) {
		if (m instanceof MessageText) {
			MessageText messageText = (MessageText) m;
			if (messageText.getText() == null) {
				return "";
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

				return retorno + "\n" +
						"Digite /1 para buscarmos restaurantes \n" +
						"Digite /2 para buscarmos Cafés \n" +
						"Digite /3 para buscarmos bares";
			}

			if (!dados.containsKey(messageText.getUserId())) {
				return "Digite /start para comerçarmos";
			}

			UserSession sessao = dados.get(messageText.getUserId());

			if (sessao.getEtapa() == Etapa.ESCOLHA_TIPO_LOCAL) {
				if (!Pattern.matches("/\\d", messageText.getText())) {
					return "\n" +
							"Opção inválida. \n" +
							"Digite /1 para buscarmos restaurantes \n" +
							"Digite /2 para buscarmos Cafés \n" +
							"Digite /3 para buscarmos bares";
				}

				int valor = Integer.parseInt(messageText.getText().substring
						(1));

				if (valor > TipoLocal.values().length) {
					return "\n" +
							"Opção inválida. \n" +
							"Digite /1 para buscarmos restaurantes \n" +
							"Digite /2 para buscarmos cafés \n" +
							"Digite /3 para buscarmos bares";
				}

				TipoLocal tipoLocal = TipoLocal.values()[valor - 1];

				sessao.setTipoLocalEscolhido(tipoLocal);

				sessao.setEtapa(Etapa.ENVIE_LOCALIZ);

				return "Envie a sua localização";
			}

			System.out.println(sessao.getEtapa());
			if (sessao.getEtapa() == Etapa.ENVIE_LOCALIZ) {
				if (m instanceof MessageLocation) {
					sessao.setLocation((MessageLocation) m);
					sessao.setEtapa(Etapa.ESCOLHA_LOCAL);
					System.out.println("-----------------------------------------------");
					return "Lista de lugares por lugar.";
				} else {
					return "Você deve enviar uma localização, moço.";
				}
			}
		}

		return "else";
	}
}
