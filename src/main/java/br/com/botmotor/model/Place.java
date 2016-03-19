package br.com.botmotor.model;

import se.walkercrou.places.Status;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;

/**
 * @author "<a href='jpbassinello@gmail.com'>João Paulo Bassinello</a>"
 */
public final class Place implements Comparable<Place> {
	private final String name;
	private final List<String> types;
	private final String address;
	private final String googleUrl;
	private final String phoneNumber;
	private final String website;
	private final BigDecimal latitude;
	private final BigDecimal longitude;
	private final Status status;
	private final BigDecimal rating;
	private final BigDecimal distanceBetweenOrigin;
	private String imgUrl;

	public Place(se.walkercrou.places.Place place, double latitudeOrigin,
			double longitueOrigin, String apiKey) {
		this.name = place.getName();
		this.types = place.getTypes();
		this.address = place.getAddress();
		this.googleUrl = place.getGoogleUrl();
		this.phoneNumber = place.getPhoneNumber();
		this.website = place.getWebsite();
		this.latitude = BigDecimal.valueOf(place.getLatitude());
		this.longitude = BigDecimal.valueOf(place.getLongitude());
		this.status = place.getStatus();
		this.rating = BigDecimal.valueOf(place.getRating());
		this.distanceBetweenOrigin = distFrom(latitudeOrigin, longitueOrigin,
				place.getLatitude(), place.getLongitude());

		try {
			String photoReference = (String) place.getJson().getJSONArray
					("photos").getJSONObject(0).get("photo_reference");

			this.imgUrl = "https://maps.googleapis" +
					".com/maps/api/place/photo?key=" + apiKey +
					"&photoreference=" + photoReference + "&maxheight=400";
		} catch (Exception e) {
			// provavelmente não tem imagem
		}
	}

	// TODO: verificar menor distância percorrendo as ruas
	// http://stackoverflow.com/questions/837872/calculate-distance-in-meters
	// -when-you-know-longitude-and-latitude-in-java
	private BigDecimal distFrom(double lat1, double lng1, double lat2,
			double lng2) {
		double earthRadius = 6371000; //meters
		double dLat = Math.toRadians(lat2 - lat1);
		double dLng = Math.toRadians(lng2 - lng1);
		double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) + Math.cos(Math
				.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
				Math.sin(dLng / 2) * Math.sin(dLng / 2);
		double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
		float dist = (float) (earthRadius * c);

		return BigDecimal.valueOf(dist);
	}

	public String getName() {
		return name;
	}

	public List<String> getTypes() {
		return types;
	}

	public String getAddress() {
		return address;
	}

	public String getGoogleUrl() {
		return googleUrl;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public String getWebsite() {
		return website;
	}

	public BigDecimal getLatitude() {
		return latitude;
	}

	public BigDecimal getLongitude() {
		return longitude;
	}

	public Status getStatus() {
		return status;
	}

	public BigDecimal getRating() {
		return rating;
	}

	public BigDecimal getDistanceBetweenOrigin() {
		return distanceBetweenOrigin;
	}

	public String getImgUrl() {
		return imgUrl;
	}

	@Override
	public String toString() {
		return "Place{" +
				"name='" + name + '\'' +
				", types=" + types +
				", latitude=" + latitude +
				", longitude=" + longitude +
				", status=" + status +
				", rating=" + rating +
				", distanceBetweenOrigin=" + distanceBetweenOrigin +
				'}';
	}

	public String toLine() {
		return this.name;
	}

	@Override
	public int compareTo(Place o) {
		return Comparator.comparing(Place::getDistanceBetweenOrigin)
				.thenComparing(Place::getRating).compare(this, o);
	}
}
