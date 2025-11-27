package crud_Colmeia;

import dao.DAO;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.StackPane;
import model.Colmeia;

import java.io.IOException;


public class ColmeiaEditController {

    @FXML private TextField txtNumero;
    @FXML private DatePicker dateInstalacao;
    @FXML private TextField txtLocalizacao;
    @FXML private ComboBox<String> comboSituacao;
    @FXML private ComboBox<String> comboTipo;
    @FXML private Spinner<Integer> spinnerNumeroQuadros;
    @FXML private TextArea txtObservacoes;

    private Colmeia colmeia;

    @FXML
    public void initialize() {
        System.out.println("ColmeiaEditController inicializado");
        
        // Configurar ComboBox
        comboSituacao.getItems().addAll("Ativa", "Inativa", "Em manutenção", "Desativada");
        comboTipo.getItems().addAll("Caixote", "Caixa de papelão", "Caixa de isopor", "Outros");

        // Configurar Spinner
        SpinnerValueFactory<Integer> valueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 20, 10);
        spinnerNumeroQuadros.setValueFactory(valueFactory);
    }

    public void setColmeia(Colmeia colmeia) {
        System.out.println("Setando Energetico para edição: " + (colmeia != null ? colmeia.getIdentificacao() : "null"));
        
        this.colmeia = colmeia;
        if (colmeia != null) {
            preencherCampos(colmeia);
        }
    }

    private void preencherCampos(Colmeia colmeia) {
        try {
            txtNumero.setText(colmeia.getIdentificacao());
            dateInstalacao.setValue(colmeia.getDataInstalacao());
            txtLocalizacao.setText(colmeia.getLocalizacao());
            comboSituacao.setValue(colmeia.getStatus());
            comboTipo.setValue(colmeia.getTipo());
            spinnerNumeroQuadros.getValueFactory().setValue(colmeia.getNumeroQuadros());
            txtObservacoes.setText(colmeia.getObservacoes());
            
            System.out.println("Campos preenchidos com sucesso");
        } catch (Exception e) {
            e.printStackTrace();
            mostrarAlertaErro("Erro ao preencher campos: " + e.getMessage());
        }
    }

    @FXML
    private void salvarColmeia() {
        try {
            System.out.println("Salvando edição dos Energeticos...");
            
            if (!validarCampos()) {
                mostrarAlertaAviso("Preencha todos os campos obrigatórios.");
                return;
            }

            if (colmeia == null) {
                mostrarAlertaErro("Nenhuma Carga selecionada para edição.");
                return;
            }

            // Atualizar dados da colmeia
            colmeia.setIdentificacao(txtNumero.getText().trim());
            colmeia.setDataInstalacao(dateInstalacao.getValue());
            colmeia.setLocalizacao(txtLocalizacao.getText().trim());
            colmeia.setStatus(comboSituacao.getValue());
            colmeia.setTipo(comboTipo.getValue());
            colmeia.setNumeroQuadros(spinnerNumeroQuadros.getValue());
            colmeia.setObservacoes(txtObservacoes.getText().trim());

            // Salvar no banco
            new DAO<>(Colmeia.class).atualizarTransacional(colmeia);

            mostrarAlertaSucesso("Energetico atualizado com sucesso!");
            voltarParaLista();

        } catch (Exception e) {
            e.printStackTrace();
            mostrarAlertaErro("Erro ao salvar Energetico: " + e.getMessage());
        }
    }

    @FXML
    private void limparCampos() {
        if (colmeia != null) {
            preencherCampos(colmeia); // Recarrega os dados originais
        }
    }

    @FXML
    private void cancelarEdicao() {
        voltarParaLista();
    }

    private boolean validarCampos() {
        boolean valido = true;
        limparEstilosErro();

        if (txtNumero.getText() == null || txtNumero.getText().trim().isEmpty()) {
            aplicarEstiloErro(txtNumero);
            valido = false;
        }

        if (txtLocalizacao.getText() == null || txtLocalizacao.getText().trim().isEmpty()) {
            aplicarEstiloErro(txtLocalizacao);
            valido = false;
        }

        if (comboTipo.getValue() == null) {
            aplicarEstiloErro(comboTipo);
            valido = false;
        }

        if (comboSituacao.getValue() == null) {
            aplicarEstiloErro(comboSituacao);
            valido = false;
        }

        if (dateInstalacao.getValue() == null) {
            aplicarEstiloErro(dateInstalacao);
            valido = false;
        }

        return valido;
    }

    private void aplicarEstiloErro(Control campo) {
        campo.setStyle("-fx-border-color: red; -fx-border-width: 2;");
    }

    private void limparEstilosErro() {
        txtNumero.setStyle("");
        txtLocalizacao.setStyle("");
        comboTipo.setStyle("");
        comboSituacao.setStyle("");
        dateInstalacao.setStyle("");
    }

    private void voltarParaLista() {
        try {
            Node telaLista = FXMLLoader.load(getClass().getResource("/telas/view/TelaListaColmeia.fxml"));
            StackPane painel = obterPainelConteudo();
            if (painel != null) {
                painel.getChildren().setAll(telaLista);
            }
        } catch (IOException e) {
            e.printStackTrace();
            mostrarAlertaErro("Erro ao voltar para lista: " + e.getMessage());
        }
    }

    private StackPane obterPainelConteudo() {
        if (txtNumero != null && txtNumero.getScene() != null) {
            return (StackPane) txtNumero.getScene().lookup("#painelConteudo");
        }
        return null;
    }

    private void mostrarAlertaErro(String mensagem) {
        Alert alerta = new Alert(Alert.AlertType.ERROR);
        alerta.setTitle("Erro");
        alerta.setHeaderText(null);
        alerta.setContentText(mensagem);
        alerta.showAndWait();
    }

    private void mostrarAlertaAviso(String mensagem) {
        Alert alerta = new Alert(Alert.AlertType.WARNING);
        alerta.setTitle("Aviso");
        alerta.setHeaderText(null);
        alerta.setContentText(mensagem);
        alerta.showAndWait();
    }

    private void mostrarAlertaSucesso(String mensagem) {
        Alert alerta = new Alert(Alert.AlertType.INFORMATION);
        alerta.setTitle("Sucesso");
        alerta.setHeaderText(null);
        alerta.setContentText(mensagem);
        alerta.showAndWait();
    }
}