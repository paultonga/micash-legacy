package ng.com.bitlab.micash.models;

import android.support.annotation.Nullable;

import java.util.Date;

/**
 * Created by Paul on 10/06/2017.
 */

public class User {

    private String uuid;
    private String fullName;
    private String email;
    private long dateCreated;
    private long lastSeen;
    @Nullable private String profileImage;
    private String phoneNumber;
    public Device device;

    public User(){}

    public User(String uuid, String fullName, String email, long dateCreated, long lastSeen, String profileImage, String phoneNumber) {
        this.uuid = uuid;
        this.fullName = fullName;
        this.email = email;
        this.dateCreated = dateCreated;
        this.lastSeen = lastSeen;
        this.profileImage = profileImage;
        this.phoneNumber = phoneNumber;
    }


    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public long getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(long dateCreated) {
        this.dateCreated = dateCreated;
    }

    public long getLastSeen() {
        return lastSeen;
    }

    public void setLastSeen(long lastSeen) {
        this.lastSeen = lastSeen;
    }

    public String getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

}
