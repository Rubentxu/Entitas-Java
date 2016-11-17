package com.ilargia.games.entitas.codeGenerator;


import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

public class CodeGeneratorController {


    @FXML private CheckBox componentIndicesGenerator;
    @FXML private CheckBox componentsGenerator;
    @FXML private CheckBox poolsGenerator;
    @FXML private TextField fieldComponentFolder;
    @FXML private TextField fieldGeneratedFolder;
    public Stage stage;

    @FXML
    private void initialize() {


    }


    @FXML
    public void handleComponentsFolder(ActionEvent event) {
        final DirectoryChooser directoryChooser = new DirectoryChooser();
        final File selectedDirectory =
                directoryChooser.showDialog(stage);
        if (selectedDirectory != null) {
            fieldComponentFolder.setText(selectedDirectory.getAbsolutePath());
        }
    }

    @FXML
    public void handleGeneratedFolder(ActionEvent event) {
        final DirectoryChooser directoryChooser = new DirectoryChooser();
        final File selectedDirectory =
                directoryChooser.showDialog(stage);
        if (selectedDirectory != null) {
            fieldGeneratedFolder.setText(selectedDirectory.getAbsolutePath());
        }
    }


    public CheckBox getComponentIndicesGenerator() {
        return componentIndicesGenerator;
    }

    public void setComponentIndicesGenerator(CheckBox componentIndicesGenerator) {
        this.componentIndicesGenerator = componentIndicesGenerator;
    }

    public CheckBox getComponentsGenerator() {
        return componentsGenerator;
    }

    public void setComponentsGenerator(CheckBox componentsGenerator) {
        this.componentsGenerator = componentsGenerator;
    }

    public CheckBox getPoolsGenerator() {
        return poolsGenerator;
    }

    public void setPoolsGenerator(CheckBox poolsGenerator) {
        this.poolsGenerator = poolsGenerator;
    }

    public TextField getFieldComponentFolder() {
        return fieldComponentFolder;
    }

    public void setFieldComponentFolder(TextField fieldComponentFolder) {
        this.fieldComponentFolder = fieldComponentFolder;
    }

    public TextField getFieldGeneratedFolder() {
        return fieldGeneratedFolder;
    }

    public void setFieldGeneratedFolder(TextField fieldGeneratedFolder) {
        this.fieldGeneratedFolder = fieldGeneratedFolder;
    }
}
