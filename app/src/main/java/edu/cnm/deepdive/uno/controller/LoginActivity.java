package edu.cnm.deepdive.uno.controller;

import static edu.cnm.deepdive.uno.R.string.login_failure;

import android.content.Intent;
import android.os.Bundle;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts.StartActivityForResult;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.material.snackbar.Snackbar;
import dagger.hilt.android.AndroidEntryPoint;
import edu.cnm.deepdive.uno.databinding.ActivityLoginBinding;
import edu.cnm.deepdive.uno.viewmodel.LoginViewModel;

@AndroidEntryPoint
public class LoginActivity extends AppCompatActivity {

  private ActivityLoginBinding binding;
  private LoginViewModel viewModel;
  private ActivityResultLauncher<Intent> launcher;
  private boolean silent;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    viewModel = new ViewModelProvider(this).get(LoginViewModel.class);
    getLifecycle().addObserver(viewModel);

    launcher = registerForActivityResult(new StartActivityForResult(), viewModel::completeSignIn);

    silent = true;
    viewModel
        .getAccount()
        .observe(this, this::handleAccount);
    viewModel
        .getThrowable()
        .observe(this, this::informFailure);
  }

  private void handleAccount(GoogleSignInAccount account) {
    if (account != null) {
      // TODO: 7/2/24 Transfer to MainActivity 
//      Intent intent = new Intent(this, MainActivity.class)
//          .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
//      startActivity(intent);
    } else if (silent) {

    }
  }

  private void informFailure(Throwable throwable) {
    if (silent) {
      silent = false;
      binding = ActivityLoginBinding.inflate(getLayoutInflater());
      binding.signIn.setOnClickListener((v) -> viewModel.startSignIn(launcher));
      setContentView(binding.getRoot());
    } else if (throwable != null) {
      Snackbar.make(binding.getRoot(), login_failure, Snackbar.LENGTH_LONG)
          .show();
    }
  }
}