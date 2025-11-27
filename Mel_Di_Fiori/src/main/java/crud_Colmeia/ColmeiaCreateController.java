package crud_Colmeia;

import dao.DAO;
import model.Colmeia;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import java.math.BigDecimal;
import java.time.LocalDate;

public class ColmeiaCreateController {
    
    @FXML
    private TextField txtNumero;
    
    @FXML
    private DatePicker dateInstalacao;
    
    @FXML
    private TextField txtLocalizacao;
    
    @FXML
    private ComboBox<String> comboSituacao;
    
    @FXML
    private ComboBox<String> comboTipo;
    
    @FXML
    private Spinner<Integer> spinnerNumeroQuadros;
    
    @FXML
    private TextArea txtObservacoes;

    // NOVOS CAMPOS ADICIONADOS
    @FXML private ComboBox<String> comboFornecedor;
    @FXML private TextField txtPrecoCusto;
    @FXML private TextField txtPrecoVenda;
    @FXML private Spinner<Integer> spinnerEstoqueAtual;
    @FXML private Spinner<Integer> spinnerEstoqueMinimo;
    @FXML private TextField txtCodigoBarras;

    @FXML
    public void initialize() {
        System.out.println("✅ ColmeiaCreateController inicializado");
        
        // Configurar ComboBox (ORIGINAL + NOVOS)
        comboSituacao.getItems().addAll("Ativa", "Inativa", "Em manutenção", "Desativada");
        comboTipo.getItems().addAll("Caixote", "Caixa de papelão", "Caixa de isopor", "Outros");
        
        // NOVOS COMBOBOX
        comboFornecedor.getItems().addAll("Red Bull", "Monster", "Coca-Cola", "Pepsi", "Ambev", "Outro");

        // Configurar Spinner (ORIGINAL)
        SpinnerValueFactory<Integer> valueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 20, 10);
        spinnerNumeroQuadros.setValueFactory(valueFactory);
        
        // NOVOS SPINNERS
        SpinnerValueFactory<Integer> valueFactoryEstoque = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 1000, 50);
        spinnerEstoqueAtual.setValueFactory(valueFactoryEstoque);
        
        SpinnerValueFactory<Integer> valueFactoryEstoqueMin = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 100, 10);
        spinnerEstoqueMinimo.setValueFactory(valueFactoryEstoqueMin);
        
        // Definir valores padrão (ORIGINAL + NOVOS)
        comboSituacao.setValue("Ativa");
        comboTipo.setValue("Caixote");
        comboFornecedor.setValue("Red Bull");
        dateInstalacao.setValue(LocalDate.now());
        
        System.out.println("✅ Campos inicializados com valores padrão");
    }

    @FXML
    private void salvarEnergetico() {
        try {
            System.out.println("🔄 Tentando salvar nova Carga de Energéticos...");
            
            // Validar campos
            if (!validarCampos()) {
                System.out.println("❌ Validação falhou");
                return;
            }

            // Coletar dados dos campos (ORIGINAL)
            String numero = txtNumero.getText().trim();
            LocalDate data = dateInstalacao.getValue();
            String local = txtLocalizacao.getText().trim();
            String situacao = comboSituacao.getValue();
            String tipo = comboTipo.getValue();
            int numeroQuadros = spinnerNumeroQuadros.getValue();
            String obs = txtObservacoes.getText().trim();

            // NOVOS CAMPOS
            String fornecedor = comboFornecedor.getValue();
            int estoqueAtual = spinnerEstoqueAtual.getValue();
            int estoqueMinimo = spinnerEstoqueMinimo.getValue();
            String codigoBarras = txtCodigoBarras.getText().trim();
            
            // Converter preços
            BigDecimal precoCusto = BigDecimal.ZERO;
            BigDecimal precoVenda = BigDecimal.ZERO;
            
            try {
                if (!txtPrecoCusto.getText().trim().isEmpty()) {
                    precoCusto = new BigDecimal(txtPrecoCusto.getText().trim().replace(",", "."));
                }
                if (!txtPrecoVenda.getText().trim().isEmpty()) {
                    precoVenda = new BigDecimal(txtPrecoVenda.getText().trim().replace(",", "."));
                }
            } catch (NumberFormatException e) {
                mostrarAlertaErro("Erro nos campos de preço. Use números válidos (ex: 5.50)");
                return;
            }

            System.out.println("📊 Dados coletados:");
            System.out.println("   Número: " + numero);
            System.out.println("   Data: " + data);
            System.out.println("   Local: " + local);
            System.out.println("   Situação: " + situacao);
            System.out.println("   Tipo: " + tipo);
            System.out.println("   Quadros: " + numeroQuadros);
            System.out.println("   Fornecedor: " + fornecedor);
            System.out.println("   Preço Custo: " + precoCusto);
            System.out.println("   Preço Venda: " + precoVenda);
            System.out.println("   Estoque: " + estoqueAtual);

            // Criar nova colmeia
            System.out.println("🔄 Criando objeto Energético...");
            Colmeia nova = new Colmeia();
            nova.setIdentificacao(numero);
            nova.setLocalizacao(local);
            nova.setTipo(tipo);
            nova.setStatus(situacao);
            nova.setDataInstalacao(data);
            nova.setNumeroQuadros(numeroQuadros);
            nova.setObservacoes(obs);
            
            // NOVOS CAMPOS
            nova.setFornecedor(fornecedor);
            nova.setPrecoCusto(precoCusto);
            nova.setPrecoVenda(precoVenda);
            nova.setEstoqueAtual(estoqueAtual);
            nova.setEstoqueMinimo(estoqueMinimo);
            nova.setCodigoBarras(codigoBarras);
            
            System.out.println("✅ Objeto Energético criado: " + nova);

            // Salvar no banco
            System.out.println("🔄 Salvando no banco...");
            DAO<Colmeia> dao = new DAO<>(Colmeia.class);
            dao.incluirTransacional(nova);
            
            System.out.println("✅ Energético salvo no banco com sucesso!");

            // Mensagem de sucesso
            mostrarAlertaSucesso("Energético cadastrado com sucesso!");

            // Limpar campos para novo cadastro
            limparCampos();

        } catch (Exception e) {
            System.err.println("❌ ERRO ao salvar Energético:");
            e.printStackTrace();
            mostrarAlertaErro("Erro ao cadastrar Energético: " + e.getMessage());
        }
    }

    private boolean validarCampos() {
        System.out.println("🔄 Validando campos...");
        limparEstiloErro();
        boolean valido = true;

        // Validar número
        if (txtNumero.getText() == null || txtNumero.getText().trim().isEmpty()) {
            System.out.println("❌ Campo número está vazio");
            colocarBordaVermelha(txtNumero);
            valido = false;
        }

        // Validar localização
        if (txtLocalizacao.getText() == null || txtLocalizacao.getText().trim().isEmpty()) {
            System.out.println("❌ Campo localização está vazio");
            colocarBordaVermelha(txtLocalizacao);
            valido = false;
        }

        // Validar tipo
        if (comboTipo.getValue() == null) {
            System.out.println("❌ Campo tipo não selecionado");
            colocarBordaVermelha(comboTipo);
            valido = false;
        }

        // Validar situação
        if (comboSituacao.getValue() == null) {
            System.out.println("❌ Campo situação não selecionado");
            colocarBordaVermelha(comboSituacao);
            valido = false;
        }

        // Validar fornecedor (NOVO)
        if (comboFornecedor.getValue() == null) {
            System.out.println("❌ Campo fornecedor não selecionado");
            colocarBordaVermelha(comboFornecedor);
            valido = false;
        }

        // Validar data
        if (dateInstalacao.getValue() == null) {
            System.out.println("❌ Campo data não selecionado");
            colocarBordaVermelha(dateInstalacao);
            valido = false;
        }

        System.out.println("📋 Validação dos campos: " + (valido ? "VÁLIDO" : "INVÁLIDO"));
        return valido;
    }

    private void limparEstiloErro() {
        txtNumero.setStyle("");
        txtLocalizacao.setStyle("");
        comboTipo.setStyle("");
        comboSituacao.setStyle("");
        comboFornecedor.setStyle("");
        dateInstalacao.setStyle("");
        txtPrecoCusto.setStyle("");
        txtPrecoVenda.setStyle("");
    }

    private void colocarBordaVermelha(Control campo) {
        campo.setStyle("-fx-border-color: red; -fx-border-width: 2;");
    }

    @FXML
    private void limparCampos() {
        System.out.println("🔄 Limpando campos...");
        txtNumero.clear();
        dateInstalacao.setValue(LocalDate.now());
        txtLocalizacao.clear();
        comboSituacao.setValue("Ativa");
        comboTipo.setValue("Caixote");
        comboFornecedor.setValue("Red Bull");
        spinnerNumeroQuadros.getValueFactory().setValue(10);
        spinnerEstoqueAtual.getValueFactory().setValue(50);
        spinnerEstoqueMinimo.getValueFactory().setValue(10);
        txtPrecoCusto.clear();
        txtPrecoVenda.clear();
        txtCodigoBarras.clear();
        txtObservacoes.clear();
        limparEstiloErro();
        System.out.println("✅ Campos limpos com sucesso");
    }

    // Método para teste rápido - preencher com dados de exemplo
    @FXML
    private void preencherComDadosExemplo() {
        System.out.println("🔄 Preenchendo com dados de exemplo...");
        txtNumero.setText("Energético " + (System.currentTimeMillis() % 1000));
        txtLocalizacao.setText("Localização Exemplo");
        comboSituacao.setValue("Ativa");
        comboTipo.setValue("Caixote");
        comboFornecedor.setValue("Red Bull");
        dateInstalacao.setValue(LocalDate.now());
        spinnerNumeroQuadros.getValueFactory().setValue(15);
        spinnerEstoqueAtual.getValueFactory().setValue(75);
        spinnerEstoqueMinimo.getValueFactory().setValue(15);
        txtPrecoCusto.setText("5.50");
        txtPrecoVenda.setText("8.90");
        txtCodigoBarras.setText("7891234567890");
        txtObservacoes.setText("Observações de exemplo");
        System.out.println("✅ Dados de exemplo preenchidos");
    }

    private void mostrarAlertaErro(String mensagem) {
        Alert alerta = new Alert(Alert.AlertType.ERROR);
        alerta.setTitle("Erro no Cadastro");
        alerta.setHeaderText("Não foi possível cadastrar");
        alerta.setContentText(mensagem);
        alerta.showAndWait();
    }

    private void mostrarAlertaSucesso(String mensagem) {
        Alert alerta = new Alert(Alert.AlertType.INFORMATION);
        alerta.setTitle("Cadastro Realizado");
        alerta.setHeaderText("Sucesso!");
        alerta.setContentText(mensagem);
        alerta.showAndWait();
    }
}