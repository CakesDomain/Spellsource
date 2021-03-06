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
import com.hiddenswitch.spellsource.client.models.PlayerEntityAttributes;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * A tuple of attribute, attribute-value. 
 */
@ApiModel(description = "A tuple of attribute, attribute-value. ")
@JsonInclude(JsonInclude.Include.NON_DEFAULT)

public class AttributeValueTuple implements Serializable {
  private static final long serialVersionUID = 1L;

  @JsonProperty("attribute")
  private PlayerEntityAttributes attribute = null;

  @JsonProperty("stringValue")
  private String stringValue = null;

  public AttributeValueTuple attribute(PlayerEntityAttributes attribute) {
    this.attribute = attribute;
    return this;
  }

   /**
   * Get attribute
   * @return attribute
  **/
  @ApiModelProperty(value = "")
  public PlayerEntityAttributes getAttribute() {
    return attribute;
  }

  public void setAttribute(PlayerEntityAttributes attribute) {
    this.attribute = attribute;
  }

  public AttributeValueTuple stringValue(String stringValue) {
    this.stringValue = stringValue;
    return this;
  }

   /**
   * Get stringValue
   * @return stringValue
  **/
  @ApiModelProperty(value = "")
  public String getStringValue() {
    return stringValue;
  }

  public void setStringValue(String stringValue) {
    this.stringValue = stringValue;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    AttributeValueTuple attributeValueTuple = (AttributeValueTuple) o;
    return Objects.equals(this.attribute, attributeValueTuple.attribute) &&
        Objects.equals(this.stringValue, attributeValueTuple.stringValue);
  }

  @Override
  public int hashCode() {
    return Objects.hash(attribute, stringValue);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class AttributeValueTuple {\n");
    
    sb.append("    attribute: ").append(toIndentedString(attribute)).append("\n");
    sb.append("    stringValue: ").append(toIndentedString(stringValue)).append("\n");
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

