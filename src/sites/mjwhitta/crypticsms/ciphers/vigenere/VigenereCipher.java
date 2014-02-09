/**
 * GNU GENERAL PUBLIC LICENSE See the file license.txt for copying conditions.
 * 
 * @author mjwhitta
 * @created Feb 11, 2012
 */
package sites.mjwhitta.crypticsms.ciphers.vigenere;

import java.io.BufferedReader;
import java.io.BufferedWriter;

import sites.mjwhitta.crypticsms.ciphers.Cipher;

/**
 * A basic VigenereCipher.
 */
public class VigenereCipher extends Cipher {
   private String m_key = "";

   /**
    * Constructor
    */
   public VigenereCipher() {
      this("");
   }

   /**
    * @param name
    */
   public VigenereCipher(String name) {
      m_name = name;
      m_type = "VigenereCipher";
   }

   /**
    * @see sites.mjwhitta.crypticsms.ciphers.Cipher#decrypt(java.lang.String)
    */
   @Override
   public String decrypt(String ciphertext) throws Exception {
      if ((m_key == null) || (m_key.length() == 0)) {
         throw new Exception("There was an error. Key was null or empty. "
               + "Please delete the cipher and try again.");
      }

      String plaintext = "";
      int keyIndex = 0;
      char[] key = m_key.toCharArray();

      for (char c : ciphertext.toCharArray()) {
         int rotation = ALPHANUMERIC.indexOf(key[keyIndex]) + 1;

         if (ALPHANUMERIC.contains(Character.toLowerCase(c))) {
            // have to add s_alphabet.size() first b/c JVM doesn't
            // handle
            // negative mod
            int index = (ALPHANUMERIC.size() + (ALPHANUMERIC.indexOf(Character
                  .toLowerCase(c)) - rotation)) % ALPHANUMERIC.size();

            plaintext += ALPHANUMERIC.get(index);

            keyIndex = (keyIndex + 1) % m_key.length();
         } else {
            plaintext += c;
         }
      }

      return plaintext;
   }

   /**
    * @see sites.mjwhitta.crypticsms.ciphers.Cipher#encrypt(java.lang.String)
    */
   @Override
   public String encrypt(String plaintext) throws Exception {
      if ((m_key == null) || (m_key.length() == 0)) {
         throw new Exception("There was an error. Key was null or empty. "
               + "Please delete the cipher and try again.");
      }

      String ciphertext = "";
      int keyIndex = 0;
      char[] key = m_key.toCharArray();

      for (char c : plaintext.toCharArray()) {
         int rotation = ALPHANUMERIC.indexOf(key[keyIndex]) + 1;

         if (ALPHANUMERIC.contains(Character.toLowerCase(c))) {
            int index = (rotation + ALPHANUMERIC.indexOf(Character
                  .toLowerCase(c))) % ALPHANUMERIC.size();

            ciphertext += ALPHANUMERIC.get(index);

            keyIndex = (keyIndex + 1) % m_key.length();
         } else {
            ciphertext += c;
         }
      }

      return ciphertext;
   }

   /**
    * @see sites.mjwhitta.crypticsms.ciphers.Cipher#getConfigureClass()
    */
   @SuppressWarnings("rawtypes")
   @Override
   public Class getConfigureClass() {
      return ConfigureVigenereCipherActivity.class;
   }

   /**
    * @see sites.mjwhitta.crypticsms.ciphers.Cipher#getConfigureId()
    */
   @Override
   public int getConfigureId() {
      return 11;
   }

   /**
    * @return The key.
    */
   public String getKey() {
      return m_key;
   }

   /**
    * @see sites.mjwhitta.crypticsms.ciphers.Cipher#restoreFromFile(java.io.InputStreamReader)
    */
   @Override
   public void restoreFromFile(BufferedReader br) throws Exception {
      // Read member attributes
      m_key = "";

      int c = br.read();
      while (c != -1) {
         m_key += (char) c;
         c = br.read();
      }
   }

   /**
    * @see sites.mjwhitta.crypticsms.ciphers.Cipher#saveToFile(java.io.FileOutputStream)
    */
   @Override
   public void saveToFile(BufferedWriter bw) throws Exception {
      // Write member attributes
      for (char c : m_key.toCharArray()) {
         bw.write(c);
      }
   }

   /**
    * Sets the key.
    * 
    * @param key
    */
   public boolean setKey(String key) {
      for (char c : key.toCharArray()) {
         if (!ALPHANUMERIC.contains(Character.toLowerCase(c))) {
            return false;
         }
      }

      m_key = key;
      return true;
   }
}
