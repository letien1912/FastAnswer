package AppDialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.media.AudioManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import game.fastanswer.MainActivity;
import game.fastanswer.R;

import static android.content.Context.MODE_APPEND;
import static android.content.Context.MODE_PRIVATE;

/**
 * Created by Admin on 06/05/2017.
 */

public class GameOverDialog extends Dialog implements View.OnClickListener {

    private Context mContext;
    private Typeface FontShowG;
    private Typeface FontSnapITC;
    private TextView textScorePoint;
    private TextView textHighScorePoint;
    private int currentScore;
    private boolean sound;
    private ImageView btSound;
    private SharedPreferences sharedPref;

    public GameOverDialog(@NonNull Context context, int currentScore) {
        super(context);
        mContext = context;
        this.currentScore = currentScore;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        setContentView(R.layout.gameover_main_view);

        getTypeFaceFromAssert();
        init();
        addFontType();
        preventCancelClickOutSide();
    }

    private void getTypeFaceFromAssert() {
        FontSnapITC = Typeface.createFromAsset(mContext.getAssets(), "fonts/snap_itc.ttf");
        FontShowG = Typeface.createFromAsset(mContext.getAssets(), "fonts/show_g.ttf");
    }

    private void init() {
        textScorePoint = (TextView) findViewById(R.id.game_over_score_point);
        textHighScorePoint = (TextView) findViewById(R.id.game_over_high_score_point);

        View btContinue = findViewById(R.id.button_continue);
        View btHome = findViewById(R.id.button_home);
        btSound = (ImageView) findViewById(R.id.button_sound);
        View btShare = findViewById(R.id.button_share);

        btContinue.setOnClickListener(this);
        btHome.setOnClickListener(this);
        btSound.setOnClickListener(this);
        btShare.setOnClickListener(this);

        sharedPref = mContext.getSharedPreferences(mContext.getString(R.string.pref_change_sound), MODE_PRIVATE);
        sound = sharedPref.getBoolean(mContext.getString(R.string.pref_change_sound), false);

        SetSoundIcon();
    }

    private void SetSoundIcon(){
        if(sound){
            btSound.setImageResource(R.drawable.ic_sound);
        }else{
            btSound.setImageResource(R.drawable.ic_sound_turn_off);
        }
    }

    private void addFontType() {
        ((TextView) findViewById(R.id.game_over_text_head)).setTypeface(FontShowG);
        ((TextView) findViewById(R.id.game_over_score)).setTypeface(FontShowG);
        ((TextView) findViewById(R.id.game_over_high_score)).setTypeface(FontShowG);
        ((TextView) findViewById(R.id.text_continue)).setTypeface(FontSnapITC);
        textHighScorePoint.setTypeface(FontShowG);
        textScorePoint.setTypeface(FontShowG);
    }

    private void preventCancelClickOutSide() {
        setCancelable(false);
        setCanceledOnTouchOutside(false);
    }

    private void ShowScore() {
        SharedPreferences sharedPref = mContext.getSharedPreferences(mContext.getString(R.string.pref_high_score), MODE_APPEND);
        int highScore = sharedPref.getInt(mContext.getString(R.string.pref_high_score), 0);
        if (currentScore > highScore) {
            SharedPreferences.Editor editHighScore = sharedPref.edit();
            editHighScore.putInt(mContext.getString(R.string.pref_high_score), currentScore);
            highScore = currentScore;
            editHighScore.apply();
            editHighScore.commit();
        }
        textScorePoint.setText(currentScore + "");
        textHighScorePoint.setText(highScore + "");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_home:
                BackToHome();
                break;
            case R.id.button_continue:
                PlayNewGame();
                break;
            case R.id.button_share:
                ShareGame();
                break;
            case R.id.button_sound:
                ChangeSound();
                break;
        }
    }

    @Override
    public void show() {
        super.show();
        ShowScore();
    }

    private void BackToHome() {
        dismiss();
        ((Activity) mContext).finish();
    }

    private void PlayNewGame() {
        dismiss();
        ((MainActivity) mContext).CreateGamePlay();
    }

    private void ShareGame() {
        String message = "Your Score is " + currentScore + " that so great !!!!";
        Intent share = new Intent(Intent.ACTION_SEND);
        share.setType("text/plain");
        share.putExtra(Intent.EXTRA_TEXT, message);
        mContext.startActivity(Intent.createChooser(share, message));
    }

    private void ChangeSound() {
        Log.d("Dialog","clicked");
        SharedPreferences.Editor editor = sharedPref.edit();
        sound = !sound;
        editor.putBoolean(mContext.getString(R.string.pref_change_sound),sound);
        editor.apply();
        editor.commit();

        SetSoundIcon();
    }
}
