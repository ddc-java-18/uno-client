package edu.cnm.deepdive.uno.adapter;

import android.content.Context;
import android.view.LayoutInflater;
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

  private class Holder extends RecyclerView.ViewHolder {

    private final ItemCardBinding binding;

    Holder(ItemCardBinding binding) {
      super(binding.getRoot());
      this.binding = binding;
    }

    public void bind(int position, Card card) {
      //noinspection DataFlowIssue
      int drawableId = rankDrawables.get(card.getRank());

      binding.cardImage.setImageResource(drawableId);

      //noinspection DataFlowIssue
      binding.cardImage.setColorFilter(suitColors.get(card.getSuit()));

      binding.cardImage.setContentDescription(
          card.getRank().toString() + " of " + card.getSuit().toString());
      binding.getRoot().setOnClickListener((v) -> onCardClickListener.onCardClick(position, card));
    }
  }

  @FunctionalInterface
  public interface OnCardClickListener {

    void onCardClick(int position, Card card);
  }
}

