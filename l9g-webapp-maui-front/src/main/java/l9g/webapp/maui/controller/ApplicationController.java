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

import java.util.Date;
import java.util.List;
import l9g.webapp.maui.client.ApiClientService;
import l9g.webapp.maui.dto.Application;
import l9g.webapp.maui.dto.ApplicationPermission;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

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

    Application application = mauiApiClientService.findApplicationById(id);
    log.debug("mauiApplication={}", application);

    List<ApplicationPermission> personPermissions 
      = mauiApiClientService.findPersonPermissions(id);
    log.debug("personPermissions={}", personPermissions);
    
    model.addAttribute("mauiApplication", application);
    model.addAttribute("mauiPersonPermissions", personPermissions);
    return "application";
  }

  @PostMapping("/delete")
  public String mauiApplicationDelete(Model model,
    @AuthenticationPrincipal DefaultOidcUser principal,
    Application mauiApplication
  )
  {
    log.debug("mauiApplicationDelete = {}", mauiApplication);
    mauiApiClientService.deleteApplicationById(mauiApplication.getId());
    return "redirect:/";
  }

  @GetMapping("/create")
  public String mauiApplicationCreate(Model model,
    @AuthenticationPrincipal DefaultOidcUser principal,
    Application mauiApplication
  )
  {
    log.debug("mauiApplicationCreate = {}", mauiApplication);
    Application newApplication = new Application("baseTopic" + System.
      currentTimeMillis(),
      "app name", "app decription", new Date());
    newApplication = mauiApiClientService.createApplication(newApplication);
    log.debug("response = {}", newApplication);
    return "redirect:/";
  }

  private final ControllerUtil controllerUtil;

  private final ApiClientService mauiApiClientService;
}
