/**
 * GNU GENERAL PUBLIC LICENSE See the file license.txt for copying conditions.
 * 
 * @author mjwhitta
 * @created Feb 3, 2012
 */
package sites.mjwhitta.crypticsms.ciphers.hill;

import java.io.BufferedReader;
import java.io.BufferedWriter;

import sites.mjwhitta.crypticsms.ciphers.Cipher;

/**
 * A basic HillCipher.
 */
public class HillCipher extends Cipher {
   private class StringParser {
      private String m_text = "";

      private int m_index = 0;

      private String m_ignored = "";

      /**
       * Constructor
       * 
       * @param text
       */
      public StringParser(String text) {
         m_text = text;
      }

      /**
       * @return The part of the string that was skipped over while finding the
       *         next letter
       */
      public String getLastIgnored() {
         return m_ignored;
      }

      /**
       * @return The next character that is in the alphabet
       */
      public Character getNextLetter() {
         m_ignored = "";
         while ((m_index < m_text.length())
               && !ALPHANUMERIC.contains(Character.toLowerCase(m_text
                     .charAt(m_index)))) {
            m_ignored += m_text.charAt(m_index++);
         }

         if (m_index < m_text.length()) {
            return Character.valueOf(m_text.charAt(m_index++));
         } else {
            return null;
         }
      }
   }

   private int[][] m_key = { { 2, 1, 3 }, { 1, 1, 1 }, { 1, 1, 6 } };

   private int[][] m_inverseKey = { { 5, 33, 34 }, { 31, 9, 1 }, { 0, 35, 1 } };

   private int m_det = 5;

   /**
    * Constructor
    */
   public HillCipher() {
      this("");
   }

   /**
    * @param name
    */
   public HillCipher(String name) {
      m_name = name;
      m_type = "HillCipher";
   }

   /**
    * @see sites.mjwhitta.crypticsms.ciphers.Cipher#decrypt(java.lang.String)
    */
   @Override
   public String decrypt(String ciphertext) {
      String plaintext = "";

      StringParser sp = new StringParser(ciphertext);

      Character c1 = sp.getNextLetter();
      plaintext += sp.getLastIgnored();
      Character c2 = sp.getNextLetter();
      String gap1 = sp.getLastIgnored();
      Character c3 = sp.getNextLetter();
      String gap2 = sp.getLastIgnored();

      while ((c1 != null) && (c2 != null) && (c3 != null)) {
         // get ciphertext indices
         int ic1 = ALPHANUMERIC.indexOf(Character.toLowerCase(c1.charValue()));
         int ic2 = ALPHANUMERIC.indexOf(Character.toLowerCase(c2.charValue()));
         int ic3 = ALPHANUMERIC.indexOf(Character.toLowerCase(c3.charValue()));

         // calculate plaintext indices
         int ip1 = (m_inverseKey[0][0] * ic1) + (m_inverseKey[0][1] * ic2)
               + (m_inverseKey[0][2] * ic3);
         while (ip1 < 0) {
            ip1 += ALPHANUMERIC.size();
         }
         while ((ip1 % m_det) != 0) {
            ip1 += ALPHANUMERIC.size();
         }
         ip1 /= m_det;
         ip1 %= ALPHANUMERIC.size();
         char p1 = ALPHANUMERIC.get(ip1).charValue();

         int ip2 = (m_inverseKey[1][0] * ic1) + (m_inverseKey[1][1] * ic2)
               + (m_inverseKey[1][2] * ic3);
         while (ip2 < 0) {
            ip2 += ALPHANUMERIC.size();
         }
         while ((ip2 % m_det) != 0) {
            ip2 += ALPHANUMERIC.size();
         }
         ip2 /= m_det;
         ip2 %= ALPHANUMERIC.size();
         char p2 = ALPHANUMERIC.get(ip2).charValue();

         int ip3 = (m_inverseKey[2][0] * ic1) + (m_inverseKey[2][1] * ic2)
               + (m_inverseKey[2][2] * ic3);
         while (ip3 < 0) {
            ip3 += ALPHANUMERIC.size();
         }
         while ((ip3 % m_det) != 0) {
            ip3 += ALPHANUMERIC.size();
         }
         ip3 /= m_det;
         ip3 %= ALPHANUMERIC.size();
         char p3 = ALPHANUMERIC.get(ip3).charValue();

         // capitalize if necessary
         if (Character.isUpperCase(c1.charValue())) {
            p1 = Character.toUpperCase(p1);
         }
         if (Character.isUpperCase(c2.charValue())) {
            p2 = Character.toUpperCase(p2);
         }
         if (Character.isUpperCase(c3.charValue())) {
            p3 = Character.toUpperCase(p3);
         }

         plaintext += p1 + gap1 + p2 + gap2 + p3;

         c1 = sp.getNextLetter();
         plaintext += sp.getLastIgnored();
         c2 = sp.getNextLetter();
         gap1 = sp.getLastIgnored();
         c3 = sp.getNextLetter();
         gap2 = sp.getLastIgnored();
      }

      if (c1 != null) {
         plaintext += c1.charValue() + gap1;
      }
      if (c2 != null) {
         plaintext += c2.charValue() + gap2;
      }

      return plaintext;
   }

   /**
    * @see sites.mjwhitta.crypticsms.ciphers.Cipher#encrypt(java.lang.String)
    */
   @Override
   public String encrypt(String plaintext) {
      String ciphertext = "";

      StringParser sp = new StringParser(plaintext);

      Character p1 = sp.getNextLetter();
      ciphertext += sp.getLastIgnored();
      Character p2 = sp.getNextLetter();
      String gap1 = sp.getLastIgnored();
      Character p3 = sp.getNextLetter();
      String gap2 = sp.getLastIgnored();

      while ((p1 != null) && (p2 != null) && (p3 != null)) {
         // get plaintext indices
         int ip1 = ALPHANUMERIC.indexOf(Character.toLowerCase(p1.charValue()));
         int ip2 = ALPHANUMERIC.indexOf(Character.toLowerCase(p2.charValue()));
         int ip3 = ALPHANUMERIC.indexOf(Character.toLowerCase(p3.charValue()));

         // calculate new ciphertext indices
         int ic1 = (m_key[0][0] * ip1) + (m_key[0][1] * ip2)
               + (m_key[0][2] * ip3);
         while (ic1 < 0) {
            ic1 += ALPHANUMERIC.size();
         }
         ic1 %= ALPHANUMERIC.size();
         char c1 = ALPHANUMERIC.get(ic1).charValue();

         int ic2 = (m_key[1][0] * ip1) + (m_key[1][1] * ip2)
               + (m_key[1][2] * ip3);
         while (ic2 < 0) {
            ic2 += ALPHANUMERIC.size();
         }
         ic2 %= ALPHANUMERIC.size();
         char c2 = ALPHANUMERIC.get(ic2).charValue();

         int ic3 = (m_key[2][0] * ip1) + (m_key[2][1] * ip2)
               + (m_key[2][2] * ip3);
         while (ic3 < 0) {
            ic3 += ALPHANUMERIC.size();
         }
         ic3 %= ALPHANUMERIC.size();
         char c3 = ALPHANUMERIC.get(ic3).charValue();

         // capitalize if necessary
         if (Character.isUpperCase(p1.charValue())) {
            c1 = Character.toUpperCase(c1);
         }
         if (Character.isUpperCase(p2.charValue())) {
            c2 = Character.toUpperCase(c2);
         }
         if (Character.isUpperCase(p3.charValue())) {
            c3 = Character.toUpperCase(c3);
         }

         ciphertext += c1 + gap1 + c2 + gap2 + c3;

         p1 = sp.getNextLetter();
         ciphertext += sp.getLastIgnored();
         p2 = sp.getNextLetter();
         gap1 = sp.getLastIgnored();
         p3 = sp.getNextLetter();
         gap2 = sp.getLastIgnored();
      }

      if (p1 != null) {
         ciphertext += p1.charValue() + gap1;
      }
      if (p2 != null) {
         ciphertext += p2.charValue() + gap2;
      }

      return ciphertext;
   }

   /**
    * @see sites.mjwhitta.crypticsms.ciphers.Cipher#getConfigureClass()
    */
   @SuppressWarnings("rawtypes")
   @Override
   public Class getConfigureClass() {
      return ConfigureHillCipherActivity.class;
   }

   /**
    * @see sites.mjwhitta.crypticsms.ciphers.Cipher#getConfigureId()
    */
   @Override
   public int getConfigureId() {
      return 5;
   }

   /**
    * @return The key.
    */
   public int[][] getKey() {
      return m_key;
   }

   private boolean hasCommonFactors(int a, int b) {
      a = Math.abs(a);
      b = Math.abs(b);

      while ((a != 1) && (b != 1)) {
         if (b == 0) {
            return true;
         }
         int temp = a;
         a = b;
         b = temp % b;
      }

      return false;
   }

   /**
    * @see sites.mjwhitta.crypticsms.ciphers.Cipher#restoreFromFile(java.io.InputStreamReader)
    */
   @Override
   public void restoreFromFile(BufferedReader br) throws Exception {
      // Read member attributes
      String val = "";
      int c = br.read();
      while (c != -1) {
         val += (char) c;
         c = br.read();
      }

      String[] vals = val.split("\n");

      int[][] key = new int[3][3];
      for (int i = 0, k = 0; i < 3; ++i) {
         for (int j = 0; j < 3; ++j, ++k) {
            key[i][j] = Integer.valueOf(vals[k]);
         }
      }

      setKey(key);
   }

   /**
    * @see sites.mjwhitta.crypticsms.ciphers.Cipher#saveToFile(java.io.FileOutputStream)
    */
   @Override
   public void saveToFile(BufferedWriter bw) throws Exception {
      // Write member attributes
      for (int i = 0; i < 3; ++i) {
         for (int j = 0; j < 3; ++j) {
            bw.write(String.valueOf(m_key[i][j]));
            bw.write('\n');
         }
      }
   }

   /**
    * Sets the matrix key for encryption
    * 
    * @param key
    * @return Whether or not the key is valid
    */
   public boolean setKey(int[][] key) {
      // make sure they are all positive
      for (int i = 0; i < 3; ++i) {
         for (int j = 0; j < 3; ++j) {
            while (key[i][j] < 0) {
               key[i][j] += ALPHANUMERIC.size();
            }
            key[i][j] %= ALPHANUMERIC.size();
         }
      }

      // calculate inverse for decryption
      int[][] inverseKey = new int[3][3];
      inverseKey[0][0] = ((key[1][1] * key[2][2]) - (key[2][1] * key[1][2]));
      inverseKey[0][1] = ((key[2][1] * key[0][2]) - (key[0][1] * key[2][2]));
      inverseKey[0][2] = ((key[0][1] * key[1][2]) - (key[1][1] * key[0][2]));
      inverseKey[1][0] = ((key[2][0] * key[1][2]) - (key[1][0] * key[2][2]));
      inverseKey[1][1] = ((key[0][0] * key[2][2]) - (key[2][0] * key[0][2]));
      inverseKey[1][2] = ((key[1][0] * key[0][2]) - (key[0][0] * key[1][2]));
      inverseKey[2][0] = ((key[1][0] * key[2][1]) - (key[2][0] * key[1][1]));
      inverseKey[2][1] = ((key[2][0] * key[0][1]) - (key[0][0] * key[2][1]));
      inverseKey[2][2] = ((key[0][0] * key[1][1]) - (key[1][0] * key[0][1]));

      // make sure they are all positive
      for (int i = 0; i < 3; ++i) {
         for (int j = 0; j < 3; ++j) {
            while (inverseKey[i][j] < 0) {
               inverseKey[i][j] += ALPHANUMERIC.size();
            }
            inverseKey[i][j] %= ALPHANUMERIC.size();
         }
      }

      // calculate the determinate
      int det = (key[0][0] * inverseKey[0][0]) + (key[0][1] * inverseKey[1][0])
            + (key[0][2] * inverseKey[2][0]);

      // make sure it is positive
      while (det < 0) {
         det += ALPHANUMERIC.size();
      }
      det %= ALPHANUMERIC.size();

      // return false if 0 or if it has any common factors with the alphabet
      // size
      if ((det == 0) || hasCommonFactors(ALPHANUMERIC.size(), det)) {
         return false;
      } else {
         m_key = key;
         m_inverseKey = inverseKey;
         m_det = det;
         return true;
      }
   }
}
