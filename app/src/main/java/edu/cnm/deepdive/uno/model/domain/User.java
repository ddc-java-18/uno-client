package edu.cnm.deepdive.uno.model.domain;

import androidx.annotation.NonNull;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import java.time.Instant;
import java.util.Date;

/**
 * The User class encapsulates the essential attributes of a user/player in the UNO application.
 * This class provides methods to retrieve user details.
 */
public class User {

  @Expose
  @SerializedName(value = "key", alternate = "id")
  private String id;

  @Expose
  private String displayName;

  @Expose
  private Date created;

  /**
   *  Constructs a User.
   */
  public User() {
    id = null;
    displayName = "";
    created = new Date();
  }

  public String getId() {
    return id;
  }

  @NonNull
  public String getDisplayName() {
    return displayName;
  }

  @NonNull
  public Date getCreated() {
    return created;
  }

}
