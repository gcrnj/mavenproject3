package com.mycompany.mavenproject3.validators;

import com.jfoenix.validation.base.ValidatorBase;
import javafx.scene.control.TextInputControl;

public class MobileNumberValidator extends ValidatorBase {

    @Override
    protected void eval() {
        TextInputControl control = (TextInputControl) srcControl.get();
        String text = control.getText();
        boolean isError = false;
        if (text == null || !text.startsWith("09")) {
            isError = true;
            setMessage("Required prefix: 09");
        } else if (text.length() != 11) {
            isError = true;
            setMessage("Required length: 11");
        }
        hasErrors.set(isError);
    }

}
