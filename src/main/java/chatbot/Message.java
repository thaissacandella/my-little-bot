package chatbot;

/**
 * Created by luan on 3/19/16.
 */
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
}
