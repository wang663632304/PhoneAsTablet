/**
 * 
 */
package info.bits.phoneastablet.listeners;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.CheckBox;
import android.widget.EditText;

/**
 * @author little
 *
 */
public class MyTextWatcher implements TextWatcher {

    private final EditText transmitter, receiver;
    private final CheckBox condition;
    /**
     * 
     */
    public MyTextWatcher(CheckBox condition, EditText...pair) {
	this.condition = condition;
	transmitter = pair[0];
	receiver = pair[1];
    }

    /* (non-Javadoc)
     * @see android.view.View.OnKeyListener#onKey(android.view.View, int, android.view.KeyEvent)
     */
    @Override
    public void afterTextChanged(Editable s) {
	if(condition.isChecked())
	    receiver.setText(transmitter.getText().toString());
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count,
	    int after) {
	// TODO Auto-generated method stub
	
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
	// TODO Auto-generated method stub
	
    }

}
