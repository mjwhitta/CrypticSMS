/**
 * GNU GENERAL PUBLIC LICENSE See the file license.txt for copying conditions.
 * 
 * @author mjwhitta
 * @created Feb 11, 2012
 */
package sites.mjwhitta.crypticsms.ciphers.triple.des;

import java.io.BufferedReader;
import java.io.BufferedWriter;

import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESedeKeySpec;
import javax.crypto.spec.IvParameterSpec;

import sites.mjwhitta.crypticsms.ciphers.Cipher;
import android.util.Base64;

/**
 * A basic VigenereCipher.
 */
public class TripleDESCipher extends Cipher {
   private javax.crypto.Cipher m_cipher = null;

   private SecretKey m_key;

   private String m_keyString = "";

   /**
    * Constructor
    */
   public TripleDESCipher() {
      this("");
   }

   /**
    * @param name
    */
   public TripleDESCipher(String name) {
      m_name = name;
      m_type = "TripleDESCipher";
   }

   /**
    * @see sites.mjwhitta.crypticsms.ciphers.Cipher#decrypt(java.lang.String)
    */
   @Override
   public String decrypt(String ciphertext) {
      try {
         IvParameterSpec iv = new IvParameterSpec(new byte[8]);
         m_cipher.init(javax.crypto.Cipher.DECRYPT_MODE, m_key, iv);
         byte[] text = Base64.decode(ciphertext, Base64.DEFAULT);
         byte[] textDecrypted = m_cipher.doFinal(text);
         return new String(textDecrypted);
      } catch (Exception e) {
         // do nothing
      }

      return "";
   }

   /**
    * @see sites.mjwhitta.crypticsms.ciphers.Cipher#encrypt(java.lang.String)
    */
   @Override
   public String encrypt(String plaintext) {
      try {
         IvParameterSpec iv = new IvParameterSpec(new byte[8]);
         m_cipher.init(javax.crypto.Cipher.ENCRYPT_MODE, m_key, iv);
         byte[] text = plaintext.getBytes();
         byte[] textEncrypted = m_cipher.doFinal(text);
         return Base64.encodeToString(textEncrypted, Base64.DEFAULT);
      } catch (Exception e) {
         // do nothing
      }

      return "";
   }

   /**
    * @see sites.mjwhitta.crypticsms.ciphers.Cipher#getConfigureClass()
    */
   @SuppressWarnings("rawtypes")
   @Override
   public Class getConfigureClass() {
      return ConfigureTripleDESCipherActivity.class;
   }

   /**
    * @see sites.mjwhitta.crypticsms.ciphers.Cipher#getConfigureId()
    */
   @Override
   public int getConfigureId() {
      return 10;
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
      m_keyString = key;

      try {
         DESedeKeySpec keySpec = new DESedeKeySpec(m_keyString.getBytes());
         m_key = SecretKeyFactory.getInstance("DESede").generateSecret(keySpec);
         m_cipher = javax.crypto.Cipher.getInstance(m_key.getAlgorithm());
      } catch (Exception e) {
         e.printStackTrace();
         return false;
      }

      return true;
   }
}
