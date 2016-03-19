package br.com.botmotor.bot;

public final class MessageText extends Message {

	private final String text;

	public MessageText(String text, long userId) {
		super(userId);
		this.text = text;
	}

	public String getText() {
		return text;
	}

	@Override
	public String toString() {
		return "MessageText{" +
				"text='" + text + '\'' +
				'}';
	}
}
