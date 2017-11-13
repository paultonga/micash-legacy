package ng.com.bitlab.micash.models;

import com.orm.SugarRecord;

/**
 * Created by Paul on 10/06/2017.
 */

public class Notification extends SugarRecord<Notification> {

    private String title;
    private String detail;
    private long dateSent;

    public Notification(){}

    public Notification(String title, String detail, long dateSent) {
        this.title = title;
        this.detail = detail;
        this.dateSent = dateSent;
    }

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
