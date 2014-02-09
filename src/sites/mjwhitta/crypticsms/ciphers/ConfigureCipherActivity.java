/**
 * GNU GENERAL PUBLIC LICENSE See the file license.txt for copying conditions.
 * 
 * @author mjwhitta
 * @created Mar 16, 2012
 */
package sites.mjwhitta.crypticsms.ciphers;

import sites.mjwhitta.crypticsms.R;
import android.os.Bundle;
import android.widget.Button;

import com.actionbarsherlock.app.SherlockActivity;

/**
 * Used to configure a Cipher.
 */
public abstract class ConfigureCipherActivity extends SherlockActivity {
   protected Cipher m_cipher;

   protected Button m_okButton;

   /**
    * Initialize everything.
    */
   protected abstract void init();

   /**
    * Called when the activity is first created.
    */
   @Override
   public void onCreate(Bundle savedInstanceState) {
      setTheme(R.style.Theme_Sherlock);
      super.onCreate(savedInstanceState);

      // FIXME
      // getSupportActionBar().setDisplayHomeAsUpEnabled(true);

      init();
   }
}
