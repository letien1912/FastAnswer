package game.fastanswer;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.games.Games;
import com.google.android.gms.games.Player;
import com.google.android.gms.games.PlayersClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

public class Login extends AppCompatActivity {

    private GoogleSignInClient mGoogleSignInClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mGoogleSignInClient = GoogleSignIn.getClient(this, GoogleSignInOptions.DEFAULT_GAMES_SIGN_IN);

        findViewById(R.id.signin).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startSignInIntent();
            }
        });
    }

    private static final int RC_SIGN_IN = 9001;
    private void startSignInIntent() {
        GoogleSignInClient signInClient = GoogleSignIn.getClient(this,
                GoogleSignInOptions.DEFAULT_GAMES_SIGN_IN);
        Intent intent = signInClient.getSignInIntent();
        startActivityForResult(intent, RC_SIGN_IN);
    }


    private static final int RC_LEADERBOARD_UI = 9004;


    @Override
    protected void onResume() {
        super.onResume();
        signInSilently();
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

    public void signInSilently() {
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

    private String mPlayerId;

    // The currently signed in account, used to check the account has changed outside of this activity when resuming.
    GoogleSignInAccount mSignedInAccount = null;

    private void onConnected(GoogleSignInAccount googleSignInAccount) {
        if (mSignedInAccount != googleSignInAccount) {

            mSignedInAccount = googleSignInAccount;


            // get the playerId from the PlayersClient
            PlayersClient playersClient = Games.getPlayersClient(this, googleSignInAccount);
            playersClient.getCurrentPlayer()
                    .addOnSuccessListener(new OnSuccessListener<Player>() {
                        @Override
                        public void onSuccess(Player player) {
                            mPlayerId = player.getPlayerId();
                            Toast.makeText(getApplicationContext(), "Welcome: " + player.getDisplayName(),Toast.LENGTH_LONG).show();
                        }
                    });
        }
    }
}
