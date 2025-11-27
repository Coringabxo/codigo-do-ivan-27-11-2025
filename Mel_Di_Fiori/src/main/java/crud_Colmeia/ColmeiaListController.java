package crud_Colmeia;

import dao.DAO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.StackPane;
import model.Colmeia;

import java.math.BigDecimal;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

public class ColmeiaListController {

    @FXML private TableView<Colmeia> tableEnergetico;
    
    // Colunas ORIGINAIS
    @FXML private TableColumn<Colmeia, Long> colId;
    @FXML private TableColumn<Colmeia, String> colIdentificacao;
    @FXML private TableColumn<Colmeia, String> colLocalizacao;
    @FXML private TableColumn<Colmeia, String> colTipo;
    @FXML private TableColumn<Colmeia, String> colStatus;
    @FXML private TableColumn<Colmeia, String> colData;
    @FXML private TableColumn<Colmeia, Integer> colQuadros;
    
    // NOVAS COLUNAS
    @FXML private TableColumn<Colmeia, BigDecimal> colPrecoCusto;
    @FXML private TableColumn<Colmeia, BigDecimal> colPrecoVenda;
    @FXML private TableColumn<Colmeia, String> colFornecedor;
    @FXML private TableColumn<Colmeia, String> colCategoria;
    @FXML private TableColumn<Colmeia, Integer> colEstoqueAtual;
    @FXML private TableColumn<Colmeia, Integer> colEstoqueMinimo;
    @FXML private TableColumn<Colmeia, String> colCodigoBarras;

    private final ObservableList<Colmeia> dados = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        System.out.println("✅ Inicializando tabela ampliada de energéticos...");
        configurarColunas();
        carregarColmeias();
    }

    private void configurarColunas() {
        // Colunas ORIGINAIS
        colId.setCellValueFactory(c -> new javafx.beans.property.SimpleObjectProperty<>(c.getValue().getId()));
        colIdentificacao.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getIdentificacao()));
        colLocalizacao.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getLocalizacao()));
        colTipo.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getTipo()));
        colStatus.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getStatus()));

        colData.setCellValueFactory(c ->
            new javafx.beans.property.SimpleStringProperty(
                c.getValue().getDataInstalacao() != null
                    ? c.getValue().getDataInstalacao().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
                    : "Não definida"
            )
        );

        colQuadros.setCellValueFactory(c -> new javafx.beans.property.SimpleObjectProperty<>(c.getValue().getNumeroQuadros()));

        // NOVAS COLUNAS
        colPrecoCusto.setCellValueFactory(new PropertyValueFactory<>("precoCusto"));
        colPrecoVenda.setCellValueFactory(new PropertyValueFactory<>("precoVenda"));
        colFornecedor.setCellValueFactory(new PropertyValueFactory<>("fornecedor"));
        colCategoria.setCellValueFactory(new PropertyValueFactory<>("categoria"));
        colEstoqueAtual.setCellValueFactory(new PropertyValueFactory<>("estoqueAtual"));
        colEstoqueMinimo.setCellValueFactory(new PropertyValueFactory<>("estoqueMinimo"));
        colCodigoBarras.setCellValueFactory(new PropertyValueFactory<>("codigoBarras"));

        // Configurar formatação das colunas monetárias
        configurarFormatadores();
        
        System.out.println("✅ Todas as colunas configuradas!");
    }

    private void configurarFormatadores() {
        // Formatador para Preço Custo
        colPrecoCusto.setCellFactory(column -> new TableCell<Colmeia, BigDecimal>() {
            @Override
            protected void updateItem(BigDecimal item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                    setStyle("");
                } else {
                    setText("R$ " + String.format("%,.2f", item));
                    setStyle("-fx-text-fill: #e74c3c; -fx-font-weight: bold;");
                }
            }
        });

        // Formatador para Preço Venda
        colPrecoVenda.setCellFactory(column -> new TableCell<Colmeia, BigDecimal>() {
            @Override
            protected void updateItem(BigDecimal item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                    setStyle("");
                } else {
                    setText("R$ " + String.format("%,.2f", item));
                    setStyle("-fx-text-fill: #27ae60; -fx-font-weight: bold;");
                }
            }
        });

        // Formatador para Estoque (com cores)
        colEstoqueAtual.setCellFactory(column -> new TableCell<Colmeia, Integer>() {
            @Override
            protected void updateItem(Integer item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                    setStyle("");
                } else {
                    setText(String.valueOf(item));
                    
                    // Verificar estoque baixo
                    Colmeia energetico = getTableView().getItems().get(getIndex());
                    int estoqueMinimo = energetico.getEstoqueMinimo();
                    
                    if (item <= estoqueMinimo) {
                        setStyle("-fx-text-fill: #e74c3c; -fx-font-weight: bold; -fx-background-color: #ffebee;");
                    } else if (item <= estoqueMinimo * 2) {
                        setStyle("-fx-text-fill: #f39c12; -fx-font-weight: bold;");
                    } else {
                        setStyle("-fx-text-fill: #27ae60; -fx-font-weight: bold;");
                    }
                }
            }
        });

        // Formatador para Status (com cores)
        colStatus.setCellFactory(column -> new TableCell<Colmeia, String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                    setStyle("");
                } else {
                    setText(item);
                    switch (item) {
                        case "Ativo":
                            setStyle("-fx-text-fill: #27ae60; -fx-font-weight: bold;");
                            break;
                        case "Inativo":
                            setStyle("-fx-text-fill: #e74c3c; -fx-font-weight: bold;");
                            break;
                        case "Em manutenção":
                            setStyle("-fx-text-fill: #f39c12; -fx-font-weight: bold;");
                            break;
                        default:
                            setStyle("-fx-text-fill: #95a5a6;");
                    }
                }
            }
        });
    }

    private void carregarColmeias() {
        try {
            System.out.println("🔄 Carregando dados dos energéticos...");
            List<Colmeia> lista = new DAO<>(Colmeia.class).obterTodos(100, 0);
            
            // Preencher dados padrão se necessário
            for (Colmeia colmeia : lista) {
                if (colmeia.getPrecoCusto() == null) colmeia.setPrecoCusto(BigDecimal.ZERO);
                if (colmeia.getPrecoVenda() == null) colmeia.setPrecoVenda(BigDecimal.ZERO);
                if (colmeia.getFornecedor() == null) colmeia.setFornecedor("Não informado");
                if (colmeia.getCategoria() == null) colmeia.setCategoria("Energético");
                if (colmeia.getEstoqueAtual() == 0) colmeia.setEstoqueAtual(0);
                if (colmeia.getEstoqueMinimo() == 0) colmeia.setEstoqueMinimo(5);
            }
            
            dados.setAll(lista);
            tableEnergetico.setItems(dados);
            
            System.out.println("✅ " + lista.size() + " energéticos carregados!");
            
        } catch (Exception e) {
            e.printStackTrace();
            mostrarAlertaErro("Erro ao carregar energéticos: " + e.getMessage());
        }
    }

    // MÉTODOS ORIGINAIS (mantidos iguais)
    @FXML
    private void abrirCadastro() {
        try {
            java.net.URL url = getClass().getResource("/telas/view/TelaCadastroColmeia.fxml");
            if (url == null) {
                mostrarAlertaErro("Arquivo de cadastro não encontrado.");
                return;
            }
            
            FXMLLoader loader = new FXMLLoader(url);
            Node telaCadastro = loader.load();
            
            StackPane painel = obterPainelConteudo();
            if (painel != null) {
                painel.getChildren().setAll(telaCadastro);
            }
            
        } catch (Exception e) {
            e.printStackTrace();
            mostrarAlertaErro("Erro ao abrir cadastro: " + e.getMessage());
        }
    }

    @FXML
    private void abrirImportacaoLote() {
        try {
            java.net.URL url = getClass().getResource("/telas/view/TelaImportacaoLote.fxml");
            if (url == null) {
                mostrarAlertaErro("Arquivo de importação não encontrado.");
                return;
            }
            
            FXMLLoader loader = new FXMLLoader(url);
            Node telaImportacao = loader.load();
            
            StackPane painel = obterPainelConteudo();
            if (painel != null) {
                painel.getChildren().setAll(telaImportacao);
            }
            
        } catch (Exception e) {
            e.printStackTrace();
            mostrarAlertaErro("Erro ao abrir importação: " + e.getMessage());
        }
    }

    @FXML
    private void editarColmeia() {
        Colmeia selecionada = tableEnergetico.getSelectionModel().getSelectedItem();
        if (selecionada == null) {
            mostrarAlertaAviso("Selecione um energético para editar.");
            return;
        }
    
        try {
            java.net.URL url = getClass().getResource("/telas/view/TelaEditColmeia.fxml");
            if (url == null) {
                mostrarAlertaErro("Arquivo de edição não encontrado.");
                return;
            }
            
            FXMLLoader loader = new FXMLLoader(url);
            Node telaEditar = loader.load();
    
            ColmeiaEditController controller = loader.getController();
            controller.setColmeia(selecionada);
    
            StackPane painel = obterPainelConteudo();
            if (painel != null) {
                painel.getChildren().setAll(telaEditar);
            }
    
        } catch (Exception e) {
            e.printStackTrace();
            mostrarAlertaErro("Erro ao abrir edição: " + e.getMessage());
        }
    }
    
    @FXML
    private void excluirColmeia() {
        Colmeia selecionada = tableEnergetico.getSelectionModel().getSelectedItem();
        if (selecionada == null) {
            mostrarAlertaAviso("Selecione um energético para excluir.");
            return;
        }
        
        Alert confirmacao = new Alert(Alert.AlertType.CONFIRMATION);
        confirmacao.setTitle("Confirmação de exclusão");
        confirmacao.setHeaderText("Excluir Energético");
        confirmacao.setContentText("Tem certeza que deseja excluir: " + selecionada.getIdentificacao() + "?");
        
        Optional<ButtonType> resultado = confirmacao.showAndWait();
        if (resultado.isPresent() && resultado.get() == ButtonType.OK) {
            try {
                new DAO<>(Colmeia.class).removerPorIdTransacional(selecionada.getId());
                carregarColmeias();
                mostrarAlertaSucesso("Energético excluído com sucesso!");
            } catch (Exception e) {
                e.printStackTrace();
                mostrarAlertaErro("Erro ao excluir energético: " + e.getMessage());
            }
        }
    }

    @FXML
    private void atualizarTabela() {
        carregarColmeias();
        mostrarAlertaSucesso("Tabela atualizada com sucesso!");
    }

    private StackPane obterPainelConteudo() {
        if (tableEnergetico != null && tableEnergetico.getScene() != null) {
            Node node = tableEnergetico.getScene().lookup("#painelConteudo");
            if (node instanceof StackPane) {
                return (StackPane) node;
            }
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