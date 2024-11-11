module com.mycompany.mavenproject3 {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.base;
    requires java.sql; // Required for SQL operations
    requires jbcrypt; // Add this line to require the jbcrypt module

    opens com.mycompany.mavenproject3 to javafx.fxml;
    exports com.mycompany.mavenproject3;
    
    
    // Exporting the dashboard package to javafx.fxml so it can be accessed
    opens com.mycompany.mavenproject3.dashboard to javafx.fxml; // Allow javafx.fxml to access this package
    exports com.mycompany.mavenproject3.dashboard to javafx.fxml;
    
    
    opens com.mycompany.mavenproject3.utils to javafx.fxml;
    exports com.mycompany.mavenproject3.utils to javafx.fxml;
    
    opens com.mycompany.mavenproject3.myforms to javafx.fxml;
    exports com.mycompany.mavenproject3.myforms to javafx.fxml;
    

}
