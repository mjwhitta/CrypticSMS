/**
 * GNU GENERAL PUBLIC LICENSE See the file license.txt for copying conditions.
 * 
 * @author mjwhitta
 * @created Dec 19, 2011
 */
package sites.mjwhitta.crypticsms.ciphers.caesar;

import java.util.ArrayList;
import java.util.List;

import sites.mjwhitta.crypticsms.R;
import sites.mjwhitta.crypticsms.ciphers.CipherSingleton;
import sites.mjwhitta.crypticsms.ciphers.ConfigureCipherActivity;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

/**
 * Used to configure a Caesar Cipher.
 */
public class ConfigureCaesarCipherActivity extends ConfigureCipherActivity {
   private Spinner m_rotateBy;

   /**
    * @see sites.mjwhitta.crypticsms.ciphers.ConfigureCipherActivity#init()
    */
   @Override
   protected void init() {
      // Set content to see
      setContentView(R.layout.configure_caesar_cipher);

      m_cipher = CipherSingleton.getInstance();

      List<Integer> values = new ArrayList<Integer>();
      for (int i = 0; i < ((CaesarCipher) m_cipher).getPossibleRotations()
            .size(); ++i) {
         values.add(i);
      }

      // Set up array adapter for rotation value
      m_rotateBy = (Spinner) findViewById(R.id.configure_caesar_cipher_rotation);
      m_rotateBy.setAdapter(new ArrayAdapter<Integer>(this,
            R.layout.rotation_list_item, values));
      m_rotateBy.setSelection(((CaesarCipher) m_cipher).getRotation());

      m_okButton = (Button) findViewById(R.id.configure_caesar_cipher_ok_button);
      m_okButton.setOnClickListener(new OnClickListener() {
         @Override
         public void onClick(View v) {
            ((CaesarCipher) m_cipher).setRotation(m_rotateBy
                  .getSelectedItemPosition());

            setResult(RESULT_OK, new Intent());
            finish();
         }
      });
   }
}
