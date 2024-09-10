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
import java.util.List;
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
@Table(name = "persons")
@ToString(callSuper = true)
public class MauiPerson extends MauiUuidObject
{
  private static final long serialVersionUID = 2377357483632195188l;

  public MauiPerson(
    String createdBy,
    String username,
    String firstname,
    String lastname,
    String email)
  {
    super(createdBy);
    this.username = username.toLowerCase();
    this.firstname = firstname;
    this.lastname = lastname;
    this.email = email.toLowerCase();
  }

  private String username;

  @Column(nullable = false)
  private String firstname;

  @Column(nullable = false)
  private String lastname;

  @Column(nullable = false, unique = true)
  private String email;

  @ToString.Exclude
  @OneToMany(mappedBy = "person", cascade = CascadeType.REMOVE,
             fetch = FetchType.EAGER)
  private List<MauiApplicationPermission> applicationPermissions;
}
