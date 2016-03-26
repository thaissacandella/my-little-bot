package mylittlebot.bot;

public final class TextMessage extends Message {

	private String text;

	public TextMessage(String text, long userId) {
		super(userId);
		this.text = text;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	@Override
	public String toString() {
		return "TextMessage{text='"+ text +'\''+'}';
	}
}
