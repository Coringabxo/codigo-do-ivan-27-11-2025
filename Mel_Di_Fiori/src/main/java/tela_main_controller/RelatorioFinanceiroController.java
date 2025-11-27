package tela_main_controller;

import dao.DAO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import model.RelatorioFinanceiro;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.ResourceBundle;

public class RelatorioFinanceiroController implements Initializable {

    @FXML private TableView<RelatorioFinanceiro> tableRelatorios;
    @FXML private TableColumn<RelatorioFinanceiro, String> colTitulo;
    @FXML private TableColumn<RelatorioFinanceiro, String> colPeriodo;
    @FXML private TableColumn<RelatorioFinanceiro, String> colTipo;
    @FXML private TableColumn<RelatorioFinanceiro, BigDecimal> colReceita;
    @FXML private TableColumn<RelatorioFinanceiro, BigDecimal> colLucro;
    @FXML private TableColumn<RelatorioFinanceiro, LocalDateTime> colDataCriacao;

    @FXML private DatePicker dateInicio;
    @FXML private DatePicker dateFim;
    @FXML private ComboBox<String> comboTipoRelatorio;
    @FXML private TextField txtTituloRelatorio;

    @FXML private Label labelReceitaTotal;
    @FXML private Label labelCustoTotal;
    @FXML private Label labelLucroBruto;
    @FXML private Label labelMargemLucro;
    @FXML private Label labelTotalVendas;
    @FXML private TextArea txtResumoExecutivo;

    private ObservableList<RelatorioFinanceiro> relatorios = FXCollections.observableArrayList();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        System.out.println("✅ RelatorioFinanceiroController inicializado!");
        configurarTabela();
        configurarComboBox();
        carregarRelatorios();
        definirDatasPadrao();
    }

    private void configurarTabela() {
        colTitulo.setCellValueFactory(new PropertyValueFactory<>("titulo"));
        
        colPeriodo.setCellValueFactory(cellData -> {
            RelatorioFinanceiro relatorio = cellData.getValue();
            String periodo = relatorio.getPeriodoInicio().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) +
                    " - " + relatorio.getPeriodoFim().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
            return new javafx.beans.property.SimpleStringProperty(periodo);
        });
        
        colTipo.setCellValueFactory(new PropertyValueFactory<>("tipoRelatorio"));
        colReceita.setCellValueFactory(new PropertyValueFactory<>("receitaTotal"));
        colLucro.setCellValueFactory(new PropertyValueFactory<>("lucroBruto"));
        colDataCriacao.setCellValueFactory(new PropertyValueFactory<>("dataCriacao"));

        // Formatar coluna de data
        colDataCriacao.setCellFactory(column -> new TableCell<RelatorioFinanceiro, LocalDateTime>() {
            @Override
            protected void updateItem(LocalDateTime item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")));
                }
            }
        });

        // Formatar colunas monetárias
        colReceita.setCellFactory(column -> new TableCell<RelatorioFinanceiro, BigDecimal>() {
            @Override
            protected void updateItem(BigDecimal item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText("R$ " + String.format("%,.2f", item));
                }
            }
        });

        colLucro.setCellFactory(column -> new TableCell<RelatorioFinanceiro, BigDecimal>() {
            @Override
            protected void updateItem(BigDecimal item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText("R$ " + String.format("%,.2f", item));
                    if (item.compareTo(BigDecimal.ZERO) < 0) {
                        setStyle("-fx-text-fill: #e74c3c; -fx-font-weight: bold;");
                    } else {
                        setStyle("-fx-text-fill: #2ecc71; -fx-font-weight: bold;");
                    }
                }
            }
        });
    }

    private void configurarComboBox() {
        comboTipoRelatorio.getItems().addAll(
            "Relatório Mensal",
            "Relatório Trimestral", 
            "Relatório Anual",
            "Relatório Personalizado"
        );
        comboTipoRelatorio.setValue("Relatório Mensal");
        
        // Listener para gerar título automático
        comboTipoRelatorio.setOnAction(e -> gerarTituloAutomatico());
    }

    private void definirDatasPadrao() {
        LocalDate hoje = LocalDate.now();
        dateInicio.setValue(hoje.withDayOfMonth(1));
        dateFim.setValue(hoje.withDayOfMonth(hoje.lengthOfMonth()));
        gerarTituloAutomatico();
    }

    @FXML
    private void gerarTituloAutomatico() {
        String tipo = comboTipoRelatorio.getValue();
        String mesAno = LocalDate.now().format(DateTimeFormatter.ofPattern("MM/yyyy"));
        
        if (tipo != null) {
            switch (tipo) {
                case "Relatório Mensal":
                    txtTituloRelatorio.setText("Relatório Financeiro - " + mesAno);
                    break;
                case "Relatório Trimestral":
                    txtTituloRelatorio.setText("Relatório Trimestral - " + mesAno);
                    break;
                case "Relatório Anual":
                    txtTituloRelatorio.setText("Relatório Anual - " + LocalDate.now().getYear());
                    break;
                default:
                    txtTituloRelatorio.setText("Relatório Personalizado");
            }
        }
    }

    @FXML
    private void gerarRelatorio() {
        try {
            System.out.println("🔄 Gerando relatório...");
            
            if (dateInicio.getValue() == null || dateFim.getValue() == null) {
                mostrarAlertaErro("Selecione o período do relatório.");
                return;
            }

            if (dateInicio.getValue().isAfter(dateFim.getValue())) {
                mostrarAlertaErro("Data de início não pode ser depois da data final.");
                return;
            }

            // Simular dados (substitua por dados reais depois)
            BigDecimal receitaTotal = new BigDecimal("12500.75");
            BigDecimal custoTotal = new BigDecimal("8450.30");
            BigDecimal lucroBruto = receitaTotal.subtract(custoTotal);
            BigDecimal margemLucro = receitaTotal.compareTo(BigDecimal.ZERO) > 0 ?
                lucroBruto.divide(receitaTotal, 4, RoundingMode.HALF_UP).multiply(BigDecimal.valueOf(100)) :
                BigDecimal.ZERO;

            int totalVendas = 150;

            // Atualizar interface - usar formatação consistente
            labelReceitaTotal.setText(String.format("R$ %,.2f", receitaTotal));
            labelCustoTotal.setText(String.format("R$ %,.2f", custoTotal));
            labelLucroBruto.setText(String.format("R$ %,.2f", lucroBruto));
            labelMargemLucro.setText(String.format("%,.2f%%", margemLucro));
            labelTotalVendas.setText(String.valueOf(totalVendas));

            // Gerar resumo
            String resumo = gerarResumoExecutivo(receitaTotal, custoTotal, lucroBruto, margemLucro, totalVendas);
            txtResumoExecutivo.setText(resumo);

            System.out.println("✅ Relatório gerado com valores:");
            System.out.println("  Receita: " + receitaTotal);
            System.out.println("  Custo: " + custoTotal);
            System.out.println("  Lucro: " + lucroBruto);
            System.out.println("  Margem: " + margemLucro + "%");

            mostrarAlertaSucesso("Relatório gerado com sucesso!\nClique em 'Salvar Relatório' para armazenar.");

        } catch (Exception e) {
            e.printStackTrace();
            mostrarAlertaErro("Erro ao gerar relatório: " + e.getMessage());
        }
    }

    @FXML
    private void salvarRelatorio() {
        try {
            if (txtTituloRelatorio.getText().trim().isEmpty()) {
                mostrarAlertaErro("Informe um título para o relatório.");
                return;
            }

            // Validar se os campos financeiros estão preenchidos
            if (labelReceitaTotal.getText().equals("R$ 0,00") || 
                labelLucroBruto.getText().equals("R$ 0,00")) {
                mostrarAlertaErro("Gere o relatório primeiro antes de salvar.");
                return;
            }

            System.out.println("🔄 Iniciando salvamento do relatório...");

            // Criar novo relatório
            RelatorioFinanceiro novoRelatorio = new RelatorioFinanceiro();
            novoRelatorio.setTitulo(txtTituloRelatorio.getText().trim());
            novoRelatorio.setPeriodoInicio(dateInicio.getValue());
            novoRelatorio.setPeriodoFim(dateFim.getValue());
            novoRelatorio.setTipoRelatorio(comboTipoRelatorio.getValue());
            
            // Converter valores das labels para BigDecimal de forma segura
            try {
                String receitaStr = labelReceitaTotal.getText()
                    .replace("R$ ", "")
                    .replace(".", "")
                    .replace(",", ".");
                String custoStr = labelCustoTotal.getText()
                    .replace("R$ ", "")
                    .replace(".", "")
                    .replace(",", ".");
                String lucroStr = labelLucroBruto.getText()
                    .replace("R$ ", "")
                    .replace(".", "")
                    .replace(",", ".");
                String margemStr = labelMargemLucro.getText()
                    .replace("%", "")
                    .replace(",", ".");

                System.out.println("💰 Valores convertidos:");
                System.out.println("  Receita: " + receitaStr);
                System.out.println("  Custo: " + custoStr);
                System.out.println("  Lucro: " + lucroStr);
                System.out.println("  Margem: " + margemStr);

                novoRelatorio.setReceitaTotal(new BigDecimal(receitaStr));
                novoRelatorio.setCustoTotal(new BigDecimal(custoStr));
                novoRelatorio.setLucroBruto(new BigDecimal(lucroStr));
                novoRelatorio.setMargemLucro(new BigDecimal(margemStr));
                
            } catch (NumberFormatException e) {
                System.err.println("❌ Erro na conversão de valores: " + e.getMessage());
                mostrarAlertaErro("Erro ao converter valores financeiros. Verifique os dados.");
                return;
            }

            // Converter valores inteiros
            try {
                novoRelatorio.setTotalVendas(Integer.parseInt(labelTotalVendas.getText()));
                novoRelatorio.setTotalEnergéticosVendidos(450); // Valor exemplo
            } catch (NumberFormatException e) {
                System.err.println("❌ Erro na conversão de valores inteiros: " + e.getMessage());
                novoRelatorio.setTotalVendas(0);
                novoRelatorio.setTotalEnergéticosVendidos(0);
            }

            novoRelatorio.setResumoExecutivo(txtResumoExecutivo.getText());
            novoRelatorio.setCriadoPor("Carlos");

            System.out.println("📊 Dados do relatório:");
            System.out.println("  Título: " + novoRelatorio.getTitulo());
            System.out.println("  Período: " + novoRelatorio.getPeriodoInicio() + " - " + novoRelatorio.getPeriodoFim());
            System.out.println("  Receita: " + novoRelatorio.getReceitaTotal());
            System.out.println("  Lucro: " + novoRelatorio.getLucroBruto());

            // Salvar no banco
            System.out.println("💾 Salvando no banco...");
            DAO<RelatorioFinanceiro> dao = new DAO<>(RelatorioFinanceiro.class);
            dao.incluirTransacional(novoRelatorio);

            System.out.println("✅ Relatório salvo com sucesso! ID: " + novoRelatorio.getId());
            mostrarAlertaSucesso("Relatório salvo com sucesso!\nID: " + novoRelatorio.getId());
            
            carregarRelatorios();
            limparCampos();

        } catch (Exception e) {
            System.err.println("❌ ERRO ao salvar relatório:");
            e.printStackTrace();
            mostrarAlertaErro("Erro detalhado ao salvar relatório:\n" + e.getMessage() + 
                             "\n\nVerifique se todos os campos estão preenchidos corretamente.");
        }
    }

    @FXML
    private void visualizarRelatorio() {
        RelatorioFinanceiro selecionado = tableRelatorios.getSelectionModel().getSelectedItem();
        if (selecionado == null) {
            mostrarAlertaAviso("Selecione um relatório para visualizar.");
            return;
        }

        try {
            // Preencher campos com dados do relatório
            txtTituloRelatorio.setText(selecionado.getTitulo());
            dateInicio.setValue(selecionado.getPeriodoInicio());
            dateFim.setValue(selecionado.getPeriodoFim());
            comboTipoRelatorio.setValue(selecionado.getTipoRelatorio());
            
            labelReceitaTotal.setText(String.format("R$ %,.2f", selecionado.getReceitaTotal()));
            labelCustoTotal.setText(String.format("R$ %,.2f", selecionado.getCustoTotal()));
            labelLucroBruto.setText(String.format("R$ %,.2f", selecionado.getLucroBruto()));
            labelMargemLucro.setText(String.format("%,.2f%%", selecionado.getMargemLucro()));
            labelTotalVendas.setText(String.valueOf(selecionado.getTotalVendas()));
            txtResumoExecutivo.setText(selecionado.getResumoExecutivo());

            mostrarAlertaSucesso("Relatório carregado para visualização!");

        } catch (Exception e) {
            e.printStackTrace();
            mostrarAlertaErro("Erro ao visualizar relatório: " + e.getMessage());
        }
    }

    @FXML
    private void exportarRelatorio() {
        RelatorioFinanceiro selecionado = tableRelatorios.getSelectionModel().getSelectedItem();
        if (selecionado == null) {
            mostrarAlertaAviso("Selecione um relatório para exportar.");
            return;
        }

        try {
            String conteudo = gerarConteudoExportacao(selecionado);
            
            TextArea textArea = new TextArea(conteudo);
            textArea.setEditable(false);
            textArea.setWrapText(true);
            textArea.setPrefSize(600, 400);

            ScrollPane scrollPane = new ScrollPane(textArea);
            scrollPane.setFitToWidth(true);
            scrollPane.setFitToHeight(true);

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Relatório Exportado");
            alert.setHeaderText("Conteúdo do Relatório: " + selecionado.getTitulo());
            alert.getDialogPane().setContent(scrollPane);
            alert.showAndWait();

        } catch (Exception e) {
            e.printStackTrace();
            mostrarAlertaErro("Erro ao exportar relatório: " + e.getMessage());
        }
    }

    @FXML
    private void excluirRelatorio() {
        RelatorioFinanceiro selecionado = tableRelatorios.getSelectionModel().getSelectedItem();
        if (selecionado == null) {
            mostrarAlertaAviso("Selecione um relatório para excluir.");
            return;
        }

        Alert confirmacao = new Alert(Alert.AlertType.CONFIRMATION);
        confirmacao.setTitle("Confirmação de Exclusão");
        confirmacao.setHeaderText("Excluir Relatório");
        confirmacao.setContentText("Tem certeza que deseja excluir o relatório: " + selecionado.getTitulo() + "?");

        if (confirmacao.showAndWait().get() == ButtonType.OK) {
            try {
                DAO<RelatorioFinanceiro> dao = new DAO<>(RelatorioFinanceiro.class);
                dao.removerPorIdTransacional(selecionado.getId());
                carregarRelatorios();
                mostrarAlertaSucesso("Relatório excluído com sucesso!");
            } catch (Exception e) {
                e.printStackTrace();
                mostrarAlertaErro("Erro ao excluir relatório: " + e.getMessage());
            }
        }
    }

    @FXML
    private void limparCampos() {
        definirDatasPadrao();
        labelReceitaTotal.setText("R$ 0,00");
        labelCustoTotal.setText("R$ 0,00");
        labelLucroBruto.setText("R$ 0,00");
        labelMargemLucro.setText("0,00%");
        labelTotalVendas.setText("0");
        txtResumoExecutivo.clear();
        System.out.println("🧹 Campos limpos!");
    }

    @FXML
    private void voltarDashboard() {
        try {
            javafx.scene.Node telaDashboard = javafx.fxml.FXMLLoader.load(getClass().getResource("/telas/view/TelaDashboard.fxml"));
            javafx.scene.layout.StackPane painel = (javafx.scene.layout.StackPane) txtTituloRelatorio.getScene().lookup("#painelConteudo");
            painel.getChildren().setAll(telaDashboard);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void carregarRelatorios() {
        try {
            DAO<RelatorioFinanceiro> dao = new DAO<>(RelatorioFinanceiro.class);
            List<RelatorioFinanceiro> lista = dao.obterTodos(50, 0);
            relatorios.setAll(lista);
            tableRelatorios.setItems(relatorios);
            System.out.println("✅ Relatórios carregados: " + lista.size());
        } catch (Exception e) {
            System.err.println("❌ Erro ao carregar relatórios: " + e.getMessage());
            // Não mostrar erro se a tabela não existir ainda
        }
    }

    private String gerarResumoExecutivo(BigDecimal receita, BigDecimal custo, BigDecimal lucro, BigDecimal margem, int vendas) {
        return String.format(
            "RELATÓRIO FINANCEIRO - RESUMO EXECUTIVO\n\n" +
            "Período: %s a %s\n\n" +
            "📈 DESEMPENHO FINANCEIRO:\n" +
            "• Receita Total: R$ %,.2f\n" +
            "• Custo Total: R$ %,.2f\n" +
            "• Lucro Bruto: R$ %,.2f\n" +
            "• Margem de Lucro: %,.2f%%\n" +
            "• Total de Vendas: %d\n\n" +
            "🎯 ANÁLISE:\n" +
            "%s\n\n" +
            "💡 RECOMENDAÇÕES:\n" +
            "%s",
            dateInicio.getValue().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")),
            dateFim.getValue().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")),
            receita, custo, lucro, margem, vendas,
            lucro.compareTo(BigDecimal.ZERO) > 0 ? 
                "Ótimo desempenho financeiro com lucro significativo." :
                "Atenção: Resultado financeiro abaixo do esperado.",
            margem.compareTo(new BigDecimal("20")) > 0 ?
                "Manter estratégias atuais - margem saudável." :
                "Revisar custos operacionais e estratégias de preço."
        );
    }

    private String gerarConteudoExportacao(RelatorioFinanceiro relatorio) {
        return String.format(
            "RELATÓRIO FINANCEIRO EXPORTADO\n" +
            "==============================\n\n" +
            "Título: %s\n" +
            "Período: %s a %s\n" +
            "Tipo: %s\n" +
            "Data de Criação: %s\n" +
            "Criado por: %s\n\n" +
            "DADOS FINANCEIROS:\n" +
            "• Receita Total: R$ %,.2f\n" +
            "• Custo Total: R$ %,.2f\n" +
            "• Lucro Bruto: R$ %,.2f\n" +
            "• Margem de Lucro: %,.2f%%\n" +
            "• Total de Vendas: %d\n" +
            "• Energéticos Vendidos: %d\n\n" +
            "RESUMO EXECUTIVO:\n%s\n\n" +
            "---\n" +
            "Relatório gerado pelo Sistema de Energéticos",
            relatorio.getTitulo(),
            relatorio.getPeriodoInicio().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")),
            relatorio.getPeriodoFim().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")),
            relatorio.getTipoRelatorio(),
            relatorio.getDataCriacao().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")),
            relatorio.getCriadoPor(),
            relatorio.getReceitaTotal(),
            relatorio.getCustoTotal(),
            relatorio.getLucroBruto(),
            relatorio.getMargemLucro(),
            relatorio.getTotalVendas(),
            relatorio.getTotalEnergéticosVendidos(),
            relatorio.getResumoExecutivo()
        );
    }

    // Métodos auxiliares para alertas
    private void mostrarAlertaErro(String mensagem) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Erro");
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }

    private void mostrarAlertaSucesso(String mensagem) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Sucesso");
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }

    private void mostrarAlertaAviso(String mensagem) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Aviso");
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }
}