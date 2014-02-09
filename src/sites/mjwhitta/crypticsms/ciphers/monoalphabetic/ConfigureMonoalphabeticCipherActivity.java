/**
 * GNU GENERAL PUBLIC LICENSE See the file license.txt for copying conditions.
 * 
 * @author mjwhitta
 * @created Dec 19, 2011
 */
package sites.mjwhitta.crypticsms.ciphers.monoalphabetic;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import sites.mjwhitta.crypticsms.R;
import sites.mjwhitta.crypticsms.ciphers.CipherSingleton;
import sites.mjwhitta.crypticsms.ciphers.ConfigureCipherActivity;
import android.content.Intent;
import android.graphics.Color;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Used to configure a Monoalphabetic Cipher.
 */
public class ConfigureMonoalphabeticCipherActivity extends
      ConfigureCipherActivity {
   private static enum Validity {
      EMPTY, IN_PROGRESS, VALID, ERROR
   }

   private List<Character> m_alphabet;

   private TextView m_choices;

   private EditText m_alphabetString;

   private Button m_randomButton;

   private Validity m_valid = Validity.EMPTY;

   private void adjustChoices() {
      ArrayList<Character> newAlphabet = new ArrayList<Character>(m_alphabet);
      for (Character c : m_alphabetString.getText().toString().toLowerCase()
            .trim().toCharArray()) {
         newAlphabet.remove(c);
      }

      String choices = "";
      for (Character c : newAlphabet) {
         choices += c;
      }

      m_choices.setText(choices);
   }

   /**
    * @see sites.mjwhitta.crypticsms.ciphers.ConfigureCipherActivity#init()
    */
   @Override
   protected void init() {
      // Set content to see
      setContentView(R.layout.configure_monoalphabetic_cipher);

      m_cipher = CipherSingleton.getInstance();

      m_choices = (TextView) findViewById(R.id.configure_monoalphabetic_cipher_choices);
      m_alphabet = ((MonoalphabeticCipher) m_cipher).getAlphabetString();
      String choices = "";
      for (Character c : m_alphabet) {
         choices += c;
      }
      m_choices.setText(choices);

      m_alphabetString = (EditText) findViewById(R.id.configure_monoalphabetic_cipher_string);
      m_alphabetString.addTextChangedListener(new TextWatcher() {
         @Override
         public void afterTextChanged(Editable s) {
            validate();
            adjustChoices();
         }

         @Override
         public void beforeTextChanged(CharSequence s, int start, int count,
               int after) {
            // do nothing
         }

         @Override
         public void onTextChanged(CharSequence s, int start, int before,
               int count) {
            // do nothing
         }
      });

      m_randomButton = (Button) findViewById(R.id.configure_monoalphabetic_cipher_random_button);
      m_randomButton.setOnClickListener(new OnClickListener() {
         @Override
         public void onClick(View v) {
            random();
         }
      });

      m_okButton = (Button) findViewById(R.id.configure_monoalphabetic_cipher_ok_button);
      m_okButton.setOnClickListener(new OnClickListener() {
         @Override
         public void onClick(View v) {
            if ((m_valid == Validity.EMPTY) || (m_valid == Validity.VALID)) {
               if (m_alphabetString.getText().toString().trim().length() == 36) {
                  ((MonoalphabeticCipher) m_cipher)
                        .setAlphabetString(m_alphabetString.getText()
                              .toString().toLowerCase().trim());
               }

               setResult(RESULT_OK, new Intent());
               finish();
            } else {
               Toast.makeText(getApplicationContext(),
                     "New alphabet is not valid", Toast.LENGTH_LONG).show();
            }
         }
      });
   }

   private void random() {
      m_alphabetString.setText("");

      String newAlphabet = "";
      ArrayList<Character> alphabet = new ArrayList<Character>(m_alphabet);
      Random rn = new Random();

      while (!alphabet.isEmpty()) {
         int index = rn.nextInt() % alphabet.size();
         index = (index < 0 ? -index : index);
         newAlphabet += alphabet.remove(index).charValue();
      }

      m_alphabetString.setText(newAlphabet);
   }

   private void validate() {
      String alphabetString = m_alphabetString.getText().toString()
            .toLowerCase();

      if (alphabetString.length() == 0) {
         m_valid = Validity.EMPTY;
      } else if ((alphabetString.length() > 0)
            && (alphabetString.length() < 36)) {
         m_valid = Validity.IN_PROGRESS;
      } else if (alphabetString.length() == 36) {
         m_valid = Validity.VALID;
      } else {
         m_valid = Validity.ERROR;
      }

      if ((m_valid == Validity.IN_PROGRESS) || (m_valid == Validity.VALID)) {
         for (Character c : alphabetString.toCharArray()) {
            if ((alphabetString.indexOf(c) != alphabetString.lastIndexOf(c))
                  || (!m_alphabet.contains(c))) {
               m_valid = Validity.ERROR;
               break;
            }
         }
      }

      if ((m_valid == Validity.EMPTY) || (m_valid == Validity.IN_PROGRESS)) {
         m_alphabetString.setBackgroundColor(Color.WHITE);
      } else if (m_valid == Validity.VALID) {
         m_alphabetString.setBackgroundColor(Color.GREEN);
      } else {
         m_alphabetString.setBackgroundColor(Color.RED);
      }
   }
}
