package ng.com.bitlab.micash.models;

/**
 * Created by Paul on 10/06/2017.
 */

public class Loan {

    private String id;
    private String title;
    private double maximum;
    private String description;
    private String icon_url;

    public Loan(String id, String title, double maximum, String description, String icon_url) {
        this.id = id;
        this.title = title;
        this.maximum = maximum;
        this.description = description;
        this.icon_url = icon_url;
    }

    public Loan(){}

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public double getMaximum() {
        return maximum;
    }

    public String getDescription() {
        return description;
    }

    public String getIcon_url() {
        return icon_url;
    }
}
