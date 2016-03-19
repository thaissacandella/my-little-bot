package br.com.botmotor;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import org.codehaus.jackson.jaxrs.JacksonJsonProvider;

import javax.ws.rs.Encoded;
import javax.ws.rs.HttpMethod;

/**
 * @author "<a href='jpbassinello@gmail.com'>João Paulo Bassinello</a>"
 */
@Encoded
public final class BotmotorClient {

	private static final String BASE_URL = "https://api.telegram.org/";
	private static final String BOT_TOKEN =
			"bot198737376:AAFrs1DR7fBwsYvKj_jDW6lZvwlOULFE9Y0";
	private static final String DEFAULT_CHARSET = "UTF-8";
	private static final int TIMEOUT = 20000;
	private String endpoint;
	private String HTTP_METHOD = HttpMethod.GET;
	private String getParameters;

	public BotmotorClient() {
	}

	public BotmotorClient withEndpoint(String endpoint) {
		this.endpoint = endpoint;
		return this;
	}

	public BotmotorClient withGetParameters(String parameters) {
		this.getParameters = parameters;
		return this;
	}

	public <T> T getSingleResult(Class<T> returnType) {
		ClientResponse response = makeRequest();
		if (returnType == null) {
			return null;
		}
		T readEntity = response.getEntity(returnType);
		reset();
		return readEntity;
	}

	private void reset() {
		this.endpoint = null;
		this.HTTP_METHOD = null;
	}

	private ClientResponse makeRequest() {
		WebResource webResource = buildClient().resource(BASE_URL + BOT_TOKEN +
				endpoint + getParameters);
		return webResource.method(HTTP_METHOD, ClientResponse.class);
	}

	private Client buildClient() {
		final DefaultClientConfig clientConfig = new DefaultClientConfig();
		clientConfig.getClasses().add(JacksonJsonProvider.class);
		Client client = Client.create(clientConfig);
		client.setConnectTimeout(TIMEOUT);
		client.setReadTimeout(TIMEOUT);

		return client;
	}

}