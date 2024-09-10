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

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.util.Base64;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

/**
 *
 * @author Thorsten Ludewig <t.ludewig@gmail.com>
 */
public class Pbkdf2sha512Generator
{
  // PBKDF2 parameters
  private static final String PBKDF2_ALGORITHM = "PBKDF2WithHmacSHA512";

  private static final int SALT_BYTE_SIZE = 16; // 128-bit salt

  private static final int HASH_BYTE_SIZE = 64; // 512-bit hash

  private static final int ITERATIONS = 100000;

  private Pbkdf2sha512Generator()
  {
    // no instance
  }

  /**
   *
   * @param password
   *
   * @return
   *
   * @throws NoSuchAlgorithmException
   * @throws InvalidKeySpecException
   */
  public static String hash(String password)
    throws NoSuchAlgorithmException, InvalidKeySpecException
  {
    // Generate a random salt
    byte[] salt = new byte[SALT_BYTE_SIZE];
    new SecureRandom().nextBytes(salt);

    // Hash the password
    PBEKeySpec spec = new PBEKeySpec(
      password.toCharArray(), salt, ITERATIONS, HASH_BYTE_SIZE * 8);
    byte[] hash = SecretKeyFactory
      .getInstance(PBKDF2_ALGORITHM).generateSecret(spec).getEncoded();

    // Convert the salt and hash to Base64
    String saltBase64 = Base64.getEncoder().encodeToString(salt);
    String hashBase64 = Base64.getEncoder().encodeToString(hash);

    // Format the output
    return String.format("PBKDF2$sha512$%d$%s$%s",
      ITERATIONS, saltBase64, hashBase64);
  }
}
