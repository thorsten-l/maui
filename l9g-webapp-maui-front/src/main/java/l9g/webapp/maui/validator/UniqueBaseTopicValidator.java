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
package l9g.webapp.maui.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import l9g.webapp.maui.client.ApiClientService;
import l9g.webapp.maui.dto.DtoErrorStatus;
import l9g.webapp.maui.form.FormApplication;
import l9g.webapp.maui.validator.annotation.UniqueBaseTopic;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 *
 * @author Thorsten Ludewig <t.ludewig@gmail.com>
 */
@RequiredArgsConstructor
@Slf4j
public class UniqueBaseTopicValidator implements
  ConstraintValidator<UniqueBaseTopic, FormApplication>
{
  @Override
  public boolean isValid(FormApplication formApplication,
    ConstraintValidatorContext context)
  {
    log.debug("{}", formApplication);

    if (formApplication == null || formApplication.getBaseTopic() == null
      || formApplication.getBaseTopic().isEmpty())
    {
      return true;
    }

    DtoErrorStatus errorStatus
      = mauiApiClientService
        .baseTopicUnique(formApplication.getId(), formApplication.getBaseTopic());

    log.debug("{}", errorStatus);

    if (errorStatus.getStatus() != DtoErrorStatus.STATUS_SUCCESS
      || errorStatus.getErrorCode()
      != DtoErrorStatus.ERROR_CODE_BASE_TOPIC_UNIQUE)
    {
      context.disableDefaultConstraintViolation();
      context.buildConstraintViolationWithTemplate(
        context.getDefaultConstraintMessageTemplate())
        .addPropertyNode("baseTopic")
        .addConstraintViolation();
      return false;
    }

    return true;
  }

  private final ApiClientService mauiApiClientService;
}
