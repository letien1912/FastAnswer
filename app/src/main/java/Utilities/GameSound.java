package Utilities;

import android.content.Context;
import android.media.MediaPlayer;

import game.fastanswer.R;

import static android.content.Context.MODE_APPEND;
import static android.content.Context.MODE_PRIVATE;

/**
 * Created by Admin on 26/05/2017.
 */

public class GameSound {
    private MediaPlayer gameOverSound;
    private MediaPlayer nextQuestionSound;
    private Context mContext;

    public GameSound(Context context) {
        mContext = context;
        gameOverSound = MediaPlayer.create(context, R.raw.game_over_sound);
        nextQuestionSound = MediaPlayer.create(context, R.raw.next_question_sound);
    }

    public void PlayGameOverSound() {
        if (IsSoundTurnOn())
            gameOverSound.start();
    }

    public void PlayNextQuestionSound() {
        if (IsSoundTurnOn())
            nextQuestionSound.start();
    }

    private boolean IsSoundTurnOn() {
        return mContext.getSharedPreferences(mContext.getString(R.string.pref_change_sound), MODE_APPEND).getBoolean(mContext.getString(R.string.pref_change_sound), true);
    }

}
