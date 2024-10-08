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

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

/**
 *
 * @author Thorsten Ludewig <t.ludewig@gmail.com>
 */
@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler
{
  @ExceptionHandler(ApiException.class)
  public ResponseEntity<String> handleApiException(
    ApiException ex, WebRequest request)
  {
    log.error("bad request: {}", ex.getMessage());
    return new ResponseEntity<>("Bad request: " + ex.getMessage(),
      HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(Throwable.class)
  public ResponseEntity<String> handleGlobalException(Exception ex,
    WebRequest request)
  {
    return new ResponseEntity<>("An unexpected error occurred: " + ex.
      getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
  }
}
