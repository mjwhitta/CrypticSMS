/**
 * GNU GENERAL PUBLIC LICENSE See the file license.txt for copying conditions.
 * 
 * @author mjwhitta
 * @created Dec 19, 2011
 */
package sites.mjwhitta.crypticsms.ciphers.reversealphabet;

import sites.mjwhitta.crypticsms.R;
import sites.mjwhitta.crypticsms.ciphers.ConfigureCipherActivity;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

/**
 * Used to configure a ReverseAlphabet Cipher.
 */
public class ConfigureReverseAlphabetCipherActivity extends
      ConfigureCipherActivity {
   /**
    * @see sites.mjwhitta.crypticsms.ciphers.ConfigureCipherActivity#init()
    */
   @Override
   protected void init() {
      // Set content to see
      setContentView(R.layout.nonconfigurable_cipher);

      m_okButton = (Button) findViewById(R.id.nonconfigurable_cipher_ok_button);
      m_okButton.setOnClickListener(new OnClickListener() {
         @Override
         public void onClick(View v) {
            setResult(RESULT_OK, new Intent());
            finish();
         }
      });
   }
}
