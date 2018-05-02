/*
 * Hidden Switch Spellsource API
 * The Spellsource API for matchmaking, user accounts, collections management and more.  To get started, create a user account and make sure to include the entirety of the returned login token as the X-Auth-Token header. You can reuse this token, or login for a new one.  ClientToServerMessage and ServerToClientMessage are used for the realtime game state and actions two-way websocket interface for actually playing a game. Envelope is used for the realtime API services. 
 *
 * OpenAPI spec version: 1.1.0
 * Contact: ben@hiddenswitch.com
 *
 * NOTE: This class is auto generated by the swagger code generator program.
 * https://github.com/swagger-api/swagger-codegen.git
 * Do not edit the class manually.
 */


package com.hiddenswitch.spellsource.client.models;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.hiddenswitch.spellsource.client.models.DecksUpdateCommandPushCardIds;
import com.hiddenswitch.spellsource.client.models.DecksUpdateCommandPushInventoryIds;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.ArrayList;
import java.util.List;
import java.io.Serializable;

/**
 * This command contains a variable number of changes to apply to a deck. Whenever multiple fields are set, the server will try to resolve their effects in the least surprising way possible.  Specifically, if setInventoryIds is set, it will override all other changes to the inventory. Otherwise, removals will be evaluated first, preferring inventory ID removals over card ID removals, followed by adds. 
 */
@ApiModel(description = "This command contains a variable number of changes to apply to a deck. Whenever multiple fields are set, the server will try to resolve their effects in the least surprising way possible.  Specifically, if setInventoryIds is set, it will override all other changes to the inventory. Otherwise, removals will be evaluated first, preferring inventory ID removals over card ID removals, followed by adds. ")

public class DecksUpdateCommand implements Serializable {
  private static final long serialVersionUID = 1L;

  @JsonProperty("setHeroClass")
  private String setHeroClass = null;

  @JsonProperty("setName")
  private String setName = null;

  @JsonProperty("setInventoryIds")
  private List<String> setInventoryIds = null;

  @JsonProperty("pushInventoryIds")
  private DecksUpdateCommandPushInventoryIds pushInventoryIds = null;

  @JsonProperty("pushCardIds")
  private DecksUpdateCommandPushCardIds pushCardIds = null;

  @JsonProperty("pullAllInventoryIds")
  private List<String> pullAllInventoryIds = null;

  @JsonProperty("pullAllCardIds")
  private List<String> pullAllCardIds = null;

  public DecksUpdateCommand setHeroClass(String setHeroClass) {
    this.setHeroClass = setHeroClass;
    return this;
  }

   /**
   * Sets the hero class of the deck in this command. If the deck now contains cards that no longer belong to this hero class, the deck becomes invalid under standard rules. 
   * @return setHeroClass
  **/
  @ApiModelProperty(value = "Sets the hero class of the deck in this command. If the deck now contains cards that no longer belong to this hero class, the deck becomes invalid under standard rules. ")
  public String getSetHeroClass() {
    return setHeroClass;
  }

  public void setSetHeroClass(String setHeroClass) {
    this.setHeroClass = setHeroClass;
  }

  public DecksUpdateCommand setName(String setName) {
    this.setName = setName;
    return this;
  }

   /**
   * Sets the name of the deck in this command. If the name is null, the deck becomes invalid. 
   * @return setName
  **/
  @ApiModelProperty(value = "Sets the name of the deck in this command. If the name is null, the deck becomes invalid. ")
  public String getSetName() {
    return setName;
  }

  public void setSetName(String setName) {
    this.setName = setName;
  }

  public DecksUpdateCommand setInventoryIds(List<String> setInventoryIds) {
    this.setInventoryIds = setInventoryIds;
    return this;
  }

  public DecksUpdateCommand addSetInventoryIdsItem(String setInventoryIdsItem) {
    if (this.setInventoryIds == null) {
      this.setInventoryIds = new ArrayList<>();
    }
    this.setInventoryIds.add(setInventoryIdsItem);
    return this;
  }

   /**
   * Sets the entire deck&#39;s inventory IDs in this command. Duplicate inventory IDs will cause the update to be rejected. If the user does not own these inventory IDs, the deck becomes invalid. Under standard rules, duplicate card IDs also make the deck invalid. Finally, adding cards whose hero class isn&#39;t neutral or the same as the deck&#39;s hero class marks the deck as invalid. 
   * @return setInventoryIds
  **/
  @ApiModelProperty(value = "Sets the entire deck's inventory IDs in this command. Duplicate inventory IDs will cause the update to be rejected. If the user does not own these inventory IDs, the deck becomes invalid. Under standard rules, duplicate card IDs also make the deck invalid. Finally, adding cards whose hero class isn't neutral or the same as the deck's hero class marks the deck as invalid. ")
  public List<String> getSetInventoryIds() {
    return setInventoryIds;
  }

  public void setSetInventoryIds(List<String> setInventoryIds) {
    this.setInventoryIds = setInventoryIds;
  }

  public DecksUpdateCommand pushInventoryIds(DecksUpdateCommandPushInventoryIds pushInventoryIds) {
    this.pushInventoryIds = pushInventoryIds;
    return this;
  }

   /**
   * Get pushInventoryIds
   * @return pushInventoryIds
  **/
  @ApiModelProperty(value = "")
  public DecksUpdateCommandPushInventoryIds getPushInventoryIds() {
    return pushInventoryIds;
  }

  public void setPushInventoryIds(DecksUpdateCommandPushInventoryIds pushInventoryIds) {
    this.pushInventoryIds = pushInventoryIds;
  }

  public DecksUpdateCommand pushCardIds(DecksUpdateCommandPushCardIds pushCardIds) {
    this.pushCardIds = pushCardIds;
    return this;
  }

   /**
   * Get pushCardIds
   * @return pushCardIds
  **/
  @ApiModelProperty(value = "")
  public DecksUpdateCommandPushCardIds getPushCardIds() {
    return pushCardIds;
  }

  public void setPushCardIds(DecksUpdateCommandPushCardIds pushCardIds) {
    this.pushCardIds = pushCardIds;
  }

  public DecksUpdateCommand pullAllInventoryIds(List<String> pullAllInventoryIds) {
    this.pullAllInventoryIds = pullAllInventoryIds;
    return this;
  }

  public DecksUpdateCommand addPullAllInventoryIdsItem(String pullAllInventoryIdsItem) {
    if (this.pullAllInventoryIds == null) {
      this.pullAllInventoryIds = new ArrayList<>();
    }
    this.pullAllInventoryIds.add(pullAllInventoryIdsItem);
    return this;
  }

   /**
   * Removes all the specified inventory IDs from the user&#39;s deck. Does nothing if the deck does not contain any of the specified inventory IDs. This method will still succeed for inventory IDs that are found. 
   * @return pullAllInventoryIds
  **/
  @ApiModelProperty(value = "Removes all the specified inventory IDs from the user's deck. Does nothing if the deck does not contain any of the specified inventory IDs. This method will still succeed for inventory IDs that are found. ")
  public List<String> getPullAllInventoryIds() {
    return pullAllInventoryIds;
  }

  public void setPullAllInventoryIds(List<String> pullAllInventoryIds) {
    this.pullAllInventoryIds = pullAllInventoryIds;
  }

  public DecksUpdateCommand pullAllCardIds(List<String> pullAllCardIds) {
    this.pullAllCardIds = pullAllCardIds;
    return this;
  }

  public DecksUpdateCommand addPullAllCardIdsItem(String pullAllCardIdsItem) {
    if (this.pullAllCardIds == null) {
      this.pullAllCardIds = new ArrayList<>();
    }
    this.pullAllCardIds.add(pullAllCardIdsItem);
    return this;
  }

   /**
   * Removes all the specified card IDs from the user&#39;s deck. Does nothing if the deck does not contain any of the specified card IDs. This method will still succeed for deck IDs that are found. 
   * @return pullAllCardIds
  **/
  @ApiModelProperty(value = "Removes all the specified card IDs from the user's deck. Does nothing if the deck does not contain any of the specified card IDs. This method will still succeed for deck IDs that are found. ")
  public List<String> getPullAllCardIds() {
    return pullAllCardIds;
  }

  public void setPullAllCardIds(List<String> pullAllCardIds) {
    this.pullAllCardIds = pullAllCardIds;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    DecksUpdateCommand decksUpdateCommand = (DecksUpdateCommand) o;
    return Objects.equals(this.setHeroClass, decksUpdateCommand.setHeroClass) &&
        Objects.equals(this.setName, decksUpdateCommand.setName) &&
        Objects.equals(this.setInventoryIds, decksUpdateCommand.setInventoryIds) &&
        Objects.equals(this.pushInventoryIds, decksUpdateCommand.pushInventoryIds) &&
        Objects.equals(this.pushCardIds, decksUpdateCommand.pushCardIds) &&
        Objects.equals(this.pullAllInventoryIds, decksUpdateCommand.pullAllInventoryIds) &&
        Objects.equals(this.pullAllCardIds, decksUpdateCommand.pullAllCardIds);
  }

  @Override
  public int hashCode() {
    return Objects.hash(setHeroClass, setName, setInventoryIds, pushInventoryIds, pushCardIds, pullAllInventoryIds, pullAllCardIds);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class DecksUpdateCommand {\n");
    
    sb.append("    setHeroClass: ").append(toIndentedString(setHeroClass)).append("\n");
    sb.append("    setName: ").append(toIndentedString(setName)).append("\n");
    sb.append("    setInventoryIds: ").append(toIndentedString(setInventoryIds)).append("\n");
    sb.append("    pushInventoryIds: ").append(toIndentedString(pushInventoryIds)).append("\n");
    sb.append("    pushCardIds: ").append(toIndentedString(pushCardIds)).append("\n");
    sb.append("    pullAllInventoryIds: ").append(toIndentedString(pullAllInventoryIds)).append("\n");
    sb.append("    pullAllCardIds: ").append(toIndentedString(pullAllCardIds)).append("\n");
    sb.append("}");
    return sb.toString();
  }

  /**
   * Convert the given object to string with each line indented by 4 spaces
   * (except the first line).
   */
  private String toIndentedString(java.lang.Object o) {
    if (o == null) {
      return "null";
    }
    return o.toString().replace("\n", "\n    ");
  }

}

