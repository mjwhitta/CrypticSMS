/**
 * GNU GENERAL PUBLIC LICENSE See the file license.txt for copying conditions.
 * 
 * @author mjwhitta
 * @created Dec 17, 2011
 */
package sites.mjwhitta.crypticsms.ciphers.playfair;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.util.ArrayList;
import java.util.List;

import sites.mjwhitta.crypticsms.ciphers.Cipher;

/**
 * A basic PlayfairCipher.
 */
public class PlayfairCipher extends Cipher {
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
               && !ALPHABET.contains(Character.toLowerCase(m_text
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

   private String m_key = "";

   private final char[][] m_keyMatrix = new char[5][5];

   /**
    * Constructor
    */
   public PlayfairCipher() {
      this("");
   }

   /**
    * @param name
    */
   public PlayfairCipher(String name) {
      m_name = name;
      m_type = "PlayfairCipher";
   }

   /**
    * @see sites.mjwhitta.crypticsms.ciphers.Cipher#decrypt(java.lang.String)
    */
   @Override
   public String decrypt(String ciphertext) throws Exception {
      if (m_keyMatrix == null) {
         throw new Exception("There was an error. Key was null. Please "
               + "delete the cipher and try again.");
      }

      String plaintext = "";

      StringParser sp = new StringParser(ciphertext);

      Character first = sp.getNextLetter();
      while (first != null) {
         plaintext += sp.getLastIgnored();
         Character second = sp.getNextLetter();

         char[] crypted = getSubstitution(first, second, -1);

         plaintext += crypted[0];
         plaintext += sp.getLastIgnored();
         plaintext += crypted[1];

         first = sp.getNextLetter();
      }

      return removeFillers(plaintext);
   }

   /**
    * @see sites.mjwhitta.crypticsms.ciphers.Cipher#encrypt(java.lang.String)
    */
   @Override
   public String encrypt(String plaintext) throws Exception {
      if (m_keyMatrix == null) {
         throw new Exception("There was an error. Key was null. Please "
               + "delete the cipher and try again.");
      }

      String ciphertext = "";
      plaintext.replace('j', 'i');

      StringParser sp = new StringParser(plaintext);

      Character first = sp.getNextLetter();
      while (first != null) {
         ciphertext += sp.getLastIgnored();
         Character second = sp.getNextLetter();

         while (first == second) {
            char[] crypted = getSubstitution(first, Character.valueOf('q'), 1);

            ciphertext += crypted[0];
            ciphertext += crypted[1];
            ciphertext += sp.getLastIgnored();

            first = second;
            second = sp.getNextLetter();
         }

         if (second == null) {
            second = Character.valueOf('q');
         }

         char[] crypted = getSubstitution(first, second, 1);

         ciphertext += crypted[0];
         ciphertext += sp.getLastIgnored();
         ciphertext += crypted[1];

         first = sp.getNextLetter();
      }

      return ciphertext;
   }

   /**
    * @see sites.mjwhitta.crypticsms.ciphers.Cipher#getConfigureClass()
    */
   @SuppressWarnings("rawtypes")
   @Override
   public Class getConfigureClass() {
      return ConfigurePlayfairCipherActivity.class;
   }

   /**
    * @see sites.mjwhitta.crypticsms.ciphers.Cipher#getConfigureId()
    */
   @Override
   public int getConfigureId() {
      return 8;
   }

   /**
    * @return The key.
    */
   public String getKey() {
      return m_key;
   }

   private int[] getRowCol(Character c) {
      int[] rowCol = new int[2];
      rowCol[0] = 0;
      rowCol[1] = 0;

      for (int i = 0; i < 5; ++i) {
         for (int j = 0; j < 5; ++j) {
            if (m_keyMatrix[i][j] == Character.toLowerCase(c.charValue())) {
               rowCol[0] = i;
               rowCol[1] = j;

               return rowCol;
            }
         }
      }

      return rowCol;
   }

   private char[] getSubstitution(Character first, Character second, int shift) {
      char[] toRet = new char[2];
      int[] firstRowCol = new int[2];
      int[] secondRowCol = new int[2];

      firstRowCol = getRowCol(first);
      secondRowCol = getRowCol(second);

      if (firstRowCol[0] == secondRowCol[0]) {
         int i = firstRowCol[0];
         int j = (firstRowCol[1] + 5 + shift) % 5;
         toRet[0] = m_keyMatrix[i][j];

         j = (secondRowCol[1] + 5 + shift) % 5;
         toRet[1] = m_keyMatrix[i][j];
      } else if (firstRowCol[1] == secondRowCol[1]) {
         int i = (firstRowCol[0] + 5 + shift) % 5;
         int j = firstRowCol[1];
         toRet[0] = m_keyMatrix[i][j];

         i = (secondRowCol[0] + 5 + shift) % 5;
         toRet[1] = m_keyMatrix[i][j];
      } else {
         int i = firstRowCol[0];
         int j = secondRowCol[1];
         toRet[0] = m_keyMatrix[i][j];

         i = secondRowCol[0];
         j = firstRowCol[1];
         toRet[1] = m_keyMatrix[i][j];
      }

      if (Character.isUpperCase(first.charValue())) {
         toRet[0] = Character.toUpperCase(toRet[0]);
      }
      if (Character.isUpperCase(second.charValue())) {
         toRet[1] = Character.toUpperCase(toRet[1]);
      }

      return toRet;
   }

   private String removeFillers(String text) {
      String result = "";

      StringParser sp = new StringParser(text);

      Character first = sp.getNextLetter();
      result += sp.getLastIgnored();
      while (first != null) {
         result += first.charValue();

         Character second = sp.getNextLetter();
         result += sp.getLastIgnored();

         Character next = sp.getNextLetter();
         String ignored = sp.getLastIgnored();

         if ((second == Character.valueOf('q'))
               && ((next == first) || (next == null))) {
            result += ignored;
         } else {
            result += second.charValue() + ignored;
         }

         first = next;
      }
      return result;
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

      setKey(m_key);
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
         if (!ALPHABET.contains(Character.toLowerCase(c))) {
            return false;
         }
      }

      m_key = key;

      List<Character> tempKey = new ArrayList<Character>();
      for (char c : m_key.toCharArray()) {
         c = Character.toLowerCase(c);
         if ((c == 'j') && !tempKey.contains(Character.valueOf('i'))) {
            tempKey.add(Character.valueOf('i'));
         } else if ((c != 'j') && !tempKey.contains(Character.valueOf(c))) {
            tempKey.add(Character.valueOf(c));
         }
      }

      for (Character c : ALPHABET) {
         if ((c.charValue() == 'j')
               && !tempKey.contains(Character.valueOf('i'))) {
            tempKey.add(Character.valueOf('i'));
         } else if ((c.charValue() != 'j') && !tempKey.contains(c)) {
            tempKey.add(c);
         }
      }

      for (int i = 0; i < 5; ++i) {
         for (int j = 0; j < 5; ++j) {
            m_keyMatrix[i][j] = tempKey.get((5 * i) + j);
         }
      }

      return true;
   }
}
