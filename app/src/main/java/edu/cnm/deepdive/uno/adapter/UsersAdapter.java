package edu.cnm.deepdive.uno.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.ViewHolder;
import edu.cnm.deepdive.uno.R;
import edu.cnm.deepdive.uno.databinding.ItemUserBinding;
import edu.cnm.deepdive.uno.model.domain.Hand;
import edu.cnm.deepdive.uno.model.domain.User;
import java.util.List;

public class UsersAdapter extends RecyclerView.Adapter<ViewHolder> {

  private final List<Hand> hands;
  private final LayoutInflater layoutInflater;
  @ColorInt
  private final int currentPlayerBackground;


  /**
   * Constructs a new UsersAdapter instance.
   *
   * @param context The context in which the adapter is running, used for layout inflation.
   * @param hands   A list of Hand objects representing the hands to be managed by the adapter.
   */
  public UsersAdapter(@NonNull Context context,@NonNull List<Hand> hands) {
    this.hands = hands;
    this.layoutInflater = LayoutInflater.from(context);
    currentPlayerBackground = context.getColor(R.color.current_player_background);
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

    /**
     * ViewHolder for displaying user information in the UsersAdapter.
     *
     * <p>This constructor initializes the ViewHolder with the specified ItemUserBinding and sets the root view for the ViewHolder.</p>
     *
     * @param binding The {@link ItemUserBinding} instance that provides access to the user item layout views.
     */
    public UserViewHolder(ItemUserBinding binding) {
      super(binding.getRoot());
      this.binding = binding;
    }

    /**
     * Binds the specified Hand data to the ViewHolder's views.
     *
     * <p>This method updates the user display name and the number of cards for the specified hand in the user item layout.</p>
     *
     * @param hand The Hand object containing the user data to be displayed.
     */
    public void bind(Hand hand) {
      binding.userDisplayName.setText(hand.getUser().getDisplayName());
      String numberOfCards = String.valueOf(hand.getNumberOfCards());
      binding.userCardCount.setText(numberOfCards);
      binding.getRoot().setBackgroundColor(hand.isTurn() ? currentPlayerBackground : Color.TRANSPARENT);
    }
  }
}
