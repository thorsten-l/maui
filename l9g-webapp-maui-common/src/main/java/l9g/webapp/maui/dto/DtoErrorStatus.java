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
public class DtoErrorStatus
{
  public static final int STATUS_NONE = 0;

  public static final int STATUS_SUCCESS = 1;

  public static final int STATUS_FAILURE = 2;

  public static final int ERROR_CODE_UNDEFINED = 0;

  public static final int ERROR_CODE_APPLICATION_UPDATE_FAILED = 0x0101;

  public static final int ERROR_CODE_BASE_TOPIC_UNIQUE = 0x0201;

  public DtoErrorStatus()
  {
    this.status = DtoErrorStatus.STATUS_NONE;
    this.errorCode = DtoErrorStatus.ERROR_CODE_UNDEFINED;
    this.timestamp = System.currentTimeMillis();
  }

  private DtoErrorStatus(int status)
  {
    this();
    this.status = status;
  }

  public static final DtoErrorStatus success()
  {
    return new DtoErrorStatus(STATUS_SUCCESS);
  }

  public static final DtoErrorStatus failure()
  {
    return new DtoErrorStatus(STATUS_FAILURE);
  }

  public DtoErrorStatus title(String title)
  {
    this.title = title;
    return this;
  }

  public DtoErrorStatus message(String message)
  {
    this.message = message;
    return this;
  }

  public DtoErrorStatus errorCode(int errorCode)
  {
    this.errorCode = errorCode;
    return this;
  }

  public DtoErrorStatus exception(String exception)
  {
    this.exception = exception;
    return this;
  }

  @JsonView(View.Base.class)
  private int status;

  @JsonView(View.Base.class)
  private int errorCode;

  @JsonView(View.Base.class)
  private String title;

  @JsonView(View.Base.class)
  private String message;

  @JsonView(View.Base.class)
  private String exception;

  @JsonView(View.Base.class)
  private final long timestamp;
}
