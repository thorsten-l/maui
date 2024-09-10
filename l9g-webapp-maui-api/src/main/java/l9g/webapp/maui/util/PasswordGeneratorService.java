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
package l9g.webapp.maui.util;

import java.util.Random;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 *
 * @author Thorsten Ludewig <t.ludewig@gmail.com>
 */
@Service
@Slf4j
public class PasswordGeneratorService
{
  private final Random random = new Random(System.currentTimeMillis());
  
  private static final StringBuffer VALID_CHARS = new StringBuffer("0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ");
  
  public String generatePassword(int length)
  {
    StringBuffer password = new StringBuffer();
    for( int i=0; i<length; i++)
    {
      password.append(VALID_CHARS.charAt(random.nextInt(VALID_CHARS.length())));
    }
    
    log.trace("new password = {}/{}", password, length);
    
    return password.toString();
  }
}
