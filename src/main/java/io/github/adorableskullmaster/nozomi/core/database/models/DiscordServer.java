package io.github.adorableskullmaster.nozomi.core.database.models;

import org.bson.types.ObjectId;

public class DiscordServer {
  private ObjectId id;
  private Long serverId;
  private String name;
  private Integer[] pwIds;
  private Integer[] allowedRoles;
  private ModuleSettings moduleSettings;

  public DiscordServer() {
  }

  public DiscordServer(ObjectId id, Long serverId, String name, Integer[] pwIds, Integer[] allowedRoles, ModuleSettings moduleSettings) {
    this.id = id;
    this.serverId = serverId;
    this.name = name;
    this.pwIds = pwIds;
    this.allowedRoles = allowedRoles;
    this.moduleSettings = moduleSettings;
  }

  public ObjectId getId() {
    return id;
  }

  public void setId(ObjectId id) {
    this.id = id;
  }

  public Long getServerId() {
    return serverId;
  }

  public void setServerId(Long serverId) {
    this.serverId = serverId;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Integer[] getPwIds() {
    return pwIds;
  }

  public void setPwIds(Integer[] pwIds) {
    this.pwIds = pwIds;
  }

  public Integer[] getAllowedRoles() {
    return allowedRoles;
  }

  public void setAllowedRoles(Integer[] allowedRoles) {
    this.allowedRoles = allowedRoles;
  }

  public ModuleSettings getModuleSettings() {
    return moduleSettings;
  }

  public void setModuleSettings(ModuleSettings moduleSettings) {
    this.moduleSettings = moduleSettings;
  }
}
