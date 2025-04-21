package com.hananfinal2;

import android.content.Context;
import android.os.Bundle;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.util.DisplayMetrics;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

public class KitchenFragment extends Fragment {
    private float dX, dY;
    private float originalX, originalY;
    private FrameLayout rootLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_kitchen, container, false);
        LinearLayout linearLayout = view.findViewById(R.id.linearlayout);
        HorizontalScrollView horizontalScrollView = view.findViewById(R.id.horizonticalScrollView);
        rootLayout = view.findViewById(R.id.kitchenlayout);

        WindowManager windowManager = (WindowManager) getActivity().getSystemService(Context.WINDOW_SERVICE);
        Display display = windowManager.getDefaultDisplay();
        DisplayMetrics displayMetrics = new DisplayMetrics();
        display.getMetrics(displayMetrics);
        int screenHeight = displayMetrics.heightPixels;
        view.post(new Runnable() {
            @Override
            public void run() {
                int fragmentHeight = view.getHeight();
                int heightDifference = screenHeight - fragmentHeight;
                for (int i = 0; i < linearLayout.getChildCount(); i++) {
                    final FrameLayout frameLayout = (FrameLayout) linearLayout.getChildAt(i);
                    final ImageView imageView = (ImageView) frameLayout.getChildAt(0);
                    final View rect = (View) frameLayout.getChildAt(1);
                    int[] rootLocation = new int[2];
                    rootLayout.getLocationOnScreen(rootLocation);

                    rect.setOnTouchListener(new View.OnTouchListener() {
                        @Override
                        public boolean onTouch(View v, MotionEvent event) {
                            switch (event.getAction()) {
                                case MotionEvent.ACTION_DOWN: // לחיצה
                                    horizontalScrollView.requestDisallowInterceptTouchEvent(true);

                                    originalX = frameLayout.getX();
                                    originalY = frameLayout.getY() - rootLocation[1];
                                    frameLayout.removeView(imageView);
                                    rootLayout.addView(imageView);
                                    imageView.setX(event.getRawX() - imageView.getWidth() / 2 );
                                    imageView.setY(event.getRawY() - imageView.getHeight() / 2 - rootLocation[1]);
                                    break;

                                case MotionEvent.ACTION_MOVE: //הזזה
                                    imageView.animate()
                                            .x(event.getRawX())
                                            .y(event.getRawY() - imageView.getHeight() / 2 - rootLocation[1])
                                            .setDuration(0)
                                            .start();
                                    break;

                                case MotionEvent.ACTION_UP: // עזיבה
                                    horizontalScrollView.requestDisallowInterceptTouchEvent(false);


                                    imageView.animate()
                                            .x(originalX)
                                            .y(originalY - imageView.getHeight() / 2)
                                            .setDuration(300)
                                            .withEndAction(() -> {
                                                rootLayout.removeView(imageView);
                                                frameLayout.addView(imageView, 0);
                                            })
                                            .start();
                                    break;

                                default:
                                    return false;
                            }
                            return true;
                        }
                    });
                }

            }
        });
        return view;
    }

}
