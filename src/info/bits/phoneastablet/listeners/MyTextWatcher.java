/**
 * 
 */
package info.bits.phoneastablet.listeners;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.CheckBox;
import android.widget.EditText;

/**
 * @author LiTTle
 * This is a listener for any text change at any text field.
 */
public class MyTextWatcher implements TextWatcher {

    private final EditText transmitter, receiver;
    private final CheckBox condition;
    /**
     * Constructs a new watcher for the text fields
     */
    public MyTextWatcher(CheckBox condition, EditText...pair) {
	this.condition = condition;
	transmitter = pair[0];
	receiver = pair[1];
    }

    /**
     * Enhances the behavior of the initial listener by mirroring the changes for one 
     * text field to another.
     * @see android.view.View.OnKeyListener#onKey(android.view.View, int, android.view.KeyEvent)
     */
    @Override
    public void afterTextChanged(Editable s) {
	if(condition.isChecked())
	    receiver.setText(transmitter.getText().toString());
    }

    /**
     * @see android.text.TextWatcher#beforeTextChanged(java.lang.CharSequence, int, int, int)
     */
    @Override
    public void beforeTextChanged(CharSequence s, int start, int count,
	    int after) {
	// TODO Auto-generated method stub
	
    }

    /**
     * @see android.text.TextWatcher#onTextChanged(java.lang.CharSequence, int, int, int)
     */
    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
	// TODO Auto-generated method stub
	
    }

}
