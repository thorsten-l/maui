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
@Table(name = "topics")
@ToString(callSuper = true)
public class MauiTopic extends MauiUuidObject
{
  private static final long serialVersionUID = 2373457469632195188l;

  public MauiTopic(
    String createdBy,
    MauiApplication application,
    String subTopic)
  {
    super(createdBy);
    this.application = application;
    this.subTopic = subTopic;
  }

  @ManyToOne
  @JoinColumn(name = "application_id", nullable = false)
  private MauiApplication application;

  @Column(name = "sub_topic", nullable = false)
  private String subTopic;

  @OneToMany(mappedBy = "topic", cascade = CascadeType.REMOVE,
             fetch = FetchType.EAGER)
  private List<MauiTopicPermission> topicPermission;
}
