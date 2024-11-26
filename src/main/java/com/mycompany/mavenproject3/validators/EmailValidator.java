package com.mycompany.mavenproject3.validators;

import com.jfoenix.validation.base.ValidatorBase;
import javafx.scene.control.TextInputControl;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EmailValidator extends ValidatorBase {

    Pattern pattern = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);

    @Override
    protected void eval() {
        setMessage("Invalid email format");
        TextInputControl control = (TextInputControl) srcControl.get();
        String text = control.getText();

        Matcher matcher = pattern.matcher(text);

        hasErrors.set(!matcher.matches());
    }

}
