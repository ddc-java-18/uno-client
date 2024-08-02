package edu.cnm.deepdive.uno.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.ViewHolder;
import edu.cnm.deepdive.uno.databinding.ItemUserBinding;
import edu.cnm.deepdive.uno.model.domain.User;
import java.util.List;

public class UsersAdapter extends RecyclerView.Adapter<ViewHolder> {

  private final List<User> users;
  private final LayoutInflater layoutInflater;

  public UsersAdapter(Context context, List<User> users) {
    this.users = users;
    this.layoutInflater = LayoutInflater.from(context);
  }

  @NonNull
  @Override
  public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//    ItemUserBinding binding = ;
    return new UserViewHolder(ItemUserBinding.inflate(layoutInflater, parent, false));
  }

  @Override
  public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
    User user = users.get(position);
    ((UserViewHolder) holder).bind(user);
  }

  @Override
  public int getItemCount() {
    return users.size();
  }

  private class UserViewHolder extends RecyclerView.ViewHolder {

    private final ItemUserBinding binding;

    public UserViewHolder(ItemUserBinding binding) {
      super(binding.getRoot());
      this.binding = binding;
    }

    public void bind(User user) {
      binding.userDisplayName.setText(user.getDisplayName());
    }
  }
}
