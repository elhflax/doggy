// Generated by view binder compiler. Do not edit!
package com.hananfinal2.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewbinding.ViewBinding;
import androidx.viewbinding.ViewBindings;
import com.hananfinal2.R;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;

public final class FragmentGameSelectionBinding implements ViewBinding {
  @NonNull
  private final LinearLayout rootView;

  @NonNull
  public final Button game1Button;

  @NonNull
  public final Button game2Button;

  @NonNull
  public final Button game3Button;

  private FragmentGameSelectionBinding(@NonNull LinearLayout rootView, @NonNull Button game1Button,
      @NonNull Button game2Button, @NonNull Button game3Button) {
    this.rootView = rootView;
    this.game1Button = game1Button;
    this.game2Button = game2Button;
    this.game3Button = game3Button;
  }

  @Override
  @NonNull
  public LinearLayout getRoot() {
    return rootView;
  }

  @NonNull
  public static FragmentGameSelectionBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static FragmentGameSelectionBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.fragment_game_selection, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static FragmentGameSelectionBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.game1_button;
      Button game1Button = ViewBindings.findChildViewById(rootView, id);
      if (game1Button == null) {
        break missingId;
      }

      id = R.id.game2_button;
      Button game2Button = ViewBindings.findChildViewById(rootView, id);
      if (game2Button == null) {
        break missingId;
      }

      id = R.id.game3_button;
      Button game3Button = ViewBindings.findChildViewById(rootView, id);
      if (game3Button == null) {
        break missingId;
      }

      return new FragmentGameSelectionBinding((LinearLayout) rootView, game1Button, game2Button,
          game3Button);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}
