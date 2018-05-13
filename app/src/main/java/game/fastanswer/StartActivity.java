package game.fastanswer;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.games.Games;
import com.google.android.gms.games.Player;
import com.google.android.gms.games.PlayersClient;
import com.google.android.gms.games.RealTimeMultiplayerClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import Entities.GamePlay;

public class StartActivity extends AppCompatActivity {

    private SharedPreferences sharedPref;
    private ImageView btSound;
    private GoogleSignInClient mGoogleSignInClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_starting);

        mGoogleSignInClient = GoogleSignIn.getClient(this, GoogleSignInOptions.DEFAULT_GAMES_SIGN_IN);
        Init();

        if(!isSignedIn())
            startSignInIntent();
    }

    private void Init() {
        btSound = (ImageView) findViewById(R.id.button_sound);
        sharedPref = getSharedPreferences(getString(R.string.pref_change_sound), MODE_PRIVATE);
    }

    private static final int RC_SIGN_IN = 9001;

    private void startSignInIntent() {
        GoogleSignInClient signInClient = GoogleSignIn.getClient(this,
                GoogleSignInOptions.DEFAULT_GAMES_SIGN_IN);
        Intent intent = signInClient.getSignInIntent();
        startActivityForResult(intent, RC_SIGN_IN);
    }


    private static final int RC_LEADERBOARD_UI = 9004;

    public void ChangeSoundEvent(View v) {
        ChangeSound();
        SetSoundIcon();
//        if (isSignedIn()) {
//            showLeaderboard();
//        } else {
//            startSignInIntent();
//        }
    }


    private void ChangeSound() {
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.clear();
        editor.putBoolean(getString(R.string.pref_change_sound), !getSoundCurrent());
        editor.apply();
        editor.commit();
    }

    private void SetSoundIcon() {
        Log.d("asdas", getSoundCurrent() + "");
        if (getSoundCurrent()) {
            btSound.setImageResource(R.drawable.ic_sound);
        } else {
            btSound.setImageResource(R.drawable.ic_sound_turn_off);
        }
    }

    private boolean getSoundCurrent() {
        return sharedPref.getBoolean(getString(R.string.pref_change_sound), true);
    }

    public void StartNewGame(View v) {
        Intent it = new Intent(this, MainActivity.class);
        GamePlay gamePlay = (GamePlay) getIntent().getExtras().get(getString(R.string.GAMEPLAY_CONTENT));
        it.putExtra(getString(R.string.GAMEPLAY_CONTENT), gamePlay);
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

    public void ViewScoreEvent(View v) {
        startSignInIntent();
    }


    @Override
    protected void onResume() {
        super.onResume();
        signInSilently();
        SetSoundIcon();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == RC_SIGN_IN) {

            Task<GoogleSignInAccount> task =
                    GoogleSignIn.getSignedInAccountFromIntent(data);

            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                onConnected(account);
            } catch (ApiException apiException) {
                String message = apiException.getMessage();
                if (message == null || message.isEmpty()) {
                    message = "mhu loz";
                }


                new android.app.AlertDialog.Builder(this)
                        .setMessage(message)
                        .setNeutralButton(android.R.string.ok, null)
                        .show();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private boolean isSignedIn() {
        return GoogleSignIn.getLastSignedInAccount(this) != null;
    }

    private void signInSilently() {
        mGoogleSignInClient.silentSignIn().addOnCompleteListener(this,
                new OnCompleteListener<GoogleSignInAccount>() {
                    @Override
                    public void onComplete(@NonNull Task<GoogleSignInAccount> task) {
                        if (task.isSuccessful()) {
                            onConnected(task.getResult());
                        } else {
                            Log.d("tag", "signInSilently(): failure", task.getException());
                        }
                    }
                });
    }

    // The currently signed in account, used to check the account has changed outside of this activity when resuming.
    private GoogleSignInAccount mSignedInAccount = null;
    private RealTimeMultiplayerClient mRealTimeMultiplayerClient;
    private String mPlayerId;

    private void onConnected(GoogleSignInAccount googleSignInAccount) {
        if (mSignedInAccount != googleSignInAccount) {

            mSignedInAccount = googleSignInAccount;

            // update the clients
            mRealTimeMultiplayerClient = Games.getRealTimeMultiplayerClient(this, googleSignInAccount);

            // get the playerId from the PlayersClient
            PlayersClient playersClient = Games.getPlayersClient(this, googleSignInAccount);
            playersClient.getCurrentPlayer()
                    .addOnSuccessListener(new OnSuccessListener<Player>() {
                        @Override
                        public void onSuccess(Player player) {
                            mPlayerId = player.getPlayerId();
                            Toast.makeText(getApplicationContext(), "Welcome " + player.getDisplayName(), Toast.LENGTH_LONG).show();
                        }
                    });

        }
    }

}
