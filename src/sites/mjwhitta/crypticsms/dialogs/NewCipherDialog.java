/**
 * GNU GENERAL PUBLIC LICENSE See the file license.txt for copying conditions.
 * 
 * @author mjwhitta
 * @created Mar 16, 2012
 */
package sites.mjwhitta.crypticsms.dialogs;

import sites.mjwhitta.crypticsms.R;
import android.app.Activity;
import android.app.Dialog;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

/**
 * The dialog created when the user selects "Add Cipher" from the menu.
 */
public abstract class NewCipherDialog extends Dialog {
   private final Activity m_parentActivity;

   /**
    * Constructor
    * 
    * @param activity
    */
   public NewCipherDialog(final Activity activity,
         final ArrayAdapter<String> cipherTypes) {
      super(activity);

      m_parentActivity = activity;

      setContentView(R.layout.new_cipher_dialog);
      setTitle("Create New Cipher");

      final Spinner cipherType = (Spinner) findViewById(R.id.cipher_spinner);
      cipherType.setAdapter(cipherTypes);

      final EditText cipherNameTextField = (EditText) findViewById(R.id.cipher_name);

      final Button okButton = (Button) findViewById(R.id.cipher_ok_button);
      okButton.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View v) {
            String name = cipherNameTextField.getText().toString();
            if (!name.equals("") && !name.contains(" ") && !name.contains("/")) {
               onOkButton(name, cipherType.getSelectedItemPosition());
            } else {
               toastError("Cipher names can't have spaces or file separators in them!");
            }
         }
      });
   }

   /**
    * Actions to take when the Ok button is clicked.
    * 
    * @param newCipherName
    */
   public abstract void onOkButton(String newCipherName, int cipherType);

   private void toastError(String err) {
      Toast.makeText(m_parentActivity.getApplicationContext(), err,
            Toast.LENGTH_LONG).show();
   }
}
