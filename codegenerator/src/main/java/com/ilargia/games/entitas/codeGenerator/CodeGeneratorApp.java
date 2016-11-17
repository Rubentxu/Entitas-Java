package com.ilargia.games.entitas.codeGenerator;/**
 * Created by rubentxu on 17/11/16.
 */

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class CodeGeneratorApp extends Application {

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
        CodeGeneratorController controller = (CodeGeneratorController) loader.getController();
        controller.stage = primaryStage;
        primaryStage.show();

    }
}
