package br.com.botmotor;

import br.com.botmotor.bot.Bot;
import br.com.botmotor.bot.MainBot;
import br.com.botmotor.bot.Message;
import br.com.botmotor.bot.Response;
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

	private static final Set<Long> ids = new HashSet<>();
	private static Bot bot = new MainBot();
	private static int LAST_MESSAGE = 323;

	public static void main(String[] args) throws IOException, InterruptedException {
		while (true) {
			String result = getUpdates();
			JsonObject obj = parse(result);
			final JsonArray array = obj.get("result").getAsJsonArray();
			for (JsonElement e : array) {
				JsonObject message = e.getAsJsonObject().get("message")
						.getAsJsonObject();
				Long id = message.get("message_id").getAsLong();
				if (id < LAST_MESSAGE || ids.contains(id)) {
					continue;
				}
				ids.add(message.get("message_id").getAsLong());
				String text = message.get("text") == null ? null : message.get
						("text").getAsString();
				Long user = message.get("chat").getAsJsonObject().get("id")
						.getAsLong();

				Message m = new Message(user, text);
				if (m == null) {
					continue;
				}
				Response r = bot.process(m);
				sendResponse(m.getUser(), r.getTexto());
			}

			Thread.sleep(1000);
		}

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