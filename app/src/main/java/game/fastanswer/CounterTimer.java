package game.fastanswer;

import android.os.CountDownTimer;

import com.akexorcist.roundcornerprogressbar.IconRoundCornerProgressBar;

import Interface.IOnCounterTimerFinish;

/**
 * Created by Admin on 16/04/2017.
 */

public class CounterTimer extends CountDownTimer {

    private long millisInFuture;
    private IconRoundCornerProgressBar counter;
    private long millisecondsLeft;
    private static final long SECONDARY_PROGRESS_RANGE = 50;
    private IOnCounterTimerFinish onCounterTimerFinish;

    public CounterTimer(long millisInFuture, long countDownInterval, IconRoundCornerProgressBar counter,
                        IOnCounterTimerFinish onCounterTimerFinish) {
        super(millisInFuture, countDownInterval);
        this.millisInFuture = millisInFuture;
        this.millisecondsLeft = millisInFuture;
        this.counter = counter;
        this.onCounterTimerFinish = onCounterTimerFinish;
    }

    @Override
    public void onTick(long millisUntilFinished) {
        SetCounterProgress(millisUntilFinished);
        millisecondsLeft = millisUntilFinished;
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
        onCounterTimerFinish.onFinished();
    }

    public long getMillisecondsLeft() {
        return millisecondsLeft;
    }
    public void setMillisInFuture(long millisInFuture) {
        this.millisInFuture = millisInFuture;
    }
}
