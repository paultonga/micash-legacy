package ng.com.bitlab.micash.models;

/**
 * Created by Paul Tonga on 07/02/2018.
 */

public class Ledger {

    private boolean isPaymentSent;
    private boolean isPaymentReceived;
    private long amount;
    private long totalAmount;
    private String loanTitle;
    private String uuid;
    private long dateDue;
    private long dateCreated;
    private long datePaid;
    private boolean isLastPayment;
    private String requestedAmount;


    public Ledger() {
    }

    public Ledger(boolean isPaymentSent, boolean isPaymentReceived, long amount, long totalAmount, String loanTitle, String uuid, long dateDue, long dateCreated, long datePaid, boolean isLastPayment, String requestedAmount) {
        this.isPaymentSent = isPaymentSent;
        this.isPaymentReceived = isPaymentReceived;
        this.amount = amount;
        this.totalAmount = totalAmount;
        this.loanTitle = loanTitle;
        this.uuid = uuid;
        this.dateDue = dateDue;
        this.dateCreated = dateCreated;
        this.datePaid = datePaid;
        this.isLastPayment = isLastPayment;
        this.requestedAmount = requestedAmount;
    }

    public String getRequestedAmount() {
        return requestedAmount;
    }

    public void setRequestedAmount(String requestedAmount) {
        this.requestedAmount = requestedAmount;
    }

    public boolean isPaymentSent() {
        return isPaymentSent;
    }

    public void setPaymentSent(boolean paymentSent) {
        isPaymentSent = paymentSent;
    }

    public boolean isPaymentReceived() {
        return isPaymentReceived;
    }

    public void setPaymentReceived(boolean paymentReceived) {
        isPaymentReceived = paymentReceived;
    }

    public long getAmount() {
        return amount;
    }

    public void setAmount(long amount) {
        this.amount = amount;
    }

    public long getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(long totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getLoanTitle() {
        return loanTitle;
    }

    public void setLoanTitle(String loanTitle) {
        this.loanTitle = loanTitle;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public long getDateDue() {
        return dateDue;
    }

    public void setDateDue(long dateDue) {
        this.dateDue = dateDue;
    }

    public long getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(long dateCreated) {
        this.dateCreated = dateCreated;
    }

    public long getDatePaid() {
        return datePaid;
    }

    public void setDatePaid(long datePaid) {
        this.datePaid = datePaid;
    }

    public boolean isLastPayment() {
        return isLastPayment;
    }

    public void setLastPayment(boolean lastPayment) {
        isLastPayment = lastPayment;
    }
}
