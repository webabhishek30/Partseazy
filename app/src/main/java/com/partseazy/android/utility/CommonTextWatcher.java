package com.partseazy.android.utility;

import android.support.design.widget.TextInputLayout;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

/**
 * Created by naveen on 21/8/17.
 */

public class CommonTextWatcher implements TextWatcher {

        private EditText editText;
        private TextInputLayout textInputLayout;

        public CommonTextWatcher(EditText view, TextInputLayout textInputLayout) {
            this.editText = view;
            this.textInputLayout = textInputLayout;
        }

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            textInputLayout.setErrorEnabled(false);
        }

        @Override
        public void afterTextChanged(Editable editable) {

        }
    }

