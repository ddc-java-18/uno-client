package edu.cnm.deepdive.uno.service;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultLauncher;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import dagger.hilt.android.qualifiers.ApplicationContext;
import edu.cnm.deepdive.uno.R;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.core.SingleEmitter;
import io.reactivex.rxjava3.schedulers.Schedulers;
import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Service for handling Google Sign-In operations.
 */
@Singleton
public class GoogleSignInService {

  private static final String TAG = GoogleSignInService.class.getSimpleName();
  private static final String BEARER_TOKEN_FORMAT = "Bearer %s";

  private final GoogleSignInClient client;

  /**
   * Constructs an instance of GoogleSignInService used to handel Google sign-in operations.
   *
   * @param context The application's context.
   */
  @Inject
  GoogleSignInService(@ApplicationContext Context context) {
    String clientId = context.getString(R.string.client_id);
    GoogleSignInOptions options = new GoogleSignInOptions.Builder()
        .requestEmail()
        .requestId()
        .requestProfile()
        .requestIdToken(clientId)
        .build();
    client = GoogleSignIn.getClient(context, options);
  }

  /**
   * Performs a silentSignIn and logs the associated bearer token.
   *
   * @return RxJava Single of GoogleSignInAccount.
   */
  public Single<GoogleSignInAccount> refresh() {
    return Single.create((SingleEmitter<GoogleSignInAccount> emitter) ->
            client
                .silentSignIn()
                .addOnSuccessListener(emitter::onSuccess)
                .addOnSuccessListener((account) -> {
                  Log.d(TAG, account.getIdToken());
                }) // FIXME: 7/2/24 Remove ASAP
                .addOnFailureListener(emitter::onError)
        )
        .observeOn(Schedulers.io());
  }

  /**
   * Refreshes the sign-in bearer token.
   *
   * @return RxJava Single containing the bearer token.
   */
  public Single<String> refreshToken() {
    return refresh()
        .map((account) -> String.format(BEARER_TOKEN_FORMAT, account.getIdToken()));
  }

  /**
   * Kicks-off the Google sign-in process.
   *
   * @param launcher An ActivityResultLauncher used to launch the sign-in process.
   */
  public void startSignIn(ActivityResultLauncher<Intent> launcher) {
    launcher.launch(client.getSignInIntent());
  }

  /**
   * Completes the sign-in process.
   *
   * @param result the ActivityResult
   * @return RxJava Single that emits the signed in GoogleSignInAccount.
   */
  public Single<GoogleSignInAccount> completeSignIn(ActivityResult result) {
    return Single.create((SingleEmitter<GoogleSignInAccount> emitter) -> {
          try {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(
                result.getData());
            GoogleSignInAccount account = task.getResult(ApiException.class);
            Log.d(TAG, account.getIdToken());
            emitter.onSuccess(account);
          } catch (ApiException e) {
            emitter.onError(e);
          }
        })
        .observeOn(Schedulers.io());
  }

  /**
   * Handles signing a user out of the application.
   *
   * @return A RxJava Completable which indicates the user has been signed out.
   */
  public Completable signOut() {
    return Completable.create((emitter) ->
        client
            .signOut()
            .addOnSuccessListener((ignored) -> emitter.onComplete())
            .addOnFailureListener(emitter::onError)
    ).observeOn(Schedulers.io());
  }

}
