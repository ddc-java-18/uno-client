package edu.cnm.deepdive.uno.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.ViewHolder;
import edu.cnm.deepdive.uno.databinding.ItemCardBinding;
import edu.cnm.deepdive.uno.model.domain.Card;
import java.util.List;

public class HandAdapter extends RecyclerView.Adapter<ViewHolder> {

  private final List<Card> cardList;
  private final Context context;
  private final LayoutInflater layoutInflater;

  public HandAdapter(List<Card> cardList, Context context) {
    this.cardList = cardList;
    this.context = context;
    layoutInflater = LayoutInflater.from(context);
  }

  @NonNull
  @Override
  public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    return new Holder(ItemCardBinding.inflate(layoutInflater, parent, false));
  }

  @Override
  public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
    Card card = cardList.get(position);
    ((Holder)holder).bind(position, card);
  }

  @Override
  public int getItemCount() {
    return cardList.size();
  }

  static class Holder extends RecyclerView.ViewHolder {
   private final ItemCardBinding binding;

    Holder(ItemCardBinding binding) {
      super(binding.getRoot());
      this.binding = binding;
    }

    public void bind (int position, Card card) {
     binding.cardRank.setText(card.getRank().toString());
      binding.cardSuit.setText(card.getSuit().toString());
    }
  }
}

