/**
 * GNU GENERAL PUBLIC LICENSE See the file license.txt for copying conditions.
 * 
 * @author mjwhitta
 * @created Dec 17, 2011
 */
package sites.mjwhitta.crypticsms.ciphers.monoalphabetic;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import sites.mjwhitta.crypticsms.ciphers.Cipher;

/**
 * A Monoalphabetic Cipher
 */
public class MonoalphabeticCipher extends Cipher {
   private final List<Character> m_alphabetString = new ArrayList<Character>(
         Arrays.asList(new Character[] { 'a', 'b', 'c', 'd', 'e', 'f', 'g',
               'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't',
               'u', 'v', 'w', 'x', 'y', 'z', '1', '2', '3', '4', '5', '6', '7',
               '8', '9', '0' }));

   /**
    * Constructor
    */
   public MonoalphabeticCipher() {
      this("");
   }

   /**
    * @param name
    */
   public MonoalphabeticCipher(String name) {
      m_name = name;
      m_type = "MonoalphabeticCipher";
   }

   /**
    * @see sites.mjwhitta.crypticsms.ciphers.Cipher#decrypt(java.lang.String)
    */
   @Override
   public String decrypt(String ciphertext) {
      String plaintext = "";

      for (char c : ciphertext.toCharArray()) {
         if (m_alphabetString.contains(c)) {
            plaintext += ALPHANUMERIC.get(m_alphabetString.indexOf(c));
         } else if (m_alphabetString.contains(Character.toLowerCase(c))) {
            plaintext += Character.toUpperCase(ALPHANUMERIC
                  .get(m_alphabetString.indexOf(Character.toLowerCase(c))));
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
         if (ALPHANUMERIC.contains(c)) {
            ciphertext += m_alphabetString.get(ALPHANUMERIC.indexOf(Character
                  .valueOf(c)));
         } else if (ALPHANUMERIC.contains(Character.toLowerCase(c))) {
            ciphertext += Character.toUpperCase(m_alphabetString
                  .get(ALPHANUMERIC.indexOf(Character.toLowerCase(c))));
         } else {
            ciphertext += c;
         }
      }

      return ciphertext;
   }

   /**
    * @return The alphabet string for this Cipher.
    */
   public List<Character> getAlphabetString() {
      return m_alphabetString;
   }

   /**
    * @see sites.mjwhitta.crypticsms.ciphers.Cipher#getConfigureClass()
    */
   @SuppressWarnings("rawtypes")
   @Override
   public Class getConfigureClass() {
      return ConfigureMonoalphabeticCipherActivity.class;
   }

   /**
    * @see sites.mjwhitta.crypticsms.ciphers.Cipher#getConfigureId()
    */
   @Override
   public int getConfigureId() {
      return 6;
   }

   /**
    * @see sites.mjwhitta.crypticsms.ciphers.Cipher#restoreFromFile(java.io.InputStreamReader)
    */
   @Override
   public void restoreFromFile(BufferedReader br) throws Exception {
      // Read member attributes
      m_alphabetString.clear();

      int c = br.read();
      while (c != -1) {
         m_alphabetString.add(Character.valueOf((char) c));
         c = br.read();
      }
   }

   /**
    * @see sites.mjwhitta.crypticsms.ciphers.Cipher#saveToFile(java.io.FileOutputStream)
    */
   @Override
   public void saveToFile(BufferedWriter bw) throws Exception {
      // Write member attributes
      for (Character c : m_alphabetString) {
         bw.write(c.charValue());
      }
   }

   /**
    * Sets the new alphabet string for this Cipher.
    * 
    * @param alphabet
    */
   public void setAlphabetString(String alphabet) {
      m_alphabetString.clear();

      for (char c : alphabet.toCharArray()) {
         m_alphabetString.add(Character.valueOf(c));
      }
   }
}
