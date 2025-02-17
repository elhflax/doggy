package com.hananfinal2;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

public class KitchenFragment extends Fragment {
    private float dX, dY;
    private float originalX, originalY;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_kitchen, container, false);
        LinearLayout linearLayout = view.findViewById(R.id.linearlayout);
        HorizontalScrollView horizontalScrollView = view.findViewById(R.id.horizonticalScrollView);

        for (int i = 0; i < linearLayout.getChildCount(); i++) {
            final ImageView imageView = (ImageView) linearLayout.getChildAt(i);

            imageView.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    switch (event.getAction()) {
                        case MotionEvent.ACTION_DOWN:
                            horizontalScrollView.requestDisallowInterceptTouchEvent(true);
                            // Record the initial touch position relative to the ImageView
                            dX = v.getX() - event.getRawX();
                            dY = v.getY() - event.getRawY();

                            // Store the original position of the ImageView
                            originalX = v.getX();
                            originalY = v.getY();
                            break;

                        case MotionEvent.ACTION_MOVE:
                            // Move the image as the user drags it
                            v.animate()
                                    .x(event.getRawX() + dX)
                                    .y(event.getRawY() + dY)
                                    .setDuration(0)
                                    .start();
                            break;

                        case MotionEvent.ACTION_UP:
                            horizontalScrollView.requestDisallowInterceptTouchEvent(false);
                            v.animate()
                                    .x(originalX)
                                    .y(originalY)
                                    .setDuration(300) // Set the duration of the return animation
                                    .start();
                            break;

                        default:
                            return false;
                    }
                    return true;
                }
            });
        }
        return view;
    }

}
