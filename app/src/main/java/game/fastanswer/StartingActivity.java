package game.fastanswer;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

public class StartingActivity extends AppCompatActivity {

    private SharedPreferences sharedPref;
    private boolean sound;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_starting);
        sharedPref = getSharedPreferences(getString(R.string.pref_change_sound), MODE_PRIVATE);
        sound = sharedPref.getBoolean(getString(R.string.pref_change_sound), false);
        ChangeSound();
    }

    private void ChangeSound() {

        AudioManager amanager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            amanager.adjustStreamVolume(AudioManager.STREAM_MUSIC, sound ? AudioManager.ADJUST_MUTE : AudioManager.ADJUST_UNMUTE, 0);
        } else {
            amanager.setStreamMute(AudioManager.STREAM_MUSIC, sound);
        }
    }

    public void StartNewGame(View v) {
        Intent it = new Intent(this, MainActivity.class);
        startActivity(it);
    }

    public void ShareGameEvent(View v) {
        String message = "Join The Game !!!";
        Intent share = new Intent(Intent.ACTION_SEND);
        share.setType("text/plain");
        share.putExtra(Intent.EXTRA_TEXT, message);
        startActivity(Intent.createChooser(share, message));
        Log.d("Share Game Event", "Share done");
    }

    public void ChangeSoundEvent(View v) {
        ChangeSoundSharePref();
        ChangeSound();
    }

    private void ChangeSoundSharePref() {
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putBoolean(getString(R.string.pref_change_sound), !sound);
        editor.apply();
    }

    public void ViewScoreEvent(View v) {

    }

}
