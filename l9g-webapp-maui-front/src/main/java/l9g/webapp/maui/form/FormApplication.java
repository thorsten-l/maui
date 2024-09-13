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
package l9g.webapp.maui.form;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonView;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.Date;
import l9g.webapp.maui.json.View;
import l9g.webapp.maui.validator.annotation.UniqueBaseTopic;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

/**
 *
 * @author Thorsten Ludewig <t.ludewig@gmail.com>
 */
@NoArgsConstructor
@Getter
@Setter
@ToString(callSuper = true)
@JsonInclude(JsonInclude.Include.NON_DEFAULT)
@UniqueBaseTopic(message = "{error.baseTopic.unique}")
public class FormApplication extends FormUuidObject
{
  @JsonView(View.Base.class)
  @NotBlank(message = "{error.baseTopic.notBlank}")
  private String baseTopic;

  @JsonView(View.Base.class)
  @NotBlank(message = "{error.name.notBlank}")
  private String name;

  @JsonView(View.Base.class)
  private String description;

  @JsonView(View.Base.class)
  @DateTimeFormat(pattern = "yyyy-MM-dd")
  @NotNull(message = "{error.expirationDate.notNull}")
  private Date expirationDate;
}
