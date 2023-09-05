// Generated by view binder compiler. Do not edit!
package com.example.joinn.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.viewbinding.ViewBinding;
import androidx.viewbinding.ViewBindings;
import com.example.joinn.R;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;

public final class FragmentRegisterBinding implements ViewBinding {
  @NonNull
  private final ConstraintLayout rootView;

  @NonNull
  public final TextView TitleText;

  @NonNull
  public final Button afterBtn;

  @NonNull
  public final Button beforeBtn;

  @NonNull
  public final EditText titleEt;

  private FragmentRegisterBinding(@NonNull ConstraintLayout rootView, @NonNull TextView TitleText,
      @NonNull Button afterBtn, @NonNull Button beforeBtn, @NonNull EditText titleEt) {
    this.rootView = rootView;
    this.TitleText = TitleText;
    this.afterBtn = afterBtn;
    this.beforeBtn = beforeBtn;
    this.titleEt = titleEt;
  }

  @Override
  @NonNull
  public ConstraintLayout getRoot() {
    return rootView;
  }

  @NonNull
  public static FragmentRegisterBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static FragmentRegisterBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.fragment_register, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static FragmentRegisterBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.TitleText;
      TextView TitleText = ViewBindings.findChildViewById(rootView, id);
      if (TitleText == null) {
        break missingId;
      }

      id = R.id.after_Btn;
      Button afterBtn = ViewBindings.findChildViewById(rootView, id);
      if (afterBtn == null) {
        break missingId;
      }

      id = R.id.before_Btn;
      Button beforeBtn = ViewBindings.findChildViewById(rootView, id);
      if (beforeBtn == null) {
        break missingId;
      }

      id = R.id.title_et;
      EditText titleEt = ViewBindings.findChildViewById(rootView, id);
      if (titleEt == null) {
        break missingId;
      }

      return new FragmentRegisterBinding((ConstraintLayout) rootView, TitleText, afterBtn,
          beforeBtn, titleEt);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}