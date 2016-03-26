package mylittlebot;

import mylittlebot.bot.*;
import mylittlebot.service.TelegramClient;
import mylittlebot.service.UrlShortnerService;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Main {

	private static final Set<Long> IDS = new HashSet<>();
	private static MainBot BOT = new MainBot();
	private static int LAST_MESSAGE = 1358;

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
					continue;
				}
				List<Response> rs;
				rs = BOT.process(m);
				if (rs == null) {
					continue;
				}
				for (Response r : rs) {
					r.send(m.getUserId());
				}
			}

			Thread.sleep(1000);
		}

	}

	private static Message extractMessage(JsonObject message) {
		long userId = message.get("chat").getAsJsonObject().get("id").getAsLong();
		if (message.get("text") != null) {
			return new TextMessage(message.get("text").getAsString(), userId);
		} else {
			final JsonObject location = message.getAsJsonObject("location");
			if (location != null) {
				final double latitude = location.get("latitude").getAsDouble();
				final double longitude = location.get("longitude").getAsDouble();
				return new LocationMessage(latitude, longitude, userId);
			}
		}
		return null;
	}

	private static JsonObject parse(String result) {
		return (JsonObject) new JsonParser().parse(result);
	}

	private static String getUpdates() {
		TelegramClient client = new TelegramClient().withEndpoint
				("/getupdates").withGetParameters("?offset=" + LAST_MESSAGE);

		String response = client.getSingleResult(String.class);
		return response;
	}

}
