/**
 * GNU GENERAL PUBLIC LICENSE See the file license.txt for copying conditions.
 * 
 * @author mjwhitta
 * @created Dec 19, 2011
 */
package sites.mjwhitta.crypticsms;

import sites.mjwhitta.crypticsms.ciphers.Cipher;
import sites.mjwhitta.crypticsms.ciphers.CipherSingleton;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockActivity;

/**
 * Used to encrypt/decrypt plaintext/ciphertext with the chosen Cipher.
 */
public class EncryptDecryptActivity extends SherlockActivity {
   private Cipher m_cipher;

   private boolean m_isEncrypted = false;

   private boolean m_isDecrypted = false;

   private TextView m_plainTextCipherText;

   /**
    * Initialize everything.
    */
   private void init() {
      // Set content to see
      setContentView(R.layout.encrypt_decrypt);

      // FIXME
      // getSupportActionBar().setDisplayHomeAsUpEnabled(true);

      m_cipher = CipherSingleton.getInstance();

      m_plainTextCipherText = (EditText) findViewById(R.id.plaintext_ciphertext);

      Button encryptButton = (Button) findViewById(R.id.encrypt_button);
      encryptButton.setOnClickListener(new OnClickListener() {
         @Override
         public void onClick(View v) {
            if (!m_isEncrypted) {
               try {
                  m_plainTextCipherText.setText(m_cipher
                        .encrypt(m_plainTextCipherText.getText().toString()));

                  m_isEncrypted = true;
                  m_isDecrypted = false;
               } catch (Exception e) {
                  showError(e);
               }
            }
         }
      });

      Button clearButton = (Button) findViewById(R.id.clear_button);
      clearButton.setOnClickListener(new OnClickListener() {
         @Override
         public void onClick(View v) {
            m_plainTextCipherText.setText("");

            m_isEncrypted = false;
            m_isDecrypted = false;
         }
      });

      Button decryptButton = (Button) findViewById(R.id.decrypt_button);
      decryptButton.setOnClickListener(new OnClickListener() {
         @Override
         public void onClick(View v) {
            if (!m_isDecrypted) {
               try {
                  m_plainTextCipherText.setText(m_cipher
                        .decrypt(m_plainTextCipherText.getText().toString()));

                  m_isEncrypted = false;
                  m_isDecrypted = true;
               } catch (Exception e) {
                  showError(e);
               }
            }
         }
      });

      Button sendButton = (Button) findViewById(R.id.encrypt_decrypt_send_button);
      sendButton.setOnClickListener(new OnClickListener() {
         @Override
         public void onClick(View v) {
            send();
         }
      });
   }

   /**
    * Called when the activity is first created.
    */
   @Override
   public void onCreate(Bundle savedInstanceState) {
      setTheme(R.style.Theme_Sherlock);
      super.onCreate(savedInstanceState);

      init();
   }

   // /**
   // * @see
   // com.actionbarsherlock.app.SherlockFragmentActivity#onCreateOptionsMenu(android.view.Menu)
   // */
   // @Override
   // public boolean onCreateOptionsMenu(Menu menu) {
   // menu.add(0, 1, 0, "Back").setShowAsAction(
   // MenuItem.SHOW_AS_ACTION_ALWAYS
   // | MenuItem.SHOW_AS_ACTION_COLLAPSE_ACTION_VIEW);
   // return true;
   // }
   //
   // /**
   // * @see
   // com.actionbarsherlock.app.SherlockFragmentActivity#onOptionsItemSelected(android.view.MenuItem)
   // */
   // @Override
   // public boolean onOptionsItemSelected(MenuItem item) {
   // switch (item.getItemId()) {
   // case 1:
   // finish();
   // break;
   // default:
   // Toast.makeText(getApplicationContext(),
   // "Unexpected action value " + item.getItemId(), Toast.LENGTH_LONG)
   // .show();
   // }
   //
   // return true;
   // }

   private void send() {
      Intent share = new Intent(Intent.ACTION_SEND);
      share.setType("text/plain");
      share.putExtra(Intent.EXTRA_TEXT, m_plainTextCipherText.getText()
            .toString().trim());
      startActivity(Intent.createChooser(share, "Share Via"));
   }

   private void showError(Exception e) {
      e.printStackTrace();
      Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG)
            .show();
   }
}
