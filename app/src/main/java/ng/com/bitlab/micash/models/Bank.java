package ng.com.bitlab.micash.models;

/**
 * Created by paul on 12/8/17.
 */

public class Bank {

    private String name;
    private String number;
    private String bvn;

    public Bank(String name, String number, String bvn) {
        this.name = name;
        this.number = number;
        this.bvn = bvn;
    }

    public Bank() {
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

    public String getBvn() {
        return bvn;
    }

    public void setBvn(String bvn) {
        this.bvn = bvn;
    }
}
