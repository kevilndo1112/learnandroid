package kevilnkaito.EggTimer;

import androidx.appcompat.app.AppCompatActivity;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    SeekBar timerSeekBar;
    TextView countDownTextView;
    Button goButton;

    boolean counterIsActive = false;
    CountDownTimer countDownTimer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        timerSeekBar = findViewById(R.id.timerSeekBar);
        countDownTextView = findViewById(R.id.countDownTextView);
        goButton = findViewById(R.id.goButton);

        // Setup: timerSeekBar
        timerSeekBar.setMax(60 * 25); //25 Phút
        timerSeekBar.setProgress(5); //05 giây
        timerSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                updateTimer(i);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    public void onGoButtonOnClick(View view) {

        if (counterIsActive) {
            resetTimer();
        } else {
            startTimer();
        }

    }


    private void updateTimer(int secondsLeft) {

        int minutes = secondsLeft / 60;
        int seconds = secondsLeft % 60;

        countDownTextView.setText((minutes < 9 ? "0" + minutes : minutes) + ":" + (seconds < 9 ? "0" + seconds : seconds));

    }

    private void resetTimer() {

        counterIsActive = false;

        timerSeekBar.setEnabled(true);
        goButton.setText("Go!");

        countDownTimer.cancel();

        /*timerSeekBar.setProgress(05); //05 giây
        countDownTextView.setText("00:05");*/

        goButton.setBackgroundColor(getResources().getColor(R.color.purple_500));
    }

    private void startTimer() {

        counterIsActive = true;

        timerSeekBar.setEnabled(false);
        goButton.setText("Stop!");

        countDownTimer = new CountDownTimer(timerSeekBar.getProgress() * 1000 + 100, 1000) {
            @Override
            public void onTick(long l) {
                int secondsLeft = (int) l / 1000;
                updateTimer(secondsLeft);
                timerSeekBar.setProgress(secondsLeft);
            }

            @Override
            public void onFinish() {
                MediaPlayer mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.airhorn);
                mediaPlayer.start();

                resetTimer();
            }
        };
        countDownTimer.start();

        goButton.setBackgroundColor(getResources().getColor(R.color.teal_700));
    }

}