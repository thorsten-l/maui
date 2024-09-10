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
package l9g.webapp.maui;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * The FisResApplication class is the entry point for the FisRes web
 * application.
 * It is responsible for starting the application by running the
 * SpringApplication.
 */
@SpringBootApplication
public class MauiFrontApplication
{

  /**
   * The entry point of the application.
   * This method is called by the JVM to start the application.
   *
   * @param args command-line arguments passed to the application.
   */
  public static void main(String[] args)
  {
    System.getProperties().put("server.error.whitelabel.enabled", "false");
    System.getProperties().put("server.error.path", "/error");
    SpringApplication.run(MauiFrontApplication.class, args);
  }
}
