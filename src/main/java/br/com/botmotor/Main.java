package br.com.botmotor;

import br.com.botmotor.bot.*;
import br.com.botmotor.service.BotmotorClient;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.HashSet;
import java.util.Set;

public class Main {

	private static final Set<Long> IDS = new HashSet<>();
	private static Bot BOT = new MainBot();
	// TODO: alterar antes da apresentação
	private static int LAST_MESSAGE = 400;

	public static void main(String[] args) throws Exception {
		while (true) {
			String updates = getUpdates();
			JsonObject obj = parse(updates);
			final JsonArray array = obj.get("result").getAsJsonArray();
			for (JsonElement e : array) {
				JsonObject message = e.getAsJsonObject().get("message")
						.getAsJsonObject();
				Long id = message.get("message_id").getAsLong();
				if (id < LAST_MESSAGE || IDS.contains(id)) {
					continue;
				}
				IDS.add(id);

				Message m = extractMessage(message);

				if (m == null) {
					// não sabemos que tipo de mensagem é. Continuando.
					continue;
				}

				Response r = BOT.process(m);
				if (r.getTexto() != null) {
					sendResponse(m.getUserId(), r.getTexto());
				}
			}

			Thread.sleep(1000);
		}

	}

	private static Message extractMessage(JsonObject message) {
		Message m = null;
		long userId = message.get("chat").getAsJsonObject().get("id")
				.getAsLong();
		String text = message.get("text") == null ? null : message.get("text")
				.getAsString();
		if (text != null) {
			m = new MessageText(text, userId);
		} else {
			final JsonObject location = message.getAsJsonObject("location");
			if (location != null) {
				final double latitude = location.get("latitude").getAsDouble();
				final double longitude = location.get("longitude")
						.getAsDouble();
				m = new MessageLocation(latitude, longitude, userId);
			}
		}
		return m;
	}

	private static JsonObject parse(String result) {
		return (JsonObject) new JsonParser().parse(result);
	}

	private static void sendLocation(long chatId, double latitude,
			double longitude) {
		BotmotorClient client = new BotmotorClient().withEndpoint
				("/sendLocation").withGetParameters("?chat_id=" + chatId +
				"&latitude=" + latitude + "&longitude=" + longitude);

		String response = client.getSingleResult(String.class);
	}

	private static void sendResponse(Long chatId,
			String text) throws IOException {
		System.out.println(chatId + ": " + text);

		BotmotorClient client = new BotmotorClient().withEndpoint
				("/sendmessage").withGetParameters("?chat_id=" + chatId +
				"&text=" + URLEncoder.encode(text, "UTF-8"));

		String response = client.getSingleResult(String.class);
		System.out.println(response);
	}

	private static String getUpdates() {
		BotmotorClient client = new BotmotorClient().withEndpoint
				("/getupdates");

		String response = client.getSingleResult(String.class);
		return response;
	}

}