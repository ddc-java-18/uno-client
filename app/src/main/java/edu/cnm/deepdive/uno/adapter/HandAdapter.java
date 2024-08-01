package edu.cnm.deepdive.uno.adapter;

import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.ViewHolder;
import edu.cnm.deepdive.uno.databinding.ItemCardBinding;
import edu.cnm.deepdive.uno.model.domain.Card;
import edu.cnm.deepdive.uno.model.domain.Card.Rank;
import edu.cnm.deepdive.uno.model.domain.Card.Suit;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.Inflater;

public class HandAdapter extends RecyclerView.Adapter<ViewHolder> {

  private final List<Card> cardList;
  private final Context context;
  private final LayoutInflater layoutInflater;
  private final Resources resources;
  private final Map<RankSuitCombo, Integer> cardGraphics;

  public HandAdapter(List<Card> cardList, Context context) {
    this.cardList = cardList;
    this.context = context;
    layoutInflater = LayoutInflater.from(context);
    resources = context.getResources();
    cardGraphics = getCardGraphics();
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

  private Map<RankSuitCombo, Integer> getCardGraphics() {
    Map<RankSuitCombo, Integer> cardGraphics = new HashMap<>();
    for (Rank rank : Rank.values()) {
      if (rank.isSuited()) {
        for (Suit suit : Suit.values()) {
          int id = resources.getIdentifier("card_" + rank.name().toLowerCase() + "_" + suit.name().toLowerCase(), "drawable",
              context.getPackageName());
          cardGraphics.put(new RankSuitCombo(rank, suit), id);
        }
      } else {
        int id = resources.getIdentifier("card_" + rank.name().toLowerCase(), "drawable",
            context.getPackageName());
        cardGraphics.put(new RankSuitCombo(rank, null), id);
      }
    }
    return cardGraphics;
  }

  private class Holder extends RecyclerView.ViewHolder {

    private final ItemCardBinding binding;

    Holder(ItemCardBinding binding) {
      super(binding.getRoot());
      this.binding = binding;
    }

    public void bind(int position, Card card) {
      //noinspection DataFlowIssue
      int drawableId = cardGraphics.get(new RankSuitCombo(card.getRank(), card.getSuit()));
//      binding.image.setImageResource(drawbleId);
      // TODO: 7/31/24 : Set content description with name of rank and name of suit.
      binding.cardRank.setText(card.getRank().toString());
      binding.cardSuit.setText(card.getSuit().toString());
    }
  }
}


