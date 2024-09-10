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
package l9g.webapp.maui.config;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;

/**
 *
 * @author Thorsten Ludewig <t.ludewig@gmail.com>
 */
public class MauiAuthoritiesConverter implements
  Converter<Jwt, Collection<GrantedAuthority>>
{

  @Override
  public Collection<GrantedAuthority> convert(Jwt jwt)
  {
    List<String> realmRoles
      = (jwt.getClaimAsMap("realm_access") != null
      && jwt.getClaimAsMap("realm_access").get("roles") != null)
      ? (List<String>) jwt.getClaimAsMap("realm_access").get("roles")
      : List.of();

    System.out.println("\n- Realm Roles -----");
    realmRoles.forEach(System.out::println);
    System.out.println();

    List<String> resourceRoles
      = (jwt.getClaimAsMap("resource_access") != null
      && jwt.getClaimAsMap("resource_access").get("maui") != null
      && ((Map) jwt.getClaimAsMap("resource_access").get("maui")).get("roles")
      != null)
        ? ((Map<String, List<String>>) jwt
          .getClaimAsMap("resource_access").get("maui")).get("roles")
        : List.of();

    System.out.println("- Resource Roles (maui) -----");
    resourceRoles.forEach(System.out::println);
    System.out.println();

    List<GrantedAuthority> authorities = Stream.concat(realmRoles.stream(),
      resourceRoles.stream())
      .map(SimpleGrantedAuthority::new)
      .collect(Collectors.toList());

    return authorities;
  }
}
