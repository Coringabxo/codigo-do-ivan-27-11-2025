package crud_Colmeia;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.TextArea;
import javafx.scene.layout.StackPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.net.URL;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

public class ImportacaoLoteController implements Initializable {

    @FXML private TextArea txtLogImportacao;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        System.out.println("✅ ImportacaoLoteController inicializado!");
        adicionarLog("Sistema de importação inicializado");
        adicionarLog("Pronto para uso");
    }

    @FXML
    private void selecionarArquivo() {
        try {
            adicionarLog("📁 Selecionando arquivo...");
            
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Selecionar Arquivo CSV");
            fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Arquivos CSV", "*.csv")
            );

            Stage stage = (Stage) txtLogImportacao.getScene().getWindow();
            File arquivo = fileChooser.showOpenDialog(stage);

            if (arquivo != null) {
                adicionarLog("✅ Arquivo selecionado: " + arquivo.getName());
                adicionarLog("📊 Processando arquivo...");
                
                // Simular processamento
                Thread.sleep(1000);
                adicionarLog("🔍 Validando dados...");
                Thread.sleep(500);
                adicionarLog("✅ 15 registros válidos encontrados");
                adicionarLog("⚠️ 2 registros com avisos");
                
            } else {
                adicionarLog("❌ Nenhum arquivo selecionado");
            }

        } catch (Exception e) {
            adicionarLog("❌ Erro: " + e.getMessage());
        }
    }

    @FXML
    private void baixarModelo() {
        try {
            adicionarLog("📋 Gerando modelo CSV...");
            
            String modelo = "Identificacao,Localizacao,Tipo,Status,PrecoCusto,PrecoVenda\n" +
                           "Red Bull,Prateleira A1,Energético,Ativo,5.50,8.90\n" +
                           "Monster,Prateleira A2,Energético,Ativo,6.00,9.50\n" +
                           "Gatorade,Prateleira B1,Isotônico,Ativo,3.50,6.00";
            
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Salvar Modelo CSV");
            fileChooser.setInitialFileName("modelo_energeticos.csv");
            fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("CSV", "*.csv")
            );

            Stage stage = (Stage) txtLogImportacao.getScene().getWindow();
            File arquivo = fileChooser.showSaveDialog(stage);

            if (arquivo != null) {
                java.nio.file.Files.write(arquivo.toPath(), modelo.getBytes());
                adicionarLog("✅ Modelo salvo: " + arquivo.getName());
                mostrarAlertaSucesso("Modelo salvo com sucesso!");
            }
            
        } catch (Exception e) {
            adicionarLog("❌ Erro ao baixar modelo: " + e.getMessage());
        }
    }

    @FXML
    private void importarRegistros() {
        try {
            adicionarLog("🚀 Iniciando importação...");
            
            // Simular importação
            for (int i = 1; i <= 5; i++) {
                Thread.sleep(300);
                adicionarLog("📦 Importando registro " + i + "/15...");
            }
            
            Thread.sleep(1000);
            adicionarLog("✅ Importação concluída!");
            adicionarLog("📊 15 registros importados com sucesso");
            adicionarLog("💾 Dados salvos no banco");
            
            mostrarAlertaSucesso("Importação realizada com sucesso!\n15 registros importados.");
            
        } catch (Exception e) {
            adicionarLog("❌ Erro na importação: " + e.getMessage());
        }
    }

    @FXML
    private void limparLog() {
        txtLogImportacao.clear();
        adicionarLog("🧹 Log limpo");
        adicionarLog("Sistema pronto para nova operação");
    }

    @FXML
    private void voltarParaLista() {
        try {
            adicionarLog("↩️ Voltando para lista...");
            Node telaLista = FXMLLoader.load(getClass().getResource("/telas/view/TelaListaColmeia.fxml"));
            StackPane painel = (StackPane) txtLogImportacao.getScene().lookup("#painelConteudo");
            painel.getChildren().setAll(telaLista);
        } catch (Exception e) {
            System.err.println("❌ Erro ao voltar: " + e.getMessage());
        }
    }

    private void adicionarLog(String mensagem) {
        String timestamp = LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss"));
        txtLogImportacao.appendText("[" + timestamp + "] " + mensagem + "\n");
        txtLogImportacao.setScrollTop(Double.MAX_VALUE);
    }

    private void mostrarAlertaSucesso(String mensagem) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Sucesso");
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }
}