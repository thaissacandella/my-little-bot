package mylittlebot.bot;

import mylittlebot.model.Place;

import java.util.List;

public class UserSession {

	private Stage stage = Stage.CHOOSE_TYPE_LOCATION;
	private LocationType locationType;

	private double latitude;
	private double longitude;

	private List<Place> places;
	private int place;

	public LocationType getLocationType() {
		return locationType;
	}

	public void setLocation(LocationMessage locationMessage) {
		this.latitude = locationMessage.getLatitude();
		this.longitude = locationMessage.getLongitude();
	}

	public void setLocationType(LocationType locationType) {
		this.locationType = locationType;
	}

	public Stage getStage() {
		return stage;
	}

	public void setStage(Stage stage) {
		this.stage = stage;
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

	public void setPlace(int place) {
		this.place = place;
	}

	public int getPlace() {
		return place;
	}
}
