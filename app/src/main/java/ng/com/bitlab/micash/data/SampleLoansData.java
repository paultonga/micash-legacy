package ng.com.bitlab.micash.data;

import com.google.common.base.Predicate;
import com.google.common.collect.ImmutableList;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import ng.com.bitlab.micash.models.Interest;
import ng.com.bitlab.micash.models.Loan;

import static com.google.common.collect.FluentIterable.from;

/**
 * Created by Paul on 12/06/2017.
 */

public class SampleLoansData {

    public List<Loan> getLoans() {
        List<Loan> loans = new ArrayList<>();

        Loan loan1 = new Loan("onemonth",
                "One Month Advance",
                300000,
                "One month salary advance",
                "https://firebasestorage.googleapis.com/v0/b/micash-e8219.appspot.com/o/icons%2Fsalary_advance.png?alt=media&token=3a28a1d3-cf82-4222-94ce-7fc11d69eb8c");
        loans.add(loan1);

        Loan loan2 = new Loan("bankers",
                "Banker's Loan",
                1000000,
                "This loan is available to bankers in Nigeria",
                "https://firebasestorage.googleapis.com/v0/b/micash-e8219.appspot.com/o/icons%2Fbankers.png?alt=media&token=fdd8966e-6b48-46f4-b5e2-305107a46e3d");
        loans.add(loan2);

        Loan loan3 = new Loan("plus",
                "miCash Plus",
                1000000,
                "This loan is provided for Plus members of miCash.",
                "https://firebasestorage.googleapis.com/v0/b/micash-e8219.appspot.com/o/icons%2Fplus.png?alt=media&token=5fc88a22-c441-459a-aa35-61480b1b2f09");
        loans.add(loan3);

        Loan loan4 = new Loan("school",
                "School Fees Advance",
                500000,
                "This loan is to provide school fees to students and parents.",
                "https://firebasestorage.googleapis.com/v0/b/micash-e8219.appspot.com/o/icons%2Fstudent.png?alt=media&token=054b8d25-eae4-4ca7-8f7d-bfc5fd05fcf9");
        loans.add(loan4);

        Loan loan5 = new Loan("furniture",
                "Electronics & Furniture Loan",
                1000000,
                "This loan scheme is coming soon.",
                "https://firebasestorage.googleapis.com/v0/b/micash-e8219.appspot.com/o/icons%2Fhousehold.png?alt=media&token=fe106c2c-9353-4551-b24e-616314f48f94");
        loans.add(loan5);

        return loans;
    }

    public List<Interest> getInterests(){
        List<Interest> interests = new ArrayList<>();

        //Bankers
        Interest interest1 = new Interest("bankers1",
                "bankers", 1, 10, "10% if repaid within a month");
        interests.add(interest1);

        Interest interest2 = new Interest("bankers2",
                "bankers", 2, 5, "5% flat per month if repaid in 2 months");
        interests.add(interest2);

        Interest interest3 = new Interest("bankers3",
                "bankers", 3, 5, "5% flat per month if repaid in 3 months");
        interests.add(interest3);

        //miCash Plus
        Interest interest4 = new Interest("plus2",
                "plus", 2, 5, "5% flat per month if repaid in 2 months");
        interests.add(interest4);

        Interest interest5 = new Interest("plus3",
                "plus", 3, 5, "5% flat per month if repaid in 3 months");
        interests.add(interest5);

        Interest interest6 = new Interest("plus4",
                "plus", 4, 4.5, "4.5% flat per month if repaid in 4 months");
        interests.add(interest6);

        Interest interest7 = new Interest("plus5",
              "plus", 5, 4.5, "4.5% flat per month if repaid in 5 months");
        interests.add(interest7);

        //One Month Advance
        Interest interest8 = new Interest("onemonth1",
                "onemonth", 1, 10, "10% flat if repaid within a month");
        interests.add(interest8);

        //School Fees
        Interest interest9 = new Interest("school1",
                "school", 1, 4.5, "4.5% flat if repaid within a month");
        interests.add(interest9);

        Interest interest10 = new Interest("school2",
                "school", 2, 4.5, "4.5% flat per month if repaid in 2 months");
        interests.add(interest10);

        Interest interest11 = new Interest("school3",
                "school", 3, 4, "4% flat per month if repaid in 3 months");
        interests.add(interest11);

        Interest interest12 = new Interest("school3",
                "school", 4, 4, "4% flat per month if repaid in 4 months");
        interests.add(interest12);


        return interests;
    }

    public List<Interest> getInterestForLoan(final String loan_id){
        List<Interest> filtered = new ArrayList<>();

        for (Interest i : getInterests()){
            if (i.getLoan_id().equals(loan_id)){
                filtered.add(i);
            }
        }
        return filtered;
    }

    public Loan getLoan(String loan_id) {

        Loan loan = new Loan();
        for (Loan l : getLoans()) {
            if (l.getId().equals(loan_id)) {
                loan = l;
            }
        }
        return loan;
    }

    public String getLoanPeriodString(String loan_id){
        List<Interest> interests = getInterestForLoan(loan_id);


        int max = 0; int min = 1000;

        for(Interest i: interests){
            if (i.getMonths() > max)
                max = i.getMonths();

            if(i.getMonths() < min)
                min = i.getMonths();
        }

        if (max == min){
            return "0-1";
        } else if (max == 0 && min == 1000) {
            return " - ";
        }
        else {
            return  min + "-" + max;
        }
    }

}
