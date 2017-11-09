package ng.com.bitlab.micash.models;

/**
 * Created by Paul on 10/06/2017.
 */

public class Notification {

    private String title;
    private String detail;
    private long dateSent;

    public Notification(){}

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public long getDateSent() {
        return dateSent;
    }

    public void setDateSent(long dateSent) {
        this.dateSent = dateSent;
    }

}
