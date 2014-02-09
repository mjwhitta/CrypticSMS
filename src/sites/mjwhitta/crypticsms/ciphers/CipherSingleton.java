/**
 * GNU GENERAL PUBLIC LICENSE See the file license.txt for copying conditions.
 * 
 * @author mjwhitta
 * @created Dec 20, 2011
 */
package sites.mjwhitta.crypticsms.ciphers;

/**
 * This class just holds a Cipher object for sharing between activities.
 */
public class CipherSingleton {
   private static Cipher INSTANCE;

   /**
    * @return The singleton instance.
    */
   public static Cipher getInstance() {
      return INSTANCE;
   }

   /**
    * Sets the singleton instance.
    * 
    * @param c
    */
   public static void setInstance(Cipher c) {
      INSTANCE = c;
   }
}
