package AppDialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

import com.akexorcist.roundcornerprogressbar.IconRoundCornerProgressBar;

import game.fastanswer.CounterTimer;
import game.fastanswer.R;

/**
 * Created by Admin on 12/05/2017.
 */

public class PauseGameDialog extends Dialog {
    private final IconRoundCornerProgressBar progressBar;
    private Context mContext;
    private Typeface FontShowG;
    private TextView textPauseGame;
    private CounterTimer counterTimer;

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
        View im = findViewById(R.id.button_home);
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
        FontShowG = Typeface.createFromAsset(mContext.getAssets(), "fonts/show_g.ttf");
    }

    private void init() {
        textPauseGame = (TextView) findViewById(R.id.text_pause_game);
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
}
