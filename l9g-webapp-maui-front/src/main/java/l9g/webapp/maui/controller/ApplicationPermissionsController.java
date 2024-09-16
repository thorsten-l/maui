
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

import l9g.webapp.maui.client.ApiClientService;
import l9g.webapp.maui.dto.DtoApplicationPermission;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 *
 * @author Thorsten Ludewig <t.ludewig@gmail.com>
 */
@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/application-permissions")
public class ApplicationPermissionsController
{

  @PostMapping(path = "/{id}/{applicationId}",
               consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
  public String updateMauiApplicationPermissions(
    @AuthenticationPrincipal DefaultOidcUser principal,
    @PathVariable String id,
    @PathVariable String applicationId,
    RedirectAttributes redirectAttributes,
    @RequestParam("permissions") Integer permissions, Model model)
  {
    log.debug("updateMauiApplicationPermissions id={}", id);
    controllerUtil.
      defaultModelAttributes(principal, model, false, applicationId);
    log.debug("updateMauiApplicationPermissions={}", permissions);

    DtoApplicationPermission dtoApplicationPermission = new DtoApplicationPermission();
    dtoApplicationPermission.setPermissions(permissions);

    dtoApplicationPermission = mauiApiClientService.updateApplicationPermission(
      id, dtoApplicationPermission);

    controllerUtil.invalidateAllApplicationPermissionsCaches();
    
    log.info("dtoApplicationPermission={}", dtoApplicationPermission);
    log.info("dtoApplicationPermission.errorStatus={}",
      dtoApplicationPermission.getErrorStatus());

    redirectAttributes.addFlashAttribute("errorStatus",
      dtoApplicationPermission.getErrorStatus());

    return "redirect:/application/" + applicationId;
  }

  private final ControllerUtil controllerUtil;

  private final ApiClientService mauiApiClientService;
}
