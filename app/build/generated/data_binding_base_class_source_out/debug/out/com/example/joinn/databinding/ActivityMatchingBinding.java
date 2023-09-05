// Generated by view binder compiler. Do not edit!
package com.example.joinn.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewbinding.ViewBinding;
import androidx.viewbinding.ViewBindings;
import com.example.joinn.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;

public final class ActivityMatchingBinding implements ViewBinding {
  @NonNull
  private final RelativeLayout rootView;

  @NonNull
  public final BottomNavigationView bottomMenu;

  @NonNull
  public final FrameLayout container;

  @NonNull
  public final RelativeLayout parentLayout;

  private ActivityMatchingBinding(@NonNull RelativeLayout rootView,
      @NonNull BottomNavigationView bottomMenu, @NonNull FrameLayout container,
      @NonNull RelativeLayout parentLayout) {
    this.rootView = rootView;
    this.bottomMenu = bottomMenu;
    this.container = container;
    this.parentLayout = parentLayout;
  }

  @Override
  @NonNull
  public RelativeLayout getRoot() {
    return rootView;
  }

  @NonNull
  public static ActivityMatchingBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static ActivityMatchingBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.activity_matching, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static ActivityMatchingBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.bottom_menu;
      BottomNavigationView bottomMenu = ViewBindings.findChildViewById(rootView, id);
      if (bottomMenu == null) {
        break missingId;
      }

      id = R.id.container;
      FrameLayout container = ViewBindings.findChildViewById(rootView, id);
      if (container == null) {
        break missingId;
      }

      RelativeLayout parentLayout = (RelativeLayout) rootView;

      return new ActivityMatchingBinding((RelativeLayout) rootView, bottomMenu, container,
          parentLayout);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}
