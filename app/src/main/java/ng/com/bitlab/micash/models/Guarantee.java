package ng.com.bitlab.micash.models;

/**
 * Created by paul on 12/14/17.
 */

public class Guarantee {

    private String uuid;
    private String email;
    private String amount;
    private String token;
    private String requester_uuid;
    private String requester_name;
    private long date_created;


    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Guarantee() {
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getRequester_uuid() {
        return requester_uuid;
    }

    public void setRequester_uuid(String requester_uuid) {
        this.requester_uuid = requester_uuid;
    }

    public String getRequester_name() {
        return requester_name;
    }

    public void setRequester_name(String requester_name) {
        this.requester_name = requester_name;
    }

    public long getDate_created() {
        return date_created;
    }

    public void setDate_created(long date_created) {
        this.date_created = date_created;
    }
}
