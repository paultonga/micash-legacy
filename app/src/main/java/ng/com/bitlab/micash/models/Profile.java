package ng.com.bitlab.micash.models;

import android.support.annotation.Nullable;

import com.google.gson.annotations.Expose;
import com.orm.SugarRecord;

/**
 * Created by Paul on 10/06/2017.
 */

public class Profile {

    private String status;
    private String office;
    private String address;

    @Nullable
    private String income;

    private String state;

    private String residential;

    public Profile() {
    }

    public Profile(String status, String office, String address, String income, String state, String residential) {

        this.status = status;
        this.office = office;
        this.address = address;
        this.income = income;
        this.state = state;
        this.residential = residential;
    }


    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getOffice() {
        return office;
    }

    public void setOffice(String office) {
        this.office = office;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }


    @Nullable
    public String getIncome() {
        return income;
    }

    public void setIncome(@Nullable String income) {
        this.income = income;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getResidential() {
        return residential;
    }

    public void setResidential(String residential) {
        this.residential = residential;
    }
}
