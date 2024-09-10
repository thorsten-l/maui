
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

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import java.util.UUID;
import l9g.webapp.maui.db.MauiClientsRepository;
import l9g.webapp.maui.db.model.MauiClient;
import l9g.webapp.maui.db.model.MauiTopic;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
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
@RequestMapping(path = "/api/v1/client",
                produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Client",
     description = "CURD frontend clients")
public class ApiClientController
{
  private static final String OK = "{\"status\": \"OK\"}";

  @GetMapping()
  public List<MauiClient> findAll()
  {
    log.debug("findAll()");
    return clientsRepository.findAll();
  }

  @DeleteMapping("/{id}")
  public String deleteEntry(
    @Parameter(description = "The client id.", required = true)
    @PathVariable String id)
  {
    log.debug("delete client: {}", id);
    MauiClient client = clientsRepository.findById(id).get();
    clientsRepository.delete(client);
    return OK;
  }

  private final MauiClientsRepository clientsRepository;
}
