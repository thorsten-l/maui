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

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import java.util.Date;
import java.util.List;
import l9g.webapp.maui.dto.DtoApplication;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 *
 * @author Thorsten Ludewig <t.ludewig@gmail.com>
 */
@Entity
@NoArgsConstructor
@Getter
@Setter
@Table(name = "applications")
@ToString(callSuper = true)
public class MauiApplication extends MauiUuidObject
{
  private static final long serialVersionUID = 2373457469632195188l;

  public MauiApplication(
    String createdBy,
    String baseTopic,
    String name,
    String description,
    Date expirationDate)
  {
    super(createdBy);
    this.baseTopic = baseTopic;
    this.name = name;
    this.description = description;
    this.expirationDate = expirationDate;
  }

  public MauiApplication(String createdBy, DtoApplication application)
  {
    this(createdBy, application.getBaseTopic(), application.getName(),
      application.getDescription(), application.getExpirationDate());
  }

  @Column(name = "base_topic", nullable = false, unique = true)
  private String baseTopic;

  @Column(nullable = false)
  private String name;

  private String description;

  @Column(name = "expiration_date", nullable = false)
  @Temporal(TemporalType.DATE)
  private Date expirationDate;

  @OneToMany(mappedBy = "application", cascade = CascadeType.REMOVE,
             fetch = FetchType.EAGER)
  private List<MauiApplicationPermission> applicationPermission;

  @OneToMany(mappedBy = "application", cascade = CascadeType.REMOVE,
             fetch = FetchType.EAGER)
  private List<MauiClient> clients;

  @OneToMany(mappedBy = "application", cascade = CascadeType.REMOVE,
             fetch = FetchType.EAGER)

  private List<MauiTopic> topics;
}
