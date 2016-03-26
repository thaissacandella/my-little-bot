package mylittlebot.bot;

public final class LocationMessage extends Message {

	private final double latitude;
	private final double longitude;

	public LocationMessage(double latitude, double longitude, long userId) {
		super(userId);
		this.latitude = latitude;
		this.longitude = longitude;
	}

	public double getLatitude() {
		return latitude;
	}

	public double getLongitude() {
		return longitude;
	}

	@Override
	public String toString() {
		return "LocationMessage{ latitude=" + latitude +", longitude="+ longitude +'}';
	}
}
