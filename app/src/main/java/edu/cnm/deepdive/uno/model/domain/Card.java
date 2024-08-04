package edu.cnm.deepdive.uno.model.domain;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * The Card class encapsulates the essential attributes of a Card in a game of UNO. This class
 * provides methods to retrieve details about an UNO card.
 */
public class Card {

  @Expose
  @SerializedName(value = "key", alternate = "id")
  private String id;

  @Expose
  private final Suit suit;

  @Expose
  private final Rank rank;

  @Expose(serialize = false)
  private final Game game;

  @Expose(serialize = false)
  private final boolean inDeck;

  @Expose(serialize = false)
  private final int position;

  @Expose(serialize = false, deserialize = false)
  private boolean selectedByUser;

  /**
   * Constructs an UNO Card with the specified Suit and Rank.
   *
   * @param suit The Suit (aka color) of the UNO Card.
   * @param rank The Rank (aka face value) of the UNO Card.
   */
  public Card(Suit suit, Rank rank) {
    this.suit = suit;
    this.rank = rank;
    id = null;
    game = null;
    inDeck = false;
    position = 0;
  }

  /**
   * Returns a String representing the external key associated with the UNO card.
   *
   * @return String representing the external key associated with the UNO card.
   */
  public String getId() {
    return id;
  }

  /**
   * Sets the id of a card.
   * @param id used to set the id of the card.
   */
  public void setId(String id) {
    this.id = id;
  }

  /**
   * Returns the Suite (color) of the UNO card.
   *
   * @return Suit (color) of the UNO card.
   */
  public Suit getSuit() {
    return suit;
  }

  /**
   * Returns the Rank (face value) of the UNO card.
   *
   * @return Rank (face value) of the UNO card.
   */
  public Rank getRank() {
    return rank;
  }

  /**
   * Returns the Game which the UNO card pertains to.
   *
   * @return Game which the UNO card belongs to.
   */
  public Game getGame() {
    return game;
  }

  /**
   * Returns a boolean flag indicating if the UNO card is in the UNO deck.
   *
   * @return boolean flag indicating if the UNO card is the game deck.
   */
  public boolean isInDeck() {
    return inDeck;
  }

  /**
   * Returns the position in the deck the card is in.
   *
   * @return int the card position.
   */
  public int getPosition() {
    return position;
  }

  /**
   * Returns if the card is selected by a user.
   *
   * @return boolean indicating if the card is selected by a user.
   */
  public boolean isSelectedByUser() {
    return selectedByUser;
  }

  /**
   * Sets the selectedByUser field of a card.
   *
   * @param selectedByUser boolean valued used to set the card's selectedByUser field.
   */
  public void setSelectedByUser(boolean selectedByUser) {
    this.selectedByUser = selectedByUser;
  }

  /**
   * An Enumeration containing all the possible Ranks (face values) an UNO card can have in a game
   * of UNO.
   */
  public enum Rank {
    ZERO(true),
    ONE(true),
    TWO(true),
    THREE(true),
    FOUR(true),
    FIVE(true),
    SIX(true),
    SEVEN(true),
    EIGHT(true),
    NINE(true),
    SKIP(true),
    REVERSE(true),
    DRAW_TWO(true);
//    DRAW_FOUR(false),
//    WILD(false);

    private final boolean suited;

    /**
     * Constructor used to initialize a rank.
     *
     * @param suited boolean indicating if the Rank should have an associated suit or not.
     */
    Rank(boolean suited) {
      this.suited = suited;
    }

    /**
     * Returns if the Rank should have an associated suit.
     *
     * @return boolean indicating if the Rank should have an associated suit.
     */
    public boolean isSuited() {
      return suited;
    }
  }

  /**
   * An Enumeration containing the possible Suits (color) an UNO card can have in a game of UNO.
   */
  public enum Suit {
    RED,
    BLUE,
    YELLOW,
    GREEN
  }
}

