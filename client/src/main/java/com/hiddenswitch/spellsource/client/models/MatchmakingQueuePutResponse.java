/*
 * Hidden Switch Spellsource API
 * The Spellsource API for matchmaking, user accounts, collections management and more.  To get started, create a user account and make sure to include the entirety of the returned login token as the X-Auth-Token header. You can reuse this token, or login for a new one.  ClientToServerMessage and ServerToClientMessage are used for the realtime game state and actions two-way websocket interface for actually playing a game. Envelope is used for the realtime API services.  For the routes that correspond to the paths in this file, visit the Gateway.java file in the Spellsource-Server GitHub repository located in this definition file. 
 *
 * OpenAPI spec version: 3.0.0
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
import com.hiddenswitch.spellsource.client.models.MatchmakingQueuePutRequest;
import com.hiddenswitch.spellsource.client.models.MatchmakingQueuePutResponseUnityConnection;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * MatchmakingQueuePutResponse
 */
@JsonInclude(JsonInclude.Include.NON_DEFAULT)

public class MatchmakingQueuePutResponse implements Serializable {
  private static final long serialVersionUID = 1L;

  @JsonProperty("unityConnection")
  private MatchmakingQueuePutResponseUnityConnection unityConnection = null;

  @JsonProperty("retry")
  private MatchmakingQueuePutRequest retry = null;

  public MatchmakingQueuePutResponse unityConnection(MatchmakingQueuePutResponseUnityConnection unityConnection) {
    this.unityConnection = unityConnection;
    return this;
  }

   /**
   * Get unityConnection
   * @return unityConnection
  **/
  @ApiModelProperty(value = "")
  public MatchmakingQueuePutResponseUnityConnection getUnityConnection() {
    return unityConnection;
  }

  public void setUnityConnection(MatchmakingQueuePutResponseUnityConnection unityConnection) {
    this.unityConnection = unityConnection;
  }

  public MatchmakingQueuePutResponse retry(MatchmakingQueuePutRequest retry) {
    this.retry = retry;
    return this;
  }

   /**
   * Get retry
   * @return retry
  **/
  @ApiModelProperty(value = "")
  public MatchmakingQueuePutRequest getRetry() {
    return retry;
  }

  public void setRetry(MatchmakingQueuePutRequest retry) {
    this.retry = retry;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    MatchmakingQueuePutResponse matchmakingQueuePutResponse = (MatchmakingQueuePutResponse) o;
    return Objects.equals(this.unityConnection, matchmakingQueuePutResponse.unityConnection) &&
        Objects.equals(this.retry, matchmakingQueuePutResponse.retry);
  }

  @Override
  public int hashCode() {
    return Objects.hash(unityConnection, retry);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class MatchmakingQueuePutResponse {\n");
    
    sb.append("    unityConnection: ").append(toIndentedString(unityConnection)).append("\n");
    sb.append("    retry: ").append(toIndentedString(retry)).append("\n");
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

