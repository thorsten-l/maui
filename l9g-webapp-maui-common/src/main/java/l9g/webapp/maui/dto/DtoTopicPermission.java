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
import l9g.webapp.maui.json.View;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 *
 * @author Thorsten Ludewig <t.ludewig@gmail.com>
 */
@Getter
@Setter
@ToString
@JsonInclude(JsonInclude.Include.NON_DEFAULT)
public class DtoTopicPermission extends DtoUuidObject
{
  public static final int TOPIC_PERMISSION_NONE = 0;

  public static final int TOPIC_PERMISSION_READONLY = 1;

  public static final int TOPIC_PERMISSION_WRITEONLY = 2;

  public static final int TOPIC_PERMISSION_READWRITE = 3;

  public static final int TOPIC_PERMISSION_CONNECT = 4;

  @JsonView(View.Base.class)
  private DtoTopic topic;

  @JsonView(View.Base.class)
  private int permissions;
}
