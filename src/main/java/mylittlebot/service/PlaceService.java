package mylittlebot.service;

import mylittlebot.bot.LocationType;
import mylittlebot.model.Place;
import se.walkercrou.places.GooglePlaces;
import se.walkercrou.places.Param;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class PlaceService {

	private static final String GOOGLE_KEY =
			"AIzaSyBU9anoKp46N76GcBh9tzN_y5u0eYABuFo";
	private static final double DEFAULT_RADIUS = 5000D;
	private static final int DEFAULT_NUMBER_OF_RESULTS = 10;

	private void printInSysOut(List<Place> places) {
		System.out.print(places.stream().map(Place::toString).collect
				(Collectors.joining("\n\n")));
	}

	public List<Place> getPlaces(double latitude, double longitude,
			LocationType locationType) {
		GooglePlaces client = new GooglePlaces(GOOGLE_KEY);

		final Param[] params = {new Param("rankBy").value("distance"), new
				Param("types").value(locationType.getApiType())};
		final List<se.walkercrou.places.Place> nearbyPlaces = client
				.getNearbyPlaces(latitude, longitude, DEFAULT_RADIUS,
						DEFAULT_NUMBER_OF_RESULTS, params);
		return nearbyPlaces.stream().map(p -> new Place(p, latitude,
				longitude, GOOGLE_KEY)).sorted().collect(Collectors.toList());
	}

	public List<Place> getPlaces(String address, LocationType locationType) {
		GooglePlaces client = new GooglePlaces(GOOGLE_KEY);
		final List<se.walkercrou.places.Place> placesByQuery = client
				.getPlacesByQuery(address, DEFAULT_NUMBER_OF_RESULTS);
		if (placesByQuery == null || placesByQuery.isEmpty()) {
			return Collections.emptyList();
		}
		// by the default, the address is a single place
		se.walkercrou.places.Place place = placesByQuery.get(0);
		return getPlaces(place.getLatitude(), place.getLongitude(), locationType);
	}

}
