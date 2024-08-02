package edu.cnm.deepdive.uno.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import edu.cnm.deepdive.uno.model.domain.User;
import java.util.List;

public class UsersAdapter extends RecyclerView.Adapter<UsersAdapter.UserViewHolder> {

  private final List<User> users; // Updated variable name
  private final LayoutInflater layoutInflater;

  public UsersAdapter(List<User> users, LayoutInflater layoutInflater) {
    this.users = users;
    this.layoutInflater = layoutInflater;
  }

  @NonNull
  @Override
  public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    ItemUserBinding binding = ItemUserBinding.inflate(layoutInflater, parent, false);
    return new UserViewHolder(binding);
  }

  @Override
  public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
    User user = users.get(position);
    holder.bind(user);
  }

  @Override
  public int getItemCount() {
    return users.size(); // Return the size of the user list
  }

  public static class UserViewHolder extends RecyclerView.ViewHolder {
    private final ItemUserBinding binding;

    public UserViewHolder(ItemUserBinding binding) {
      super(binding.getRoot());
      this.binding = binding;
    }

    public void bind(User user) {
      binding.userNameTextView.setText(user.getName()); // Assuming User has a getName() method
      // Bind other user properties as needed
    }
  }
}
