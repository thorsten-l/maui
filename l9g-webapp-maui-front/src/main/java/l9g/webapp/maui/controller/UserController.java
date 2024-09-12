/*
 * Copyright 2022 Thorsten Ludewig (t.ludewig@gmail.com).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
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

import io.micrometer.core.instrument.MeterRegistry;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import l9g.webapp.maui.client.ApiClientService;
import l9g.webapp.maui.dto.DtoApplication;
import l9g.webapp.maui.dto.DtoApplicationPermission;
import l9g.webapp.maui.dto.DtoErrorStatus;
import l9g.webapp.maui.dto.DtoPerson;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.info.BuildProperties;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.security.oauth2.core.oidc.OidcUserInfo;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author Thorsten Ludewig (t.ludewig@gmail.com)
 */
@RestController
@RequiredArgsConstructor
@Slf4j
public class UserController
{
  private final ApiClientService mauiApiClientService;

  @GetMapping("/principal")
  public OAuth2User principalGET(
    @AuthenticationPrincipal OAuth2User principal)
  {
    log.debug("principal.class={}", principal.getClass().getCanonicalName());
    log.debug("principal.name={}", principal.getName());
    return principal;
  }

  @GetMapping("/principal-authtoken")
  public OAuth2AuthenticationToken principalAuthTokenGET(
    OAuth2AuthenticationToken authToken)
  {
    log.debug("authToken={}", authToken);
    return authToken;
  }

  @GetMapping("/authentication")
  public Authentication accessTokenGET(Authentication authentication)
  {
    log.debug("authentication.class={}", authentication.getClass().
      getCanonicalName());
    return authentication;
  }

  @GetMapping("/principal-attributes")
  public Map<String, Object> principalAttributesGET(
    @AuthenticationPrincipal DefaultOidcUser principal)
  {
    return principal.getAttributes();
  }

  @GetMapping("/principal-userinfo")
  public OidcUserInfo principalUserinfoGET(
    @AuthenticationPrincipal DefaultOidcUser principal)
  {
    return principal.getUserInfo();
  }

  @GetMapping("/principal-idtoken")
  public OidcIdToken principalIdTokenGET(
    @AuthenticationPrincipal DefaultOidcUser principal)
  {
    log.debug("idToken={}", principal.getIdToken().getTokenValue());
    return principal.getIdToken();
  }

  @GetMapping("/principal-claims")
  public Map<String, Object> principalClaimsGET(
    @AuthenticationPrincipal DefaultOidcUser principal)
  {
    return principal.getClaims();
  }

  @GetMapping("/build-properties")
  public Map<String, String> buildinfo()
  {
    ArrayList<String> keys = new ArrayList<>();
    buildProperties.forEach(p -> keys.add(p.getKey()));
    Collections.sort(keys);
    LinkedHashMap<String, String> properties = new LinkedHashMap<>();
    for (String key : keys)
    {
      properties.put(key, buildProperties.get(key));
    }

    return properties;
  }

  @GetMapping("/maui-api/build-properties")
  public Map<String, String> mauiApiBuildInfo()
  {
    log.debug("mauiApiBuildInfo");
    return mauiApiClientService.buildInfo();
  }

  @GetMapping("/maui-api/persons")
  public List<DtoPerson> findAllPersons()
  {
    log.debug("findAllPersons");
    return mauiApiClientService.findAllPersons();
  }

  @GetMapping("/maui-api/admin/persons")
  public List<DtoPerson> adminFindAllPersons()
  {
    log.debug("adminFindAllPersons");
    return mauiApiClientService.adminFindAllPersons();
  }

  @GetMapping("/maui-api/application-permissions")
  public List<DtoApplicationPermission> findApplicationPermissions()
  {
    log.debug("findApplicationPermissions");
    return mauiApiClientService.findApplicationPermissions();
  }

  @GetMapping("/maui-api/person-permissions/{id}")
  public List<DtoApplicationPermission> findPersonPermissions(@PathVariable String id)
  {
    log.debug("findApplicationPermissions");
    return mauiApiClientService.findPersonPermissions(id);
  }

  @GetMapping("/maui-api/applications")
  public List<DtoApplication> findApplications()
  {
    log.debug("findApplications");
    return mauiApiClientService.findApplications();
  }

  @GetMapping("/maui-api/application/{id}")
  public DtoApplication findApplicationById(@PathVariable String id)
  {
    log.debug("findApplicationById({})", id);
    return mauiApiClientService.findApplicationById(id);
  }

  @GetMapping("/maui-api/delete-application/{id}")
  public DtoErrorStatus deleteApplicationById(@PathVariable String id)
  {
    log.debug("deleteApplicationById({})", id);
    return mauiApiClientService.deleteApplicationById(id);
  }

  @GetMapping("/maui-api/create-application")
  public DtoApplication createApplication()
  {
    log.debug("createApplication");
    DtoApplication newApplication = new DtoApplication( "baseTopic" + System.currentTimeMillis(), 
      "app name", "app decription", new Date());
    newApplication = mauiApiClientService.createApplication(newApplication);
    log.debug("response = {}", newApplication);
    return newApplication;
  }

  @GetMapping("/api/v1/metrics")
  public Map<String, Double> metrics()
  {
    log.debug("metrics");
    Map<String, Double> metrics = new LinkedHashMap<>();
    metrics.put("jvm.memory.used", meterRegistry.get("jvm.memory.used").gauge().
      value());
    metrics.put("process.start.time", meterRegistry.get("process.start.time").
      gauge().value());
    metrics.put("process.uptime", meterRegistry.get("process.uptime").gauge().
      value());
    return metrics;
  }

  private final MeterRegistry meterRegistry;

  private final BuildProperties buildProperties;
}
