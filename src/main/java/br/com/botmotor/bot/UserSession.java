package br.com.botmotor.bot;

/**
 * Created by luan on 3/19/16.
 */
public class UserSession {

	private Etapa etapa = Etapa.ESCOLHA_TIPO_LOCAL;
	private TipoLocal tipoLocalEscolhido;

	private double latitude;
	private double longitude;

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
}
