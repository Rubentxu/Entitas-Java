package com.ilargia.games.entitas.codeGeneration;

import com.ilargia.games.entitas.codeGeneration.config.CodeGeneratorConfig;
import com.ilargia.games.entitas.codeGeneration.interfaces.ICodeGenerator;
import com.ilargia.games.entitas.codeGeneration.interfaces.ICodeDataProvider;
import com.ilargia.games.entitas.codeGeneration.dataProviders.components.ComponentDataProvider;
import com.ilargia.games.entitas.codeGeneration.data.CodeGenFile;
import com.ilargia.games.entitas.codeGeneration.data.SourceDataFile;
import com.ilargia.games.entitas.codeGeneration.data.StoreCodeGenerator;
import com.ilargia.games.entitas.codeGenerator.CodeGeneratorOld;
import com.ilargia.games.entitas.codeGeneration.config.Preferences;
import com.ilargia.games.entitas.codeGenerator.generators.*;
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
import org.jboss.forge.roaster.Roaster;
import org.jboss.forge.roaster.model.source.JavaClassSource;

import java.io.*;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

public class CodeGeneratorJFX extends Application implements Initializable {

    private StoreCodeGenerator storeCodeGenerator;
    public Stage stage;
    public String path;
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

    public CodeGeneratorJFX() {
        this.storeCodeGenerator = new StoreCodeGenerator();
    }

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
                codeGenerators.add(new EntitasGenerator());

                TypeReflectionProvider provider = new TypeReflectionProvider(fieldComponentFolder.getText());
                CodeGeneratorOld generator = new CodeGeneratorOld();
                return generator.generate(provider, fieldGeneratedFolder.getText(), codeGenerators);
            }
        };

        Thread loadingThread = new Thread(loader, "generated-loader");
        loadingThread.setDaemon(true);
        loadingThread.start();


    }

    public static Map<String, List<File>> readFileComponents(String pathComponents) {
        Map<String, List<File>> recursiveList = new HashMap() {{
            put("", new ArrayList<>());
        }};
        File d = new File(pathComponents);

        if (d.isDirectory()) {
            for (File listFile : d.listFiles()) {
                if (listFile.isDirectory()) {
                    List<File> listSubDir = Arrays.asList(listFile.listFiles());
                    if (listSubDir.size() > 0) {
                        Path path = Paths.get(listSubDir.get(0).getAbsolutePath());
                        String subDir = path.getName(path.getNameCount() - 2).toString();
                        recursiveList.put(subDir, listSubDir);
                    }

                } else {
                    recursiveList.get("").add(listFile);
                }
            }

        }
        return recursiveList;

    }

    private void getSourceDataFile() {
        List<SourceDataFile> sources = new ArrayList<>();
        Map<String, List<File>> mapFiles = readFileComponents(path);
        mapFiles.forEach((subDir, files) -> {
            sources.addAll(files.stream()
                    .map((file) -> {
                        try {
                            return Roaster.parse(JavaClassSource.class, file);
                        } catch (FileNotFoundException e) {
                            return null;
                        }
                    }).filter((source) -> source != null)
                    .filter((source) -> source.getInterfaces().toString().matches(".*\\bIComponent\\b.*"))
                    .map((source) -> new SourceDataFile(source.getName(), null, subDir, source))
                    .collect(Collectors.toList()));
        });
    }


//    public static void generate() {
//
//        CodeGeneratorConfig config = new CodeGeneratorConfig(Preferences.loadProperties());
//
//        var codeGenerator = new CodeGenerator(
//                getEnabled < ICodeDataProvider > (config.dataProviders),
//                getEnabled < ICodeGenerator > (config.codeGenerators),
//                getEnabled < ICodeGenFilePostProcessor > (config.postProcessors)
//        );
//
//        var dryFiles = codeGenerator.DryRun();
//        var sloc = dryFiles
//                .Select(file = > file.source.ToUnixLineEndings())
//                .Sum(content = > content.Split(new[]{
//            '\n'
//        },StringSplitOptions.RemoveEmptyEntries).Length);
//
//        var files = codeGenerator.Generate();
//        var totalGeneratedFiles = files.Select(file = > file.fileName).Distinct().Count();
//        var loc = files
//                .Select(file = > file.source.ToUnixLineEndings())
//                .Sum(content = > content.Split(new[]{
//            '\n'
//        }).Length);
//
//        foreach(var file in files) {
//            Debug.Log(file.generatorName + ": " + file.fileName);
//        }
//
//        Debug.Log("Generated " + totalGeneratedFiles + " files (" + sloc + " sloc, " + loc + " loc)");
//
//        AssetDatabase.Refresh();
//    }
//
//    static List<ICodeDataProvider> getGeneratorDataProvider(String[] types,) {
//        return new ArrayList<>({{
//                add(new ComponentDataProvider())
//        }})
//    }
//
//    public static Type[] GetTypes<T>()
//
//    {
//        return Assembly.GetAssembly(typeof(T)).GetTypes()
//                .Where(type = > type.ImplementsInterface < T > ())
//                .OrderBy(type = > type.FullName)
//                .ToArray();
//    }
}
