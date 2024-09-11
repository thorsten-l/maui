/*
 * Copyright 2024 Thorsten Ludewig <t.ludewig@gmail.com>.
 *
 * Licensed under the Apache License, Version 2.0 (the "License")
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package l9g.webapp.maui.controller;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import l9g.webapp.maui.client.ApiClientService;
import l9g.webapp.maui.dto.DtoApplication;
import l9g.webapp.maui.dto.DtoApplicationPermission;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;

/**
 *
 * @author Thorsten Ludewig <t.ludewig@gmail.com>
 */
@Component
@Slf4j
public class ControllerUtil
{

  public ControllerUtil(ApiClientService mauiApiClientService)
  {
    this.mauiApiClientService = mauiApiClientService;
    this.applicationPermissionCache = Caffeine.newBuilder()
      .expireAfterWrite(1, TimeUnit.HOURS)
      .build();
    this.ownApplicationPermissionCache = Caffeine.newBuilder()
      .expireAfterWrite(1, TimeUnit.HOURS)
      .build();
    this.uuid = UUID.randomUUID().toString();
  }

  public List<DtoApplicationPermission> cachedApplicationPermissions(
    String username, boolean forceReload)
  {
    List<DtoApplicationPermission> cachedPermissions
      = applicationPermissionCache.getIfPresent(username);

    if (forceReload || cachedPermissions == null)
    {
      log.
        debug(
          "Cache miss or force reload for username={}, calling findApplicationPermissions",
          username);
      cachedPermissions = mauiApiClientService.findApplicationPermissions();
      applicationPermissionCache.put(username, cachedPermissions);
    }
    else
    {
      log.debug("Cache hit for username={}", username);
    }
    return cachedPermissions;
  }

  public String ownCacheKey(String username, String applicationId)
  {
    return username + ";" + applicationId;
  }

  public DtoApplicationPermission cachedOwnApplicationPermissions(
    String username, String applicationId, boolean forceReload)
  {
    String cacheKey = ownCacheKey(username, applicationId);
    DtoApplicationPermission cachedPermissions
      = ownApplicationPermissionCache.getIfPresent(cacheKey);

    if (forceReload || cachedPermissions == null)
    {
      log.
        debug(
          "Cache miss or force reload for applicationId={}, calling findOwnPersonPermissions",
          applicationId);
      cachedPermissions = mauiApiClientService.findOwnPersonPermissions(
        applicationId);
      ownApplicationPermissionCache.put(cacheKey, cachedPermissions);
    }
    else
    {
      log.debug("Cache hit for own application permissions applicationId={}",
        applicationId);
    }
    return cachedPermissions;
  }

  public void invalidateCachedOwnApplicationPermissions(
    String username, String applicationId)
  {
    ownApplicationPermissionCache
      .invalidate(ownCacheKey(username, applicationId));
  }

  public void defaultModelAttributes(DefaultOidcUser principal, Model model,
    boolean forceReloadCache, String applicationId)
  {
    log.debug("defaultModelAttributes username={} {} {}", principal.
      getPreferredUsername(), applicationId, uuid);

    Optional<ErrorStatus> optionalErrorStatus = Optional.ofNullable(
      (ErrorStatus) model.getAttribute("errorStatus"));

    model.addAttribute("errorStatus", optionalErrorStatus.orElse(
      new ErrorStatus(0, null, null)));

    if (applicationId != null)
    {
      DtoApplicationPermission ownApplicationPermission
        = cachedOwnApplicationPermissions(
          principal.getPreferredUsername(), applicationId, forceReloadCache);
      log.debug("own application permission = {}", ownApplicationPermission);
      model.addAttribute("mauiOwnApplicationPermission",
        ownApplicationPermission.getPermissions());
      model.addAttribute("mauiOwnerOrManager",
        ownApplicationPermission.getPermissions()
        == DtoApplicationPermission.APPLICATION_PERMISSION_OWNER
        || ownApplicationPermission.getPermissions()
        == DtoApplicationPermission.APPLICATION_PERMISSION_MANAGER);
    }
    else
    {
      model.addAttribute("mauiOwnApplicationPermission",
        DtoApplicationPermission.APPLICATION_PERMISSION_NONE);
      model.addAttribute("mauiOwnerOrManager", false);
    }

    model.addAttribute("fullname", principal.getFullName());
    model.addAttribute("mauiApplication", new DtoApplication());
    model.addAttribute("mauiApplicationPermissions",
      cachedApplicationPermissions(principal.getPreferredUsername(),
        forceReloadCache));
    model.addAttribute("mauiDebugOidc", mauiApiClientService.isMauiDebugOidc());
  }

  public void defaultModelAttributes(DefaultOidcUser principal, Model model,
    boolean forceReloadCache)
  {
    defaultModelAttributes(principal, model, forceReloadCache, null);
  }

  public void defaultModelAttributes(DefaultOidcUser principal, Model model)
  {
    defaultModelAttributes(principal, model, false, null);
  }

  private final ApiClientService mauiApiClientService;

  private final Cache<String, List<DtoApplicationPermission>> applicationPermissionCache;

  private final Cache<String, DtoApplicationPermission> ownApplicationPermissionCache;

  private final String uuid;
}
