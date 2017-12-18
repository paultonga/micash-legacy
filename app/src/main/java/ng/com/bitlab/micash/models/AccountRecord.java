package ng.com.bitlab.micash.models;

import com.orm.SugarRecord;

/**
 * Created by paul on 12/3/17.
 */

public class AccountRecord extends SugarRecord<AccountRecord> {

    private String bank;
    private String number;
    private String bvn;
    private Card card;

    public Card getCard() {
        return card;
    }

    public void setCard(Card card) {
        this.card = card;
    }

    public AccountRecord(String bank, String number, String bvn, Card card) {
        this.bank = bank;
        this.number = number;
        this.bvn = bvn;
        this.card = card;
    }

    public AccountRecord() {
    }

    public AccountRecord(Account account){
        this.bank = account.getBank();
        this.bvn = account.getBvn();
        this.number = account.getNumber();
        this.card = account.getCard();
    }

    public String getBank() {
        return bank;
    }

    public void setBank(String bank) {
        this.bank = bank;
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
