package tela_main_controller;

import dao.DAO;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import model.Colmeia;


import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.ResourceBundle;

public class TelaBoasVindasController implements Initializable {

    @FXML private Label labelMensagem;
    @FXML private Label labelDataHora;
    @FXML private Label labelUsuario;
    @FXML private Label labelTotalEnergeticos;
    @FXML private Label labelEnergeticosAtivos;
    @FXML private Label labelTiposDiferentes;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        System.out.println("✅ TelaBoasVindasController inicializada");
        
        // Definir mensagem de boas-vindas baseada no horário
        String mensagem = getMensagemBoasVindas();
        labelMensagem.setText(mensagem);
        
        // Definir data e hora atual
        atualizarDataHora();
        
        // Definir usuário
        labelUsuario.setText("Carlos");
        
        // Carregar estatísticas
        carregarEstatisticas();
        
        System.out.println("🎉 Tela de boas-vindas carregada: " + mensagem);
    }
    
    private String getMensagemBoasVindas() {
        int hora = LocalDateTime.now().getHour();
        
        if (hora >= 5 && hora < 12) {
            return "Bom Dia! 🌅";
        } else if (hora >= 12 && hora < 18) {
            return "Boa Tarde! ☀️";
        } else {
            return "Boa Noite! 🌙";
        }
    }
    
    private void atualizarDataHora() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy - HH:mm");
        String dataHora = LocalDateTime.now().format(formatter);
        labelDataHora.setText(dataHora);
    }
    
    private void carregarEstatisticas() {
        try {
            DAO<Colmeia> dao = new DAO<>(Colmeia.class);
            List<Colmeia> energeticos = dao.obterTodos(1000, 0);
            
            int total = energeticos.size();
            long ativos = energeticos.stream().filter(e -> "Ativa".equals(e.getStatus())).count();
            long tiposDiferentes = energeticos.stream().map(Colmeia::getTipo).distinct().count();
            
            labelTotalEnergeticos.setText(String.valueOf(total));
            labelEnergeticosAtivos.setText(String.valueOf(ativos));
            labelTiposDiferentes.setText(String.valueOf(tiposDiferentes));
            
        } catch (Exception e) {
            System.err.println("❌ Erro ao carregar estatísticas: " + e.getMessage());
            labelTotalEnergeticos.setText("0");
            labelEnergeticosAtivos.setText("0");
            labelTiposDiferentes.setText("0");
        }
    }

    @FXML
    private void irParaLista() {
        try {
            Node telaLista = FXMLLoader.load(getClass().getResource("/telas/view/TelaListaColmeia.fxml"));
            StackPane painel = (StackPane) labelMensagem.getScene().lookup("#painelConteudo");
            painel.getChildren().setAll(telaLista);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void irParaCadastro() {
        try {
            Node telaCadastro = FXMLLoader.load(getClass().getResource("/telas/view/TelaCadastroColmeia.fxml"));
            StackPane painel = (StackPane) labelMensagem.getScene().lookup("#painelConteudo");
            painel.getChildren().setAll(telaCadastro);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}