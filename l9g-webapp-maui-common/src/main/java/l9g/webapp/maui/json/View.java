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
package l9g.webapp.maui.json;

/**
 *
 * @author Thorsten Ludewig <t.ludewig@gmail.com>
 */
public interface View
{
  static final String JSON_STATUS_OK = "{\"status\": \"OK\"}";

  public static class None
  {
  };

  public static class Base
  {
  };

  public static class Person extends Base
  {
  };

  public static class Application extends Base
  {
  };

  public static class ApplicationPermissionApplication extends Base
  {
  };

  public static class ApplicationPermissionPerson extends Base
  {
  };

  public static class Client extends Base
  {
  };

  public static class Topic extends Base
  {
  };

  public static class Admin extends Base
  {
  };

}
