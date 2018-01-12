package ng.com.bitlab.micash.models;

/**
 * Created by paul on 1/5/18.
 */

public class Message {

    private String from;
    private String message;
    private long date;

    public Message() {
    }

    public Message(String from, String message, long date) {
        this.from = from;
        this.message = message;
        this.date = date;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }
}
