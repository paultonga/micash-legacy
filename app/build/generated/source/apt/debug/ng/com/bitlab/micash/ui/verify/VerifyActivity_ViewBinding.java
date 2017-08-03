// Generated code from Butter Knife. Do not modify!
package ng.com.bitlab.micash.ui.verify;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import butterknife.Unbinder;
import butterknife.internal.DebouncingOnClickListener;
import butterknife.internal.Utils;
import com.github.reinaldoarrosi.maskededittext.MaskedEditText;
import de.hdodenhof.circleimageview.CircleImageView;
import java.lang.IllegalStateException;
import java.lang.Override;
import ng.com.bitlab.micash.R;

public class VerifyActivity_ViewBinding implements Unbinder {
  private VerifyActivity target;

  private View view2131558579;

  private View view2131558581;

  private View view2131558586;

  private View view2131558576;

  private View view2131558580;

  private View view2131558585;

  private View view2131558590;

  @UiThread
  public VerifyActivity_ViewBinding(VerifyActivity target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public VerifyActivity_ViewBinding(final VerifyActivity target, View source) {
    this.target = target;

    View view;
    target.phoneNumberLayout = Utils.findRequiredViewAsType(source, R.id.phone_number_layout, "field 'phoneNumberLayout'", RelativeLayout.class);
    target.profileImageLayout = Utils.findRequiredViewAsType(source, R.id.profile_image_layout, "field 'profileImageLayout'", RelativeLayout.class);
    target.codeLayout = Utils.findRequiredViewAsType(source, R.id.code_layout, "field 'codeLayout'", RelativeLayout.class);
    target.completedLayout = Utils.findRequiredViewAsType(source, R.id.completed_layout, "field 'completedLayout'", RelativeLayout.class);
    target.phoneInput = Utils.findRequiredViewAsType(source, R.id.editTextPhone, "field 'phoneInput'", MaskedEditText.class);
    target.codeInput = Utils.findRequiredViewAsType(source, R.id.editTextCode, "field 'codeInput'", MaskedEditText.class);
    view = Utils.findRequiredView(source, R.id.profile_image, "field 'profileImage' and method 'selectProfileImage'");
    target.profileImage = Utils.castView(view, R.id.profile_image, "field 'profileImage'", CircleImageView.class);
    view2131558579 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.selectProfileImage();
      }
    });
    view = Utils.findRequiredView(source, R.id.tv_skip, "field 'textViewSkip' and method 'onSkipProfilePictureClicked'");
    target.textViewSkip = Utils.castView(view, R.id.tv_skip, "field 'textViewSkip'", TextView.class);
    view2131558581 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onSkipProfilePictureClicked();
      }
    });
    view = Utils.findRequiredView(source, R.id.tv_resend, "field 'textViewResend' and method 'onResendCodeClicked'");
    target.textViewResend = Utils.castView(view, R.id.tv_resend, "field 'textViewResend'", TextView.class);
    view2131558586 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onResendCodeClicked();
      }
    });
    view = Utils.findRequiredView(source, R.id.btn_request_code, "field 'btnRequestCode' and method 'onRequestCodeClicked'");
    target.btnRequestCode = Utils.castView(view, R.id.btn_request_code, "field 'btnRequestCode'", Button.class);
    view2131558576 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onRequestCodeClicked();
      }
    });
    view = Utils.findRequiredView(source, R.id.btn_set_profile_image, "field 'btnSaveProfileImage' and method 'onSetProfilePictureClicked'");
    target.btnSaveProfileImage = Utils.castView(view, R.id.btn_set_profile_image, "field 'btnSaveProfileImage'", Button.class);
    view2131558580 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onSetProfilePictureClicked();
      }
    });
    view = Utils.findRequiredView(source, R.id.btn_verify, "field 'btnVerify' and method 'onVerifyClicked'");
    target.btnVerify = Utils.castView(view, R.id.btn_verify, "field 'btnVerify'", Button.class);
    view2131558585 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onVerifyClicked();
      }
    });
    view = Utils.findRequiredView(source, R.id.btn_get_started, "field 'btnGetStarted', method 'startMainActivity', and method 'getStartedClicked'");
    target.btnGetStarted = Utils.castView(view, R.id.btn_get_started, "field 'btnGetStarted'", Button.class);
    view2131558590 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.startMainActivity();
        target.getStartedClicked();
      }
    });
  }

  @Override
  @CallSuper
  public void unbind() {
    VerifyActivity target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.phoneNumberLayout = null;
    target.profileImageLayout = null;
    target.codeLayout = null;
    target.completedLayout = null;
    target.phoneInput = null;
    target.codeInput = null;
    target.profileImage = null;
    target.textViewSkip = null;
    target.textViewResend = null;
    target.btnRequestCode = null;
    target.btnSaveProfileImage = null;
    target.btnVerify = null;
    target.btnGetStarted = null;

    view2131558579.setOnClickListener(null);
    view2131558579 = null;
    view2131558581.setOnClickListener(null);
    view2131558581 = null;
    view2131558586.setOnClickListener(null);
    view2131558586 = null;
    view2131558576.setOnClickListener(null);
    view2131558576 = null;
    view2131558580.setOnClickListener(null);
    view2131558580 = null;
    view2131558585.setOnClickListener(null);
    view2131558585 = null;
    view2131558590.setOnClickListener(null);
    view2131558590 = null;
  }
}
