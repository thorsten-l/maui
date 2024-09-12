
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

import jakarta.validation.Valid;
import java.util.Date;
import java.util.List;
import l9g.webapp.maui.client.ApiClientService;
import l9g.webapp.maui.dto.DtoApplication;
import l9g.webapp.maui.dto.DtoApplicationPermission;
import l9g.webapp.maui.dto.DtoErrorStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 *
 * @author Thorsten Ludewig <t.ludewig@gmail.com>
 */
@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/application")
public class ApplicationController
{
  @GetMapping("/{id}")
  public String mauiApplication(
    @AuthenticationPrincipal DefaultOidcUser principal,
    @PathVariable String id, Model model
  )
  {
    log.debug("mauiApplication id={}", id);
    controllerUtil.defaultModelAttributes(principal, model, false, id);

    DtoApplication application = mauiApiClientService.findApplicationById(id);
    log.debug("mauiApplication={}", application);

    List<DtoApplicationPermission> personPermissions
      = mauiApiClientService.findPersonPermissions(id);
    log.debug("personPermissions={}", personPermissions);

    model.addAttribute("mauiApplication", application);
    model.addAttribute("dtoApplication", application);
    model.addAttribute("mauiPersonPermissions", personPermissions);
    return "application";
  }

  @PostMapping("/{id}")
  public String updateMauiApplication(
    @AuthenticationPrincipal DefaultOidcUser principal,
    @PathVariable String id,
    RedirectAttributes redirectAttributes,
    @Valid @ModelAttribute("dtoApplication") DtoApplication dtoApplication, BindingResult bindingResult, Model model)
  {
    log.debug("mauiApplication id={}", id);
    controllerUtil.defaultModelAttributes(principal, model, false, id);
    log.debug("dtoApplication={}", dtoApplication);

    if (bindingResult.hasErrors())
    {
      log.debug("Form error: {}", bindingResult);
      DtoApplication application = mauiApiClientService.findApplicationById(id);
      List<DtoApplicationPermission> personPermissions
        = mauiApiClientService.findPersonPermissions(id);
      model.addAttribute("dtoApplication", dtoApplication);
      model.addAttribute("mauiApplication", application);
      model.addAttribute("mauiPersonPermissions", personPermissions);
      log.debug("show errors....");
      return "application";
    }

    DtoApplication updatedApplication = mauiApiClientService.updateApplication(
      dtoApplication, id);
    
    log.info("updatedApplication={}", updatedApplication);
    log.info("updatedApplication.errorStatus={}", updatedApplication.getErrorStatus());
    
    redirectAttributes.addFlashAttribute("errorStatus", updatedApplication.getErrorStatus());

    return "redirect:/application/" + id;
  }

  @PostMapping("/delete")
  public String mauiApplicationDelete(Model model,
    @AuthenticationPrincipal DefaultOidcUser principal,
    DtoApplication formApplication
  )
  {
    log.debug("mauiApplicationDelete = {}", formApplication);

    DtoErrorStatus errorStatus = mauiApiClientService.deleteApplicationById(formApplication.getId());
    log.info("errorStatus={}", errorStatus);

    return "redirect:/";
  }

  @GetMapping("/create")
  public String mauiApplicationCreate(Model model,
    @AuthenticationPrincipal DefaultOidcUser principal,
    DtoApplication mauiApplication
  )
  {
    // TODO: not implemented yet
    DtoApplication newApplication = new DtoApplication("baseTopic" + System.
      currentTimeMillis(),
      "app name", "app decription", new Date());

    log.debug("mauiApplicationCreate = {}", newApplication);    
    newApplication = mauiApiClientService.createApplication(newApplication);
    log.debug("response = {}", newApplication);
    return "redirect:/";
  }

  private final ControllerUtil controllerUtil;

  private final ApiClientService mauiApiClientService;
}
