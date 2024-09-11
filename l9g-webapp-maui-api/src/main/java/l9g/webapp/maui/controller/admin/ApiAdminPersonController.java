
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
package l9g.webapp.maui.controller.admin;

import com.fasterxml.jackson.annotation.JsonView;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import l9g.webapp.maui.db.MauiPersonsRepository;
import l9g.webapp.maui.json.View;
import static l9g.webapp.maui.json.View.JSON_STATUS_OK;
import l9g.webapp.maui.db.model.MauiPerson;
import l9g.webapp.maui.dto.DtoPerson;
import l9g.webapp.maui.mapper.MauiDtoMapper;
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
@RequestMapping(path = "/api/v1/admin/person",
                produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Administration Person",
     description = "CURD frontend persons")
public class ApiAdminPersonController
{
  @GetMapping()
  @JsonView(View.Admin.class)
  public List<DtoPerson> adminFindAll()
  {
    log.debug("adminFindAll()");
    return MauiDtoMapper.INSTANCE.mauiPersonListToPersonList(personsRepository.findAll());
  }

  @DeleteMapping("/{id}")
  public String deleteEntry(
    @Parameter(description = "The topics id.", required = true)
    @PathVariable String id)
  {
    log.debug("delete topic: {}", id);
    MauiPerson tester = personsRepository.findById(id).get();
    personsRepository.delete(tester);
    return JSON_STATUS_OK;
  }

  private final MauiPersonsRepository personsRepository;
}
