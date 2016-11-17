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


}
