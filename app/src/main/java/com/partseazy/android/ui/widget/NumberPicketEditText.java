package com.partseazy.android.ui.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.widget.EditText;

/**
 * Created by gaurav on 21/12/16.
 */

public class NumberPicketEditText extends EditText {

    private EditTextImeBackListener mOnImeBack;

    public NumberPicketEditText(Context context) {
        super(context);
    }

    public NumberPicketEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public NumberPicketEditText(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public boolean onKeyPreIme(int keyCode, KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_UP) {
            if (mOnImeBack != null) mOnImeBack.onImeBack(this, this.getText().toString());
        }
        return super.dispatchKeyEvent(event);
    }

    public void setOnEditTextImeBackListener(EditTextImeBackListener listener) {
        mOnImeBack = listener;
    }

}


