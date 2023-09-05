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

public final class ActivityNicknameBinding implements ViewBinding {
  @NonNull
  private final ConstraintLayout rootView;

  @NonNull
  public final EditText NickNameText;

  @NonNull
  public final TextView NickText;

  @NonNull
  public final Button SaveBtn;

  @NonNull
  public final TextView Text1;

  @NonNull
  public final TextView Text2;

  private ActivityNicknameBinding(@NonNull ConstraintLayout rootView,
      @NonNull EditText NickNameText, @NonNull TextView NickText, @NonNull Button SaveBtn,
      @NonNull TextView Text1, @NonNull TextView Text2) {
    this.rootView = rootView;
    this.NickNameText = NickNameText;
    this.NickText = NickText;
    this.SaveBtn = SaveBtn;
    this.Text1 = Text1;
    this.Text2 = Text2;
  }

  @Override
  @NonNull
  public ConstraintLayout getRoot() {
    return rootView;
  }

  @NonNull
  public static ActivityNicknameBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static ActivityNicknameBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.activity_nickname, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static ActivityNicknameBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.NickNameText;
      EditText NickNameText = ViewBindings.findChildViewById(rootView, id);
      if (NickNameText == null) {
        break missingId;
      }

      id = R.id.NickText;
      TextView NickText = ViewBindings.findChildViewById(rootView, id);
      if (NickText == null) {
        break missingId;
      }

      id = R.id.SaveBtn;
      Button SaveBtn = ViewBindings.findChildViewById(rootView, id);
      if (SaveBtn == null) {
        break missingId;
      }

      id = R.id.Text1;
      TextView Text1 = ViewBindings.findChildViewById(rootView, id);
      if (Text1 == null) {
        break missingId;
      }

      id = R.id.Text2;
      TextView Text2 = ViewBindings.findChildViewById(rootView, id);
      if (Text2 == null) {
        break missingId;
      }

      return new ActivityNicknameBinding((ConstraintLayout) rootView, NickNameText, NickText,
          SaveBtn, Text1, Text2);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}