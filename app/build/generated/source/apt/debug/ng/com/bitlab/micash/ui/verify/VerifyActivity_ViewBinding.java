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
import java.lang.IllegalStateException;
import java.lang.Override;
import ng.com.bitlab.micash.R;

public class VerifyActivity_ViewBinding implements Unbinder {
  private VerifyActivity target;

  private View view2131231046;

  private View view2131230760;

  private View view2131230765;

  private View view2131231032;

  @UiThread
  public VerifyActivity_ViewBinding(VerifyActivity target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public VerifyActivity_ViewBinding(final VerifyActivity target, View source) {
    this.target = target;

    View view;
    target.phoneNumberLayout = Utils.findRequiredViewAsType(source, R.id.phone_number_layout, "field 'phoneNumberLayout'", RelativeLayout.class);
    target.codeLayout = Utils.findRequiredViewAsType(source, R.id.code_layout, "field 'codeLayout'", RelativeLayout.class);
    target.phoneInput = Utils.findRequiredViewAsType(source, R.id.editTextPhone, "field 'phoneInput'", MaskedEditText.class);
    target.codeInput = Utils.findRequiredViewAsType(source, R.id.editTextCode, "field 'codeInput'", MaskedEditText.class);
    view = Utils.findRequiredView(source, R.id.tv_resend, "field 'textViewResend' and method 'onResendCodeClicked'");
    target.textViewResend = Utils.castView(view, R.id.tv_resend, "field 'textViewResend'", TextView.class);
    view2131231046 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onResendCodeClicked();
      }
    });
    target.verifyNotice = Utils.findRequiredViewAsType(source, R.id.tv_verify_notice, "field 'verifyNotice'", TextView.class);
    view = Utils.findRequiredView(source, R.id.btn_request_code, "field 'btnRequestCode' and method 'onRequestCodeClicked'");
    target.btnRequestCode = Utils.castView(view, R.id.btn_request_code, "field 'btnRequestCode'", Button.class);
    view2131230760 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onRequestCodeClicked();
      }
    });
    view = Utils.findRequiredView(source, R.id.btn_verify, "field 'btnVerify' and method 'onVerifyClicked'");
    target.btnVerify = Utils.castView(view, R.id.btn_verify, "field 'btnVerify'", Button.class);
    view2131230765 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onVerifyClicked();
      }
    });
    view = Utils.findRequiredView(source, R.id.tv_edit_number, "method 'showEditNumberDialog'");
    view2131231032 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.showEditNumberDialog();
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
    target.codeLayout = null;
    target.phoneInput = null;
    target.codeInput = null;
    target.textViewResend = null;
    target.verifyNotice = null;
    target.btnRequestCode = null;
    target.btnVerify = null;

    view2131231046.setOnClickListener(null);
    view2131231046 = null;
    view2131230760.setOnClickListener(null);
    view2131230760 = null;
    view2131230765.setOnClickListener(null);
    view2131230765 = null;
    view2131231032.setOnClickListener(null);
    view2131231032 = null;
  }
}
