package tela.main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.Parent;
import javafx.stage.Stage;

public class App extends Application {

    @Override
    public void start(Stage primaryStage) {
        try {
            System.out.println("🚀 INICIANDO SISTEMA DE ENERGÉTICOS...");
            System.out.println("==========================================");

            // Carrega a tela de login
            Parent root = FXMLLoader.load(getClass().getResource("/telas/view/TelaLogin.fxml"));

            Scene scene = new Scene(root, 1000, 700);
            scene.getStylesheets().add(getClass().getResource("/globalStyle/style.css").toExternalForm());

            primaryStage.setTitle("Login - Sistema de Energéticos");
            primaryStage.setScene(scene);
            primaryStage.setResizable(false);
            primaryStage.centerOnScreen();

            primaryStage.show();

            System.out.println("✅ Tela de login carregada com sucesso!");

        } catch (Exception e) {
            System.err.println("💥 ERRO CRÍTICO: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        System.out.println("⚡ INICIANDO APLICAÇÃO...");
        launch(args);
    }
}