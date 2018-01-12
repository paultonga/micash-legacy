package ng.com.bitlab.micash.common;

import net.orange_box.storebox.annotations.method.ClearMethod;
import net.orange_box.storebox.annotations.method.KeyByString;
import net.orange_box.storebox.annotations.method.RemoveMethod;

import ng.com.bitlab.micash.utils.Constants;

/**
 * Created by Paul on 04/10/2017.
 */

public interface AppPreference {

    @KeyByString("key_notifs")
    void setNotifCount(int count);

    @KeyByString("key_notifs")
    int getNotifCount();

    //Name
    @KeyByString("key_name")
    void setName(String name);

    @KeyByString("key_name")
    String getName();

    //Email
    @KeyByString("key_email")
    void setEmail(String email);

    @KeyByString("key_email")
    String getEmail();

    //Password
    @KeyByString("key_password")
    void setPassword(String password);

    @KeyByString("key_password")
    String getPassword();

    @KeyByString("key_password")
    @RemoveMethod
    void removePassword();

    //Phone
    @KeyByString("key_phone")
    void setPhone(String phone);

    @KeyByString("key_phone")
    String getPhone();

    //Code
    @KeyByString("key_code")
    void setCode(String code);

    @KeyByString("key_code")
    String getCode();


    //User
    @KeyByString(Constants.USER)
    void setUser(String user);

    @KeyByString(Constants.USER)
    String getUser();

    //User Profile Image
    @KeyByString("key_profile")
    void setProfile(String profile);

    @KeyByString("key_profile")
    String getProfile();

    //First launch boolean

    @KeyByString("key_first")
    void setFirst(String first);

    @KeyByString("key_first")
    String getFirst();

    //Device token
    @KeyByString("key_token")
    void setToken(String first);

    @KeyByString("key_token")
    String getToken();


    //Clear all preferences
    @ClearMethod
    void clear();

    //Request Loan Check
    @KeyByString("key_saved")
    void setProfileSaved(String saved);

    @KeyByString("key_saved")
    String getProfileSaved();

    @KeyByString("key_saved")
    void setBankingSaved(String saved);

    @KeyByString("key_saved")
    String getBankingSaved();

    //if Contact Detauls are save
    @KeyByString("key_saved")
    void setBContactSaved(String saved);

    @KeyByString("key_saved")
    String getContactSaved();

    //if Employment saved
    @KeyByString("key_saved")
    void setEmploymentSaved(String saved);

    @KeyByString("key_saved")
    String getEmploymentSaved();












}
