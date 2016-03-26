package mylittlebot.bot;

public abstract class Message {
	/*
	{"update_id":344901419,
"message":{"message_id":7,"from":{"id":192468144,"first_name":"Lucas",
"last_name":"Laurindo dos Santos"},"chat":{"id":192468144,
"first_name":"Lucas","last_name":"Laurindo dos Santos","type":"private"},
"date":1458403367,"location":{"longitude":-47.047745,"latitude":-22
.893990}}},{"update_id":344901420,
"message":{"message_id":8,"from":{"id":98891960,"first_name":"Felipe",
"last_name":"Mir","username":"Felipe_Mir"},"chat":{"id":98891960,
"first_name":"Felipe","last_name":"Mir","username":"Felipe_Mir",
"type":"private"},"date":1458404679,"text":"Oi"}}
	 */

	private long userId;

	public Message(long user) {
		this.userId = user;
	}

	public long getUserId() {
		return userId;
	}

	@Override
	public String toString() {
		return "Message{" +
				"userId=" + userId +
				'}';
	}
}
