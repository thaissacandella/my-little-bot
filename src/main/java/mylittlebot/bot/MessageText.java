package mylittlebot.bot;

public final class MessageText extends Message {

	private String text;

	public MessageText(String text, long userId) {
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
		return "MessageText{" +
				"text='" + text + '\'' +
				'}';
	}
}
