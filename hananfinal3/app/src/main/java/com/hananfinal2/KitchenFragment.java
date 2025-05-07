package com.hananfinal2;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.util.Log;
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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

public class KitchenFragment extends Fragment {
    private float dX, dY;
    private float originalX, originalY;
    private FrameLayout rootLayout;
    protected Pet pet;

    private ImageView dogImageView;
    private AnimationDrawable dogAnimation;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_kitchen, container, false);
        LinearLayout linearLayout = view.findViewById(R.id.linearlayout);
        HorizontalScrollView horizontalScrollView = view.findViewById(R.id.horizonticalScrollView);
        rootLayout = view.findViewById(R.id.kitchenlayout);

        pet = PetManager.getInstance().getPet();

        WindowManager windowManager = (WindowManager) getActivity().getSystemService(Context.WINDOW_SERVICE);
        Display display = windowManager.getDefaultDisplay();
        DisplayMetrics displayMetrics = new DisplayMetrics();
        display.getMetrics(displayMetrics);
        int screenHeight = displayMetrics.heightPixels;

        dogImageView = view.findViewById(R.id.kitchen_dog);
        dogImageView.setImageResource(R.drawable.dog1_sit);
        dogAnimation = (AnimationDrawable) dogImageView.getDrawable();

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
                    ViewGroup decorView = (ViewGroup) getActivity().getWindow().getDecorView();

                    rect.setOnTouchListener(new View.OnTouchListener() {
                        @Override
                        public boolean onTouch(View v, MotionEvent event) {
                            switch (event.getAction()) {
                                case MotionEvent.ACTION_DOWN: // לחיצה
                                    horizontalScrollView.requestDisallowInterceptTouchEvent(true);

                                    int[] imagePos = new int[2];
                                    imageView.getLocationOnScreen(imagePos);

                                    originalX = imagePos[0] ;
                                    originalY = imagePos[1] ;
                                    Log.d("DEBUG", "BEFORE Image X/Y on screen: " + imagePos[0] + ", " + imagePos[1]);
                                    if (imageView.getParent() != null && imageView.getParent() != decorView) {
                                        ((ViewGroup) imageView.getParent()).removeView(imageView);
                                        decorView.addView(imageView);
                                    }
                                    Log.d("DEBUG", " AFTER Image X/Y on screen: " + imagePos[0] + ", " + imagePos[1]);
                                    int[] decorLocation = new int[2];
                                    decorView.getLocationOnScreen(decorLocation);
                                    imageView.setX(originalX);
                                    imageView.setY(originalY);
                                    break;

                                case MotionEvent.ACTION_MOVE: //הזזה
                                    imageView.animate()
                                            .x(event.getRawX() - imageView.getWidth() / 2)
                                            .y(event.getRawY() - imageView.getHeight() / 2)
                                            .setDuration(0)
                                            .start();
                                    break;

                                case MotionEvent.ACTION_UP: // עזיבה
                                    horizontalScrollView.requestDisallowInterceptTouchEvent(false);
                                    View dropPoint = rootLayout.findViewById(R.id.dropPoint);
                                    int[] dropLocation = new int[2];
                                    dropPoint.getLocationOnScreen(dropLocation);
                                    int dropX = dropLocation[0];
                                    int dropY = dropLocation[1];
                                    int dropWidth = dropPoint.getWidth();
                                    int dropHeight = dropPoint.getHeight();

                                    float droppedX = event.getRawX();
                                    float droppedY = event.getRawY();

                                    boolean isInDropZone =
                                            droppedX >= dropX && droppedX <= dropX + dropWidth &&
                                                    droppedY >= dropY && droppedY <= dropY + dropHeight;

                                    if (isInDropZone) {
                                        pet.feed(10);
                                    }
                                    imageView.animate()
                                            .x(originalX)
                                            .y(originalY)
                                            .setDuration(300)
                                            .withEndAction(() -> {
                                                // Remove from rootLayout and return to frameLayout
                                                if (imageView.getParent() != null) {
                                                    ((ViewGroup) imageView.getParent()).removeView(imageView);
                                                }
                                                imageView.setTranslationX(0);
                                                imageView.setTranslationY(0);
                                                FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(imageView.getWidth(),imageView.getHeight());
                                                params.gravity = Gravity.CENTER;

                                                frameLayout.addView(imageView, 0, params);
                                            })
                                            .start();

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
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (dogAnimation != null && !dogAnimation.isRunning()) {
            dogAnimation.start();
        }
    }

    @Override
    public void onStop() {
        super.onStop();

        if (dogAnimation != null && dogAnimation.isRunning()) {
            dogAnimation.stop();
        }
    }

}
