package ng.com.bitlab.micash.models;

import java.net.URL;

/**
 * Created by paul on 1/9/18.
 */

public class Notif {

    private String title;
    private String description;
    private long created;
    private boolean isRead;
    private boolean isOpen;


    public Notif() {
    }

    public Notif(String title, String description, long created, boolean isRead, boolean isOpen) {
        this.title = title;
        this.description = description;
        this.created = created;
        this.isRead = isRead;
        this.isOpen = isOpen;

    }


    public long getCreated() {
        return created;
    }

    public void setCreated(long created) {
        this.created = created;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }


    public boolean isRead() {
        return isRead;
    }

    public void setRead(boolean read) {
        isRead = read;
    }

    public boolean isOpen() {
        return isOpen;
    }

    public void setOpen(boolean open) {
        isOpen = open;
    }
}
