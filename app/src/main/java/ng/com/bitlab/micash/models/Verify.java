package ng.com.bitlab.micash.models;

/**
 * Created by Paul on 11/09/2017.
 */

public class Verify {

    private String uuid;
    private String phone;
    private long dateCreated;
    private String code;

    public Verify(String uuid, String phone, long dateCreated, String code) {
        this.uuid = uuid;
        this.phone = phone;
        this.dateCreated = dateCreated;
        this.code = code;
    }

    public  Verify(){

    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public long getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(long dateCreated) {
        this.dateCreated = dateCreated;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
