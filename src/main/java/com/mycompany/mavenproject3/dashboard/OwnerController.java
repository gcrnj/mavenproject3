package com.mycompany.mavenproject3.dashboard;

import com.mycompany.mavenproject3.models.DbHelper;
import com.mycompany.mavenproject3.models.Owner;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class OwnerController {

    @FXML
    private TableView<Owner> ownerTable;
    @FXML
    private TableColumn<Owner, Integer> colOwnerId;
    @FXML
    private TableColumn<Owner, String> colName;
    @FXML
    private TableColumn<Owner, String> colContact;

    @FXML
    private TextField txtOwnerId;
    @FXML
    private TextField txtName;
    @FXML
    private TextField txtContact;

    private ObservableList<Owner> ownerList = FXCollections.observableArrayList();
    private Connection connection;

    @FXML
    public void initialize() {
        // Initialize database connection
        connection = DbHelper.getConnection();

        // Set up table columns
        colOwnerId.setCellValueFactory(new PropertyValueFactory<>("ownerId"));
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colContact.setCellValueFactory(new PropertyValueFactory<>("contactInfo"));

        // Load owner data
        loadOwners();
    }

    private void loadOwners() {
        ownerList.clear();
        try (PreparedStatement statement = connection.prepareStatement("SELECT * FROM dbo.Owner")) {
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                ownerList.add(new Owner(rs.getInt("OwnerId"), rs.getString("Name"), rs.getString("ContactInfo")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        ownerTable.setItems(ownerList);
    }

    @FXML
    private void handleAdd() {
        String name = txtName.getText();
        String contact = txtContact.getText();

        try (PreparedStatement statement = connection.prepareStatement("INSERT INTO dbo.Owner (Name, ContactInfo) VALUES (?, ?)")) {
            statement.setString(1, name);
            statement.setString(2, contact);
            statement.executeUpdate();
            loadOwners();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleEdit() {
        Owner selectedOwner = ownerTable.getSelectionModel().getSelectedItem();
        if (selectedOwner != null) {
            txtOwnerId.setText(String.valueOf(selectedOwner.getOwnerId()));
            txtName.setText(selectedOwner.getName());
            txtContact.setText(selectedOwner.getContactInfo());
        }
    }

    @FXML
    private void handleSave() {
        int ownerId = Integer.parseInt(txtOwnerId.getText());
        String name = txtName.getText();
        String contact = txtContact.getText();

        try (PreparedStatement statement = connection.prepareStatement("UPDATE dbo.Owner SET Name = ?, ContactInfo = ? WHERE OwnerId = ?")) {
            statement.setString(1, name);
            statement.setString(2, contact);
            statement.setInt(3, ownerId);
            statement.executeUpdate();
            loadOwners();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleDelete() {
        Owner selectedOwner = ownerTable.getSelectionModel().getSelectedItem();
        if (selectedOwner != null) {
            try (PreparedStatement statement = connection.prepareStatement("DELETE FROM dbo.Owner WHERE OwnerId = ?")) {
                statement.setInt(1, selectedOwner.getOwnerId());
                statement.executeUpdate();
                loadOwners();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}