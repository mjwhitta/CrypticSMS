/**
 * GNU GENERAL PUBLIC LICENSE See the file license.txt for copying conditions.
 * 
 * @author mjwhitta
 * @created Aug 10, 2012
 */
package sites.mjwhitta.crypticsms.ciphers.aes;

import sites.mjwhitta.andlib.mjwhitta.utils.StringUtils;
import sites.mjwhitta.crypticsms.R;
import sites.mjwhitta.crypticsms.ciphers.CipherSingleton;
import sites.mjwhitta.crypticsms.ciphers.ConfigureCipherActivity;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Used to configure a AES Cipher.
 */
public class ConfigureAESCipherActivity extends ConfigureCipherActivity {
   private TextView m_key;

   /**
    * @see sites.mjwhitta.crypticsms.ciphers.ConfigureCipherActivity#init()
    */
   @Override
   protected void init() {
      // Set content to see
      setContentView(R.layout.configure_aes_cipher);

      m_cipher = CipherSingleton.getInstance();

      m_key = (EditText) findViewById(R.id.configure_aes_cipher_key);
      m_key.setText(String.valueOf(((AESCipher) m_cipher).getKey().trim()));

      m_okButton = (Button) findViewById(R.id.configure_aes_cipher_ok_button);
      m_okButton.setOnClickListener(new OnClickListener() {
         @Override
         public void onClick(View v) {
            String key = m_key.getText().toString().trim();
            if (!StringUtils.isNullorEmpty(key)) {
               if (((AESCipher) m_cipher).setKey(key)) {
                  setResult(RESULT_OK, new Intent());
                  finish();
               } else {
                  Toast.makeText(
                        getApplicationContext(),
                        "Error saving key, make sure length is less than "
                              + "or equal to 32 bytes/characters",
                        Toast.LENGTH_SHORT).show();
               }
            } else {
               Toast.makeText(
                     getApplicationContext(),
                     "Error saving key, make sure length is greater than 0 bytes/characters",
                     Toast.LENGTH_SHORT).show();
            }
         }
      });
   }
}
