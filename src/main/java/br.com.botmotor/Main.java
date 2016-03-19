package br.com.botmotor;

import com.sun.jersey.api.client.GenericType;

import java.util.List;

/**
 * @author "<a href='jpbassinello@gmail.com'>João Paulo Bassinello</a>"
 */
public class Main {

	public static void main(String[] args) {
		// https://api.telegram
		// .org/bot198737376:AAFrs1DR7fBwsYvKj_jDW6lZvwlOULFE9Y0/getupdates
		String botToken = "bot198737376:AAFrs1DR7fBwsYvKj_jDW6lZvwlOULFE9Y0";
		BotmotorClient client = new BotmotorClient(botToken);
		client.withEndpoint("/getupdates");

		String result = client.getSingleResult(String.class);

		System.out.println(result);
	}

}
