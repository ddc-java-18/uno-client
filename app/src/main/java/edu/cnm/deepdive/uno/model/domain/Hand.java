package edu.cnm.deepdive.uno.model.domain;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import java.util.LinkedList;
import java.util.List;

/**
 * The Hand class encapsulates the essential attributes of a Hand in a game of UNO. This class
 * provides methods to retrieve details about a particular player hand.
 */
public class Hand {

  @Expose(serialize = false)
  @SerializedName(value = "key", alternate = "id")
  private final String id;

  @Expose(serialize = false)
  private final User user;

  @Expose(serialize = false)
  private final Game game;

  @Expose(serialize = false)
  private final int points;

  @Expose(serialize = false)
  private final List<Card> cards;

  @Expose(serialize = false)
  private final boolean turn;

  @Expose(serialize = false)
  private final int numberOfCards;

  /**
   * Constructs a representation of a players hand in a game of UNO.
   */
  public Hand() {
    id = null;
    user = null;
    game = null;
    points = 0;
    cards = new LinkedList<>();
    turn = false;
    numberOfCards = 0;
  }

  /**
   * Returns a String representing the external key associated with a player's hand in an UNO game.
   *
   * @return String representing the external key associated with a player's hand in an UNO game.
   */
  public String getId() {
    return id;
  }

  /**
   * Returns the User who is associated with the UNO hand.
   *
   * @return User associated with the UNO hand.
   */
  public User getUser() {
    return user;
  }

  /**
   * Returns the game which the UNO hand belongs to.
   *
   * @return Game which the hand belongs to.
   */
  public Game getGame() {
    return game;
  }

  /**
   * Returns the point value of the UNO hand. The point value of an UNO hand is calculated by
   * summing up the card values in the hand.
   *
   * @return int representing the point value of the UNO hand.
   */
  public int getPoints() {
    return points;
  }

  /**
   * Returns the cards that are a part of the UNO hand.
   *
   * @return List of Card in the hand.
   */
  public List<Card> getCards() {
    return cards;
  }

  /**
   * Returns a boolean flag indicating if it is the current hand's turn.
   *
   * @return boolean indicating if it is the current hand's turn.
   */
  public boolean isTurn() {
    return turn;
  }

  /**
   * Returns the number of cards in a hand.
   *
   * @return int representing the number of cards in a hand.
   */
  public int getNumberOfCards() {
    return numberOfCards;
  }
}
