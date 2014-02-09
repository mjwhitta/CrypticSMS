/**
 * GNU GENERAL PUBLIC LICENSE See the file license.txt for copying conditions.
 * 
 * @author mjwhitta
 * @created Dec 17, 2011
 */
package sites.mjwhitta.crypticsms.ciphers;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.util.Arrays;
import java.util.List;

/**
 * The interface to be implemented by all ciphers.
 */
public abstract class Cipher {
   protected String m_name;

   protected String m_type;

   protected static final List<Character> ALPHABET = Arrays
         .asList(new Character[] { 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i',
               'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v',
               'w', 'x', 'y', 'z' });

   protected static final List<Character> NUMBERS = Arrays
         .asList(new Character[] { '1', '2', '3', '4', '5', '6', '7', '8', '9',
               '0' });

   protected static final List<Character> ALPHANUMERIC = Arrays
         .asList(new Character[] { 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i',
               'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v',
               'w', 'x', 'y', 'z', '1', '2', '3', '4', '5', '6', '7', '8', '9',
               '0' });

   /**
    * @param ciphertext
    * @return plaintext from ciphertext
    */
   public abstract String decrypt(String ciphertext) throws Exception;

   /**
    * @param plaintext
    * @return ciphertext from plaintext
    */
   public abstract String encrypt(String plaintext) throws Exception;

   /**
    * @return The activity for configuring the Cipher.
    */
   @SuppressWarnings("rawtypes")
   public abstract Class getConfigureClass();

   /**
    * @return the id for the configure activity
    */
   public abstract int getConfigureId();

   /**
    * @return The name of the Cipher.
    */
   public String getName() {
      return m_name;
   }

   /**
    * @return The type of Cipher.
    */
   public String getType() {
      return m_type;
   }

   /**
    * Restore configuration from file.
    * 
    * @param br
    * @throws Exception
    */
   public abstract void restoreFromFile(BufferedReader br) throws Exception;

   /**
    * Save configuration to file.
    * 
    * @param bw
    * @throws Exception
    */
   public abstract void saveToFile(BufferedWriter bw) throws Exception;

   /**
    * Set the name of the Cipher.
    * 
    * @param name
    */
   public void setName(String name) {
      m_name = name;
   }

   /**
    * @return The type of Cipher plus the name.
    */
   @Override
   public String toString() {
      return getType() + ":\n\t" + getName();
   }
}
