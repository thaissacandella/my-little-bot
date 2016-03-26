package mylittlebot.bot;

import se.walkercrou.places.Types;

public enum LocationType {
	RESTAURANTES(Types.TYPE_RESTAURANT),
	CAFES(Types.TYPE_CAFE),
	BARES(Types.TYPE_BAR);

	private final String apiType;

	LocationType(String apiType) {
		this.apiType = apiType;
	}

	public String getApiType() {
		return apiType;
	}
}
