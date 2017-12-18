package ng.com.bitlab.micash.models;

/**
 * Created by paul on 12/16/17.
 */

public class Guarantor {

    private String uuid;
    private String email;
    private String instanceID;

    public Guarantor(String uuid, String email, String instanceID) {
        this.uuid = uuid;
        this.email = email;
        this.instanceID = instanceID;
    }

    public Guarantor() {
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getInstanceID() {
        return instanceID;
    }

    public void setInstanceID(String instanceID) {
        this.instanceID = instanceID;
    }
}
