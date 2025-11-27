package tela_main_controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class MainLayoutController {

    @FXML private Label labelRelogio;
    @FXML private Label labelEstacao;
    @FXML private StackPane painelConteudo;

    @FXML
    public void initialize() {
        System.out.println("✅ MainLayoutController inicializado");
        atualizarRelogio();
        carregarTelaInicial();
    }

    @FXML
    private void abrirBoasVindas() {
        try {
            System.out.println("🔄 Tentando abrir Boas Vindas...");
            Node telaBoasVindas = FXMLLoader.load(getClass().getResource("/telas/view/TelaBoasVindas.fxml"));
            painelConteudo.getChildren().setAll(telaBoasVindas);
            System.out.println("✅ Tela Boas Vindas carregada!");
        } catch (IOException e) {
            System.err.println("❌ Erro ao abrir Boas Vindas: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void abrirDashboard() {
        try {
            System.out.println("🔄 Tentando abrir Dashboard...");
            Node telaDashboard = FXMLLoader.load(getClass().getResource("/telas/view/TelaDashboard.fxml"));
            painelConteudo.getChildren().setAll(telaDashboard);
            System.out.println("✅ Dashboard carregado!");
        } catch (IOException e) {
            System.err.println("❌ Erro ao abrir Dashboard: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void abrirListaColmeia() {
        try {
            System.out.println("🔄 Tentando abrir Lista de Energéticos...");
            Node telaLista = FXMLLoader.load(getClass().getResource("/telas/view/TelaListaColmeia.fxml"));
            painelConteudo.getChildren().setAll(telaLista);
            System.out.println("✅ Lista de Energéticos carregada!");
        } catch (IOException e) {
            System.err.println("❌ Erro ao abrir Lista: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void abrirRelatoriosFinanceiros() {
        try {
            System.out.println("🔄🔄🔄 TENTANDO ABRIR RELATÓRIOS FINANCEIROS...");
            
            // Primeiro, vamos testar se o arquivo existe
            java.net.URL url = getClass().getResource("/telas/view/TelaRelatorioFinanceiro.fxml");
            System.out.println("📁 URL do FXML: " + url);
            
            if (url == null) {
                System.err.println("❌❌❌ ARQUIVO NÃO ENCONTRADO: TelaRelatorioFinanceiro.fxml");
                mostrarAlertaErro("Arquivo TelaRelatorioFinanceiro.fxml não encontrado!\nVerifique se o arquivo existe na pasta /telas/view/");
                return;
            }
            
            System.out.println("✅ Arquivo FXML encontrado!");
            
            // Tentar carregar o FXML
            FXMLLoader loader = new FXMLLoader(url);
            Node telaRelatorios = loader.load();
            
            System.out.println("✅✅✅ Tela de relatórios financeiros carregada com SUCESSO!");
            
            // Trocar a tela
            painelConteudo.getChildren().setAll(telaRelatorios);
            System.out.println("🎉 Tela de relatórios exibida no painel!");
            
        } catch (Exception e) {
            System.err.println("❌❌❌ ERRO CRÍTICO ao abrir relatórios financeiros:");
            e.printStackTrace();
            mostrarAlertaErro("Erro detalhado: " + e.getMessage() + "\n\nVerifique:\n1. Se o arquivo FXML existe\n2. Se o controller está correto\n3. Console para mais detalhes");
        }
    }

    @FXML
    private void sair() {
        System.exit(0);
    }

    private void carregarTelaInicial() {
        abrirBoasVindas();
    }

    private void atualizarRelogio() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        labelRelogio.setText(LocalDateTime.now().format(formatter));
    }

    private void mostrarAlertaErro(String mensagem) {
        javafx.scene.control.Alert alert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.ERROR);
        alert.setTitle("Erro");
        alert.setHeaderText("Problema ao carregar tela");
        alert.setContentText(mensagem);
        alert.showAndWait();
    }
}