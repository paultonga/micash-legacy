package ng.com.bitlab.micash.models;

import com.orm.SugarRecord;

/**
 * Created by Paul on 10/06/2017.
 */

public class Notification extends SugarRecord<Notification> {

    private String title;
    private String detail;
    private long dateSent;
    private boolean isRead;
    private String identifier;

    public Notification(){}



    public Notification(String title, String detail, long dateSent, boolean isRead, String identifier) {
        this.title = title;
        this.detail = detail;
        this.dateSent = dateSent;
        this.isRead = isRead;
        this.identifier = identifier;
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

    public boolean isRead() {
        return isRead;
    }

    public void setRead(boolean read) {
        isRead = read;
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

}
