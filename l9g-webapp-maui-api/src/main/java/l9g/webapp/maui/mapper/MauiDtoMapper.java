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
package l9g.webapp.maui.mapper;

import java.util.List;
import l9g.webapp.maui.db.model.*;
import l9g.webapp.maui.dto.*;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

/**
 *
 * @author Thorsten Ludewig <t.ludewig@gmail.com>
 */
@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface MauiDtoMapper
{
  public MauiDtoMapper INSTANCE = Mappers.getMapper(MauiDtoMapper.class);

  // DtoApplication
  public DtoApplication mauiApplicationToDtoApplication(
    MauiApplication mauiApplication);

  public List<DtoApplication> mauiApplicationListToDtoApplicationList(
    List<MauiApplication> mauiApplication);

  // DtoApplicationPermission
  public DtoApplicationPermission mauiApplicationPermissionToDtoApplicationPermission(
    MauiApplicationPermission mauiApplicationPermission);

  public List<DtoApplicationPermission> mauiApplicationPermissionToDtoApplicationPermissionList(
    List<MauiApplicationPermission> mauiApplicationPermission);

  // DtoClient
  public DtoClient mauiClientToClient(MauiClient mauiClient);

  public List<DtoClient> mauiClientToClientList(List<MauiClient> mauiClient);

  // DtoPerson
  public DtoPerson mauiPersonToDtoPerson(MauiPerson mauiPerson);

  public List<DtoPerson> mauiPersonListToDtoPersonList(List<MauiPerson> mauiPersonList);

  // DtoTopic
  public DtoTopic mauiTopicToDtoTopic(MauiTopic mauiTopic);

  public List<DtoTopic> mauiTopicToDtoTopicList(List<MauiTopic> mauiTopic);

  // DtoTopicPermission
  public DtoTopicPermission mauiTopicPermissionToDtoTopicPermission(MauiTopicPermission mauiTopicPermission);

  public List<DtoTopicPermission> mauiTopicPermissionToDtoTopicPermissionList(
    List<DtoTopicPermission> mauiTopicPermission);
}
