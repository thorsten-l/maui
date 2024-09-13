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

import l9g.webapp.maui.dto.*;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

/**
 *
 * @author Thorsten Ludewig <t.ludewig@gmail.com>
 */
//@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
@Mapper
public interface FormDtoMapper
{
  public FormDtoMapper INSTANCE = Mappers.getMapper(FormDtoMapper.class);

  // FormApplication
  public FormApplication dtoApplicationToFormApplication(
    DtoApplication dtoApplication);

  // DtoApplication
  @Mapping(target = "errorStatus", ignore = true)
  @Mapping(target = "clients", ignore = true)
  @Mapping(target = "topics", ignore = true)
  public DtoApplication formApplicationToDtoApplication(
    FormApplication formApplication);
}
