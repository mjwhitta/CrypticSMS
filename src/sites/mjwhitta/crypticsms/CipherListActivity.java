/**
 * GNU GENERAL PUBLIC LICENSE See the file license.txt for copying conditions.
 * 
 * @author mjwhitta
 * @created Dec 16, 2011
 */
package sites.mjwhitta.crypticsms;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

import sites.mjwhitta.andlib.mjwhitta.activities.ListActivity;
import sites.mjwhitta.andlib.mjwhitta.dialogs.OutputDialog;
import sites.mjwhitta.andlib.mjwhitta.utils.CheckBoxList;
import sites.mjwhitta.crypticsms.ciphers.Cipher;
import sites.mjwhitta.crypticsms.ciphers.CipherSingleton;
import sites.mjwhitta.crypticsms.ciphers.aes.AESCipher;
import sites.mjwhitta.crypticsms.ciphers.backwards.BackwardsCipher;
import sites.mjwhitta.crypticsms.ciphers.caesar.CaesarCipher;
import sites.mjwhitta.crypticsms.ciphers.des.DESCipher;
import sites.mjwhitta.crypticsms.ciphers.hill.HillCipher;
import sites.mjwhitta.crypticsms.ciphers.monoalphabetic.MonoalphabeticCipher;
import sites.mjwhitta.crypticsms.ciphers.morsecode.MorseCodeCipher;
import sites.mjwhitta.crypticsms.ciphers.playfair.PlayfairCipher;
import sites.mjwhitta.crypticsms.ciphers.reversealphabet.ReverseAlphabetCipher;
import sites.mjwhitta.crypticsms.ciphers.triple.des.TripleDESCipher;
import sites.mjwhitta.crypticsms.ciphers.vigenere.VigenereCipher;
import sites.mjwhitta.crypticsms.dialogs.LongClickCipherDialog;
import sites.mjwhitta.crypticsms.dialogs.NewCipherDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;

public class CipherListActivity extends ListActivity<Cipher> {
   /**
    * This class restores all of the ciphers
    */
   protected class RestoreCiphersAsyncTask extends
         AsyncTask<Void, String, List<Cipher>> {
      protected int m_numOperations = 0;

      protected int m_operation = 0;

      protected String m_message = "Restoring ciphers";

      /**
       * @see android.os.AsyncTask#doInBackground(Params[])
       */
      @Override
      protected List<Cipher> doInBackground(Void... params) {
         List<Cipher> ciphers = new ArrayList<Cipher>();

         for (String fileName : fileList()) {
            if (!isCancelled()) {
               publishProgress();
               ++m_operation;

               // Parse Cipher's class and name from fileName
               String cipherClass = fileName.substring(0,
                     fileName.lastIndexOf("_"));
               String cipherName = fileName.substring(
                     fileName.lastIndexOf("_") + 1, fileName.length());

               try {
                  BufferedReader br = new BufferedReader(new InputStreamReader(
                        openFileInput(fileName)));

                  // Create Cipher and set name
                  Cipher c = (Cipher) Class.forName(cipherClass).newInstance();
                  c.setName(cipherName);

                  // Restore configuration from file
                  c.restoreFromFile(br);

                  // Close file
                  br.close();

                  // Add to list and display
                  ciphers.add(c);
               } catch (Exception e) {
                  publishProgress("Failed to read " + cipherClass + ": "
                        + cipherName);
               }
            }
         }

         publishProgress();
         return ciphers;
      }

      /**
       * @see android.os.AsyncTask#onPostExecute(java.lang.Object)
       */
      @Override
      protected void onPostExecute(List<Cipher> ciphers) {
         m_list.addAll(ciphers);
         setLoading(false);
      }

      /**
       * @see android.os.AsyncTask#onPreExecute()
       */
      @Override
      protected void onPreExecute() {
         setLoading(true, m_message, 0);
         m_numOperations = fileList().length;
         m_list.removeAll();
      }

      /**
       * @see android.os.AsyncTask#onProgressUpdate(Progress[])
       */
      @Override
      protected void onProgressUpdate(String... progress) {
         double percent = ((double) m_operation / (double) m_numOperations) * 100;
         setLoading(true, m_message, (int) percent);
         if (progress.length > 0) {
            toastError(progress[0]);
         }
      }
   }

   private static final String[] CIPHER_TYPES = {
         AESCipher.class.getSimpleName(),
         BackwardsCipher.class.getSimpleName(),
         CaesarCipher.class.getSimpleName(), DESCipher.class.getSimpleName(),
         HillCipher.class.getSimpleName(),
         MonoalphabeticCipher.class.getSimpleName(),
         MorseCodeCipher.class.getSimpleName(),
         PlayfairCipher.class.getSimpleName(),
         ReverseAlphabetCipher.class.getSimpleName(),
         TripleDESCipher.class.getSimpleName(),
         VigenereCipher.class.getSimpleName() };

   private static final String[] CIPHER_CLASSES = {
         AESCipher.class.getCanonicalName(),
         BackwardsCipher.class.getCanonicalName(),
         CaesarCipher.class.getCanonicalName(),
         DESCipher.class.getCanonicalName(),
         HillCipher.class.getCanonicalName(),
         MonoalphabeticCipher.class.getCanonicalName(),
         MorseCodeCipher.class.getCanonicalName(),
         PlayfairCipher.class.getCanonicalName(),
         ReverseAlphabetCipher.class.getCanonicalName(),
         TripleDESCipher.class.getCanonicalName(),
         VigenereCipher.class.getCanonicalName() };

   private ArrayAdapter<String> m_cipherDropDownList;

   private RestoreCiphersAsyncTask m_restoreTask;

   /**
    * Create a new Cipher and add it to the list.
    */
   private void addNewCipher(String newCipherName, int cipherType) {
      Cipher c = null;
      try {
         c = (Cipher) Class.forName(CIPHER_CLASSES[cipherType]).newInstance();
         c.setName(newCipherName);
      } catch (Exception e) {
         Toast.makeText(getApplicationContext(),
               "Error creating " + cipherType, Toast.LENGTH_LONG).show();
      }

      if (c != null) {
         if (!m_list.contains(c)) {
            // Save new Cipher to file
            saveCipherToFile(c);

            // Add new Cipher to the list and update the GUI
            m_list.add(c);

            setLoading(false);

            // Set CipherSingleton to be the new Cipher
            CipherSingleton.setInstance(c);

            if (c.getConfigureId() != R.id.nonconfigurable_cipher_ok_button) {
               startActivityForResult(new Intent(this, c.getConfigureClass()),
                     c.getConfigureId());
            }
         } else {
            Toast.makeText(getApplicationContext(), "Cipher already exists",
                  Toast.LENGTH_SHORT).show();
         }
      }
   }

   /**
    * Configure the selected Cipher.
    */
   private void configureCipher() {
      startActivityForResult(new Intent(this, CipherSingleton.getInstance()
            .getConfigureClass()), CipherSingleton.getInstance()
            .getConfigureId());
   }

   /**
    * @param c
    * @return Return a filename for the specific Cipher.
    */
   private String getFileName(Cipher c) {
      return c.getClass().getCanonicalName() + "_" + c.getName();
   }

   /**
    * Initialize everything.
    */
   private void init() {
      m_list = new CheckBoxList<Cipher>(this);
      m_list.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
      m_list.showCheckBox(false);

      // Set up array adapter for Cipher types
      m_cipherDropDownList = new ArrayAdapter<String>(this,
            R.layout.cipher_type_list_item, CIPHER_TYPES) {
         /**
          * @see android.widget.ArrayAdapter#getDropDownView(int,
          *      android.view.View, android.view.ViewGroup)
          */
         @Override
         public View getDropDownView(int position, View convertView,
               ViewGroup parent) {
            View view = super.getView(position, convertView, parent);
            if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.GINGERBREAD_MR1) {
               TextView text = (TextView) view
                     .findViewById(R.id.dropdown_list_item);
               text.setBackgroundColor(Color.BLACK);
            }

            return view;
         }
      };
      m_cipherDropDownList
            .setDropDownViewResource(R.layout.cipher_type_list_item);

      // Restore any previously created Ciphers
      restoreCiphers();
   }

   /**
    * Something from the CheckBoxList was clicked, open the
    * EncryptDecryptActivity
    * 
    * @param position
    */
   @Override
   public void listItemClick(int position) {
      CipherSingleton.setInstance(m_list.getSelectedItems().get(0));
      startActivity(new Intent(this, EncryptDecryptActivity.class));
   }

   /**
    * Something from the CheckBoxList was long clicked
    * 
    * @param position
    */
   @Override
   public void listItemLongClick(int position) {
      CipherSingleton.setInstance(m_list.getSelectedItems().get(0));
      showDialog(R.id.cipher_configure_button);
   }

   /**
    * @see android.app.Activity#onActivityResult(int, int,
    *      android.content.Intent)
    */
   @Override
   protected void onActivityResult(int requestCode, int resultCode, Intent data) {
      if (resultCode == RESULT_OK) {
         saveCipherToFile(CipherSingleton.getInstance());
      }

      super.onActivityResult(requestCode, resultCode, data);
   }

   /**
    * Called when the activity is first created.
    */
   @Override
   public void onCreate(Bundle savedInstanceState) {
      setTheme(R.style.Theme_Sherlock);
      super.onCreate(savedInstanceState);

      // Set content to see
      setContentView(R.layout.cipher_list);

      init();
   }

   /**
    * @see android.app.Activity#onCreateDialog(int)
    */
   @Override
   protected Dialog onCreateDialog(int id) {
      switch (id) {
      case R.id.add_cipher:
         return new NewCipherDialog(this, m_cipherDropDownList) {
            /**
             * @see sites.mjwhitta.crypticsms.dialogs.NewCipherDialog#onOkButton(java.lang.String,
             *      int)
             */
            @Override
            public void onOkButton(String newCipherName, int cipherType) {
               removeDialog(R.id.add_cipher);
               addNewCipher(newCipherName, cipherType);
            }
         };
      case R.id.cipher_configure_button:
         return new LongClickCipherDialog(this) {
            /**
             * @see sites.mjwhitta.crypticsms.dialogs.LongClickCipherDialog#onCancelButton()
             */
            @Override
            public void onCancelButton() {
               removeDialog(R.id.cipher_configure_button);
            }

            /**
             * @see sites.mjwhitta.crypticsms.dialogs.LongClickCipherDialog#onConfigureButton()
             */
            @Override
            public void onConfigureButton() {
               removeDialog(R.id.cipher_configure_button);
               configureCipher();
            }

            /**
             * @see sites.mjwhitta.crypticsms.dialogs.LongClickCipherDialog#onDeleteButton()
             */
            @Override
            public void onDeleteButton() {
               removeDialog(R.id.cipher_configure_button);
               removeCipher();
            }
         };
      case R.id.usage:
         return new OutputDialog(this, "Usage:") {
            /**
             * @see sites.mjwhitta.andlib.mjwhitta.dialogs.OutputDialog#getOutput()
             */
            @Override
            public String getOutput() {
               return getString(R.string.usage);
            }

            /**
             * @see sites.mjwhitta.andlib.mjwhitta.dialogs.OutputDialog#onCloseButton()
             */
            @Override
            public void onCloseButton() {
               removeDialog(R.id.usage);
            }
         };
      default:
         return null;
      }
   }

   /**
    * @see com.actionbarsherlock.app.SherlockFragmentActivity#onCreateOptionsMenu(android.view.Menu)
    */
   @Override
   public boolean onCreateOptionsMenu(Menu menu) {
      menu.add(0, R.id.add_cipher, 0, "Add Cipher").setShowAsAction(
            MenuItem.SHOW_AS_ACTION_IF_ROOM);
      menu.add(0, R.id.usage, 0, "Usage").setShowAsAction(
            MenuItem.SHOW_AS_ACTION_NEVER);
      return true;
   }

   /**
    * @see com.actionbarsherlock.app.SherlockFragmentActivity#onOptionsItemSelected(android.view.MenuItem)
    */
   @Override
   public boolean onOptionsItemSelected(MenuItem item) {
      switch (item.getItemId()) {
      case R.id.add_cipher:
         showDialog(R.id.add_cipher);
         break;
      case R.id.usage:
         showDialog(R.id.usage);
         break;
      default:
         Toast.makeText(getApplicationContext(),
               "Unexpected action value " + item.getItemId(), Toast.LENGTH_LONG)
               .show();
      }

      return true;
   }

   /**
    * @see android.app.Activity#onPrepareDialog(int, android.app.Dialog)
    */
   @Override
   protected void onPrepareDialog(int id, Dialog dialog) {
      super.onPrepareDialog(id, dialog);
   }

   /**
    * Deletes a cipher from the list.
    */
   private void removeCipher() {
      Cipher c = CipherSingleton.getInstance();
      if (!deleteFile(getFileName(c))) {
         Toast.makeText(getApplicationContext(),
               "Failed to delete file " + getFileName(c), Toast.LENGTH_LONG)
               .show();
      }

      // Remove Cipher from the list and update the GUI
      m_list.remove(c);
      setLoading(false);
   }

   /**
    * Restores all Ciphers from files.
    */
   private void restoreCiphers() {
      if (m_restoreTask != null) {
         m_restoreTask.cancel(true);
      }
      m_restoreTask = new RestoreCiphersAsyncTask();
      m_restoreTask.execute();
   }

   /**
    * Saves a Cipher to a file.
    * 
    * @param c
    */
   private void saveCipherToFile(Cipher c) {
      deleteFile(getFileName(c));
      try {
         BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(
               openFileOutput(getFileName(c), Context.MODE_PRIVATE)));

         // Write Cipher specific data
         c.saveToFile(bw);

         // Close file
         bw.close();
      } catch (Exception e) {
         Toast.makeText(getApplicationContext(),
               "Couldn't save Cipher to file", Toast.LENGTH_LONG).show();
      }
   }

   /**
    * @see sites.mjwhitta.andlib.mjwhitta.activities.ListActivity#setLoading(boolean)
    */
   @Override
   protected void setLoading(boolean loading) {
      setLoading(loading, "No scripts found", 101);
   }

   /**
    * @see sites.mjwhitta.andlib.mjwhitta.activities.ListActivity#setLoading(boolean,
    *      int)
    */
   @Override
   protected void setLoading(boolean loading, int percent) {
      setLoading(loading, "No scripts found", percent);
   }
}
