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
package l9g.webapp.maui.db;

import java.util.List;
import java.util.Optional;
import l9g.webapp.maui.db.model.MauiApplication;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Thorsten Ludewig <t.ludewig@gmail.com>
 */
@Repository
public interface MauiApplicationsRepository extends
  ListCrudRepository<MauiApplication, String>
{
  @Query(
    "SELECT a FROM MauiApplication a, MauiApplicationPermission ap, MauiPerson pe "
    + "WHERE ap.person.id = pe.id AND pe.username = :username AND ap.application.id = a.id "
    + "order by a.name")
  List<MauiApplication> findByUsername(String username);

  @Query("SELECT a FROM MauiApplication a WHERE a.baseTopic = :baseTopic")
  Optional<MauiApplication> findByBaseTopic(String baseTopic);

  @Query(
    "SELECT a FROM MauiApplication a, MauiApplicationPermission ap, MauiPerson pe "
    + "WHERE ap.person.id = pe.id AND pe.username = :username AND ap.application.id = a.id AND a.id = :id")
  Optional<MauiApplication> findByUsernameAndId(String username, String id);
}
