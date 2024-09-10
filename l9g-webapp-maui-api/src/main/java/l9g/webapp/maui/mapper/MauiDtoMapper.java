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
import org.mapstruct.factory.Mappers;

/**
 *
 * @author Thorsten Ludewig <t.ludewig@gmail.com>
 */
@Mapper
public interface MauiDtoMapper
{
  public MauiDtoMapper INSTANCE = Mappers.getMapper(MauiDtoMapper.class);

  // Application
  public Application mauiApplicationToApplication(
    MauiApplication mauiApplication);

  public List<Application> mauiApplicationToApplicationList(
    List<MauiApplication> mauiApplication);

  // ApplicationPermission
  public ApplicationPermission mauiApplicationPermissionToApplicationPermission(
    MauiApplicationPermission mauiApplicationPermission);

  public List<ApplicationPermission> mauiApplicationPermissionToApplicationPermissionList(
    List<MauiApplicationPermission> mauiApplicationPermission);

  // Client
  public Client mauiClientToClient(MauiClient mauiClient);

  public List<Client> mauiClientToClientList(List<MauiClient> mauiClient);

  // Person
  public Person mauiPersonToPerson(MauiPerson mauiPerson);

  public List<Person> mauiPersonListToPersonList(List<MauiPerson> mauiPersonList);

  // Topic
  public Topic mauiTopicToTopic(MauiTopic mauiTopic);

  public List<Topic> mauiTopicToTopicList(List<MauiTopic> mauiTopic);

  // TopicPermission
  public TopicPermission mauiTopicPermissionToTopicPermission(MauiTopicPermission mauiTopicPermission);

  public List<TopicPermission> mauiTopicPermissionToTopicPermissionList(
    List<TopicPermission> mauiTopicPermission);
}
