package com.ilargia.games.entitas.codeGenerator;

import com.ilargia.games.entitas.codeGenerator.generators.ComponentExtensionsGenerator;
import com.ilargia.games.entitas.codeGenerator.generators.ComponentIndicesGenerator;
import com.ilargia.games.entitas.codeGenerator.generators.PoolsGenerator;
import com.ilargia.games.entitas.codeGenerator.interfaces.ICodeGenerator;
import com.ilargia.games.entitas.codeGenerator.providers.TypeReflectionProvider;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CodeGeneratorApp extends Application {

    public Stage stage;
    @FXML
    private CheckBox componentIndicesGenerator;
    @FXML
    private CheckBox componentsGenerator;
    @FXML
    private CheckBox poolsGenerator;
    @FXML
    private TextField fieldComponentFolder;
    @FXML
    private TextField fieldGeneratedFolder;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("EntitasGenerator.fxml"));
        Parent root = loader.load();
        primaryStage.setTitle("CodeGenerator");
        primaryStage.setScene(new Scene(root, 560, 531));
        primaryStage.setResizable(false);
        primaryStage.show();
        stage = primaryStage;

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

    @FXML
    public void handleGenerate(ActionEvent actionEvent) throws IOException {
        List<ICodeGenerator> codeGenerators = new ArrayList<>();

        if (componentsGenerator.isSelected())
            codeGenerators.add(new ComponentExtensionsGenerator());
        if (componentIndicesGenerator.isSelected())
            codeGenerators.add(new ComponentIndicesGenerator());
        if (poolsGenerator.isSelected())
            codeGenerators.add(new PoolsGenerator());

        TypeReflectionProvider provider = new TypeReflectionProvider(fieldComponentFolder.getText());
        CodeGenerator generator = new CodeGenerator();
        generator.generate(provider, fieldGeneratedFolder.getText(), codeGenerators);

    }
}
