/**
 * GNU GENERAL PUBLIC LICENSE See the file license.txt for copying conditions.
 * 
 * @author mjwhitta
 * @created Feb 3, 2012
 */
package sites.mjwhitta.crypticsms.ciphers.hill;

import sites.mjwhitta.crypticsms.R;
import sites.mjwhitta.crypticsms.ciphers.CipherSingleton;
import sites.mjwhitta.crypticsms.ciphers.ConfigureCipherActivity;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Used to configure a Hill Cipher.
 */
public class ConfigureHillCipherActivity extends ConfigureCipherActivity {
   private EditText m_key11;

   private EditText m_key12;

   private EditText m_key13;

   private EditText m_key21;

   private EditText m_key22;

   private EditText m_key23;

   private EditText m_key31;

   private EditText m_key32;

   private EditText m_key33;

   /**
    * @see sites.mjwhitta.crypticsms.ciphers.ConfigureCipherActivity#init()
    */
   @Override
   protected void init() {
      // Set content to see
      setContentView(R.layout.configure_hill_cipher);

      m_cipher = CipherSingleton.getInstance();

      int[][] key = ((HillCipher) m_cipher).getKey();

      m_key11 = (EditText) findViewById(R.id.configure_hill_cipher_k11);
      int prefWidth = m_key11.getWidth();
      m_key11.setMinWidth(prefWidth);
      m_key11.setMaxWidth(prefWidth);
      m_key11.setText(String.valueOf(key[0][0]));

      m_key12 = (EditText) findViewById(R.id.configure_hill_cipher_k12);
      m_key12.setMinWidth(prefWidth);
      m_key12.setMaxWidth(prefWidth);
      m_key12.setText(String.valueOf(key[0][1]));

      m_key13 = (EditText) findViewById(R.id.configure_hill_cipher_k13);
      m_key13.setMinWidth(prefWidth);
      m_key13.setMaxWidth(prefWidth);
      m_key13.setText(String.valueOf(key[0][2]));

      m_key21 = (EditText) findViewById(R.id.configure_hill_cipher_k21);
      m_key21.setMinWidth(prefWidth);
      m_key21.setMaxWidth(prefWidth);
      m_key21.setText(String.valueOf(key[1][0]));

      m_key22 = (EditText) findViewById(R.id.configure_hill_cipher_k22);
      m_key22.setMinWidth(prefWidth);
      m_key22.setMaxWidth(prefWidth);
      m_key22.setText(String.valueOf(key[1][1]));

      m_key23 = (EditText) findViewById(R.id.configure_hill_cipher_k23);
      m_key23.setMinWidth(prefWidth);
      m_key23.setMaxWidth(prefWidth);
      m_key23.setText(String.valueOf(key[1][2]));

      m_key31 = (EditText) findViewById(R.id.configure_hill_cipher_k31);
      m_key31.setMinWidth(prefWidth);
      m_key31.setMaxWidth(prefWidth);
      m_key31.setText(String.valueOf(key[2][0]));

      m_key32 = (EditText) findViewById(R.id.configure_hill_cipher_k32);
      m_key32.setMinWidth(prefWidth);
      m_key32.setMaxWidth(prefWidth);
      m_key32.setText(String.valueOf(key[2][1]));

      m_key33 = (EditText) findViewById(R.id.configure_hill_cipher_k33);
      m_key33.setMinWidth(prefWidth);
      m_key33.setMaxWidth(prefWidth);
      m_key33.setText(String.valueOf(key[2][2]));

      m_okButton = (Button) findViewById(R.id.configure_hill_cipher_ok_button);
      m_okButton.setOnClickListener(new OnClickListener() {
         @Override
         public void onClick(View v) {
            try {
               int[][] key = new int[3][3];
               key[0][0] = Integer.valueOf(m_key11.getText().toString());
               key[0][1] = Integer.valueOf(m_key12.getText().toString());
               key[0][2] = Integer.valueOf(m_key13.getText().toString());
               key[1][0] = Integer.valueOf(m_key21.getText().toString());
               key[1][1] = Integer.valueOf(m_key22.getText().toString());
               key[1][2] = Integer.valueOf(m_key23.getText().toString());
               key[2][0] = Integer.valueOf(m_key31.getText().toString());
               key[2][1] = Integer.valueOf(m_key32.getText().toString());
               key[2][2] = Integer.valueOf(m_key33.getText().toString());

               if (((HillCipher) m_cipher).setKey(key)) {
                  setResult(RESULT_OK, new Intent());
                  finish();
               } else {
                  Toast.makeText(
                        getApplicationContext(),
                        "Error, determinant can not equal 0 and can not have common divisors with alphabet size",
                        Toast.LENGTH_LONG).show();
               }
            } catch (Exception e) {
               // do nothing
            }
         }
      });
   }
}
