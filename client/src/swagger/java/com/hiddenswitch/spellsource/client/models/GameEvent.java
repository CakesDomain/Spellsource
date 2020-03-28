/*
 * Hidden Switch Spellsource API
 * The Spellsource API for matchmaking, user accounts, collections management and more.  To get started, create a user account and make sure to include the entirety of the returned login token as the X-Auth-Token header. You can reuse this token, or login for a new one.  ClientToServerMessage and ServerToClientMessage are used for the realtime game state and actions two-way websocket interface for actually playing a game. Envelope is used for the realtime API services.  For the routes that correspond to the paths in this file, visit the Gateway.java file in the Spellsource-Server GitHub repository located in this definition file. 
 *
 * OpenAPI spec version: 4.0.1
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
import com.hiddenswitch.spellsource.client.models.CardEvent;
import com.hiddenswitch.spellsource.client.models.Entity;
import com.hiddenswitch.spellsource.client.models.GameEventDamage;
import com.hiddenswitch.spellsource.client.models.GameEventJoust;
import com.hiddenswitch.spellsource.client.models.GameEventPerformedGameAction;
import com.hiddenswitch.spellsource.client.models.GameEventTriggerFired;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * GameEvent
 */
@JsonInclude(JsonInclude.Include.NON_DEFAULT)

public class GameEvent implements Serializable {
  private static final long serialVersionUID = 1L;

  @JsonProperty("id")
  private Integer id = null;

  @JsonProperty("isSourcePlayerLocal")
  private Boolean isSourcePlayerLocal = null;

  @JsonProperty("isTargetPlayerLocal")
  private Boolean isTargetPlayerLocal = null;

  @JsonProperty("entityTouched")
  private Integer entityTouched = null;

  @JsonProperty("entityUntouched")
  private Integer entityUntouched = null;

  @JsonProperty("source")
  private Entity source = null;

  @JsonProperty("target")
  private Entity target = null;

  @JsonProperty("value")
  private Integer value = null;

  @JsonProperty("description")
  private String description = null;

  @JsonProperty("isPowerHistory")
  private Boolean isPowerHistory = null;

  /**
   * The game event type corresponding to this game event. 
   */
  public enum EventTypeEnum {
    ALL("ALL"),
    
    AFTER_PHYSICAL_ATTACK("AFTER_PHYSICAL_ATTACK"),
    
    AFTER_PLAY_CARD("AFTER_PLAY_CARD"),
    
    AFTER_SPELL_CASTED("AFTER_SPELL_CASTED"),
    
    AFTER_SUMMON("AFTER_SUMMON"),
    
    ATTRIBUTE_APPLIED("ATTRIBUTE_APPLIED"),
    
    ARMOR_GAINED("ARMOR_GAINED"),
    
    BEFORE_PHYSICAL_ATTACK("BEFORE_PHYSICAL_ATTACK"),
    
    BEFORE_SUMMON("BEFORE_SUMMON"),
    
    BOARD_CHANGED("BOARD_CHANGED"),
    
    CARD_ADDED_TO_DECK("CARD_ADDED_TO_DECK"),
    
    CARD_SHUFFLED("CARD_SHUFFLED"),
    
    DAMAGE("DAMAGE"),
    
    DECAY("DECAY"),
    
    DID_END_SEQUENCE("DID_END_SEQUENCE"),
    
    DISCARD("DISCARD"),
    
    DISCOVER("DISCOVER"),
    
    DRAIN("DRAIN"),
    
    DRAW_CARD("DRAW_CARD"),
    
    ENRAGE_CHANGED("ENRAGE_CHANGED"),
    
    ENTITY_TOUCHED("ENTITY_TOUCHED"),
    
    ENTITY_UNTOUCHED("ENTITY_UNTOUCHED"),
    
    EXCESS_HEAL("EXCESS_HEAL"),
    
    FATIGUE("FATIGUE"),
    
    GAME_START("GAME_START"),
    
    HEAL("HEAL"),
    
    HERO_POWER_USED("HERO_POWER_USED"),
    
    INVOKED("INVOKED"),
    
    JOUST("JOUST"),
    
    KILL("KILL"),
    
    LOSE_DIVINE_SHIELD("LOSE_DIVINE_SHIELD"),
    
    LOSE_DEFLECT("LOSE_DEFLECT"),
    
    LOSE_STEALTH("LOSE_STEALTH"),
    
    MAX_HP_INCREASED("MAX_HP_INCREASED"),
    
    MAX_MANA("MAX_MANA"),
    
    MANA_MODIFIED("MANA_MODIFIED"),
    
    OVERLOAD("OVERLOAD"),
    
    PERFORMED_GAME_ACTION("PERFORMED_GAME_ACTION"),
    
    PHYSICAL_ATTACK("PHYSICAL_ATTACK"),
    
    PLAY_CARD("PLAY_CARD"),
    
    PRE_DAMAGE("PRE_DAMAGE"),
    
    PRE_GAME_START("PRE_GAME_START"),
    
    QUEST_PLAYED("QUEST_PLAYED"),
    
    QUEST_SUCCESSFUL("QUEST_SUCCESSFUL"),
    
    RETURNED_TO_HAND("RETURNED_TO_HAND"),
    
    ROASTED("ROASTED"),
    
    REVEAL_CARD("REVEAL_CARD"),
    
    SECRET_PLAYED("SECRET_PLAYED"),
    
    SECRET_REVEALED("SECRET_REVEALED"),
    
    SPELL_CASTED("SPELL_CASTED"),
    
    SUMMON("SUMMON"),
    
    TARGET_ACQUISITION("TARGET_ACQUISITION"),
    
    TRIGGER_FIRED("TRIGGER_FIRED"),
    
    TURN_END("TURN_END"),
    
    TURN_START("TURN_START"),
    
    SILENCE("SILENCE"),
    
    WEAPON_DESTROYED("WEAPON_DESTROYED"),
    
    WEAPON_EQUIPPED("WEAPON_EQUIPPED"),
    
    WILL_END_SEQUENCE("WILL_END_SEQUENCE");

    private String value;

    EventTypeEnum(String value) {
      this.value = value;
    }

    @JsonValue
    public String getValue() {
      return value;
    }

    @Override
    public String toString() {
      return String.valueOf(value);
    }

    @JsonCreator
    public static EventTypeEnum fromValue(String text) {
      for (EventTypeEnum b : EventTypeEnum.values()) {
        if (String.valueOf(b.value).equals(text)) {
          return b;
        }
      }
      return null;
    }
  }

  @JsonProperty("eventType")
  private EventTypeEnum eventType = null;

  @JsonProperty("cardEvent")
  private CardEvent cardEvent = null;

  @JsonProperty("performedGameAction")
  private GameEventPerformedGameAction performedGameAction = null;

  @JsonProperty("joust")
  private GameEventJoust joust = null;

  @JsonProperty("damage")
  private GameEventDamage damage = null;

  @JsonProperty("triggerFired")
  private GameEventTriggerFired triggerFired = null;

  public GameEvent id(Integer id) {
    this.id = id;
    return this;
  }

   /**
   * An integer ID corresponding to the order of this event from the client&#39;s point of view. 
   * @return id
  **/
  @ApiModelProperty(value = "An integer ID corresponding to the order of this event from the client's point of view. ")
  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public GameEvent isSourcePlayerLocal(Boolean isSourcePlayerLocal) {
    this.isSourcePlayerLocal = isSourcePlayerLocal;
    return this;
  }

   /**
   * Stores the source player according to the game event data. Typically this is the player who is casting the card or otherwise the source of an event. 
   * @return isSourcePlayerLocal
  **/
  @ApiModelProperty(value = "Stores the source player according to the game event data. Typically this is the player who is casting the card or otherwise the source of an event. ")
  public Boolean isIsSourcePlayerLocal() {
    return isSourcePlayerLocal;
  }

  public void setIsSourcePlayerLocal(Boolean isSourcePlayerLocal) {
    this.isSourcePlayerLocal = isSourcePlayerLocal;
  }

  public GameEvent isTargetPlayerLocal(Boolean isTargetPlayerLocal) {
    this.isTargetPlayerLocal = isTargetPlayerLocal;
    return this;
  }

   /**
   * Stores the target player according to the game event data. 
   * @return isTargetPlayerLocal
  **/
  @ApiModelProperty(value = "Stores the target player according to the game event data. ")
  public Boolean isIsTargetPlayerLocal() {
    return isTargetPlayerLocal;
  }

  public void setIsTargetPlayerLocal(Boolean isTargetPlayerLocal) {
    this.isTargetPlayerLocal = isTargetPlayerLocal;
  }

  public GameEvent entityTouched(Integer entityTouched) {
    this.entityTouched = entityTouched;
    return this;
  }

   /**
   * The ID of the entity that has starting being touched by the opponent.
   * @return entityTouched
  **/
  @ApiModelProperty(value = "The ID of the entity that has starting being touched by the opponent.")
  public Integer getEntityTouched() {
    return entityTouched;
  }

  public void setEntityTouched(Integer entityTouched) {
    this.entityTouched = entityTouched;
  }

  public GameEvent entityUntouched(Integer entityUntouched) {
    this.entityUntouched = entityUntouched;
    return this;
  }

   /**
   * The ID of the entity that is no longer being touched by the opponent.
   * @return entityUntouched
  **/
  @ApiModelProperty(value = "The ID of the entity that is no longer being touched by the opponent.")
  public Integer getEntityUntouched() {
    return entityUntouched;
  }

  public void setEntityUntouched(Integer entityUntouched) {
    this.entityUntouched = entityUntouched;
  }

  public GameEvent source(Entity source) {
    this.source = source;
    return this;
  }

   /**
   * Get source
   * @return source
  **/
  @ApiModelProperty(value = "")
  public Entity getSource() {
    return source;
  }

  public void setSource(Entity source) {
    this.source = source;
  }

  public GameEvent target(Entity target) {
    this.target = target;
    return this;
  }

   /**
   * Get target
   * @return target
  **/
  @ApiModelProperty(value = "")
  public Entity getTarget() {
    return target;
  }

  public void setTarget(Entity target) {
    this.target = target;
  }

  public GameEvent value(Integer value) {
    this.value = value;
    return this;
  }

   /**
   * When not null, indicates this game event comes with a value. This is typically the damage dealt, the amount of healing, etc. 
   * @return value
  **/
  @ApiModelProperty(value = "When not null, indicates this game event comes with a value. This is typically the damage dealt, the amount of healing, etc. ")
  public Integer getValue() {
    return value;
  }

  public void setValue(Integer value) {
    this.value = value;
  }

  public GameEvent description(String description) {
    this.description = description;
    return this;
  }

   /**
   * A plain text description of this event that should be shown to the user. 
   * @return description
  **/
  @ApiModelProperty(value = "A plain text description of this event that should be shown to the user. ")
  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public GameEvent isPowerHistory(Boolean isPowerHistory) {
    this.isPowerHistory = isPowerHistory;
    return this;
  }

   /**
   * Should this event be rendered in the power history? 
   * @return isPowerHistory
  **/
  @ApiModelProperty(value = "Should this event be rendered in the power history? ")
  public Boolean isIsPowerHistory() {
    return isPowerHistory;
  }

  public void setIsPowerHistory(Boolean isPowerHistory) {
    this.isPowerHistory = isPowerHistory;
  }

  public GameEvent eventType(EventTypeEnum eventType) {
    this.eventType = eventType;
    return this;
  }

   /**
   * The game event type corresponding to this game event. 
   * @return eventType
  **/
  @ApiModelProperty(value = "The game event type corresponding to this game event. ")
  public EventTypeEnum getEventType() {
    return eventType;
  }

  public void setEventType(EventTypeEnum eventType) {
    this.eventType = eventType;
  }

  public GameEvent cardEvent(CardEvent cardEvent) {
    this.cardEvent = cardEvent;
    return this;
  }

   /**
   * Get cardEvent
   * @return cardEvent
  **/
  @ApiModelProperty(value = "")
  public CardEvent getCardEvent() {
    return cardEvent;
  }

  public void setCardEvent(CardEvent cardEvent) {
    this.cardEvent = cardEvent;
  }

  public GameEvent performedGameAction(GameEventPerformedGameAction performedGameAction) {
    this.performedGameAction = performedGameAction;
    return this;
  }

   /**
   * Get performedGameAction
   * @return performedGameAction
  **/
  @ApiModelProperty(value = "")
  public GameEventPerformedGameAction getPerformedGameAction() {
    return performedGameAction;
  }

  public void setPerformedGameAction(GameEventPerformedGameAction performedGameAction) {
    this.performedGameAction = performedGameAction;
  }

  public GameEvent joust(GameEventJoust joust) {
    this.joust = joust;
    return this;
  }

   /**
   * Get joust
   * @return joust
  **/
  @ApiModelProperty(value = "")
  public GameEventJoust getJoust() {
    return joust;
  }

  public void setJoust(GameEventJoust joust) {
    this.joust = joust;
  }

  public GameEvent damage(GameEventDamage damage) {
    this.damage = damage;
    return this;
  }

   /**
   * Get damage
   * @return damage
  **/
  @ApiModelProperty(value = "")
  public GameEventDamage getDamage() {
    return damage;
  }

  public void setDamage(GameEventDamage damage) {
    this.damage = damage;
  }

  public GameEvent triggerFired(GameEventTriggerFired triggerFired) {
    this.triggerFired = triggerFired;
    return this;
  }

   /**
   * Get triggerFired
   * @return triggerFired
  **/
  @ApiModelProperty(value = "")
  public GameEventTriggerFired getTriggerFired() {
    return triggerFired;
  }

  public void setTriggerFired(GameEventTriggerFired triggerFired) {
    this.triggerFired = triggerFired;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    GameEvent gameEvent = (GameEvent) o;
    return Objects.equals(this.id, gameEvent.id) &&
        Objects.equals(this.isSourcePlayerLocal, gameEvent.isSourcePlayerLocal) &&
        Objects.equals(this.isTargetPlayerLocal, gameEvent.isTargetPlayerLocal) &&
        Objects.equals(this.entityTouched, gameEvent.entityTouched) &&
        Objects.equals(this.entityUntouched, gameEvent.entityUntouched) &&
        Objects.equals(this.source, gameEvent.source) &&
        Objects.equals(this.target, gameEvent.target) &&
        Objects.equals(this.value, gameEvent.value) &&
        Objects.equals(this.description, gameEvent.description) &&
        Objects.equals(this.isPowerHistory, gameEvent.isPowerHistory) &&
        Objects.equals(this.eventType, gameEvent.eventType) &&
        Objects.equals(this.cardEvent, gameEvent.cardEvent) &&
        Objects.equals(this.performedGameAction, gameEvent.performedGameAction) &&
        Objects.equals(this.joust, gameEvent.joust) &&
        Objects.equals(this.damage, gameEvent.damage) &&
        Objects.equals(this.triggerFired, gameEvent.triggerFired);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, isSourcePlayerLocal, isTargetPlayerLocal, entityTouched, entityUntouched, source, target, value, description, isPowerHistory, eventType, cardEvent, performedGameAction, joust, damage, triggerFired);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class GameEvent {\n");
    
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    isSourcePlayerLocal: ").append(toIndentedString(isSourcePlayerLocal)).append("\n");
    sb.append("    isTargetPlayerLocal: ").append(toIndentedString(isTargetPlayerLocal)).append("\n");
    sb.append("    entityTouched: ").append(toIndentedString(entityTouched)).append("\n");
    sb.append("    entityUntouched: ").append(toIndentedString(entityUntouched)).append("\n");
    sb.append("    source: ").append(toIndentedString(source)).append("\n");
    sb.append("    target: ").append(toIndentedString(target)).append("\n");
    sb.append("    value: ").append(toIndentedString(value)).append("\n");
    sb.append("    description: ").append(toIndentedString(description)).append("\n");
    sb.append("    isPowerHistory: ").append(toIndentedString(isPowerHistory)).append("\n");
    sb.append("    eventType: ").append(toIndentedString(eventType)).append("\n");
    sb.append("    cardEvent: ").append(toIndentedString(cardEvent)).append("\n");
    sb.append("    performedGameAction: ").append(toIndentedString(performedGameAction)).append("\n");
    sb.append("    joust: ").append(toIndentedString(joust)).append("\n");
    sb.append("    damage: ").append(toIndentedString(damage)).append("\n");
    sb.append("    triggerFired: ").append(toIndentedString(triggerFired)).append("\n");
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
