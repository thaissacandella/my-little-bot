package mylittlebot.bot;

import mylittlebot.service.TelegramClient;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class TextResponse  extends Response{
    private String text;

    public TextResponse(String text) {
        this.text = text;
    }

    public String getText() {
        return this.text;
    }

    @Override
    public void send(long chatId) {
        TelegramClient client = null;
        try {
            client = new TelegramClient().withEndpoint
                    ("/sendmessage").withGetParameters("?chat_id=" + chatId +
                    "&text=" + (text == null ? "null" : URLEncoder.encode(text,"UTF-8")));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        String response = client.getSingleResult(String.class);

    }
}
