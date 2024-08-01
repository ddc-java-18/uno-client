package edu.cnm.deepdive.uno.model.domain;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

/**
 * The Game class encapsulates the essential attributes of a game of UNO. This class provides
 * methods to retrieve game details.
 */
public final class Game {

  @Expose(serialize = false)
  @SerializedName(value = "key", alternate = "id")
  private final String id;

  @Expose(serialize = false)
  private final Date startTime;

  @Expose(serialize = false)
  private final Date endTime;

  @Expose(serialize = false)
  private final Date lastUpdatedOn;

  @Expose(serialize = false)
  private final boolean gameCompleted;

  @Expose(serialize = false)
  private final boolean rotationOrder;

  @Expose(serialize = false)
  private final User winner;

  @Expose(serialize = false)
  private final Card topDiscard;

  @Expose
  private final int minPlayer;

  @Expose
  private final int maxPlayer;

  @Expose(serialize = false)
  private final int playerCount;

  @Expose(serialize = false)
  private final GameState gameState;

  @Expose(serialize = false)
  private final List<Hand> hands;

  // TODO: 7/29/24 Might need to add method to get the current players hand List<Card> cards.

  /**
   * Constructs an UNO game using the specified min and max number of players.
   *
   * @param minPlayer number of minimum players to start the game.
   * @param maxPlayer maximum number of players who can join the game.
   */
  public Game(int minPlayer, int maxPlayer) {
    this.minPlayer = minPlayer;
    this.maxPlayer = maxPlayer;
    id = null;
    startTime = null;
    endTime = null;
    lastUpdatedOn = null;
    gameCompleted = false;
    rotationOrder = false;
    winner = null;
    topDiscard = null;
    playerCount = 0;
    gameState = GameState.CREATED;
    hands = new LinkedList<>();
  }

  /**
   * Returns a String representing the external key associated with the UNO game.
   *
   * @return a String used to identify a specific UNO game.
   */
  public String getId() {
    return id;
  }

  /**
   * Returns a Date object representing the time at which the UNO game was created.
   *
   * @return Date representing the time at which the UNO game was created.
   */
  public Date getStartTime() {
    return startTime;
  }

  /**
   * Returns a Date object representing the time at which the UNO game concluded.
   *
   * @return Date representing the time at which the UNO game concluded.
   */
  public Date getEndTime() {
    return endTime;
  }

  public Date getLastUpdatedOn() {
    return lastUpdatedOn;
  }

  /**
   * Returns whether the game has concluded based.
   *
   * @return boolean indicating if the game has concluded.
   */
  public boolean isGameCompleted() {
    return gameCompleted;
  }

  /**
   * Returns a boolean flag indicating the player turn order direction.
   *
   * @return boolean indicating the play turn order in the UNO game.
   */
  public boolean isRotationOrder() {
    return rotationOrder;
  }

  /**
   * Returns the User who won the game of UNO.
   *
   * @return User who won the game.
   */
  public User getWinner() {
    return winner;
  }

  /**
   * Returns the Card at the top of the UNO game discard pile.
   *
   * @return Card from the top of the game discard pile.
   */
  public Card getTopDiscard() {
    return topDiscard;
  }

  /**
   * Returns the minimum number of players needed to start the UNO game.
   *
   * @return int representing the minimum number of players needed to start the UNO game.
   */
  public int getMinPlayer() {
    return minPlayer;
  }

  /**
   * Returns the max number of players that can join the UNO game.
   *
   * @return int representing the max number of players which can join the UNO game,
   */
  public int getMaxPlayer() {
    return maxPlayer;
  }

  /**
   * Returns the count/number of players in the game.
   *
   * @return int representing the number of players in the game.
   */
  public int getPlayerCount() {
    return playerCount;
  }

  /**
   * Returns the GameState which the UNO game is currently in.
   *
   * @return GameState at which the game is currently at.
   */
  public GameState getGameState() {
    return gameState;
  }

  /**
   * Returns the current hands in the game of UNO.
   *
   * @return List of Hand representing hands of the player's in the game.
   */
  public List<Hand> getHands() {
    return hands;
  }

  public Card validateMove(Card card) {
    // Check that it is the current players turn
    // Check if card is in players hand
    // Check that card meets playing conditions according to top discard card.
    return card;
  }

  /**
   * Enumeration representing the various game states a game of UNO can be in at any given point in
   * time.
   */
  public enum GameState {
    CREATED, IN_PROGRESS, COMPLETED
  }
}
