package br.com.botmotor.bot;

public class Message {

	private String message;
	private Long user;

	public Message(Long user, String message) {
		this.user = user;
		this.message = message;
	}

	public String getMessage() {
		return message;
	}

	public Long getUser() {
		return user;
	}

	@Override
	public String toString() {
		return this.user + ": " + this.message;
	}
}
