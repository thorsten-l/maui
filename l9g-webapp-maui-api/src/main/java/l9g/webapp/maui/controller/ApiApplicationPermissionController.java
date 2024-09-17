
/*
 * Copyright 2024 Thorsten Ludewig (t.ludewig@gmail.com).
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

import com.fasterxml.jackson.annotation.JsonView;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import java.util.Optional;
import l9g.webapp.maui.db.MauiApplicationPermissionsRepository;
import l9g.webapp.maui.db.MauiPersonsRepository;
import l9g.webapp.maui.db.model.MauiApplicationPermission;
import l9g.webapp.maui.json.View;
import l9g.webapp.maui.db.model.MauiPerson;
import l9g.webapp.maui.dto.DtoApplicationPermission;
import l9g.webapp.maui.dto.DtoErrorStatus;
import l9g.webapp.maui.mapper.MauiDtoMapper;
import l9g.webapp.maui.util.PasswordGeneratorService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Thorsten Ludewig (t.ludewig@gmail.com)
 */
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping(path = "/api/v1/application-permission",
                produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Application permission",
     description = "CURD frontend for application permissions")
public class ApiApplicationPermissionController
{

  @GetMapping()
  @JsonView(View.ApplicationPermissionApplication.class)
  public List<DtoApplicationPermission> findByUsername(
    JwtAuthenticationToken jwtAuthenticationToken)
  {
    log.debug("findByUsername() name={}", jwtAuthenticationToken.getName());

    MauiPerson person = JwtUtil.personFromAuthenticationToken(
      jwtAuthenticationToken);

    if (person != null && person.getUsername() != null)
    {
      person = updateOrInsertWithTTL(person);
    }

    log.debug("person={}", person);

    return person != null && person.getUsername() != null
      ? MauiDtoMapper.INSTANCE.
        mauiApplicationPermissionToDtoApplicationPermissionList(
          applicationPermissionsRepository.findByUsername(person.getUsername()))
      : List.of();
  }

  @PostMapping(path = "/{id}")
  @JsonView(View.ApplicationPermissionApplication.class)
  public DtoApplicationPermission updateById(
    JwtAuthenticationToken jwtAuthenticationToken,
    @PathVariable String id,
    @RequestBody DtoApplicationPermission applicationPermission)
  {
    log.debug("updateById() name={}", jwtAuthenticationToken.getName());
    log.debug("updateById() id={}, applicationPermission={}", id,
      applicationPermission);

    String username = JwtUtil
      .usernameFromAuthenticationToken(jwtAuthenticationToken);

    MauiApplicationPermission updateApplicationPermission
      = applicationPermissionsRepository.findById(id).orElseThrow(
        () -> new ApiException(
          "application permissions " + id + " not found."));

    DtoApplicationPermission dtoApplicationPermission;

    try
    {
      updateApplicationPermission.setPermissions(applicationPermission.
        getPermissions());
      updateApplicationPermission.setModifiedBy(username);

      MauiApplicationPermission mauiApplicationPermission
        = applicationPermissionsRepository.save(updateApplicationPermission);

      dtoApplicationPermission
        = MauiDtoMapper.INSTANCE
          .mauiApplicationPermissionToDtoApplicationPermission(
            mauiApplicationPermission);

      dtoApplicationPermission.setErrorStatus(DtoErrorStatus.success().title(
        "Application permissions update").message("Update succeed."));
    }
    catch (Throwable t)
    {
      dtoApplicationPermission = new DtoApplicationPermission();
      dtoApplicationPermission.setId(id);
      dtoApplicationPermission.setErrorStatus(
        DtoErrorStatus.failure()
          .errorCode(
            DtoErrorStatus.ERROR_CODE_APPLICATION_PERMISSIONS_UPDATE_FAILED)
          .title("Application Permissions Update")
          .message("Application Permissions update failed.")
          .exception(t.getMessage())
      );
    }

    log.debug("Response application: {}", dtoApplicationPermission);

    return dtoApplicationPermission;
  }

  @GetMapping("/{id}/persons")
  @JsonView(View.ApplicationPermissionPerson.class)
  public List<DtoApplicationPermission> findByUsernameAndApplicationId(
    JwtAuthenticationToken jwtAuthenticationToken,
    @PathVariable String id)
  {
    log.debug("findByUsernameAndApplicationId() name={} id={}",
      jwtAuthenticationToken.getName(), id);

    MauiPerson person = JwtUtil.personFromAuthenticationToken(
      jwtAuthenticationToken);

    log.debug("person={}", person);

    return person != null && person.getUsername() != null
      ? MauiDtoMapper.INSTANCE.mauiApplicationPermissionToDtoApplicationPermissionList(
          applicationPermissionsRepository.findByApplicationId(id))
      : List.of();
  }

  @GetMapping("/{id}/own")
  @JsonView(View.ApplicationPermissionPerson.class)
  public DtoApplicationPermission findOwnByUsernameAndApplicationId(
    JwtAuthenticationToken jwtAuthenticationToken,
    @PathVariable String id)
  {
    log.debug("findOwnByUsernameAndApplicationId() name={} id={}",
      jwtAuthenticationToken.getName(), id);

    MauiPerson person = JwtUtil.personFromAuthenticationToken(
      jwtAuthenticationToken);

    log.debug("person={}", person);

    DtoApplicationPermission permission = null;

    if (person != null && person.getUsername() != null)
    {
      Optional<MauiApplicationPermission> optional
        = applicationPermissionsRepository
          .findByUsernameAndApplicationId(person.getUsername(), id);

      permission = MauiDtoMapper.INSTANCE
        .mauiApplicationPermissionToDtoApplicationPermission(
          optional.orElseThrow(
            () ->
          {
            throw new ApiException("own permissions not found");
          }
          ));
      log.debug("own application permissions = {}", permission);
    }

    return permission;
  }

  private MauiPerson updateOrInsertWithTTL(MauiPerson person)
  {
    MauiPerson entry;
    Optional<MauiPerson> optional
      = personsRepository.findByUsername(person.getUsername());

    if (optional.isPresent())
    {
      entry = optional.get();
      long passedTime
        = (System.currentTimeMillis()
        - entry.getModifyTimestamp().getTime()) / 1000;
      log.debug("update person if ttl is expired TTL={}s, passed time={}",
        personUpdateTTL, passedTime);

      if (passedTime >= personUpdateTTL)
      {
        log.debug("update person entry has expired");
        entry.setFirstname(person.getFirstname());
        entry.setLastname(person.getLastname());
        entry.setEmail(person.getEmail());
        entry.setModifiedBy(person.getUsername());
        entry = personsRepository.save(entry);
      }
    }
    else
    {
      log.debug("insert new person {}", person);
      entry = personsRepository.save(person);
    }

    return entry;
  }

  private final MauiApplicationPermissionsRepository applicationPermissionsRepository;

  private final MauiPersonsRepository personsRepository;

  private final PasswordGeneratorService passwordGeneratorService;

  @Value("${maui.person-update-ttl}")
  private long personUpdateTTL;
}
