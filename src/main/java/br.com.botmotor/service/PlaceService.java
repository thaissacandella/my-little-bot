package br.com.botmotor.service;

import se.walkercrou.places.GooglePlaces;
import se.walkercrou.places.Place;

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
	private static final int DEFAULT_NUMBER_OF_RESULTS = 20;
	private static final String TYPES = "";

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
		System.out.println("================= teste2: by address");
		placeService.printInSysOut(placeService.getPlaces(address));
	}

	private void printInSysOut(List<br.com.botmotor.model.Place> places) {
		System.out.print(places.stream().map(br.com.botmotor.model
				.Place::toString).collect(Collectors.joining("\n\n")));
	}

	public List<br.com.botmotor.model.Place> getPlaces(double latitude,
			double longitude) {
		GooglePlaces client = new GooglePlaces(GOOGLE_KEY);
		return client.getNearbyPlaces(latitude, longitude, DEFAULT_RADIUS,
				DEFAULT_NUMBER_OF_RESULTS).stream().map(p -> new br.com
				.botmotor.model.Place(p, latitude, longitude)).collect
				(Collectors.toList());
	}

	public List<br.com.botmotor.model.Place> getPlaces(String address) {
		GooglePlaces client = new GooglePlaces(GOOGLE_KEY);
		final List<Place> placesByQuery = client.getPlacesByQuery(address,
				DEFAULT_NUMBER_OF_RESULTS);
		if (placesByQuery == null || placesByQuery.isEmpty()) {
			return Collections.emptyList();
		}
		// by the default, the address is a single place
		Place place = placesByQuery.get(0);
		return getPlaces(place.getLatitude(), place.getLongitude());
	}

}
