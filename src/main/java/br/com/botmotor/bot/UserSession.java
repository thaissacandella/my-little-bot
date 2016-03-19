package br.com.botmotor.bot;

import chatbot.Etapa;

/**
 * Created by luan on 3/19/16.
 */
public class UserSession {
    private Etapa etapa = Etapa.ESCOLHA_TIPO_LOCAL;
    private TipoLocal tipoLocalEscolhido;

    public TipoLocal getTipoLocalEscolhido() {
        return tipoLocalEscolhido;
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
}
