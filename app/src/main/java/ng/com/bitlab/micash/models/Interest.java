package ng.com.bitlab.micash.models;

/**
 * Created by Paul on 27/09/2017.
 */

public classInterest {

    private String id;
    private String loan_id;
    private int months;
    private double percentage;
    private String description;

    public Interest(String id, String loan_id, int months, double percentage, String description) {
        this.id = id;
        this.loan_id = loan_id;
        this.months = months;
        this.percentage = percentage;
        this.description = description;
    }

    public Interest(){}

    public String getId() {
        return id;
    }

    public String getLoan_id() {
        return loan_id;
    }

    public int getMonths() {
        return months;
    }

    public double getPercentage() {
        return percentage;
    }

    public String getDescription() {
        return description;
    }
}
