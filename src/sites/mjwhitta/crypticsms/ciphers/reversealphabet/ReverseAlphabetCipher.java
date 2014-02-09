/**
 * GNU GENERAL PUBLIC LICENSE See the file license.txt for copying conditions.
 * 
 * @author mjwhitta
 * @created Dec 17, 2011
 */
package sites.mjwhitta.crypticsms.ciphers.reversealphabet;

import java.io.BufferedReader;
import java.io.BufferedWriter;

import sites.mjwhitta.crypticsms.ciphers.Cipher;

/**
 * A basic cipher based on reversing the alphabet.
 */
public class ReverseAlphabetCipher extends Cipher {
   /**
    * Constructor
    */
   public ReverseAlphabetCipher() {
      this("");
   }

   /**
    * @param name
    */
   public ReverseAlphabetCipher(String name) {
      m_name = name;
      m_type = "ReverseAlphabet";
   }

   /**
    * @see sites.mjwhitta.crypticsms.ciphers.Cipher#decrypt(java.lang.String)
    */
   @Override
   public String decrypt(String ciphertext) {
      String plaintext = "";

      for (char c : ciphertext.toCharArray()) {
         if (ALPHABET.contains(c)) {
            int index = (ALPHABET.size() - 1) - ALPHABET.indexOf(c);
            plaintext += ALPHABET.get(index);
         } else if (ALPHABET.contains(Character.toLowerCase(c))) {
            int index = (ALPHABET.size() - 1)
                  - ALPHABET.indexOf(Character.toLowerCase(c));
            plaintext += Character.toUpperCase(ALPHABET.get(index));
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
         if (ALPHABET.contains(c)) {
            int index = (ALPHABET.size() - 1) - ALPHABET.indexOf(c);
            ciphertext += ALPHABET.get(index);
         } else if (ALPHABET.contains(Character.toLowerCase(c))) {
            int index = (ALPHABET.size() - 1)
                  - ALPHABET.indexOf(Character.toLowerCase(c));
            ciphertext += Character.toUpperCase(ALPHABET.get(index));
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
      return ConfigureReverseAlphabetCipherActivity.class;
   }

   /**
    * @see sites.mjwhitta.crypticsms.ciphers.Cipher#getConfigureId()
    */
   @Override
   public int getConfigureId() {
      return 9;
   }

   /**
    * @see sites.mjwhitta.crypticsms.ciphers.Cipher#restoreFromFile(java.io.InputStreamReader)
    */
   @Override
   public void restoreFromFile(BufferedReader br) throws Exception {
      // Read member attributes
   }

   /**
    * @see sites.mjwhitta.crypticsms.ciphers.Cipher#saveToFile(java.io.FileOutputStream)
    */
   @Override
   public void saveToFile(BufferedWriter bw) throws Exception {
      // Write member attributes
   }
}
