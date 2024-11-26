package com.mycompany.mavenproject3.dashboard;

import com.mycompany.mavenproject3.interfaces.Refreshable;
import com.mycompany.mavenproject3.myforms.CreateEmployeeController;
import javafx.fxml.FXML;

public interface EmployeesPage extends Refreshable {


    default void loadEmployees() {


    }

    @FXML
    default void createEmployeeForm() {
        CreateEmployeeController.startNewScene();
    }

    @Override
    default void refresh() {

    }


}
