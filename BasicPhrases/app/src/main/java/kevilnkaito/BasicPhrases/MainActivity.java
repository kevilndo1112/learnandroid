package kevilnkaito.BasicPhrases;

import androidx.appcompat.app.AppCompatActivity;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void playPhrases(View view) {

        Button button = (Button) view;
        String fileName = button.getTag().toString();
        int resId = getResources().getIdentifier(fileName, "raw", getPackageName());

        MediaPlayer mediaPlayer = MediaPlayer.create(this, resId);

        mediaPlayer.start();

    }
}