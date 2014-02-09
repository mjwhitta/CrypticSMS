/**
 * GNU GENERAL PUBLIC LICENSE See the file license.txt for copying conditions.
 * 
 * @author mjwhitta
 * @created Dec 17, 2011
 */
package sites.mjwhitta.crypticsms.ciphers.caesar;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.util.List;

import sites.mjwhitta.crypticsms.ciphers.Cipher;

/**
 * A basic CaesarCipher.
 */
public class CaesarCipher extends Cipher {
   private int m_rotation = 0;

   /**
    * Constructor
    */
   public CaesarCipher() {
      this("");
   }

   /**
    * @param name
    */
   public CaesarCipher(String name) {
      m_name = name;
      m_type = "CaesarCipher";
   }

   /**
    * @see sites.mjwhitta.crypticsms.ciphers.Cipher#decrypt(java.lang.String)
    */
   @Override
   public String decrypt(String ciphertext) {
      String plaintext = "";

      for (char c : ciphertext.toCharArray()) {
         if (ALPHANUMERIC.contains(Character.toLowerCase(c))) {
            // have to add m_alphaNumeric.size() first b/c JVM doesn't
            // handle
            // negative mod
            int index = (ALPHANUMERIC.size() + (ALPHANUMERIC.indexOf(Character
                  .toLowerCase(c)) - m_rotation)) % ALPHANUMERIC.size();

            plaintext += ALPHANUMERIC.get(index);
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
   public String encrypt(String plaintext) {
      String ciphertext = "";

      for (char c : plaintext.toCharArray()) {
         if (ALPHANUMERIC.contains(Character.toLowerCase(c))) {
            int index = (m_rotation + ALPHANUMERIC.indexOf(Character
                  .toLowerCase(c))) % ALPHANUMERIC.size();

            ciphertext += ALPHANUMERIC.get(index);
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
      return ConfigureCaesarCipherActivity.class;
   }

   /**
    * @see sites.mjwhitta.crypticsms.ciphers.Cipher#getConfigureId()
    */
   @Override
   public int getConfigureId() {
      return 3;
   }

   protected List<Character> getPossibleRotations() {
      return ALPHANUMERIC;
   }

   /**
    * @return The amount to rotate by.
    */
   public int getRotation() {
      return m_rotation;
   }

   /**
    * @see sites.mjwhitta.crypticsms.ciphers.Cipher#restoreFromFile(java.io.InputStreamReader)
    */
   @Override
   public void restoreFromFile(BufferedReader br) throws Exception {
      // Read member attributes
      m_rotation = br.read();
   }

   /**
    * @see sites.mjwhitta.crypticsms.ciphers.Cipher#saveToFile(java.io.FileOutputStream)
    */
   @Override
   public void saveToFile(BufferedWriter bw) throws Exception {
      // Write member attributes
      bw.write(m_rotation);
   }

   /**
    * Sets the amount to rotate by.
    * 
    * @param r
    */
   public void setRotation(int r) {
      m_rotation = r;
   }
}
