package com.hananfinal2;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.graphics.Rect;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.Random;

public class ClickGameFragment extends Fragment {
    private View targetContainer;
    private ImageView targetPaw;
    private ProgressBar targetTimer;
    private TextView targetTimerText;
    private TextView scoreText;
    private ImageView heart1;
    private ImageView heart2;
    private ImageView heart3;
    private int score = 0;
    private int misses = 0;
    private float timeLeft = 3.0f;
    private boolean isGameActive = true;
    private Handler handler = new Handler();
    private Random random = new Random();
    private int difficulty = 1;
    private static final int MAX_MISSES = 3;
    private CountDownTimer targetCountDownTimer;
    private Button restartButton;
    private View endContainer;
    private TextView finalScore;
    private View rootView;
    protected Pet pet;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_click_game, container, false);

        targetContainer = rootView.findViewById(R.id.target_container);
        targetPaw = rootView.findViewById(R.id.target_paw);
        targetTimer = rootView.findViewById(R.id.target_timer);
        targetTimerText = rootView.findViewById(R.id.target_timer_text);
        scoreText = rootView.findViewById(R.id.score_text);
        heart1 = rootView.findViewById(R.id.heart1);
        heart2 = rootView.findViewById(R.id.heart2);
        heart3 = rootView.findViewById(R.id.heart3);
        restartButton = rootView.findViewById(R.id.restart_button);
        endContainer = rootView.findViewById(R.id.end_container);
        finalScore = rootView.findViewById(R.id.final_score);

        pet = PetManager.getInstance().getPet();

        targetContainer.setOnClickListener(v -> {
            if (isGameActive) {
                score++;
                scoreText.setText("Score: " + score);
                if (score % 5 == 0) {
                    difficulty++;
                }
                showNextTarget();
            }
        });

        startGame();
        return rootView;
    }

    private void startGame() {
        isGameActive = true;
        score = 0;
        misses = 0;
        difficulty = 1;
        scoreText.setText("Score: 0");
        resetHearts();

        showNextTarget();
    }

    private void resetHearts() {
        heart1.setVisibility(View.VISIBLE);
        heart2.setVisibility(View.VISIBLE);
        heart3.setVisibility(View.VISIBLE);
    }

    private void removeHeart() {
        switch (misses) {
            case 1:
                heart3.setVisibility(View.INVISIBLE);
                break;
            case 2:
                heart2.setVisibility(View.INVISIBLE);
                break;
            case 3:
                heart1.setVisibility(View.INVISIBLE);
                break;
        }
    }

    private void showNextTarget() {
        if (!isGameActive) return;

        Rect visibleRect = new Rect();
        rootView.getGlobalVisibleRect(visibleRect);

        int targetWidth = targetContainer.getWidth();
        int targetHeight = targetContainer.getHeight();

        int padding = 20;
        int maxX = visibleRect.width() - targetWidth - padding;
        int maxY = visibleRect.height() - targetHeight - padding;

        if (maxX <= 0 || maxY <= 0) {
            rootView.post(() -> showNextTarget());
            return;
        }

        int newX = padding + random.nextInt(maxX);
        int newY = padding + random.nextInt(maxY);

        targetContainer.setX(newX);
        targetContainer.setY(newY);
        targetContainer.setVisibility(View.VISIBLE);

        timeLeft = Math.max(0.5f, 3.0f - (difficulty * 0.2f));
        targetTimer.setProgress(100);
        targetTimerText.setText(String.format("%.1f", timeLeft));

        if (targetCountDownTimer != null) {
            targetCountDownTimer.cancel();
        }

        targetCountDownTimer = new CountDownTimer((long)(timeLeft * 1000), 50) {
            @Override
            public void onTick(long millisUntilFinished) {
                float remainingTime = millisUntilFinished / 1000f;
                int progress = (int)((remainingTime / timeLeft) * 100);
                targetTimer.setProgress(progress);
                targetTimerText.setText(String.format("%.1f", remainingTime));
            }

            @Override
            public void onFinish() {
                if (targetContainer.getVisibility() == View.VISIBLE) {
                    targetContainer.setVisibility(View.INVISIBLE);
                    misses++;
                    removeHeart();
                    
                    if (misses >= MAX_MISSES) {
                        endGame();
                    } else {
                        showNextTarget();
                    }
                }
            }
        }.start();
    }

    private void endGame() {
        isGameActive = false;
        targetContainer.setVisibility(View.INVISIBLE);
        if (targetCountDownTimer != null) {
            targetCountDownTimer.cancel();
        }
        finalScore.setText("Final Score: " + score);
        endContainer.setVisibility(View.VISIBLE);
        pet.play(score);
        restartButton.setOnClickListener(v -> {
            endContainer.setVisibility(View.INVISIBLE);
            startGame();
        });
    }
} 