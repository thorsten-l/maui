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
package l9g.webapp.maui.db.model;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

/**
 *
 * @author Thorsten Ludewig <t.ludewig@gmail.com>
 */
@Entity
@NoArgsConstructor
@Slf4j
@Getter
@ToString
@Table(name = "application_permissions")
public class MauiApplicationPermission extends MauiUuidObject
{
  private static final long serialVersionUID = -2491693591385545398L;

  public static final int APPLICATION_PERMISSION_NONE = 0;
  public static final int APPLICATION_PERMISSION_OWNER = 1;
  public static final int APPLICATION_PERMISSION_MANAGER = 2;
  public static final int APPLICATION_PERMISSION_CONSUMER = 3;

  public MauiApplicationPermission(String createdBy, MauiPerson person,
    MauiApplication application, int permissions)
  {
    super(createdBy);
    this.person = person;
    this.application = application;
    this.permissions = permissions;
  }
  
  @ManyToOne
  @JoinColumn(name = "person_id", nullable = false)
  @ToString.Exclude
  private MauiPerson person;

  @ManyToOne
  @JoinColumn(name = "application_id", nullable = false)
  @ToString.Exclude
  private MauiApplication application;
  
  @Setter
  private int permissions;
}
