package mylittlebot.bot;

public abstract class Message {

	private long userId;

	public Message(long user) {
		this.userId = user;
	}

	public long getUserId() {
		return userId;
	}

	@Override
	public String toString() {
		return "Message{userId=" + userId + "}";
	}
}
