package edu.cnm.deepdive.uno.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.ViewHolder;
import edu.cnm.deepdive.uno.databinding.ItemCardBinding;
import edu.cnm.deepdive.uno.model.domain.Card;
import edu.cnm.deepdive.uno.model.domain.Card.Rank;
import edu.cnm.deepdive.uno.model.domain.Card.Suit;
import java.util.List;
import java.util.Map;

public class HandAdapter extends RecyclerView.Adapter<ViewHolder> {

  private final List<Card> cardList;
  private final LayoutInflater layoutInflater;
  private final Map<Rank, Integer> rankDrawables;
  private final Map<Suit, Integer> suitColors;
  private final OnCardClickListener onCardClickListener;

  /**
   * Constructs a new HandAdapter instance.
   *
   * @param context            The context in which the adapter is running, used for layout inflation and resource access.
   * @param cardList          A list of Card objects to be displayed by the adapter.
   * @param rankDrawables     A map that associates each Rank with its corresponding drawable resource ID for card representation.
   * @param suitColors        A map that associates each Suit with its corresponding color resource ID for card representation.
   * @param onCardClickListener The listener that will be notified when a card is clicked.
   */
  public HandAdapter(Context context, List<Card> cardList, Map<Rank, Integer> rankDrawables,
      Map<Suit, Integer> suitColors, OnCardClickListener onCardClickListener) {
    this.cardList = cardList;
    layoutInflater = LayoutInflater.from(context);
    this.rankDrawables = rankDrawables;
    this.suitColors = suitColors;
    this.onCardClickListener = onCardClickListener;
  }

  @NonNull
  @Override
  public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    return new Holder(ItemCardBinding.inflate(layoutInflater, parent, false));
  }

  @Override
  public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
    Card card = cardList.get(position);

    ((Holder) holder).bind(position, card);
  }

  @Override
  public int getItemCount() {
    return cardList.size();
  }

  /**
   * Handles the event when an item in the card list is clicked.
   *
   * This method retrieves the Card at the specified position in the card list and notifies that the item at this position has changed, prompting the adapter to refresh its view.
   *
   * @param position The position of the clicked item in the card list.
   * @throws IndexOutOfBoundsException if the specified position is out of range
   *                                    (position &lt; 0 || position >= cardList.size()).
   */
  public void onItemClicked(int position) {
    Card c = cardList.get(position);
    notifyItemChanged(position);
  }

  private class Holder extends RecyclerView.ViewHolder {

    private final ItemCardBinding binding;

    Holder(ItemCardBinding binding) {
      super(binding.getRoot());
      this.binding = binding;
    }

    /**
     * Binds the data from the specified Card to the view at the given position.
     *
     * This method updates the card image, applies a color filter based on the card's suit, sets the content description, and configures the visibility of the selection indicator.
     *
     * @param position The position of the item within the adapter.
     * @param card     The Card object containing the data to bind to the view.
     */
    public void bind(int position, Card card) {
      //noinspection DataFlowIssue
      int drawableId = rankDrawables.get(card.getRank());

      binding.cardImage.setImageResource(drawableId);

      if (card.getSuit() != null) {
        binding.cardImage.setColorFilter(suitColors.get(card.getSuit()));
      }

      String description =  card.getRank().toString() + " of ";
      description += (card.getSuit() != null) ? card.getSuit().toString() : "WILD";

      if (card.isSelectedByUser()) {
        binding.selectedCard.setVisibility(View.VISIBLE);
      } else {
        binding.selectedCard.setVisibility(View.INVISIBLE);
      }
      binding.cardImage.setContentDescription(description);
      binding.getRoot().setOnClickListener((v) -> {
        onCardClickListener.onCardClick(position, card);
      });
    }
  }

  /**
   * A functional interface for handling card click events.
   *
   * <p>This interface defines a single method, onCardClick(int, Card), which is called when a card is clicked. Implementations of this interface can define the behavior that should occur upon a card click.</p>
   */
  @FunctionalInterface
  public interface OnCardClickListener {

    void onCardClick(int position, Card newSelectedCard);
  }
}

