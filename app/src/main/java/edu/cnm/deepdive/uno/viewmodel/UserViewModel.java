package edu.cnm.deepdive.uno.viewmodel;

import androidx.lifecycle.DefaultLifecycleObserver;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import dagger.hilt.android.lifecycle.HiltViewModel;
import edu.cnm.deepdive.uno.model.domain.User;
import edu.cnm.deepdive.uno.service.UserRepository;
import javax.inject.Inject;

/**
 * ViewModel class for managing user data.
 */
@HiltViewModel
public class UserViewModel extends ViewModel implements DefaultLifecycleObserver {

  private final UserRepository userRepository;
  private final MutableLiveData<User> user;

  /**
   * Constructs a UserViewModel using the provided userRepository.
   *
   * @param userRepository The repository used to get user data.
   */
  @Inject
  public UserViewModel(UserRepository userRepository) {
    this.userRepository = userRepository;
    user = new MutableLiveData<>();
    this.userRepository.getCurrentUser()
        .doOnSuccess(user::postValue)
        .subscribe();
  }

  /**
   * Returns the LivaData used to store a user.
   *
   * @return LiveData containing the current User.
   */
  public LiveData<User> getUser() {
    return user;
  }

}
