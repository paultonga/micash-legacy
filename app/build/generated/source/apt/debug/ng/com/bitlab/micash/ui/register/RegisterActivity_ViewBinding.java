// Generated code from Butter Knife. Do not modify!
package ng.com.bitlab.micash.ui.register;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import butterknife.Unbinder;
import butterknife.internal.DebouncingOnClickListener;
import butterknife.internal.Utils;
import java.lang.IllegalStateException;
import java.lang.Override;
import ng.com.bitlab.micash.R;

public class RegisterActivity_ViewBinding implements Unbinder {
  private RegisterActivity target;

  private View view2131296315;

  private View view2131296653;

  @UiThread
  public RegisterActivity_ViewBinding(RegisterActivity target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public RegisterActivity_ViewBinding(final RegisterActivity target, View source) {
    this.target = target;

    View view;
    target.passwordEditText = Utils.findRequiredViewAsType(source, R.id.editTextPassword, "field 'passwordEditText'", EditText.class);
    target.nameEditText = Utils.findRequiredViewAsType(source, R.id.editTextName, "field 'nameEditText'", EditText.class);
    target.emailEditText = Utils.findRequiredViewAsType(source, R.id.editTextEmail, "field 'emailEditText'", EditText.class);
    target.agreementCheck = Utils.findRequiredViewAsType(source, R.id.check_agreement, "field 'agreementCheck'", CheckBox.class);
    view = Utils.findRequiredView(source, R.id.btn_register, "method 'onRegisterClicked'");
    view2131296315 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onRegisterClicked();
      }
    });
    view = Utils.findRequiredView(source, R.id.tv_agreement, "method 'showTermsAndConditions'");
    view2131296653 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.showTermsAndConditions();
      }
    });
  }

  @Override
  @CallSuper
  public void unbind() {
    RegisterActivity target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.passwordEditText = null;
    target.nameEditText = null;
    target.emailEditText = null;
    target.agreementCheck = null;

    view2131296315.setOnClickListener(null);
    view2131296315 = null;
    view2131296653.setOnClickListener(null);
    view2131296653 = null;
  }
}
