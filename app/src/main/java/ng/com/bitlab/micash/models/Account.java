package ng.com.bitlab.micash.models;

/**
 * Created by paul on 12/2/17.
 */

public class Account {

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

    public Account(String bank, String number, String bvn, Card card) {
        this.bank = bank;
        this.number = number;
        this.bvn = bvn;
        this.card = card;
    }

    public Account() {
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
