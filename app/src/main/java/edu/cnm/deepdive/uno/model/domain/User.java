package edu.cnm.deepdive.uno.model.domain;

import androidx.annotation.NonNull;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import java.time.Instant;
import java.util.Date;

public class User {

  @Expose
  @SerializedName(value = "key", alternate = "id")
  private String id;

  @Expose
  private String displayName;

  @Expose
  private Date created;

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
