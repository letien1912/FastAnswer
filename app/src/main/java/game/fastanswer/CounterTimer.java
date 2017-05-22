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
    private long millisecondsLeft;
    private boolean isPause = false;
    private static final long SECONDARY_PROGRESS_RANGE = 50;

    public CounterTimer(long millisInFuture, long countDownInterval, IconRoundCornerProgressBar counter) {
        super(millisInFuture, countDownInterval);
        this.millisInFuture = millisInFuture;
        this.millisecondsLeft = millisInFuture;
        this.counter = counter;
    }

    @Override
    public void onTick(long millisUntilFinished) {
        if(isPause){
            cancel();
        } else{
            SetCounterProgress(millisUntilFinished);
            millisecondsLeft = millisUntilFinished;
        }
    }

    private void SetCounterProgress(long millisUntilFinished) {
        counter.setProgress(millisUntilFinished);
        counter.setSecondaryProgress(millisUntilFinished + SECONDARY_PROGRESS_RANGE);
    }

    @Override
    public void onFinish() {
        millisecondsLeft = 0;
        counter.setProgress(millisecondsLeft);
        counter.setSecondaryProgress(millisecondsLeft);
    }

    public long getMillisecondsLeft() {
        return millisecondsLeft;
    }
    public boolean isPause() {
        return isPause;
    }

    public void setPause(boolean pause) {
        isPause = pause;
    }

    public void setMillisInFuture(long millisInFuture) {
        this.millisInFuture = millisInFuture;
    }
}
