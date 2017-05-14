package game.fastanswer;

import android.os.CountDownTimer;
import android.util.Log;

import com.akexorcist.roundcornerprogressbar.IconRoundCornerProgressBar;

/**
 * Created by Admin on 16/04/2017.
 */

public class CounterTimer extends CountDownTimer {

    private long millisInFuture;
    private IconRoundCornerProgressBar counter;
    private long millisecondsLeft = 0;
    private boolean isRunning = false;
    public CounterTimer(long millisInFuture, long countDownInterval, IconRoundCornerProgressBar counter) {
        super(millisInFuture, countDownInterval);
        this.millisInFuture = millisInFuture;
        this.counter = counter;
    }

    @Override
    public void onTick(long millisUntilFinished) {
        isRunning = true;
        counter.setProgress(millisUntilFinished);
        counter.setSecondaryProgress(millisUntilFinished + 100);
        millisecondsLeft = millisUntilFinished;
        Log.d("check timer", millisUntilFinished+"");
    }

    @Override
    public void onFinish() {
        isRunning = false;
        millisecondsLeft = 0;
        counter.setProgress(millisecondsLeft);
        counter.setSecondaryProgress(millisecondsLeft);
    }

    public long getMillisecondsLeft() {
        return millisecondsLeft;
    }

    public boolean isRunning() {
        return isRunning;
    }

    public void setMillisInFuture(long millisInFuture) {
        this.millisInFuture = millisInFuture;
    }
}
