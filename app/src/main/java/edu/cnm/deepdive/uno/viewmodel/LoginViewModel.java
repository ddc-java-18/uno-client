package edu.cnm.deepdive.uno.viewmodel;

import android.content.Intent;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.lifecycle.DefaultLifecycleObserver;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import dagger.hilt.android.lifecycle.HiltViewModel;
import edu.cnm.deepdive.uno.service.GoogleSignInService;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import javax.inject.Inject;

/**
 * ViewModel class for managing Google Sign-In operations.
 */
@HiltViewModel
public class LoginViewModel extends ViewModel implements DefaultLifecycleObserver {

  private final GoogleSignInService signInService;
  private final MutableLiveData<GoogleSignInAccount> account;
  private final MutableLiveData<Throwable> throwable;
  private final CompositeDisposable pending;

  /**
   * Constructs a LoginView Model.
   *
   * @param signInService The GoogleSignInService used to perform sign-in operations.
   */
  @Inject
  LoginViewModel(GoogleSignInService signInService) {
    this.signInService = signInService;
    account = new MutableLiveData<>();
    throwable = new MutableLiveData<>();
    pending = new CompositeDisposable();
    refresh();
  }

  @Override
  public void onStop(@NonNull LifecycleOwner owner) {
    pending.clear();
    DefaultLifecycleObserver.super.onStop(owner);
  }

  /**
   * Returns the LiveData containing the signed-in account data.
   * @return LiveData that emits the signed-in account data.
   */
  public LiveData<GoogleSignInAccount> getAccount() {
    return account;
  }

  /**
   * Returns the LiveData containing any errors caused by sign-in operations.
   * @return LiveData that emits any throwable errors.
   */
  public LiveData<Throwable> getThrowable() {
    return throwable;
  }

  /**
   * Refreshes the bearer token and updates the account data.
   */
  public void refresh(){
    throwable.setValue(null);
    signInService
        .refresh()
        .subscribe(
            this.account::postValue,
            this.throwable::postValue,
            pending
        );
  }

  /**
   * Initiates the Google sign-in process.
   * @param launcher The launcher to start the sign-in intent.
   */
  public void startSignIn(ActivityResultLauncher<Intent> launcher) {
    signInService.startSignIn(launcher);
  }

  /**
   * Completes the Google sign-in process.
   * @param result The ActivityResult of the sign-in process.
   */
  public void completeSignIn(ActivityResult result) {
    throwable.setValue(null);
    signInService
        .completeSignIn(result)
        .subscribe(
            account::postValue,
            throwable::postValue,
            pending
        );
  }

  /**
   * Signs out the user who is currently signed in.
   */
  public void signOut() {
    throwable.setValue(null);
    signInService
        .signOut()
        .doFinally(() -> account.postValue(null))
        .subscribe(
            () -> {},
            throwable::postValue,
            pending
        );
  }

}
