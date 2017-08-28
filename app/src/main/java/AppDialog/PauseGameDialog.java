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

import com.akexorcist.roundcornerprogressbar.IconRoundCornerProgressBar;
import com.akexorcist.roundcornerprogressbar.TextRoundCornerProgressBar;

import org.w3c.dom.Text;

import Utilities.CounterTimer;
import game.fastanswer.MainActivity;
import game.fastanswer.R;

public class PauseGameDialog extends Dialog implements View.OnClickListener {
    private final IconRoundCornerProgressBar progressBar;
    private Context mContext;
    private Typeface FontShowG;
    private TextView textPauseGame;
    private CounterTimer counterTimer;
    private TextView textProgress;
    public PauseGameDialog(@NonNull Context context, CounterTimer counterTimer, IconRoundCornerProgressBar progressBar, TextView textProgress) {
        super(context);
        this.counterTimer = counterTimer;
        this.progressBar = progressBar;
        mContext = context;
        this.textProgress = textProgress;
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
        View btHome = findViewById(R.id.button_home);
        View btContinue = findViewById(R.id.button_continue);
        View btRestart = findViewById(R.id.button_restart);

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
                BackToHome();
                break;
            case R.id.button_continue:
                ContinuePlayGame();
                break;
            case R.id.button_restart:
                RestartGame();
                break;
        }
    }

    private void BackToHome() {
        dismiss();
        ((Activity) mContext).finish();
    }

    private void ContinuePlayGame() {
        ContinueCountTimer();
        dismiss();
    }

    private void RestartGame() {
        dismiss();
        ((MainActivity) mContext).CreateGamePlay();
    }

    private void ContinueCountTimer() {
        counterTimer = new CounterTimer(counterTimer.getMillisecondsLeft(), 10, progressBar, ((MainActivity) mContext),textProgress);
        ((MainActivity) mContext).setCurrentCounter(counterTimer);
        counterTimer.start();
    }
}
