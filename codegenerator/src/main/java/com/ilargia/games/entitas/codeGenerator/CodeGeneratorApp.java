package com.ilargia.games.entitas.codeGenerator;

import com.ilargia.games.entitas.codeGenerator.generators.EntityGenerator;
import com.ilargia.games.entitas.codeGenerator.generators.ComponentIndicesGenerator;
import com.ilargia.games.entitas.codeGenerator.generators.ContextGenerator;
import com.ilargia.games.entitas.codeGenerator.generators.MatcherGenerator;
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
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.ResourceBundle;

public class CodeGeneratorApp extends Application implements Initializable {

    public Stage stage;
    @FXML
    ProgressIndicator progress;
    @FXML
    Label result;
    @FXML
    private CheckBox componentIndicesGenerator;
    @FXML
    private CheckBox componentsGenerator;
    @FXML
    private CheckBox contextsGenerator;
    @FXML
    private TextField fieldComponentFolder;
    @FXML
    private TextField fieldGeneratedFolder;
    @FXML
    private Button btnGenerate;
    private Properties props;

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
        try {
            loadProperties();
        } catch (Exception ex) {
            props = new Properties();
        }


    }

    private void loadProperties() throws Exception {
        Properties prop = new Properties();
        InputStream input = null;

        try {
            input = new FileInputStream("config_generator.properties");
            prop.load(input);
            fieldComponentFolder.setText(prop.getProperty("fieldComponentFolder"));
            fieldGeneratedFolder.setText(prop.getProperty("fieldGeneratedFolder"));

        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void saveProperties() {

        OutputStream output = null;
        try {
            output = new FileOutputStream("config_generator.properties");
            props.store(output, null);

        } catch (IOException io) {
            io.printStackTrace();
        } finally {
            if (output != null) {
                try {
                    output.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }
    }

    @FXML
    public void handleComponentsFolder(ActionEvent event) {
        final DirectoryChooser directoryChooser = new DirectoryChooser();
        final File selectedDirectory =
                directoryChooser.showDialog(stage);
        if (selectedDirectory != null) {
            fieldComponentFolder.setText(selectedDirectory.getAbsolutePath());
            if (props != null)
                props.setProperty("fieldComponentFolder", selectedDirectory.getAbsolutePath());
        }

    }

    @FXML
    public void handleGeneratedFolder(ActionEvent event) {
        final DirectoryChooser directoryChooser = new DirectoryChooser();
        final File selectedDirectory =
                directoryChooser.showDialog(stage);
        if (selectedDirectory != null) {
            fieldGeneratedFolder.setText(selectedDirectory.getAbsolutePath());
            if (props != null)
                props.setProperty("fieldGeneratedFolder", selectedDirectory.getAbsolutePath());
        }
    }

    @FXML
    public void handleGenerate(ActionEvent actionEvent) throws IOException {
        result.setText("");
        progress.setVisible(true);
        result.setText("Generating...");

        if (props != null) saveProperties();

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
                    codeGenerators.add(new EntityGenerator());
                if (componentIndicesGenerator.isSelected())
                    codeGenerators.add(new ComponentIndicesGenerator());
                if (contextsGenerator.isSelected())
                    codeGenerators.add(new ContextGenerator());

                codeGenerators.add(new MatcherGenerator());

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
