package AppDialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import game.fastanswer.MainActivity;
import game.fastanswer.R;

/**
 * Created by Admin on 06/05/2017.
 */

    public class GameOverDialog extends Dialog implements View.OnClickListener {

    private Context mContext;
    private Typeface FontShowG;
    private Typeface FontSnapITC;
    private TextView textScorePoint;
    private TextView textHighScorePoint;

    public GameOverDialog(@NonNull Context context) {
        super(context);
        mContext = context;
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
        View btSound = findViewById(R.id.button_sound);
        View btShare = findViewById(R.id.button_share);

        btContinue.setOnClickListener(this);
        btHome.setOnClickListener(this);
        btSound.setOnClickListener(this);
        btShare.setOnClickListener(this);
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

    private void BackToHome() {
        dismiss();
        ((Activity) mContext).finish();
    }

    private void PlayNewGame() {
        dismiss();
        ((MainActivity) mContext).CreateGamePlay();
    }

    private void ShareGame() {

    }

    private void ChangeSound() {

    }
}
