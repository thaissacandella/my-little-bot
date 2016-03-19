package br.com.botmotor.bot;

import br.com.botmotor.model.Place;

import java.util.List;

/**
 * Created by luan on 3/19/16.
 */
public class UserSession {

	private Etapa etapa = Etapa.ESCOLHA_TIPO_LOCAL;
	private TipoLocal tipoLocalEscolhido;

	private double latitude;
	private double longitude;

	private List<Place> places;
	private int placeEscolhido;

	public TipoLocal getTipoLocalEscolhido() {
		return tipoLocalEscolhido;
	}

	public void setLocation(MessageLocation l) {
		this.latitude = l.getLatitude();
		this.longitude = l.getLongitude();
	}

	public void setTipoLocalEscolhido(TipoLocal tipoLocalEscolhido) {
		this.tipoLocalEscolhido = tipoLocalEscolhido;
	}

	public Etapa getEtapa() {
		return etapa;
	}

	public void setEtapa(Etapa etapa) {
		this.etapa = etapa;
	}

	public double getLatitude() {
		return latitude;
	}

	public double getLongitude() {
		return longitude;
	}

	public void setPlaces(List<Place> places) {
		this.places = places;
	}

	public Place getPlace(int i) {
		return places.get(i);
	}

	public void setPlaceEscolhido(int placeEscolhido) {
		this.placeEscolhido = placeEscolhido;
	}

	public int getPlaceEscolhido() {
		return placeEscolhido;
	}
}
