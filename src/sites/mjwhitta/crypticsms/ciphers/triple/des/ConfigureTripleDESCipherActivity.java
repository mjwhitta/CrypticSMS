/**
 * GNU GENERAL PUBLIC LICENSE See the file license.txt for copying conditions.
 * 
 * @author mjwhitta
 * @created Mar 17, 2012
 */
package sites.mjwhitta.crypticsms.ciphers.triple.des;

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
 * Used to configure a Triple DES Cipher.
 */
public class ConfigureTripleDESCipherActivity extends ConfigureCipherActivity {
   private TextView m_key;

   /**
    * @see sites.mjwhitta.crypticsms.ciphers.ConfigureCipherActivity#init()
    */
   @Override
   protected void init() {
      // Set content to see
      setContentView(R.layout.configure_triple_des_cipher);

      m_cipher = CipherSingleton.getInstance();

      m_key = (EditText) findViewById(R.id.configure_triple_des_cipher_key);
      m_key.setText(String.valueOf(((TripleDESCipher) m_cipher).getKey()));

      m_okButton = (Button) findViewById(R.id.configure_triple_des_cipher_ok_button);
      m_okButton.setOnClickListener(new OnClickListener() {
         @Override
         public void onClick(View v) {
            if (((TripleDESCipher) m_cipher).setKey(m_key.getText().toString()
                  .trim())) {
               setResult(RESULT_OK, new Intent());
               finish();
            } else {
               Toast.makeText(
                     getApplicationContext(),
                     "Error saving key, make sure length is greater than "
                           + "or equal to 24 bytes/characters",
                     Toast.LENGTH_SHORT).show();
            }
         }
      });
   }
}
