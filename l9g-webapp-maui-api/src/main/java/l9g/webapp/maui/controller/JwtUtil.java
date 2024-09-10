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

import java.util.Map;
import l9g.webapp.maui.db.model.MauiPerson;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.core.oidc.StandardClaimNames;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

/**
 *
 * @author Thorsten Ludewig <t.ludewig@gmail.com>
 */
@Slf4j
public class JwtUtil
{
  private JwtUtil()
  {
  }
  
  public static final String usernameFromAuthenticationToken(
    JwtAuthenticationToken jwtAuthenticationToken)
  {
    String username = null;

    Map<String, Object> attributes = jwtAuthenticationToken.getTokenAttributes();

    if (attributes != null)
    {
      username = (String) attributes.get(StandardClaimNames.PREFERRED_USERNAME);
    }

    if (username == null)
    {
      throw new ApiException("Invalid JWT token or missing username.");
    }

    log.debug("username={}", username);
    return username;
  }

  public static final MauiPerson personFromAuthenticationToken(
    JwtAuthenticationToken jwtAuthenticationToken)
  {
    MauiPerson person = null;

    Map<String, Object> attributes = jwtAuthenticationToken.getTokenAttributes();

    if (attributes != null)
    {
      String username = (String) attributes.get(
        StandardClaimNames.PREFERRED_USERNAME);
      String givenName = (String) attributes.get(StandardClaimNames.GIVEN_NAME);
      String familyName = (String) attributes.
        get(StandardClaimNames.FAMILY_NAME);
      String email = (String) attributes.get(StandardClaimNames.EMAIL);

      person = new MauiPerson(username, username, givenName, familyName, email);
    }

    log.debug("person={}", person);
    return person;
  }
}
