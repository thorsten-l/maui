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

import java.util.Optional;
import l9g.webapp.maui.db.model.MauiProperty;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
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
public class DbService
{
  public static final String KEY_DB_INITIALIZED = "database.initialized";

  public static final String KEY_SYSTEM_USER = "*SYSTEM*";

  /**
   * Handles the application startup event.
   *
   * This method is triggered when the application is ready and performs
   * necessary initialization tasks.
   */
  @EventListener(ApplicationReadyEvent.class)
  public void onStartup()
  {
    log.info("application startup");

    Optional<MauiProperty> optional = propertiesRepository.findByKey(
      KEY_DB_INITIALIZED);

    if (optional != null && optional.isPresent() && !optional.isEmpty()
      && "true".equalsIgnoreCase(optional.get().getValue()))
    {
      log.info("*** database is already initialized");
    }
    else
    {
      MauiProperty dbInitialized
        = new MauiProperty(KEY_SYSTEM_USER, DbService.KEY_DB_INITIALIZED, "true");
      propertiesRepository.save(dbInitialized);
      
      dbInitialized = propertiesRepository.findById(dbInitialized.getId()).get();
      log.debug("{}", dbInitialized);
      log.debug("initializeWithSampleData = {}", initializeWithSampleData);

      if ("true".equalsIgnoreCase(initializeWithSampleData))
      {
        sampleDataService.initializeWithSampleData();
      }
    }
  }

  @Value("${maui.initialize-with-sample-data}")
  private String initializeWithSampleData;

  private final MauiPropertiesRepository propertiesRepository;

  private final DbSampleDataService sampleDataService;
}
