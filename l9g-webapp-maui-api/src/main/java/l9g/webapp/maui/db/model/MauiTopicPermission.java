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
@Setter
@ToString
@Table(name = "topic_permissions")
public class MauiTopicPermission extends MauiUuidObject
{
  private static final long serialVersionUID = -2491693591385545398L;

  public static final int TOPIC_PERMISSION_NONE = 0;
  public static final int TOPIC_PERMISSION_READONLY = 1;
  public static final int TOPIC_PERMISSION_WRITEONLY = 2;
  public static final int TOPIC_PERMISSION_READWRITE = 3;
  public static final int TOPIC_PERMISSION_CONNECT = 4;
  
  public MauiTopicPermission(String createdBy, MauiClient client,
    MauiTopic topic, int permissions)
  {
    super(createdBy);
    this.client = client;
    this.topic = topic;
    this.permissions = permissions;
  }
  
  @ManyToOne
  @JoinColumn(name = "client_id", nullable = false)
  private MauiClient client;

  @ManyToOne
  @JoinColumn(name = "topic_id", nullable = false)
  private MauiTopic topic;
  
  private int permissions;
}
