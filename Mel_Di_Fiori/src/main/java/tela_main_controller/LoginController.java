package tela_main_controller;

import dao.UserDAO;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.Control;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import model.User;

public class LoginController {

    @FXML
    private TextField txtUsuario;
    @FXML
    private PasswordField txtSenha;
    @FXML
    private Label lblMensagem;
    @FXML
    private Button btnLogin;

    private UserDAO userDAO;

    @FXML
    public void initialize() {
        System.out.println("✅ LoginController inicializado!");

        try {
            // Inicializar UserDAO com tratamento de erro robusto
            inicializarUserDAO();
            
            // Focar no campo usuário automaticamente
            txtUsuario.requestFocus();

            System.out.println("🎯 Sistema de login pronto para uso!");

        } catch (Exception e) {
            System.err.println("❌ Erro na inicialização do login: " + e.getMessage());
            e.printStackTrace();
            lblMensagem.setText("❌ Erro ao inicializar sistema. Tente novamente.");
        }
    }

    private void inicializarUserDAO() {
        try {
            userDAO = new UserDAO();
            System.out.println("✅ UserDAO criado com sucesso!");

            // Criar usuários iniciais se necessário
            userDAO.criarUsuariosIniciais();
            System.out.println("✅ Usuários iniciais verificados!");

        } catch (Exception e) {
            System.err.println("❌ ERRO CRÍTICO: Falha ao inicializar UserDAO: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Não foi possível inicializar o sistema de autenticação", e);
        }
    }

    @FXML
    private void fazerLogin() {
        try {
            String usuario = txtUsuario.getText().trim();
            String senha = txtSenha.getText().trim();

            // Validar campos
            if (usuario.isEmpty() || senha.isEmpty()) {
                mostrarMensagemErro("⚠️ Preencha todos os campos!");
                aplicarEstiloErro(txtUsuario);
                aplicarEstiloErro(txtSenha);
                return;
            }

            // Verificar se userDAO foi inicializado
            if (userDAO == null) {
                System.err.println("❌ UserDAO é null - tentando reinicializar...");
                inicializarUserDAO();
                
                // Se ainda for null após tentativa de reinicialização
                if (userDAO == null) {
                    mostrarMensagemErro("💥 Erro crítico no sistema. Reinicie o aplicativo.");
                    return;
                }
            }

            // Limpar estilos de erro
            limparEstilosErro();

            System.out.println("🔐 Tentando autenticar usuário: " + usuario);

            // Mostrar loading
            mostrarMensagemLoading("🔐 Autenticando...");

            // Autenticar usuário
            User usuarioAutenticado = userDAO.autenticar(usuario, senha);

            if (usuarioAutenticado != null) {
                System.out.println("✅ Login bem-sucedido: " + usuarioAutenticado.getNome());
                mostrarMensagemSucesso("🎉 Login realizado com sucesso!");

                // Pequeno delay para mostrar mensagem de sucesso
                new Thread(() -> {
                    try {
                        Thread.sleep(1000);
                        javafx.application.Platform.runLater(() -> {
                            abrirSistemaPrincipal(usuarioAutenticado);
                        });
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }).start();

            } else {
                System.out.println("❌ Falha na autenticação para: " + usuario);
                mostrarMensagemErro("❌ Usuário ou senha inválidos!");
                aplicarEstiloErro(txtUsuario);
                aplicarEstiloErro(txtSenha);
                txtSenha.clear();
                txtSenha.requestFocus();
            }

        } catch (Exception e) {
            System.err.println("❌ Erro crítico no login: " + e.getMessage());
            e.printStackTrace();
            mostrarMensagemErro("💥 Erro no sistema: " + e.getMessage());
        }
    }

    @FXML
    private void esqueciSenha() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Recuperação de Senha");
        alert.setHeaderText("🔑 Esqueci minha senha");
        alert.setContentText("Entre em contato com o administrador do sistema:\n\n" +
                "📞 Telefone: (11) 99999-9999\n" +
                "✉️ Email: admin@energeticos.com.br\n" +
                "🕒 Horário: Segunda a Sexta, 8h às 18h");

        alert.showAndWait();
    }

    // Método para capturar Enter nos campos
    @FXML
    private void pressionarEnter(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
            fazerLogin();
        }
    }

    private void abrirSistemaPrincipal(User usuario) {
        try {
            System.out.println("🚀 Iniciando sistema principal para: " + usuario.getNome());

            // Fechar tela de login
            Stage stageLogin = (Stage) txtUsuario.getScene().getWindow();
            stageLogin.close();

            // Carregar MainLayout
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/telas/view/MainLayout.fxml"));
            Parent root = loader.load();

            // Criar nova stage
            Stage stagePrincipal = new Stage();
            stagePrincipal.setTitle("Sistema de Energéticos - " + usuario.getNome() + " [" + usuario.getTipo() + "]");

            Scene scene = new Scene(root, 1200, 800);
            scene.getStylesheets().add(getClass().getResource("/globalStyle/style.css").toExternalForm());
            stagePrincipal.setScene(scene);
            stagePrincipal.setMaximized(true);
            stagePrincipal.show();

            System.out.println("🎉 Sistema principal aberto com sucesso!");
            System.out.println("👤 Usuário logado: " + usuario.getNome() + " (" + usuario.getTipo() + ")");

        } catch (Exception e) {
            System.err.println("❌ Erro crítico ao abrir sistema: " + e.getMessage());
            e.printStackTrace();
            mostrarAlertaErro("Erro ao iniciar sistema: " + e.getMessage());
        }
    }

    private void mostrarMensagemErro(String mensagem) {
        lblMensagem.setText(mensagem);
        lblMensagem.setStyle("-fx-text-fill: #e74c3c; -fx-font-weight: bold;");
    }

    private void mostrarMensagemSucesso(String mensagem) {
        lblMensagem.setText(mensagem);
        lblMensagem.setStyle("-fx-text-fill: #2ecc71; -fx-font-weight: bold;");
    }

    private void mostrarMensagemLoading(String mensagem) {
        lblMensagem.setText(mensagem);
        lblMensagem.setStyle("-fx-text-fill: #f39c12; -fx-font-weight: bold;");
    }

    private void aplicarEstiloErro(Control campo) {
        campo.setStyle("-fx-border-color: #e74c3c; -fx-border-width: 2; -fx-background-color: #2a2a2a;");
    }

    private void limparEstilosErro() {
        txtUsuario.setStyle("-fx-background-color: #2a2a2a; -fx-border-color: #ff6b00;");
        txtSenha.setStyle("-fx-background-color: #2a2a2a; -fx-border-color: #ff6b00;");
    }

    private void mostrarAlertaErro(String mensagem) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Erro do Sistema");
        alert.setHeaderText("Ocorreu um erro");
        alert.setContentText(mensagem);
        alert.showAndWait();
    }
}