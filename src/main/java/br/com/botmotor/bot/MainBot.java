package br.com.botmotor.bot;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class MainBot implements Bot {

	private final Map<Long, UserSession> dados = new HashMap<>();

	@Override
	public Response process(Message m) {
		return new Response(processString(m));
	}

	public String processString(Message m) {
		if (m.getMessage() == null) {
			return "";
		}

		if ("/start".equals(m.getMessage())) {

			String retorno;

			if (dados.containsKey(m.getUser())) {
				retorno = "E agora, o que você procura?";
			} else {
				retorno = "E ai, o que você está procurando?";
			}
			dados.put(m.getUser(), new UserSession());

			return retorno + "\n" +
					"Digite /1 para buscarmos restaurantes \n" +
					"Digite /2 para buscarmos Cafés \n" +
					"Digite /3 para buscarmos bares";
		}

		if (!dados.containsKey(m.getUser())) {
			return "Digite /start para comerçarmos";
		}

		UserSession sessao = dados.get(m.getUser());

		if (sessao.getEtapa() == Etapa.ESCOLHA_TIPO_LOCAL) {
			if (!Pattern.matches("/\\d", m.getMessage())) {
				return "\n" +
						"Opção inválida. \n" +
						"Digite /1 para buscarmos restaurantes \n" +
						"Digite /2 para buscarmos Cafés \n" +
						"Digite /3 para buscarmos bares";
			}

			int valor = Integer.parseInt(m.getMessage().substring(1));

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

		return "else";
	}
}
