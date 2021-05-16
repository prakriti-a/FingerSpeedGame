package com.prakriti.fingerspeedgame;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private final String REM_TIME_KEY = "remainingTime";
    private final String THOUSAND_KEY = "aThousand";

    private TextView txtTimer, txtThousand;
    private Button btnTap;

    private CountDownTimer countDownTimer;
    private long initialCountDownMillis = 60000;
    private int timeIntervalMillis = 1000, remainingTime = 60;

    private int aThousand = 1000;

    String alertTitle = "TIME IS UP!";
    String alertWinner = "CONGRATULATIONS! YOU HAVE WON THE GAME!";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txtTimer = findViewById(R.id.txtTimer);
        txtThousand = findViewById(R.id.txtThousand);
        btnTap = findViewById(R.id.btnTap);

        txtThousand.setText(aThousand + "");

        // check for saved instance state
        if (savedInstanceState != null) {
            remainingTime = savedInstanceState.getInt(REM_TIME_KEY);
            aThousand = savedInstanceState.getInt(THOUSAND_KEY);
            // code to restore the game UI
            restoreTheGame();
        }

        // set click listener for button
        btnTap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                aThousand--;
                txtThousand.setText(aThousand + "");
                if (remainingTime > 0 && aThousand <= 0) {
                    showToast("CONGRATULATIONS!");
                    // show alert here and on finish
                    showAlert(alertWinner);
                }
            }
        });
        // initialising timer object
        if (savedInstanceState == null) {
            countDownTimer = new CountDownTimer(initialCountDownMillis, timeIntervalMillis) {
                // start timer from 60 seconds, decrease at interval of one second
                @Override
                public void onTick(long millisUntilFinished) {
                    remainingTime = (int) millisUntilFinished / 1000; // in seconds
                    txtTimer.setText(remainingTime + "");
                }

                @Override
                public void onFinish() {
                    showToast("Countdown has ended!");
                    showAlert(alertTitle);
                }
            };
            countDownTimer.start();
        }
    }


    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(REM_TIME_KEY, remainingTime);
        outState.putInt(THOUSAND_KEY, aThousand);
        countDownTimer.cancel();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.menu_item1) {
            showToast("APP VERSION " + BuildConfig.VERSION_NAME);
        }
        return true;
    }

    // method to reset the game
    private void resetTheGame() {

        if(countDownTimer != null) {
            countDownTimer.cancel();
            countDownTimer = null;
        }
        aThousand = 1000;
        // remainingTime = 60;
        txtThousand.setText(aThousand + "");
        txtTimer.setText(remainingTime + "");
        countDownTimer = new CountDownTimer(initialCountDownMillis, timeIntervalMillis) {
            @Override
            public void onTick(long millisUntilFinished) {
                remainingTime = (int) millisUntilFinished / 1000; // in seconds
                txtTimer.setText(remainingTime + "");
            }
            @Override
            public void onFinish() {
                showToast("Countdown has ended!");
                showAlert(alertTitle);
            }
        };
        countDownTimer.start();
    }


    // method to show alert message
    private void showAlert(String title) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivity.this);
        alertDialog.setTitle(title);
        alertDialog.setMessage("You have reached " + (1000 - Integer.parseInt(txtThousand.getText().toString())) + " taps in a minute!" +
                        "\nWould you like to reset the game?");
        alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // code to reset the game
                resetTheGame();
            }
        });
        alertDialog.setNegativeButton("CLOSE APP", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });
        alertDialog.setCancelable(false);
        alertDialog.show();
    }


        // method to restore game on change of instance state
    private void restoreTheGame() {
        int restoredRemainingTime = remainingTime;
        int restoredThousand = aThousand;
        txtTimer.setText(restoredRemainingTime + "");
        txtThousand.setText(restoredThousand + "");

        countDownTimer = new CountDownTimer((remainingTime*1000), timeIntervalMillis) {
            @Override
            public void onTick(long millisUntilFinished) {
                remainingTime = (int) millisUntilFinished / 1000;
                txtTimer.setText(remainingTime + "");
            }
            @Override
            public void onFinish() {
                showToast("Countdown has ended!");
                showAlert(alertTitle);
            }
        };
        countDownTimer.start();
    }


    private void showToast(String message) {
        Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT).show();
    }
}