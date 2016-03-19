package br.com.botmotor.bot;

/**
 * Created by luan on 3/19/16.
 */
public class Response {
	private String texto;
	private double latitude;
	private double longitude;
	private ResponseType responseType;

	public Response(String text) {
		this.texto = text;
		this.responseType = ResponseType.TEXT;
	}

	public Response(double latitude, double longitude) {
		this.latitude = latitude;
		this.longitude = longitude;
		this.responseType = ResponseType.LOCATION;
	}

	public String getTexto() {
		return this.texto;
	}

	public double getLatitude() {
		return latitude;
	}

	public double getLongitude() {
		return longitude;
	}

	public ResponseType getResponseType() {
		return responseType;
	}

}
