package com.mycompany.mavenproject3.utils;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.List;

public class Util {

    public static <T> ObservableList<T> getObservableList(List<T> list) {
        ObservableList<T> observableAppointments = FXCollections.observableArrayList();
        observableAppointments.addAll(list);
        return observableAppointments;
    }
}

