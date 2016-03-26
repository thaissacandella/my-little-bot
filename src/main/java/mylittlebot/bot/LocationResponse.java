package mylittlebot.bot;

import mylittlebot.service.TelegramClient;

public class LocationResponse extends Response{
    private double latitude;
    private double longitude;

    public LocationResponse(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    @Override
    public void send(long chatId) {
        TelegramClient client = new TelegramClient().withEndpoint
                ("/sendLocation").withGetParameters("?chat_id=" + chatId +
                "&latitude=" + latitude + "&longitude=" + longitude);

        String response = client.getSingleResult(String.class);
    }
}
