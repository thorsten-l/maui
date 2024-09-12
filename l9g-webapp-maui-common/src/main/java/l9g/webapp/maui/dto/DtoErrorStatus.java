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

  private int status;

  private int errorCode;

  private String title;

  private String message;

  private String exception;

  public DtoErrorStatus()
  {
    status = DtoErrorStatus.STATUS_NONE;
    status = DtoErrorStatus.ERROR_CODE_UNDEFINED;
  }

  public DtoErrorStatus(int status, int errorCode, String message, String title,
    String exception)
  {
    this.status = status;
    this.errorCode = errorCode;
    this.message = message;
    this.title = title;
    this.exception = exception;
  }

  public DtoErrorStatus(int status, int errorCode, String message, String exception)
  {
    this(status, errorCode, message, null, exception);
  }

  public DtoErrorStatus(int status, String message)
  {
    this(status, ERROR_CODE_UNDEFINED, message, null, null);
  } 
  
  public DtoErrorStatus(int status)
  {
    this(status, ERROR_CODE_UNDEFINED, null, null, null);
  }
  
  public static final DtoErrorStatus success()
  {
    return new DtoErrorStatus(STATUS_SUCCESS);
  }
  
  public static final DtoErrorStatus failure()
  {
    return new DtoErrorStatus(STATUS_FAILURE);
  }
}
