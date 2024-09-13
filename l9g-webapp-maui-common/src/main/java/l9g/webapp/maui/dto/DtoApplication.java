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
package l9g.webapp.maui.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonView;
import java.util.Date;
import java.util.List;
import l9g.webapp.maui.json.View;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 *
 * @author Thorsten Ludewig <t.ludewig@gmail.com>
 */
@NoArgsConstructor
@Getter
@Setter
@ToString(callSuper = true)
@JsonInclude(JsonInclude.Include.NON_DEFAULT)
public class DtoApplication extends DtoUuidObject
{

  public DtoApplication(String baseTopic, String name, String description,
    Date expirationDate)
  {
    this.baseTopic = baseTopic;
    this.name = name;
    this.description = description;
    this.expirationDate = expirationDate;
  }

  @JsonView(View.Base.class)
  private String baseTopic;

  @JsonView(View.Base.class)
  private String name;

  @JsonView(View.Base.class)
  private String description;

  @JsonView(View.Base.class)
  private Date expirationDate;

  @JsonView(
    {
      View.Admin.class, View.Application.class
    })
  private List<DtoClient> clients;

  @JsonView(
    {
      View.Admin.class, View.Application.class
    })
  private List<DtoTopic> topics;
}
