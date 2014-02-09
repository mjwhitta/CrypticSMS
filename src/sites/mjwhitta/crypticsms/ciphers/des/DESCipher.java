/**
 * GNU GENERAL PUBLIC LICENSE See the file license.txt for copying conditions.
 * 
 * @author mjwhitta
 * @created Feb 11, 2012
 */
package sites.mjwhitta.crypticsms.ciphers.des;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.security.spec.KeySpec;

import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

import sites.mjwhitta.crypticsms.ciphers.Cipher;
import android.util.Base64;

/**
 * A basic VigenereCipher.
 */
public class DESCipher extends Cipher {
   private javax.crypto.Cipher m_cipher = null;

   private SecretKey m_key;

   private String m_keyString = "";

   private final byte[] m_salt = { (byte) 0xDE, (byte) 0xAD, (byte) 0xBE,
         (byte) 0xEF, (byte) 0xAB, (byte) 0xAD, (byte) 0x1D, (byte) 0xEA };

   /**
    * Constructor
    */
   public DESCipher() {
      this("");
   }

   /**
    * @param name
    */
   public DESCipher(String name) {
      m_name = name;
      m_type = "DESCipher";
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
         m_cipher.init(javax.crypto.Cipher.ENCRYPT_MODE, m_key);
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
      return ConfigureDESCipherActivity.class;
   }

   /**
    * @see sites.mjwhitta.crypticsms.ciphers.Cipher#getConfigureId()
    */
   @Override
   public int getConfigureId() {
      return 4;
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
   public void setKey(String key) {
      m_keyString = key;

      int iterationCount = 2;
      KeySpec keySpec = new PBEKeySpec(m_keyString.toCharArray(), m_salt,
            iterationCount);
      try {
         m_key = SecretKeyFactory.getInstance("PBEWithMD5AndDES")
               .generateSecret(keySpec);
         m_cipher = javax.crypto.Cipher.getInstance(m_key.getAlgorithm());
      } catch (Exception e) {
         // do nothing
      }
   }
}
