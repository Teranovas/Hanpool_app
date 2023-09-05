// Generated by view binder compiler. Do not edit!
package com.example.joinn.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewbinding.ViewBinding;
import androidx.viewbinding.ViewBindings;
import com.airbnb.lottie.LottieAnimationView;
import com.example.joinn.R;
import de.hdodenhof.circleimageview.CircleImageView;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;

public final class FragmentMyPageBinding implements ViewBinding {
  @NonNull
  private final LinearLayout rootView;

  @NonNull
  public final TextView calendertxt;

  @NonNull
  public final ImageView date;

  @NonNull
  public final TextView drivertxt;

  @NonNull
  public final ImageView editProfile;

  @NonNull
  public final ImageView history;

  @NonNull
  public final TextView historytxt;

  @NonNull
  public final CircleImageView imguser;

  @NonNull
  public final ImageView level;

  @NonNull
  public final TextView level2;

  @NonNull
  public final TextView leveltxt;

  @NonNull
  public final ImageView license;

  @NonNull
  public final Button logoutBtn;

  @NonNull
  public final LottieAnimationView lottieAnimationView;

  @NonNull
  public final TextView profile;

  @NonNull
  public final Button revokeBtn;

  @NonNull
  public final TextView txtNickname;

  private FragmentMyPageBinding(@NonNull LinearLayout rootView, @NonNull TextView calendertxt,
      @NonNull ImageView date, @NonNull TextView drivertxt, @NonNull ImageView editProfile,
      @NonNull ImageView history, @NonNull TextView historytxt, @NonNull CircleImageView imguser,
      @NonNull ImageView level, @NonNull TextView level2, @NonNull TextView leveltxt,
      @NonNull ImageView license, @NonNull Button logoutBtn,
      @NonNull LottieAnimationView lottieAnimationView, @NonNull TextView profile,
      @NonNull Button revokeBtn, @NonNull TextView txtNickname) {
    this.rootView = rootView;
    this.calendertxt = calendertxt;
    this.date = date;
    this.drivertxt = drivertxt;
    this.editProfile = editProfile;
    this.history = history;
    this.historytxt = historytxt;
    this.imguser = imguser;
    this.level = level;
    this.level2 = level2;
    this.leveltxt = leveltxt;
    this.license = license;
    this.logoutBtn = logoutBtn;
    this.lottieAnimationView = lottieAnimationView;
    this.profile = profile;
    this.revokeBtn = revokeBtn;
    this.txtNickname = txtNickname;
  }

  @Override
  @NonNull
  public LinearLayout getRoot() {
    return rootView;
  }

  @NonNull
  public static FragmentMyPageBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static FragmentMyPageBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.fragment_my_page, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static FragmentMyPageBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.calendertxt;
      TextView calendertxt = ViewBindings.findChildViewById(rootView, id);
      if (calendertxt == null) {
        break missingId;
      }

      id = R.id.date;
      ImageView date = ViewBindings.findChildViewById(rootView, id);
      if (date == null) {
        break missingId;
      }

      id = R.id.drivertxt;
      TextView drivertxt = ViewBindings.findChildViewById(rootView, id);
      if (drivertxt == null) {
        break missingId;
      }

      id = R.id.editProfile;
      ImageView editProfile = ViewBindings.findChildViewById(rootView, id);
      if (editProfile == null) {
        break missingId;
      }

      id = R.id.history;
      ImageView history = ViewBindings.findChildViewById(rootView, id);
      if (history == null) {
        break missingId;
      }

      id = R.id.historytxt;
      TextView historytxt = ViewBindings.findChildViewById(rootView, id);
      if (historytxt == null) {
        break missingId;
      }

      id = R.id.imguser;
      CircleImageView imguser = ViewBindings.findChildViewById(rootView, id);
      if (imguser == null) {
        break missingId;
      }

      id = R.id.level;
      ImageView level = ViewBindings.findChildViewById(rootView, id);
      if (level == null) {
        break missingId;
      }

      id = R.id.level2;
      TextView level2 = ViewBindings.findChildViewById(rootView, id);
      if (level2 == null) {
        break missingId;
      }

      id = R.id.leveltxt;
      TextView leveltxt = ViewBindings.findChildViewById(rootView, id);
      if (leveltxt == null) {
        break missingId;
      }

      id = R.id.license;
      ImageView license = ViewBindings.findChildViewById(rootView, id);
      if (license == null) {
        break missingId;
      }

      id = R.id.logoutBtn;
      Button logoutBtn = ViewBindings.findChildViewById(rootView, id);
      if (logoutBtn == null) {
        break missingId;
      }

      id = R.id.lottieAnimationView;
      LottieAnimationView lottieAnimationView = ViewBindings.findChildViewById(rootView, id);
      if (lottieAnimationView == null) {
        break missingId;
      }

      id = R.id.profile;
      TextView profile = ViewBindings.findChildViewById(rootView, id);
      if (profile == null) {
        break missingId;
      }

      id = R.id.revokeBtn;
      Button revokeBtn = ViewBindings.findChildViewById(rootView, id);
      if (revokeBtn == null) {
        break missingId;
      }

      id = R.id.txtNickname;
      TextView txtNickname = ViewBindings.findChildViewById(rootView, id);
      if (txtNickname == null) {
        break missingId;
      }

      return new FragmentMyPageBinding((LinearLayout) rootView, calendertxt, date, drivertxt,
          editProfile, history, historytxt, imguser, level, level2, leveltxt, license, logoutBtn,
          lottieAnimationView, profile, revokeBtn, txtNickname);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}