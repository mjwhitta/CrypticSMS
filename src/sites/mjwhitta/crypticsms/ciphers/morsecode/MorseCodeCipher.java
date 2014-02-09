/**
 * GNU GENERAL PUBLIC LICENSE See the file license.txt for copying conditions.
 * 
 * @author mjwhitta
 * @created Dec 17, 2011
 */
package sites.mjwhitta.crypticsms.ciphers.morsecode;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.util.Arrays;
import java.util.List;

import sites.mjwhitta.crypticsms.ciphers.Cipher;

/**
 * A basic MorseCodeCipher.
 */
public class MorseCodeCipher extends Cipher {
   private static final String LETTER_SPACE = "  ";

   private static final String WORD_SPACE = "   ";

   private static final List<String> MORSE_CODE_ALPHABET = Arrays
         .asList(new String[] { ". _", "_ . . .", "_ . _ .", "_ . .", ".",
               ". . _ .", "_ _ .", ". . . .", ". .", ". _ _ _", "_ . _",
               ". _ . .", "_ _", "_ .", "_ _ _", ". _ _ .", "_ _ . _", ". _ .",
               ". . .", "_", ". . _", ". . . _", ". _ _", "_ . . _", "_ . _ _",
               "_ _ . ." });

   private static final List<String> MORSE_CODE_NUMBERS = Arrays
         .asList(new String[] { ". _ _ _ _", ". . _ _ _", ". . . _ _",
               ". . . . _", ". . . . .", "_ . . . .", "_ _ . . .", "_ _ _ . .",
               "_ _ _ _ .", "_ _ _ _ _" });

   /**
    * Constructor
    */
   public MorseCodeCipher() {
      this("");
   }

   /**
    * @param name
    */
   public MorseCodeCipher(String name) {
      m_name = name;
      m_type = "MorseCodeCipher";
   }

   /**
    * @see sites.mjwhitta.crypticsms.ciphers.Cipher#decrypt(java.lang.String)
    */
   @Override
   public String decrypt(String ciphertext) {
      String plaintext = "";

      for (String word : ciphertext.split(WORD_SPACE)) {
         for (String letter : word.split(LETTER_SPACE)) {
            if (MORSE_CODE_ALPHABET.contains(letter)) {
               plaintext += ALPHABET.get(getMorseCodeIndex(letter,
                     MORSE_CODE_ALPHABET));
            } else if (MORSE_CODE_NUMBERS.contains(letter)) {
               plaintext += NUMBERS.get(getMorseCodeIndex(letter,
                     MORSE_CODE_NUMBERS));
            } else if (letter.equals("STOP")) {
               plaintext += ".";
            } else {
               plaintext += letter;
            }
         }

         plaintext += " ";
      }

      return plaintext.substring(0, plaintext.length() - 1);
   }

   /**
    * @see sites.mjwhitta.crypticsms.ciphers.Cipher#encrypt(java.lang.String)
    */
   @Override
   public String encrypt(String plaintext) {
      String ciphertext = "";

      for (char c : plaintext.toCharArray()) {
         if (ALPHABET.contains(Character.toLowerCase(c))) {
            ciphertext += MORSE_CODE_ALPHABET.get(getLetterIndex(
                  Character.toLowerCase(c), ALPHABET))
                  + LETTER_SPACE;
         } else if (NUMBERS.contains(c)) {
            ciphertext += MORSE_CODE_NUMBERS.get(getLetterIndex(c, NUMBERS))
                  + LETTER_SPACE;
         } else if (c == ' ') {
            ciphertext += WORD_SPACE;
         } else if (c == '.') {
            ciphertext += "STOP" + LETTER_SPACE;
         } else {
            ciphertext += c + LETTER_SPACE;
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
      return ConfigureMorseCodeCipherActivity.class;
   }

   /**
    * @see sites.mjwhitta.crypticsms.ciphers.Cipher#getConfigureId()
    */
   @Override
   public int getConfigureId() {
      return 7;
   }

   private int getLetterIndex(Character c, List<Character> list) {
      if (list.contains(c)) {
         int index = 0;
         for (Character character : list) {
            if (c == character) {
               return index;
            }
            ++index;
         }
      }

      // Should never happen since list.contains(c) is called earlier.
      return -1;
   }

   private int getMorseCodeIndex(String c, List<String> list) {
      if (list.contains(c)) {
         int index = 0;
         for (String s : list) {
            if (c.equals(s)) {
               return index;
            }
            ++index;
         }
      }

      // Should never happen since list.contains(c) is called earlier.
      return -1;
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
