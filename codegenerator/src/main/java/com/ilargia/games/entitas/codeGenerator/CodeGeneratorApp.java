package com.ilargia.games.entitas.codeGenerator;

import com.ilargia.games.entitas.codeGenerator.generators.ComponentExtensionsGenerator;
import com.ilargia.games.entitas.codeGenerator.generators.ComponentIndicesGenerator;
import com.ilargia.games.entitas.codeGenerator.generators.PoolsGenerator;
import com.ilargia.games.entitas.codeGenerator.interfaces.ICodeGenerator;
import com.ilargia.games.entitas.codeGenerator.intermediate.CodeGenFile;
import com.ilargia.games.entitas.codeGenerator.providers.TypeReflectionProvider;
import javafx.application.Application;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class CodeGeneratorApp extends Application implements Initializable {

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
    @FXML
    private Button btnGenerate;
    @FXML
    ProgressIndicator progress;
    @FXML
    Label result;

    public static void main(String[] args) {
        launch(args);

    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("EntitasGenerator.fxml"));
        Parent root = loader.load();
        primaryStage.setTitle("CodeGenerator");
        primaryStage.setScene(new Scene(root, 560, 575));
        primaryStage.setResizable(false);
        primaryStage.show();

        stage = primaryStage;

    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        progress.setVisible(false);
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
        result.setText("");
        progress.setVisible(true);

        // loads the items at another thread, asynchronously
        Task loader = new Task<List<CodeGenFile>>() {
            {
                setOnSucceeded(workerStateEvent -> {
                    progress.setVisible(false);
                    result.setText("Success");
                });

                setOnFailed(workerStateEvent -> {
                        result.setText("Failed");
                        getException().printStackTrace();
                });

            }

            @Override
            protected List<CodeGenFile> call() throws Exception {
                List<ICodeGenerator> codeGenerators = new ArrayList<>();

                if (componentsGenerator.isSelected())
                    codeGenerators.add(new ComponentExtensionsGenerator());
                if (componentIndicesGenerator.isSelected())
                    codeGenerators.add(new ComponentIndicesGenerator());
                if (poolsGenerator.isSelected())
                    codeGenerators.add(new PoolsGenerator());

                TypeReflectionProvider provider = new TypeReflectionProvider(fieldComponentFolder.getText());
                CodeGenerator generator = new CodeGenerator();
                return generator.generate(provider, fieldGeneratedFolder.getText(), codeGenerators);
            }
        };

        Thread loadingThread = new Thread(loader, "generated-loader");
        loadingThread.setDaemon(true);
        loadingThread.start();


    }
}
