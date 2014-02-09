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
import android.widget.Button;

/**
 * The dialog created when the user long clicks a cipher.
 */
public abstract class LongClickCipherDialog extends Dialog {
   /**
    * Constructor
    * 
    * @param activity
    */
   public LongClickCipherDialog(final Activity activity) {
      super(activity);

      setContentView(R.layout.long_click_cipher_dialog);
      setTitle("Make a selection");

      final Button configureButton = (Button) findViewById(R.id.cipher_configure_button);
      configureButton.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View v) {
            onConfigureButton();
         }
      });

      final Button deleteButton = (Button) findViewById(R.id.cipher_delete_button);
      deleteButton.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View v) {
            onDeleteButton();
         }
      });

      final Button cancelButton = (Button) findViewById(R.id.cipher_cancel_button);
      cancelButton.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View v) {
            onCancelButton();
         }
      });
   }

   /**
    * Actions to take when the Cancel button is clicked.
    */
   public abstract void onCancelButton();

   /**
    * Actions to take when the Configure button is clicked.
    */
   public abstract void onConfigureButton();

   /**
    * Actions to take when the Delete button is clicked.
    */
   public abstract void onDeleteButton();
}
