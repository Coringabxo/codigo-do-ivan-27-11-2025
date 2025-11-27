package tela_main_controller;

import dao.DAO;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import model.Colmeia;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.ResourceBundle;

public class DashboardController implements Initializable {

    @FXML private Label labelTotalColmeias;
    @FXML private Label labelColmeiasAtivas;
    @FXML private Label labelColmeiasInativas;
    @FXML private Label labelTiposEmbalagem;
    @FXML private BarChart<String, Number> barChartSituacao;
    @FXML private BarChart<String, Number> barChartTipo;

    private List<Colmeia> energeticos;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        System.out.println("✅ DashboardController inicializado!");
        carregarDadosDashboard();
    }

    private void carregarDadosDashboard() {
        try {
            DAO<Colmeia> dao = new DAO<>(Colmeia.class);
            energeticos = dao.obterTodos(1000, 0);
            
            int total = energeticos.size();
            long ativos = energeticos.stream().filter(e -> "Ativa".equals(e.getStatus())).count();
            long inativos = energeticos.stream().filter(e -> "Inativa".equals(e.getStatus())).count();
            long manutencao = energeticos.stream().filter(e -> "Em manutenção".equals(e.getStatus())).count();

            // Atualizar labels
            labelTotalColmeias.setText(String.valueOf(total));
            labelColmeiasAtivas.setText(String.valueOf(ativos));
            labelColmeiasInativas.setText(String.valueOf(inativos + manutencao));
            labelTiposEmbalagem.setText(String.valueOf(energeticos.stream().map(Colmeia::getTipo).distinct().count()));

            // Atualizar gráficos
            atualizarGraficos(energeticos);

            System.out.println("✅ Dashboard carregado: " + total + " energéticos");

        } catch (Exception e) {
            System.err.println("❌ Erro no dashboard: " + e.getMessage());
            e.printStackTrace();
            
            // Valores padrão em caso de erro
            labelTotalColmeias.setText("0");
            labelColmeiasAtivas.setText("0");
            labelColmeiasInativas.setText("0");
            labelTiposEmbalagem.setText("0");
        }
    }

    private void atualizarGraficos(List<Colmeia> energeticos) {
        // Gráfico de situação
        barChartSituacao.getData().clear();
        XYChart.Series<String, Number> seriesSituacao = new XYChart.Series<>();
        
        long ativos = energeticos.stream().filter(e -> "Ativa".equals(e.getStatus())).count();
        long inativos = energeticos.stream().filter(e -> "Inativa".equals(e.getStatus())).count();
        long manutencao = energeticos.stream().filter(e -> "Em manutenção".equals(e.getStatus())).count();
        long desativados = energeticos.stream().filter(e -> "Desativada".equals(e.getStatus())).count();

        seriesSituacao.getData().add(new XYChart.Data<>("Ativos", ativos));
        seriesSituacao.getData().add(new XYChart.Data<>("Inativos", inativos));
        seriesSituacao.getData().add(new XYChart.Data<>("Manutenção", manutencao));
        seriesSituacao.getData().add(new XYChart.Data<>("Desativados", desativados));
        
        barChartSituacao.getData().add(seriesSituacao);

        // Gráfico de tipo
        barChartTipo.getData().clear();
        XYChart.Series<String, Number> seriesTipo = new XYChart.Series<>();
        
        long caixote = energeticos.stream().filter(e -> "Caixote".equals(e.getTipo())).count();
        long papelao = energeticos.stream().filter(e -> "Caixa de papelão".equals(e.getTipo())).count();
        long isopor = energeticos.stream().filter(e -> "Caixa de isopor".equals(e.getTipo())).count();
        long outros = energeticos.stream().filter(e -> "Outros".equals(e.getTipo())).count();

        seriesTipo.getData().add(new XYChart.Data<>("Caixote", caixote));
        seriesTipo.getData().add(new XYChart.Data<>("Papelão", papelao));
        seriesTipo.getData().add(new XYChart.Data<>("Isopor", isopor));
        seriesTipo.getData().add(new XYChart.Data<>("Outros", outros));
        
        barChartTipo.getData().add(seriesTipo);
    }

    @FXML
    private void atualizarDashboard() {
        System.out.println("🔄 Atualizando dashboard...");
        carregarDadosDashboard();
        
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Dashboard Atualizado");
        alert.setHeaderText(null);
        alert.setContentText("Os dados do dashboard foram atualizados com sucesso!");
        alert.showAndWait();
    }

    @FXML
    private void verRelatorioCompleto() {
        try {
            if (energeticos == null || energeticos.isEmpty()) {
                mostrarAlertaErro("Sem Dados", "Não há dados disponíveis para gerar o relatório.");
                return;
            }

            String relatorio = gerarRelatorioTexto();
            
            // Mostrar relatório em alerta
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Relatório Completo");
            alert.setHeaderText("Relatório Detalhado do Sistema");
            alert.setContentText(relatorio);
            alert.getDialogPane().setPrefSize(500, 400);
            alert.showAndWait();
            
            System.out.println("📊 Relatório completo gerado com sucesso!");

        } catch (Exception e) {
            System.err.println("❌ Erro ao gerar relatório: " + e.getMessage());
            e.printStackTrace();
            mostrarAlertaErro("Erro ao gerar relatório", "Ocorreu um erro ao gerar o relatório completo: " + e.getMessage());
        }
    }

    @FXML
    private void baixarRelatorio() {
        try {
            if (energeticos == null || energeticos.isEmpty()) {
                mostrarAlertaErro("Sem Dados", "Não há dados disponíveis para baixar o relatório.");
                return;
            }

            // Criar seletor de arquivo
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Salvar Relatório");
            fileChooser.setInitialFileName("relatorio_energeticos_" + 
                LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss")) + ".txt");
            fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Arquivos de Texto", "*.txt")
            );

            // Mostrar diálogo de salvamento
            Stage stage = (Stage) labelTotalColmeias.getScene().getWindow();
            File file = fileChooser.showSaveDialog(stage);

            if (file != null) {
                // Gerar e salvar relatório
                String relatorio = gerarRelatorioTexto();
                
                try (FileWriter writer = new FileWriter(file)) {
                    writer.write(relatorio);
                    writer.flush();
                    
                    // Mensagem de sucesso
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Relatório Salvo");
                    alert.setHeaderText(null);
                    alert.setContentText("Relatório salvo com sucesso em:\n" + file.getAbsolutePath());
                    alert.showAndWait();
                    
                    System.out.println("💾 Relatório salvo em: " + file.getAbsolutePath());
                    
                } catch (IOException e) {
                    throw new RuntimeException("Erro ao salvar arquivo: " + e.getMessage(), e);
                }
            }

        } catch (Exception e) {
            System.err.println("❌ Erro ao baixar relatório: " + e.getMessage());
            e.printStackTrace();
            mostrarAlertaErro("Erro ao Baixar", "Ocorreu um erro ao baixar o relatório: " + e.getMessage());
        }
    }
    
    private String gerarRelatorioTexto() {
        // Estatísticas detalhadas
        int total = energeticos.size();
        long ativos = energeticos.stream().filter(e -> "Ativa".equals(e.getStatus())).count();
        long inativos = energeticos.stream().filter(e -> "Inativa".equals(e.getStatus())).count();
        long manutencao = energeticos.stream().filter(e -> "Em manutenção".equals(e.getStatus())).count();
        long desativados = energeticos.stream().filter(e -> "Desativada".equals(e.getStatus())).count();
        
        // Distribuição por tipo
        long caixote = energeticos.stream().filter(e -> "Caixote".equals(e.getTipo())).count();
        long papelao = energeticos.stream().filter(e -> "Caixa de papelão".equals(e.getTipo())).count();
        long isopor = energeticos.stream().filter(e -> "Caixa de isopor".equals(e.getTipo())).count();
        long outros = energeticos.stream().filter(e -> "Outros".equals(e.getTipo())).count();
        
        // Data e hora do relatório
        String dataHora = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));
        
        // Criar relatório detalhado
        StringBuilder relatorio = new StringBuilder();
        relatorio.append("=").append("=".repeat(60)).append("\n");
        relatorio.append("📊 RELATÓRIO COMPLETO - DISTRIBUIDORA DE ENERGÉTICOS\n");
        relatorio.append("=").append("=".repeat(60)).append("\n\n");
        
        relatorio.append("Data de Geração: ").append(dataHora).append("\n");
        relatorio.append("-".repeat(60)).append("\n\n");
        
        relatorio.append("📈 ESTATÍSTICAS GERAIS:\n");
        relatorio.append("-".repeat(30)).append("\n");
        relatorio.append("• Total de Energéticos: ").append(total).append("\n");
        relatorio.append("• Energéticos Ativos: ").append(ativos).append(" (").append(calcularPercentual(ativos, total)).append("%)\n");
        relatorio.append("• Energéticos Inativos: ").append(inativos).append(" (").append(calcularPercentual(inativos, total)).append("%)\n");
        relatorio.append("• Em Manutenção: ").append(manutencao).append(" (").append(calcularPercentual(manutencao, total)).append("%)\n");
        relatorio.append("• Desativados: ").append(desativados).append(" (").append(calcularPercentual(desativados, total)).append("%)\n\n");
        
        relatorio.append("📦 DISTRIBUIÇÃO POR TIPO DE EMBALAGEM:\n");
        relatorio.append("-".repeat(40)).append("\n");
        relatorio.append("• Caixote: ").append(caixote).append(" (").append(calcularPercentual(caixote, total)).append("%)\n");
        relatorio.append("• Caixa de Papelão: ").append(papelao).append(" (").append(calcularPercentual(papelao, total)).append("%)\n");
        relatorio.append("• Caixa de Isopor: ").append(isopor).append(" (").append(calcularPercentual(isopor, total)).append("%)\n");
        relatorio.append("• Outros: ").append(outros).append(" (").append(calcularPercentual(outros, total)).append("%)\n\n");
        
        relatorio.append("🎯 RESUMO ANALÍTICO:\n");
        relatorio.append("-".repeat(25)).append("\n");
        relatorio.append("• Taxa de Atividade: ").append(calcularPercentual(ativos, total)).append("%\n");
        relatorio.append("• Taxa de Inatividade: ").append(calcularPercentual(inativos + desativados, total)).append("%\n");
        relatorio.append("• Diversidade de Embalagens: ").append(energeticos.stream().map(Colmeia::getTipo).distinct().count()).append(" tipos\n");
        relatorio.append("• Embalagem Mais Comum: ").append(obterEmbalagemMaisComum()).append("\n");
        relatorio.append("• Status Mais Frequente: ").append(obterStatusMaisFrequente()).append("\n\n");
        
        relatorio.append("=").append("=".repeat(60)).append("\n");
        relatorio.append("Relatório gerado automaticamente pelo Sistema de Energéticos\n");
        relatorio.append("=").append("=".repeat(60)).append("\n");
        
        return relatorio.toString();
    }
    
    private String calcularPercentual(long valor, int total) {
        if (total == 0) return "0";
        double percentual = (valor * 100.0) / total;
        return String.format("%.1f", percentual);
    }
    
    private String obterEmbalagemMaisComum() {
        return energeticos.stream()
            .collect(java.util.stream.Collectors.groupingBy(Colmeia::getTipo, java.util.stream.Collectors.counting()))
            .entrySet().stream()
            .max(java.util.Map.Entry.comparingByValue())
            .map(entry -> entry.getKey() + " (" + entry.getValue() + " unidades)")
            .orElse("N/A");
    }
    
    private String obterStatusMaisFrequente() {
        return energeticos.stream()
            .collect(java.util.stream.Collectors.groupingBy(Colmeia::getStatus, java.util.stream.Collectors.counting()))
            .entrySet().stream()
            .max(java.util.Map.Entry.comparingByValue())
            .map(entry -> entry.getKey() + " (" + entry.getValue() + " energéticos)")
            .orElse("N/A");
    }
    
    private void mostrarAlertaErro(String titulo, String mensagem) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }
}