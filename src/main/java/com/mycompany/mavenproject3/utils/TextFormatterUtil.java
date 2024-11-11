/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.mavenproject3.utils;

import javafx.scene.control.TextFormatter;

/**
 *
 * @author GNew
 */
import javafx.scene.control.TextFormatter;

import java.util.function.UnaryOperator;
import java.util.regex.Pattern;

public class TextFormatterUtil {

    // Integer-only TextFormatter
    public static TextFormatter<Integer> intTextFormatter() {
        Pattern intPattern = Pattern.compile("\\d*"); // Matches only digits
        UnaryOperator<TextFormatter.Change> intFilter = change -> {
            String newText = change.getControlNewText();
            return intPattern.matcher(newText).matches() ? change : null;
        };
        return new TextFormatter<>(intFilter);
    }

    // Double (decimal) TextFormatter
    public static TextFormatter<Double> doubleTextFormatter() {
        Pattern doublePattern = Pattern.compile("\\d*(\\.\\d*)?"); // Matches digits with optional decimal point
        UnaryOperator<TextFormatter.Change> doubleFilter = change -> {
            String newText = change.getControlNewText();
            return doublePattern.matcher(newText).matches() ? change : null;
        };
        return new TextFormatter<>(doubleFilter);
    }
}

