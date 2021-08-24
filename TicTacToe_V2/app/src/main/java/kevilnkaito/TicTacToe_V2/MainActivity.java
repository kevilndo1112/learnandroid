package kevilnkaito.TicTacToe_V2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.gridlayout.widget.GridLayout;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

import java.util.Arrays;

public class MainActivity extends AppCompatActivity {

    //region - Viewer -
    GridLayout gridLayout;

    private TextView lblResult;
    private TextView lblNextTurn;
    private Button btnPlayAgain;
    private Button btnReset;
    //endregion


    //region - MediaPlayer -

    private MediaPlayer mediaPlayer_welcome;
    private MediaPlayer mediaPlayer_tap;
    private MediaPlayer mediaPlayer_win;
    private MediaPlayer mediaPlayer_draw;
    private MediaPlayer mediaPlayer_reset;
    private MediaPlayer mediaPlayer_playAgain;
    //endregion


    //region - Define Fields -
    // Red/Player_1: 1, Yellow/Player_2: 2, Empty: 0
    //int[] gameStage = {0, 0, 0, 0, 0, 0, 0, 0, 0};

    enum Player {PLAYER_1_RED, PLAYER_2_YELLOW, EMPTY}

    Player[] gameStage = {Player.EMPTY, Player.EMPTY, Player.EMPTY, Player.EMPTY, Player.EMPTY, Player.EMPTY, Player.EMPTY, Player.EMPTY, Player.EMPTY};

    int[][] winningPotions = {
            {0, 1, 2}, {3, 4, 5}, {6, 7, 8}, //Hàng ngang
            {0, 3, 6}, {1, 4, 7}, {2, 5, 8}, //Cột dọc
            {0, 4, 8}, {2, 4, 6} //2 Hàng chéo
    };

    //int activePlayer = 1;
    Player activePlayer = Player.PLAYER_1_RED;
    boolean gameActive = true;

    int count = 0;
    //endregion


    //region - Event -
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        gridLayout = findViewById(R.id.gridLayout);

        lblResult = findViewById(R.id.lblResult);
        lblNextTurn = findViewById(R.id.lblNextTurn);
        btnPlayAgain = findViewById(R.id.btnPlayAgain);
        btnReset = findViewById(R.id.btnReset);

        lblNextTurn.setText("Next turn: RED");

        mediaPlayer_welcome = MediaPlayer.create(this, R.raw.welcome__winxp_start);
        mediaPlayer_tap = MediaPlayer.create(this, R.raw.tap__mario_coin_sound_effect);
        mediaPlayer_win = MediaPlayer.create(this, R.raw.win__laughter);
        mediaPlayer_draw = MediaPlayer.create(this, R.raw.draw__yay);
        mediaPlayer_reset = MediaPlayer.create(this, R.raw.reset__1up);
        mediaPlayer_playAgain = MediaPlayer.create(this, R.raw.play_again__power_up_mario);

        mediaPlayer_welcome.seekTo(0);
        mediaPlayer_welcome.start();

        Toast.makeText(this, "Welcome TicTocToe - KEVILN - Toast", Toast.LENGTH_LONG).show();
        Snackbar.make(gridLayout, "Welcome TicTocToe - KEVILN - Snack bar", Snackbar.LENGTH_LONG).show();

    }


    public void tap(View view) {

        btnReset.setVisibility(View.VISIBLE);

        ImageView imageView = (ImageView) view;

        int index = Integer.parseInt(imageView.getTag().toString());
        if (gameStage[index] == Player.EMPTY && gameActive) {

            mediaPlayer_tap.seekTo(0);
            mediaPlayer_tap.start();

            gameStage[index] = activePlayer;

            setImageToView(view);

            if (checkForWin()) { //Kiểm tra thắng
                notificationWinner();
                return;
            }

            count++;
            if (count == 9) { //Kiểm tra hòa
                notificationDraw();
            }

        }

    }

    public void btnPlayAgain_onClick(View view) {
        reset();


        mediaPlayer_playAgain.seekTo(0);
        mediaPlayer_playAgain.start();
    }

    public void btnReset_onClick(View view) {
        reset();

        mediaPlayer_reset.seekTo(0);
        mediaPlayer_reset.start();
    }
    //endregion


    //region - Private Method -
    private void setImageToView(View view) {

        //Change image:
        ImageView imageView = (ImageView) view;
        if (activePlayer == Player.PLAYER_1_RED) {
            imageView.setImageResource(R.drawable.red);
            activePlayer = Player.PLAYER_2_YELLOW;
            lblNextTurn.setText("Next turn: YELLOW");
        } else {
            imageView.setImageResource(R.drawable.yellow);
            activePlayer = Player.PLAYER_1_RED;
            lblNextTurn.setText("Next turn: RED");
        }

        //Animation:
        imageView.setTranslationY(-1000);
        imageView.setRotation(-360 * 10);
        imageView.animate().translationYBy(1000).setDuration(500);
        imageView.animate().rotation(360 * 10).setDuration(1000);

    }

    private boolean checkForWin() {

        for (int[] winningPotion : winningPotions) {

            if (gameStage[winningPotion[0]] == gameStage[winningPotion[1]]
                    && gameStage[winningPotion[1]] == gameStage[winningPotion[2]]
                    && gameStage[winningPotion[2]] != Player.EMPTY) {

                return true;
            }

        }

        return false;
    }

    private void notificationWinner() {

        lblResult.setVisibility(View.VISIBLE);
        lblNextTurn.setVisibility(View.INVISIBLE);
        btnPlayAgain.setVisibility(View.VISIBLE);
        btnReset.setVisibility(View.INVISIBLE);

        //Thông báo kết quả:
        String winner = "";
        if (activePlayer == Player.PLAYER_1_RED) {
            winner = "YELLOW";
        } else {
            winner = "RED";
        }

        lblResult.setText(winner + " has won!");

        Snackbar.make(gridLayout, winner + " has won!", Snackbar.LENGTH_LONG).show();

        gameActive = false;

        mediaPlayer_win.seekTo(0);
        mediaPlayer_win.start();

    }

    private void notificationDraw() {

        lblResult.setVisibility(View.VISIBLE);
        lblNextTurn.setVisibility(View.INVISIBLE);
        btnPlayAgain.setVisibility(View.VISIBLE);
        btnReset.setVisibility(View.INVISIBLE);

        //Thông báo kết quả:
        lblResult.setText("Result: Draw!!!");

        Snackbar.make(gridLayout, "Result: Draw!!!", Snackbar.LENGTH_LONG).show();

        gameActive = false;

        mediaPlayer_draw.seekTo(0);
        mediaPlayer_draw.start();

    }

    private void reset() {

        //Reset lbl, btn
        lblResult.setVisibility(View.INVISIBLE);
        lblNextTurn.setVisibility(View.VISIBLE);
        btnPlayAgain.setVisibility(View.INVISIBLE);
        btnReset.setVisibility(View.INVISIBLE);

        lblNextTurn.setText("Next turn: RED");

        //Reset GridLayout (Image)
        for (int i = 0; i < gridLayout.getChildCount(); i++) {
            ImageView imageView = (ImageView) gridLayout.getChildAt(i);
            imageView.setImageDrawable(null);
        }

        //Reset gameStage
        Arrays.fill(gameStage, Player.EMPTY);

        activePlayer = Player.PLAYER_1_RED;
        gameActive = true;

        count = 0;

        Toast.makeText(this, "Reset complete!", Toast.LENGTH_LONG).show();

    }
    //endregion

}