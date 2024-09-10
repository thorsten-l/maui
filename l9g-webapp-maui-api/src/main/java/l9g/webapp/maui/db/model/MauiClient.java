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
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.List;
import l9g.webapp.maui.util.Pbkdf2sha512Generator;
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
@Getter
@Setter
@Table(name = "clients")
@ToString(callSuper = true)
@Slf4j
public class MauiClient extends MauiUuidObject
{
  private static final long serialVersionUID = 2377357483632199998l;

  public MauiClient(
    String createdBy,
    MauiApplication application,
    String username,
    String passwordClear,
    String name,
    String description)
  {
    super(createdBy);
    this.application = application;
    this.username = username;
    setPasswordClearHash(passwordClear);
    this.name = name;
    this.description = description;
    this.active = true;
    this.admin = false;
  }

  public final void setPasswordClearHash(String passwordClear)
  {
    this.passwordClear = passwordClear;

    try
    {
      this.passwordHash = Pbkdf2sha512Generator.hash(passwordClear);
    }
    catch (NoSuchAlgorithmException | InvalidKeySpecException ex)
    {
      log.error("password hash failed ");
    }
  }

  @ManyToOne
  @JoinColumn(name = "application_id", nullable = false)
  private MauiApplication application;

  @Column(nullable = false, unique = true)
  private String username;

  @Column(nullable = false, name = "password_clear")
  private String passwordClear;

  @Column(nullable = false, name = "password_hash")
  private String passwordHash;

  @Column(nullable = false)
  private String name;

  private String description;

  @Column(name = "is_active")
  private boolean active;

  @Column(name = "is_admin")
  private boolean admin;

  @OneToMany(mappedBy = "client", cascade = CascadeType.REMOVE,
             fetch = FetchType.EAGER)
  private List<MauiTopicPermission> topicPermissions;
}
