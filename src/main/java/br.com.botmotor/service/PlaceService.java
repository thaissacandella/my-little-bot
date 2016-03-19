package br.com.botmotor.service;

import br.com.botmotor.model.Place;
import se.walkercrou.places.GooglePlaces;
import se.walkercrou.places.Param;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author "<a href='jpbassinello@gmail.com'>João Paulo Bassinello</a>"
 */
public class PlaceService {

	private static final String GOOGLE_KEY =
			"AIzaSyBU9anoKp46N76GcBh9tzN_y5u0eYABuFo";
	private static final double DEFAULT_RADIUS = 5000D;
	private static final int DEFAULT_NUMBER_OF_RESULTS = 10;
	private static final String TYPES;

	static {
		try {
			TYPES = URLEncoder.encode("bar|cafe|restaurant", "UTF-8");
		} catch (UnsupportedEncodingException e) {
			throw new IllegalStateException(e);
		}
	}

	public static void main(String[] args) {
		final PlaceService placeService = new PlaceService();

		// teste1: by lat long
		final double latitude = -22.893990D;
		final double longitude = -47.047745D;

		System.out.println("================= teste1: by lat long");
		placeService.printInSysOut(placeService.getPlaces(latitude,
				longitude));

		// teste2: by address
		String address = "Rua Adelino Martins, 500, Campinas - SP";
		System.out.println("\n\n================= teste2: by address");
		placeService.printInSysOut(placeService.getPlaces(address));
	}

	private void printInSysOut(List<Place> places) {
		System.out.print(places.stream().map(Place::toString).collect
				(Collectors.joining("\n\n")));
	}

	public List<Place> getPlaces(double latitude, double longitude) {
		GooglePlaces client = new GooglePlaces(GOOGLE_KEY);
		final Param[] params = {new Param("rankBy").value("distance"), new
				Param("types").value(TYPES)};
		final List<se.walkercrou.places.Place> nearbyPlaces = client
				.getNearbyPlaces(latitude, longitude, DEFAULT_RADIUS,
						DEFAULT_NUMBER_OF_RESULTS, params);
		return nearbyPlaces.stream().map(p -> new Place(p, latitude,
				longitude)).sorted().collect(Collectors.toList());
	}

	public List<Place> getPlaces(String address) {
		GooglePlaces client = new GooglePlaces(GOOGLE_KEY);
		final List<se.walkercrou.places.Place> placesByQuery = client
				.getPlacesByQuery(address, DEFAULT_NUMBER_OF_RESULTS);
		if (placesByQuery == null || placesByQuery.isEmpty()) {
			return Collections.emptyList();
		}
		// by the default, the address is a single place
		se.walkercrou.places.Place place = placesByQuery.get(0);
		return getPlaces(place.getLatitude(), place.getLongitude());
	}

}
