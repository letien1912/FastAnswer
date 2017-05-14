package AppDialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.akexorcist.roundcornerprogressbar.IconRoundCornerProgressBar;

import game.fastanswer.CounterTimer;
import game.fastanswer.R;

/**
 * Created by Admin on 06/05/2017.
 */

public class GameOverDialog extends Dialog {

    private final IconRoundCornerProgressBar progressBar;
    private Context mContext;
    private Typeface FontShowG;
    private Typeface FontSnapITC;
    private TextView textScorePoint;
    private TextView textHighScorePoint;
    private CounterTimer counterTimer;

    public GameOverDialog(@NonNull Context context, CounterTimer counterTimer, IconRoundCornerProgressBar progressBar) {
        super(context);
        this.counterTimer = counterTimer;
        this.progressBar = progressBar;
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
        View im = findViewById(R.id.button_continue);
        im.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mContext, "ahihi", Toast.LENGTH_SHORT).show();
                continueCountTimer();

                dismiss();
            }


        });
    }

    private void continueCountTimer() {
        new CounterTimer(counterTimer.getMillisecondsLeft(), 10, progressBar).start();
    }

    private void getTypeFaceFromAssert() {
        FontSnapITC = Typeface.createFromAsset(mContext.getAssets(), "fonts/snap_itc.ttf");
        FontShowG = Typeface.createFromAsset(mContext.getAssets(), "fonts/show_g.ttf");
    }

    private void init() {
        textScorePoint = (TextView) findViewById(R.id.game_over_score_point);
        textHighScorePoint = (TextView) findViewById(R.id.game_over_high_score_point);
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
    protected void onStart() {
        counterTimer.cancel();
        super.onStart();
    }
}
