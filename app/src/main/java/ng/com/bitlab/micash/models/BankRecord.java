package ng.com.bitlab.micash.models;

import com.orm.SugarRecord;

/**
 * Created by paul on 12/8/17.
 */

public class BankRecord extends SugarRecord<BankRecord> {

    private String name;
    private String number;
    private String bvn;

    public BankRecord(String name, String number, String bvn) {
        this.name = name;
        this.number = number;
        this.bvn = bvn;
    }

    public BankRecord() {
    }

    public BankRecord(Bank bank){
        this.name = bank.getName();
        this.bvn = bank.getBvn();
        this.number = bank.getNumber();
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
