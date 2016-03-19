package chatbot;

/**
 * Created by luan on 3/19/16.
 */
public class Response {
    private String texto;
    private double[] location;

    public Response(String text) {
        this.texto = text;
    }

    public Response(double... location) {
        this.location = location;
    }

    public boolean isLocation() {
        return this.location != null;
    }

    public String getTexto() {
        return this.texto;
    }

    public double getLat() {
        return this.location[0];
    }

    public double getLong() {
        return this.location[1];
    }
}
