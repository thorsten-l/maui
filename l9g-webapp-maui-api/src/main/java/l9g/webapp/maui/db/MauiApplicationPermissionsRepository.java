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
import l9g.webapp.maui.db.model.MauiApplicationPermission;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Thorsten Ludewig <t.ludewig@gmail.com>
 */
@Repository
public interface MauiApplicationPermissionsRepository extends
  ListCrudRepository<MauiApplicationPermission, String>
{
  @Query("SELECT COUNT(ap) FROM MauiApplicationPermission ap "
    + "WHERE ap.application.id = :applicationId AND ap.permissions = :permission")
  long countPermissionsByApplicationIdAndPermission(String applicationId,
    int permission);
  
  @Query("SELECT ap FROM MauiApplicationPermission ap, MauiPerson pe, MauiApplication a "
    + "WHERE ap.person.id = pe.id AND pe.username = :username AND ap.application.id = a.id "
    + "ORDER BY a.name")
  List<MauiApplicationPermission> findByUsername(String username);  
  
  @Query("SELECT ap FROM MauiApplicationPermission ap "
    + "WHERE ap.application.id = :applicationId")
  List<MauiApplicationPermission> findByApplicationId(String applicationId);  
  
  @Query("SELECT ap FROM MauiApplicationPermission ap, MauiPerson pe "
    + "WHERE ap.person.id = pe.id AND pe.username = :username AND ap.application.id = :applicationId" )
  Optional<MauiApplicationPermission> findByUsernameAndApplicationId(String username, String applicationId);
}
