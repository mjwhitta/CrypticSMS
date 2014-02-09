/**
 * GNU GENERAL PUBLIC LICENSE See the file license.txt for copying conditions.
 * 
 * @author mjwhitta
 * @created Aug 10, 2012
 */
package sites.mjwhitta.crypticsms.ciphers.aes;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.security.NoSuchAlgorithmException;

import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import sites.mjwhitta.crypticsms.ciphers.Cipher;
import android.util.Base64;

/**
 * A basic VigenereCipher.
 */
public class AESCipher extends Cipher {
   private javax.crypto.Cipher m_cipher = null;

   private SecretKey m_key;

   private String m_keyString = "";

   /**
    * Constructor
    */
   public AESCipher() {
      this("");
   }

   /**
    * @param name
    */
   public AESCipher(String name) {
      m_name = name;
      m_type = "AESCipher";
      try {
         m_cipher = javax.crypto.Cipher.getInstance("AES");
      } catch (NoSuchAlgorithmException e) {
         e.printStackTrace();
      } catch (NoSuchPaddingException e) {
         e.printStackTrace();
      }
   }

   /**
    * @see sites.mjwhitta.crypticsms.ciphers.Cipher#decrypt(java.lang.String)
    */
   @Override
   public String decrypt(String ciphertext) {
      try {
         m_cipher.init(javax.crypto.Cipher.DECRYPT_MODE, m_key);
         byte[] text = Base64.decode(ciphertext, Base64.DEFAULT);
         byte[] textDecrypted = m_cipher.doFinal(text);
         return new String(textDecrypted);
      } catch (Exception e) {
         e.printStackTrace();
      }

      return "";
   }

   /**
    * @see sites.mjwhitta.crypticsms.ciphers.Cipher#encrypt(java.lang.String)
    */
   @Override
   public String encrypt(String plaintext) {
      try {
         m_cipher.init(javax.crypto.Cipher.ENCRYPT_MODE, m_key);
         byte[] text = plaintext.getBytes();
         byte[] textEncrypted = m_cipher.doFinal(text);
         return Base64.encodeToString(textEncrypted, Base64.DEFAULT);
      } catch (Exception e) {
         e.printStackTrace();
      }

      return "";
   }

   /**
    * @see sites.mjwhitta.crypticsms.ciphers.Cipher#getConfigureClass()
    */
   @SuppressWarnings("rawtypes")
   @Override
   public Class getConfigureClass() {
      return ConfigureAESCipherActivity.class;
   }

   /**
    * @see sites.mjwhitta.crypticsms.ciphers.Cipher#getConfigureId()
    */
   @Override
   public int getConfigureId() {
      return 1;
   }

   /**
    * @return m_keyString
    */
   public String getKey() {
      return m_keyString;
   }

   /**
    * @see sites.mjwhitta.crypticsms.ciphers.Cipher#restoreFromFile(java.io.InputStreamReader)
    */
   @Override
   public void restoreFromFile(BufferedReader br) throws Exception {
      // Read member attributes
      String key = "";

      int c = br.read();
      while (c != -1) {
         key += (char) c;
         c = br.read();
      }

      setKey(key);
   }

   /**
    * @see sites.mjwhitta.crypticsms.ciphers.Cipher#saveToFile(java.io.FileOutputStream)
    */
   @Override
   public void saveToFile(BufferedWriter bw) throws Exception {
      // Write member attributes
      for (char c : m_keyString.toCharArray()) {
         bw.write(c);
      }
   }

   /**
    * Sets the key
    * 
    * @param key
    */
   public boolean setKey(String key) {
      // Very basic key padding
      while (key.length() < 16) {
         key += " ";
      }
      while ((key.length() > 16) && (key.length() < 24)) {
         key += " ";
      }
      while ((key.length() > 24) && (key.length() < 32)) {
         key += " ";
      }
      if (key.length() > 32) {
         return false;
      }

      m_keyString = key;
      m_key = new SecretKeySpec(m_keyString.getBytes(), "AES");
      return true;
   }
}
