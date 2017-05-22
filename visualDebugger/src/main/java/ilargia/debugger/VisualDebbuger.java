package com.ilargia.games.entitas.visual;

import com.j256.simplejmx.client.JmxClient;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

import javax.management.MBeanAttributeInfo;
import javax.management.MBeanOperationInfo;
import javax.management.ObjectName;
import java.io.*;
import java.net.URL;
import java.util.Properties;
import java.util.ResourceBundle;
import java.util.Set;

public class VisualDebbuger extends Application implements Initializable {

    public Stage stage;
    JmxClient client;
    @FXML
    Label beanNames;
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
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("VisualDebbuger.fxml"));
        Parent root = loader.load();
        primaryStage.setTitle("CodeGenerator");
        primaryStage.setScene(new Scene(root, 560, 575));
        primaryStage.setResizable(false);
        primaryStage.show();

        stage = primaryStage;


        client = new JmxClient("localhost", 1313);

        Set<ObjectName> names = client.getBeanNames();

        beanNames.setText(names.stream().map(n-> n.getKeyPropertyListString()).reduce(String::concat).get());

//        MBeanAttributeInfo[] attributeInfos =
//                client.getAttributesInfo(objectName);
//        MBeanOperationInfo[] operationInfos =
//                client.getOperationsInfo(objectName);



    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
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

        result.setText("Generating...");

        if (props != null) saveProperties();

        // loads the items at another thread, asynchronously
//        Task loader = new Task<List<CodeGenFile>>() {
//            {
//                setOnSucceeded(workerStateEvent -> {
//                    progress.setVisible(false);
//                    result.setText("Success");
//                });
//
//                setOnFailed(workerStateEvent -> {
//                    result.setText("Failed");
//                    getException().printStackTrace();
//                });
//
//            }
//
//            @Override
//            protected List<CodeGenFile> call() throws Exception {
//                List<ICodeGenerator> codeGenerators = new ArrayList<>();
//
//                if (componentsGenerator.isSelected())
//                    codeGenerators.add(new EntityGenerator());
//                if (componentIndicesGenerator.isSelected())
//                    codeGenerators.add(new ComponentIndicesGenerator());
//                if (contextsGenerator.isSelected())
//                    codeGenerators.add(new ContextGenerator());
//
//                codeGenerators.add(new MatcherGenerator());
//                codeGenerators.add(new EntitasGenerator());
//
//                TypeReflectionProvider provider = new TypeReflectionProvider(fieldComponentFolder.getText());
//                CodeGeneratorOld generator = new CodeGeneratorOld();
//                return generator.generate(provider, fieldGeneratedFolder.getText(), codeGenerators);
//            }
//        };

//        Thread loadingThread = new Thread(loader, "generated-loader");
//        loadingThread.setDaemon(true);
//        loadingThread.start();


    }
}
