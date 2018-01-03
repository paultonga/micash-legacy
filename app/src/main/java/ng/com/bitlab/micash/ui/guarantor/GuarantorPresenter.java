package ng.com.bitlab.micash.ui.guarantor;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;

import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.List;

import ng.com.bitlab.micash.listeners.FirebaseDataListener;
import ng.com.bitlab.micash.models.Guarantee;
import ng.com.bitlab.micash.models.Guarantor;
import ng.com.bitlab.micash.ui.common.BasePresenter;

/**
 * Created by Paul Tonga on 21/12/2017.
 */

public class GuarantorPresenter extends BasePresenter<GuarantorContract.View> implements GuarantorContract.Presenter {

    private GuarantorContract.Repository mRepository;
    GuarantorContract.View mView;

    public GuarantorPresenter(GuarantorContract.View mView) {
        this.mView = mView;
        mRepository = new GuarantorRepository();
    }

    @Override
    public void fetchGuarantorRequests() {
        mView.showRecyclerView(getDummyData());
        /*String uuid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        mRepository.loadGuarantorRequests(uuid, new FirebaseDataListener() {
            @Override
            public void onStart() {
                mView.showLoadingLayout();
            }

            @Override
            public void onSuccess(DataSnapshot ds) {
                if(ds.exists()){
                    List<Guarantee> guarantees = new ArrayList<>();
                    for(DataSnapshot dataSnapshot : ds.getChildren()){
                        Guarantee g = dataSnapshot.getValue(Guarantee.class);
                        guarantees.add(g);
                    }
                    //mView.showRecyclerView(guarantees);
                }else {
                    //mView.showEmptyDataLayout();
                }
            }

            @Override
            public void onFailed(DatabaseError de) {
                mView.showEmptyDataLayout();
            }

            @Override
            public void onComplete(DatabaseError de) {

            }
        });
       */
    }

    @Override
    public void getSingleRequest() {

    }

    @Override
    public void approveRequest() {
        mView.showToast("Approve guarantor request");
    }

    @Override
    public void rejectRequest() {
        mView.showToast("Decline guarantor request");
    }

    public List<Guarantee> getDummyData(){
        List<Guarantee> guarantees = new ArrayList<>();

        Guarantee guarantee = new Guarantee();
        guarantee.setApproved(false);
        guarantee.setDecided(false);
        guarantee.setAmount("100,000");
        guarantee.setDate_created(DateTime.now().getMillis());
        guarantee.setEmail("paul.yhwh@gmail.com");
        guarantee.setRepaid(false);
        guarantee.setRequester_name("Paul Audu Tonga");
        guarantee.setRequester_uuid("dfdfdfdssfrrtr");
        guarantee.setUuid("sdsdsdsdsdsfdfdf");
        guarantee.setToken("sdsdsdsdsddrtrtrt");

        Guarantee g = new Guarantee();
        g.setApproved(true);
        g.setDecided(true);
        g.setAmount("80,000");
        g.setDate_created(DateTime.now().getMillis());
        g.setEmail("paul.yhwh@gmail.com");
        g.setRepaid(false);
        g.setRequester_name("John Doe");
        g.setRequester_uuid("dfdfdfdssfrrtr");
        g.setUuid("sdsdsdsdsdsfdfdf");
        g.setToken("sdsdsdsdsddrtrtrt");

        Guarantee g1 = new Guarantee();
        g1.setApproved(false);
        g1.setDecided(true);
        g1.setAmount("250,000");
        g1.setDate_created(DateTime.now().getMillis());
        g1.setEmail("paul.yhwh@gmail.com");
        g1.setRepaid(false);
        g1.setRequester_name("Jane Doe");
        g1.setRequester_uuid("dfdfdfdssfrrtr");
        g1.setUuid("sdsdsdsdsdsfdfdf");
        g1.setToken("sdsdsdsdsddrtrtrt");


        guarantees.add(guarantee);
        guarantees.add(g);
        guarantees.add(g1);
        return guarantees;
    }
}
