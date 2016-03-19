package chatbot;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import sun.java2d.pipe.SpanShapeRenderer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URLEncoder;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Main {

    private static final Set<Long> ids = new HashSet<Long>();
    private static Bot bot = new SimpleBot();

    public static class SimpleBot implements Bot {

        public List<String> process(Message m) {
            return Arrays.asList(m.getUser() + ": " + m.getMessage());
        }
    }

    public static void main(String[] args) throws IOException, InterruptedException {
        while (true) {
            String result = getUpdates();
            JsonObject obj = (JsonObject) new JsonParser().parse(result);
            for (JsonElement e : obj.get("result").getAsJsonArray()) {
                JsonObject message = e.getAsJsonObject().get("message").getAsJsonObject();
                if (ids.contains(message.get("message_id").getAsLong())) {
                    continue;
                }
                ids.add(message.get("message_id").getAsLong());
                String text = message.get("text") == null ? null : message.get("text").getAsString();
                Long user = message.get("chat").getAsJsonObject().get("id").getAsLong();

                Message m = new Message(user, text);
                List<String> r = bot.process(m);
                sendResponse(m.getUser(), r.get(0).toString());
            }

            Thread.sleep(1000);
        }

    }

    private static void sendResponse(Long chatId, String text) throws IOException {
        String url = "https://api.telegram.org/bot198737376:AAFrs1DR7fBwsYvKj_jDW6lZvwlOULFE9Y0/sendmessage?chat_id=" + chatId + "&text=" + URLEncoder.encode(text, "UTF-8");

        HttpClient client = HttpClientBuilder.create().build();
        HttpGet request = new HttpGet(url);

        // add request header
        HttpResponse response = client.execute(request);
        System.out.println(response);
    }

    private static String getUpdates() throws IOException {
        String url = "https://api.telegram.org/bot198737376:AAFrs1DR7fBwsYvKj_jDW6lZvwlOULFE9Y0/getupdates";

        HttpClient client = HttpClientBuilder.create().build();
        HttpGet request = new HttpGet(url);

        // add request header
        HttpResponse response = client.execute(request);

        System.out.println("Response Code : "
                + response.getStatusLine().getStatusCode());

        BufferedReader rd = new BufferedReader(
                new InputStreamReader(response.getEntity().getContent()));

        StringBuffer result = new StringBuffer();
        String line;
        while ((line = rd.readLine()) != null) {
            result.append(line);
        }
        return result.toString();
    }


}
