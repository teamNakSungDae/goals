package nexters.hashgoals.validation;

import android.text.TextUtils;
import android.widget.EditText;


import lombok.Data;
import lombok.experimental.Builder;

/**
 * Created by kwongiho on 2017. 4. 15..
 *
 * There are can using all of them. but not make perfect.
 * Will add some method in future huhu.
 */

@Data
@Builder
public class EditTextValidation {

    private EditText editText;

    public boolean isValid() {
        if( isNullOrEmpty() )
            return false;
        return true;
    }

    public boolean isValidByNumber(){
        if( isNullOrEmpty() )
            return false;
        return true;
    }

    public boolean isNullOrEmpty() {
        //TextUtils.isEmpty() - > null or length == 1 일경우 false return.
        return TextUtils.isEmpty(editText.getText());
    }
    public boolean isNumberic(){
        if(isNullOrEmpty())
            return false;
        return TextUtils.isDigitsOnly(editText.getText());
    }



}
