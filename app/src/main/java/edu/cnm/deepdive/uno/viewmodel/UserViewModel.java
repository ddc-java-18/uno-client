package edu.cnm.deepdive.uno.viewmodel;

import androidx.lifecycle.DefaultLifecycleObserver;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import dagger.hilt.android.lifecycle.HiltViewModel;
import edu.cnm.deepdive.uno.model.domain.User;
import edu.cnm.deepdive.uno.service.UserRepository;
import javax.inject.Inject;

@HiltViewModel
public class UserViewModel extends ViewModel implements DefaultLifecycleObserver {

  private final UserRepository userRepository;
  private final MutableLiveData<User> user;

  @Inject
  public UserViewModel(UserRepository userRepository) {
    this.userRepository = userRepository;
    user = new MutableLiveData<>();
    this.userRepository.getCurrentUser()
        .doOnSuccess(user::postValue)
        .subscribe();
  }

  public LiveData<User> getUser() {
    return user;
  }

}
