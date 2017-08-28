package Utilities;

import android.os.CountDownTimer;
import android.widget.TextView;

import com.akexorcist.roundcornerprogressbar.IconRoundCornerProgressBar;

import Interface.IOnCounterTimerFinish;

/**
 * Created by Admin on 16/04/2017.
 */

public class CounterTimer extends CountDownTimer {

    private long millisInFuture;
    private IconRoundCornerProgressBar progress;
    private TextView textProgress;
    private long millisecondsLeft;
    private static final long SECONDARY_PROGRESS_RANGE = 50;
    private IOnCounterTimerFinish onCounterTimerFinish;

    public CounterTimer(long millisInFuture, long countDownInterval, IconRoundCornerProgressBar progress,
                        IOnCounterTimerFinish onCounterTimerFinish, TextView textProgress) {
        super(millisInFuture, countDownInterval);
        this.millisInFuture = millisInFuture;
        this.millisecondsLeft = millisInFuture;
        this.progress = progress;
        this.onCounterTimerFinish = onCounterTimerFinish;
        this.textProgress = textProgress;
    }

    @Override
    public void onTick(long millisUntilFinished) {
        SetCounterProgress(millisUntilFinished);
        millisecondsLeft = millisUntilFinished;
    }

    private void SetCounterProgress(long millisUntilFinished) {
        textProgress.setText(millisUntilFinished/100.0 + "");
        progress.setProgress(millisUntilFinished);
        progress.setSecondaryProgress(millisUntilFinished + SECONDARY_PROGRESS_RANGE);
    }

    @Override
    public void onFinish() {
        millisecondsLeft = SECONDARY_PROGRESS_RANGE;
        progress.setProgress(millisecondsLeft);
        textProgress.setText("0.0");
        progress.setSecondaryProgress(millisecondsLeft);
        onCounterTimerFinish.onFinished();
    }

    public long getMillisecondsLeft() {
        return millisecondsLeft;
    }
    public void setMillisInFuture(long millisInFuture) {
        this.millisInFuture = millisInFuture;
    }
}
