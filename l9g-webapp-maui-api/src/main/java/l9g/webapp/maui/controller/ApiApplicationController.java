
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
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import l9g.webapp.maui.db.MauiApplicationPermissionsRepository;
import l9g.webapp.maui.db.MauiApplicationsRepository;
import l9g.webapp.maui.db.MauiPersonsRepository;
import l9g.webapp.maui.json.View;
import l9g.webapp.maui.db.model.MauiApplication;
import l9g.webapp.maui.db.model.MauiApplicationPermission;
import static l9g.webapp.maui.db.model.MauiApplicationPermission.APPLICATION_PERMISSION_OWNER;
import l9g.webapp.maui.db.model.MauiPerson;
import l9g.webapp.maui.dto.Application;
import l9g.webapp.maui.mapper.MauiDtoMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Thorsten Ludewig (t.ludewig@gmail.com)
 */
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping(path = "/api/v1/application",
                produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Application",
     description = "CURD frontend appliactions")
public class ApiApplicationController
{
  @GetMapping()
  @JsonView(View.Application.class)
  public List<Application> findAll(
    JwtAuthenticationToken jwtAuthenticationToken)
  {
    log.debug("findAll()");

    String username = JwtUtil
      .usernameFromAuthenticationToken(jwtAuthenticationToken);

    return username != null
      ? MauiDtoMapper.INSTANCE.mauiApplicationToApplicationList(
        applicationsRepository.findByUsername(username))
      : List.of();
  }

  @GetMapping("/{id}")
  @JsonView(View.Application.class)
  public Application findById(
    JwtAuthenticationToken jwtAuthenticationToken,
    @Parameter(description = "The application unique id", required = true)
    @PathVariable String id)
  {
    log.debug("findByUsernameAndId()");

    String username = JwtUtil
      .usernameFromAuthenticationToken(jwtAuthenticationToken);

    return MauiDtoMapper.INSTANCE.mauiApplicationToApplication(
      applicationsRepository.
        findByUsernameAndId(username, id).orElseThrow(() -> new ApiException(
        "application " + id + " not found.")));
  }

  @PutMapping()
  @JsonView(View.Application.class)
  public Application createApplication(
    JwtAuthenticationToken jwtAuthenticationToken,
    @RequestBody Application application)
  {
    log.debug("createApplication()");

    String username = JwtUtil
      .usernameFromAuthenticationToken(jwtAuthenticationToken);

    log.debug("Username: {}", username);
    log.debug("Received application: {}", application);
    MauiApplication mauiApplication = new MauiApplication(username, application);

    MauiPerson mauiPerson = personsRepository.findByUsername(username)
      .orElseThrow(() -> new ApiException("Person entry not in database."));

    mauiApplication = applicationsRepository.save(mauiApplication);
    applicationPermissionsRepository.save(new MauiApplicationPermission(
      username, mauiPerson, mauiApplication,
      APPLICATION_PERMISSION_OWNER));

    log.debug("Response application: {}", mauiApplication);
    return MauiDtoMapper.INSTANCE.mauiApplicationToApplication(mauiApplication);
  }

  @PostMapping("/{id}")
  @JsonView(View.Application.class)
  public Application updateApplication(
    JwtAuthenticationToken jwtAuthenticationToken,
    @Parameter(description = "The application unique id", required = true)
    @PathVariable String id,
    @RequestBody Application application)
  {
    log.debug("updateApplication({})", id);

    String username = JwtUtil
      .usernameFromAuthenticationToken(jwtAuthenticationToken);

    log.debug("Username: {}", username);
    log.debug("Received application: {}", application);

    MauiApplication updateApplication = applicationsRepository.
      findByUsernameAndId(username, id).orElseThrow(() -> new ApiException(
      "application " + id + " not found."));

    updateApplication.setBaseTopic(application.getBaseTopic());
    updateApplication.setName(application.getName());
    updateApplication.setDescription(application.getDescription());
    updateApplication.setExpirationDate(application.getExpirationDate());
    updateApplication.setModifiedBy(username);
    MauiApplication mauiApplication = applicationsRepository.save(
      updateApplication);

    log.debug("Response application: {}", mauiApplication);
    return MauiDtoMapper.INSTANCE.mauiApplicationToApplication(mauiApplication);
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<String> deleteById(
    JwtAuthenticationToken jwtAuthenticationToken,
    @Parameter(description = "The application unique id", required = true)
    @PathVariable String id)
  {
    log.debug("deleteById({})", id);

    String username = JwtUtil
      .usernameFromAuthenticationToken(jwtAuthenticationToken);

    MauiApplicationPermission permissions
      = applicationPermissionsRepository.findByUsernameAndApplicationId(
        username, id)
        .orElseThrow(() -> new ApiException("Application permissions not found")
        );

    if (permissions.getPermissions() != APPLICATION_PERMISSION_OWNER)
    {
      log.error("user {} not OWNER of application {}", username, id);
      throw new ApiException("Not application owner");
    }

    log.debug("!!! delete application={}", permissions.getApplication());

    applicationsRepository.deleteById(id);

    return ResponseEntity.ok("Application with ID " + id
      + " successfully deleted.");
  }

  private final MauiApplicationsRepository applicationsRepository;

  private final MauiApplicationPermissionsRepository applicationPermissionsRepository;

  private final MauiPersonsRepository personsRepository;
}
