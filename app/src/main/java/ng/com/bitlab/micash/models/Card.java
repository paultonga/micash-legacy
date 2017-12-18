package ng.com.bitlab.micash.models;

import com.orm.SugarRecord;

/**
 * Created by paul on 12/2/17.
 */

public class Card {

    private String name;
    private String number;
    private String cvc;
    private String expiry;

    public Card(String name, String number, String cvc, String expiry) {
        this.name = name;
        this.number = number;
        this.cvc = cvc;
        this.expiry = expiry;
    }

    public Card() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getCvc() {
        return cvc;
    }

    public void setCvc(String cvc) {
        this.cvc = cvc;
    }

    public String getExpiry() {
        return expiry;
    }

    public void setExpiry(String expiry) {
        this.expiry = expiry;
    }
}
