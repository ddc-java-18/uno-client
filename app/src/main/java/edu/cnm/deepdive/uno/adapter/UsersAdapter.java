package edu.cnm.deepdive.uno.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.ViewHolder;
import edu.cnm.deepdive.uno.databinding.ItemUserBinding;
import edu.cnm.deepdive.uno.model.domain.Hand;
import edu.cnm.deepdive.uno.model.domain.User;
import java.util.List;

public class UsersAdapter extends RecyclerView.Adapter<ViewHolder> {

  private final List<Hand> hands;
  private final LayoutInflater layoutInflater;

  public UsersAdapter(Context context, List<Hand> hands) {
    this.hands = hands;
    this.layoutInflater = LayoutInflater.from(context);
  }

  @NonNull
  @Override
  public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    return new UserViewHolder(ItemUserBinding.inflate(layoutInflater, parent, false));
  }

  @Override
  public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
    Hand hand = hands.get(position);
    ((UserViewHolder) holder).bind(hand);
  }

  @Override
  public int getItemCount() {
    return hands.size();
  }

  private class UserViewHolder extends RecyclerView.ViewHolder {

    private final ItemUserBinding binding;

    public UserViewHolder(ItemUserBinding binding) {
      super(binding.getRoot());
      this.binding = binding;
    }

    public void bind(Hand hand) {
      binding.userDisplayName.setText(hand.getUser().getDisplayName());
      String numberOfCards = String.valueOf(hand.getNumberOfCards());
      binding.userCardCount.setText(numberOfCards);
    }
  }
}
