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

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 *
 * @author Thorsten Ludewig <t.ludewig@gmail.com>
 */
@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/")
public class HomeController
{
  @GetMapping()
  public String home(Model model,
    @AuthenticationPrincipal DefaultOidcUser principal)
  {
    log.debug("home");
    controllerUtil.defaultModelAttributes(principal, model, true);
    return "home";
  }
  
  @GetMapping("/debug-api")
  public String debugApi(Model model,
    @AuthenticationPrincipal DefaultOidcUser principal)
  {
    log.debug("debugApi");
    controllerUtil.defaultModelAttributes(principal, model);
    return "debug-api";
  }

  private final ControllerUtil controllerUtil;
}
