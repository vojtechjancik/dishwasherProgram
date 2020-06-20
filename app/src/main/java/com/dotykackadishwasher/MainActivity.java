package com.dotykackadishwasher;

import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.Locale;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    private TextView countdownText;
    private Button buttonStartPause;
    private Button resetButton;
    private Button dishwasherFull;
    private Button dishwasherDirty;
    private Button dishwasherClean;
    private TextView textViewStatus;
    private TextView textViewDontPut;
    private LinearLayout linearLayout;
    private Spinner dropdown;
    private String longModeString = "1 hodina 35 minut";
    private String shortModeString = "35 minut";

    private boolean isTimerRunning;

    private CountDownTimer countDownTimer;
    private static final long START_TIME_IN_MILLIS_LONG_MODE = 5_700_000; //1 hour 35 minutes
    //private static final long START_TIME_IN_MILLIS_SHORT_MODE = 2_100_000;
    private static final long START_TIME_IN_MILLIS_SHORT_MODE = 5_000;
    private long timeLeftInMillisLongMode = START_TIME_IN_MILLIS_LONG_MODE;
    private long timeLeftInMillisShortMode = START_TIME_IN_MILLIS_SHORT_MODE;
    private int timeLeft;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dishwasherClean = findViewById(R.id.dishwasherClean);
        dishwasherDirty = findViewById(R.id.dishwasherDirty);
        dishwasherFull = findViewById(R.id.dishwasherFull);

        textViewStatus = findViewById(R.id.textInformation);
        textViewDontPut = findViewById(R.id.dontPut);

        linearLayout = findViewById(R.id.linearLayout);

        dropdown = findViewById(R.id.spinner1);
        String[] items = new String[]{longModeString, shortModeString};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items);
        dropdown.setAdapter(adapter);

        buttonStartPause = findViewById(R.id.startTimer_button);
        countdownText = findViewById(R.id.countdownText);
        resetButton = findViewById(R.id.resetButton);

        textViewStatus.setTextColor(Color.parseColor("#ffffff"));
        dishwasherClean.performClick();

        dishwasherClean.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                linearLayout.setBackgroundColor(Color.parseColor("#00e676"));
                textViewStatus.setTextColor(Color.parseColor("#ffffff"));
                textViewStatus.setText("Myčka je volná, můžeš vkládat nádobí!");
                textViewDontPut.setText("");
            }
        });

        dishwasherFull.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                linearLayout.setBackgroundColor(Color.parseColor("#ff9800"));
                textViewStatus.setText("Myčka doběhla svůj program a je plná, nevkládej.");
                textViewDontPut.setText("VYSKLÁDEJ NEBO NEOTEVÍREJ!!!");
                textViewDontPut.setTextSize(50);
            }
        });

        dishwasherDirty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                linearLayout.setBackgroundColor(Color.parseColor("#d32f2f"));
                textViewStatus.setText("Myčka právě jede!");
                textViewDontPut.setText("NEOTEVÍREJ!!!");
            }
        });

        dropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                /*Toast.makeText(parent.getContext(), parent.getItemAtPosition(position).toString(),
                Toast.LENGTH_SHORT).show(); */
                countdownText.setText(parent.getItemAtPosition(position).toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        buttonStartPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isTimerRunning) {
                    pauseTimer();
                } else {
                    startTimer();
                    linearLayout.setBackgroundColor(Color.parseColor("#d32f2f"));
                    textViewStatus.setText("Myčka právě jede!");
                    textViewDontPut.setText("NEOTEVÍREJ!!!");
                    dishwasherClean.setEnabled(false);
                    dishwasherFull.setEnabled(false);
                }
            }
        });

        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resetTimer();
            }
        });

        updateCountdownText();
    }

    public void startTimer() {
        long timeLeft = 0;

        if(dropdown.getSelectedItem().equals(shortModeString)) {
            timeLeft = (int) timeLeftInMillisShortMode;
        } else {
            timeLeft = (int) timeLeftInMillisLongMode;
        }

        countDownTimer = new CountDownTimer(timeLeft, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                if(dropdown.getSelectedItem().equals(shortModeString)) {
                    timeLeftInMillisShortMode = millisUntilFinished;
                } else {
                    timeLeftInMillisLongMode = millisUntilFinished;
                }

                    updateCountdownText();

            }

            @Override
            public void onFinish() {
                isTimerRunning = false;
                buttonStartPause.setText("Pauza");
                buttonStartPause.setVisibility(View.INVISIBLE);
                resetButton.setVisibility(View.VISIBLE);
            }
        }.start();

        isTimerRunning = true;
        buttonStartPause.setText("PAUZA");
        resetButton.setVisibility(View.INVISIBLE);
        dropdown.setEnabled(false);
    }

    private void pauseTimer() {
        countDownTimer.cancel();
        isTimerRunning = false;
        buttonStartPause.setText("Start");
        resetButton.setVisibility(View.VISIBLE);
        dropdown.setEnabled(true);
    }

    private void resetTimer() {
        if(dropdown.getSelectedItem().equals(shortModeString)) {
            timeLeftInMillisShortMode = START_TIME_IN_MILLIS_SHORT_MODE;
        } else {
            timeLeftInMillisLongMode = START_TIME_IN_MILLIS_LONG_MODE;
        }

        updateCountdownText();
        buttonStartPause.setVisibility(View.VISIBLE);
        resetButton.setVisibility(View.INVISIBLE);
        buttonStartPause.setText("Start");
        dishwasherClean.setEnabled(true);
        dishwasherFull.setEnabled(true);
        dropdown.setEnabled(true);
    }

    public void updateCountdownText(){
        int timeLeft = 0;

        if(dropdown.getSelectedItem().equals(shortModeString)) {
            timeLeft = (int) timeLeftInMillisShortMode;
        } else {
            timeLeft = (int) timeLeftInMillisLongMode;
        }

        int hours = timeLeft / 3_600_000;
        int minutes =  timeLeft % 3_600_000 / 60_000;
        int seconds =  ((timeLeft % 3_600_000) % 60_000) / 1000;

        String timeLeftFormatted = String.format(Locale.getDefault(), "%02d:%02d:%02d", hours, minutes, seconds);

        countdownText.setText(timeLeftFormatted);

        if(timeLeftFormatted.equals("00:00:01")) {
            resetTimer();
            dishwasherFull.setEnabled(true);
            dishwasherFull.performClick();
            //sendMessageToSlack();
        }
    }

    /*public static void sendMessageToSlack() throws IOException {
        String webhookURL = "https://hooks.slack.com/services/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";
        String charset = "UTF-8";
        String param1 = "Myčka právě doběhla program";

        /*HttpURLConnection httpURLConnection = (HttpURLConnection) new URL(webhookURL).openConnection();
        httpURLConnection.setRequestMethod("POST");
        httpURLConnection.setRequestProperty("Accept-Charset", charset);
        httpURLConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset=" + charset);
        httpURLConnection.setDoOutput(true);
        OutputStream outputStream = httpURLConnection.getOutputStream();
        outputStream.write(param1.getBytes());
        outputStream.flush();
        outputStream.close();

        

    } */

}
