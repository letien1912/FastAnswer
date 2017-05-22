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
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.akexorcist.roundcornerprogressbar.IconRoundCornerProgressBar;

import game.fastanswer.CounterTimer;
import game.fastanswer.MainActivity;
import game.fastanswer.R;

/**
 * Created by Admin on 12/05/2017.
 */

public class PauseGameDialog extends Dialog implements View.OnClickListener {
    private final IconRoundCornerProgressBar progressBar;
    private Context mContext;
    private Typeface FontShowG;
    private TextView textPauseGame;
    private CounterTimer counterTimer;

    private View btHome;
    private View btRestart;
    private View btContinue;

    public PauseGameDialog(@NonNull Context context, CounterTimer counterTimer, IconRoundCornerProgressBar progressBar) {
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
        setContentView(R.layout.pause_game);

        getTypeFaceFromAssert();
        init();
        addFontType();
        preventCancelClickOutSide();
    }

    private void getTypeFaceFromAssert() {
        FontShowG = Typeface.createFromAsset(mContext.getAssets(), "fonts/show_g.ttf");
    }

    private void init() {
        textPauseGame = (TextView) findViewById(R.id.text_pause_game);
        btHome = findViewById(R.id.button_home);
        btContinue = findViewById(R.id.button_continue);
        btRestart = findViewById(R.id.button_restart);

        btHome.setOnClickListener(this);
        btContinue.setOnClickListener(this);
        btRestart.setOnClickListener(this);
    }

    private void addFontType() {
        textPauseGame.setTypeface(FontShowG);
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_home:
                backToHomeEvent();
                break;
            case R.id.button_continue:
                ContinuePlayGame();
                break;
            case R.id.button_restart:
                break;
        }
    }

    private void backToHomeEvent() {
        dismiss();
        ((Activity) mContext).finish();
    }

    private void ContinuePlayGame() {
        continueCountTimer();
        dismiss();
    }

    private void continueCountTimer() {
        counterTimer = new CounterTimer(counterTimer.getMillisecondsLeft(), 10, progressBar);
        ((MainActivity) mContext).setCurrentCounter(counterTimer);
        counterTimer.start();
    }
}
