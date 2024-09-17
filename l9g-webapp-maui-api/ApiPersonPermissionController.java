
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
import l9g.webapp.maui.db.model.MauiApplicationPermission;
import l9g.webapp.maui.json.View;
import l9g.webapp.maui.db.model.MauiPerson;
import l9g.webapp.maui.dto.DtoApplicationPermission;
import l9g.webapp.maui.mapper.MauiDtoMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Thorsten Ludewig (t.ludewig@gmail.com)
 */
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping(path = "/api/v1/person-permission",
                produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Application permission",
     description = "CURD frontend for application permissions for persons")
public class ApiPersonPermissionController
{

  @GetMapping("/{id}")
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

  @GetMapping("/own/{id}")
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

  private final MauiApplicationPermissionsRepository applicationPermissionsRepository;
}
