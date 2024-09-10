/*
 * Copyright 2024 Thorsten Ludewig (t.ludewig@gmail.com).
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
package l9g.webapp.maui.db;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import static l9g.webapp.maui.db.DbService.KEY_SYSTEM_USER;
import l9g.webapp.maui.db.model.MauiApplication;
import l9g.webapp.maui.db.model.MauiApplicationPermission;
import static l9g.webapp.maui.db.model.MauiApplicationPermission.APPLICATION_PERMISSION_CONSUMER;
import static l9g.webapp.maui.db.model.MauiApplicationPermission.APPLICATION_PERMISSION_MANAGER;
import static l9g.webapp.maui.db.model.MauiApplicationPermission.APPLICATION_PERMISSION_OWNER;
import l9g.webapp.maui.db.model.MauiClient;
import l9g.webapp.maui.db.model.MauiPerson;
import l9g.webapp.maui.db.model.MauiTopic;
import l9g.webapp.maui.db.model.MauiTopicPermission;
import static l9g.webapp.maui.db.model.MauiTopicPermission.TOPIC_PERMISSION_CONNECT;
import static l9g.webapp.maui.db.model.MauiTopicPermission.TOPIC_PERMISSION_READONLY;
import static l9g.webapp.maui.db.model.MauiTopicPermission.TOPIC_PERMISSION_WRITEONLY;
import l9g.webapp.maui.util.PasswordGeneratorService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * DbService is responsible for managing and updating the properties related to
 * projects, persons, and organizations.
 *
 * @author Thorsten Ludewig (t.ludewig@gmail.com)
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class DbSampleDataService
{
  public void initializeWithSampleData()
  {
    log.info("*** initialize with sample data");

    MauiPerson tester = new MauiPerson(KEY_SYSTEM_USER, "c1test1", "Tester",
      "Test", "tester@test.de");
    MauiPerson testine = new MauiPerson(KEY_SYSTEM_USER, "c1test2",
      "Testine", "Test", "testine@test.de");

    personsRepository.save(tester);
    personsRepository.save(testine);

    LocalDate oneYearFromNow = LocalDate.now().plusYears(1);
    Date expirationDate = Date.from(oneYearFromNow.atStartOfDay(ZoneId.
      systemDefault()).toInstant());

    MauiApplication appEins = new MauiApplication(KEY_SYSTEM_USER,
      "app-eins", "AppEins", "Dieses ist die Test App 1", expirationDate);

    MauiApplication appZwei = new MauiApplication(KEY_SYSTEM_USER,
      "app-zwei", "AppZwei", "Dieses ist die Test App 2", expirationDate);

    applicationsRepository.save(appEins);
    applicationsRepository.save(appZwei);

    applicationPermissionsRepository.save(new MauiApplicationPermission(
      KEY_SYSTEM_USER, tester, appEins, APPLICATION_PERMISSION_OWNER)); // owner      
    applicationPermissionsRepository.save(new MauiApplicationPermission(
      KEY_SYSTEM_USER, tester, appZwei, APPLICATION_PERMISSION_OWNER)); // owner      
    applicationPermissionsRepository.save(new MauiApplicationPermission(
      KEY_SYSTEM_USER, testine, appEins, APPLICATION_PERMISSION_MANAGER)); // manager      
    applicationPermissionsRepository.save(new MauiApplicationPermission(
      KEY_SYSTEM_USER, testine, appZwei, APPLICATION_PERMISSION_CONSUMER)); // consumer

    MauiClient mc000001 = new MauiClient(KEY_SYSTEM_USER, appEins,
      "mc000001", passwordGeneratorService.generatePassword(32), 
      "D1mini", "Beispiel Temperatursensor");
    MauiClient mc000002 = new MauiClient(KEY_SYSTEM_USER, appEins,
      "mc000002", passwordGeneratorService.generatePassword(32), 
      "nodered", "Beispiel NodeRed Applikation");

    clientsRepository.save(mc000001);
    clientsRepository.save(mc000002);

    //////////////////////////////////////////////////////////////////////////
    MauiTopic topicState = new MauiTopic(KEY_SYSTEM_USER, appEins, "/state");
    MauiTopic topicCommand = new MauiTopic(KEY_SYSTEM_USER, appEins, "/command");
    MauiTopic topicApplication = new MauiTopic(KEY_SYSTEM_USER, appEins, "/application");
    topicsRepository.save(topicState);
    topicsRepository.save(topicCommand);
    topicsRepository.save(topicApplication);

    MauiTopicPermission topicPermConnect
      = new MauiTopicPermission(KEY_SYSTEM_USER, mc000001, topicState,
        TOPIC_PERMISSION_CONNECT);
    
    MauiTopicPermission topicPermReadonly
      = new MauiTopicPermission(KEY_SYSTEM_USER, mc000001, topicState,
        TOPIC_PERMISSION_READONLY);

    MauiTopicPermission topicPermCommand
      = new MauiTopicPermission(KEY_SYSTEM_USER, mc000002, topicCommand,
        TOPIC_PERMISSION_WRITEONLY);

    topicPermissionsRepository.save(topicPermConnect);
    topicPermissionsRepository.save(topicPermReadonly);
    topicPermissionsRepository.save(topicPermCommand);
  }

  private final MauiPropertiesRepository propertiesRepository;

  private final MauiPersonsRepository personsRepository;

  private final MauiApplicationsRepository applicationsRepository;

  private final MauiApplicationPermissionsRepository applicationPermissionsRepository;

  private final MauiClientsRepository clientsRepository;

  private final MauiTopicsRepository topicsRepository;

  private final MauiTopicPermissionsRepository topicPermissionsRepository;

  private final PasswordGeneratorService passwordGeneratorService;
}
