package dao;

import model.User;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.persistence.TypedQuery;
import java.time.LocalDateTime;
import java.util.List;

public class UserDAO {

    private static EntityManagerFactory emf;
    private EntityManager em;

    static {
        try {
            emf = Persistence.createEntityManagerFactory("Mel_Di_Fiori");
            System.out.println("✅ EntityManagerFactory criado com sucesso!");
        } catch (Exception e) {
            System.err.println("❌ ERRO CRÍTICO ao criar EntityManagerFactory: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public UserDAO() {
        try {
            if (emf == null) {
                System.err.println("❌ EntityManagerFactory é NULL - tentando recriar...");
                emf = Persistence.createEntityManagerFactory("Mel_Di_Fiori");
            }
            
            if (!emf.isOpen()) {
                System.err.println("❌ EntityManagerFactory está fechado - recriando...");
                emf = Persistence.createEntityManagerFactory("Mel_Di_Fiori");
            }
            
            this.em = emf.createEntityManager();
            System.out.println("✅ UserDAO inicializado - EntityManager criado");
            
        } catch (Exception e) {
            System.err.println("❌ ERRO ao criar EntityManager: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Falha ao inicializar UserDAO", e);
        }
    }

    public User autenticar(String username, String senha) {
        try {
            System.out.println("🔐 Tentando autenticar: " + username);

            if (em == null || !em.isOpen()) {
                System.err.println("❌ EntityManager não disponível - recriando...");
                this.em = emf.createEntityManager();
            }

            String jpql = "SELECT u FROM User u WHERE u.username = :username AND u.senha = :senha AND u.ativo = true";
            TypedQuery<User> query = em.createQuery(jpql, User.class);
            query.setParameter("username", username);
            query.setParameter("senha", senha);

            List<User> resultados = query.getResultList();
            
            if (resultados.isEmpty()) {
                System.out.println("❌ Nenhum usuário encontrado para: " + username);
                return null;
            }

            User usuario = resultados.get(0);

            if (usuario != null) {
                try {
                    if (!em.getTransaction().isActive()) {
                        em.getTransaction().begin();
                    }
                    usuario.setUltimoLogin(LocalDateTime.now());
                    em.merge(usuario);
                    em.getTransaction().commit();
                    System.out.println("✅ Usuário autenticado: " + usuario.getNome());
                } catch (Exception txError) {
                    if (em.getTransaction().isActive()) {
                        em.getTransaction().rollback();
                    }
                    System.err.println("❌ Erro ao atualizar último login: " + txError.getMessage());
                }
            }

            return usuario;

        } catch (Exception e) {
            System.err.println("❌ ERRO na autenticação para: " + username + " - " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    public void criarUsuariosIniciais() {
        try {
            System.out.println("👤 Verificando usuários iniciais...");

            if (em == null || !em.isOpen()) {
                System.err.println("❌ EntityManager não disponível - recriando...");
                this.em = emf.createEntityManager();
            }

            Long count = 0L;
            try {
                count = em.createQuery("SELECT COUNT(u) FROM User u", Long.class).getSingleResult();
                System.out.println("📊 Total de usuários no banco: " + count);
            } catch (Exception countError) {
                System.err.println("❌ Tabela de usuários pode não existir: " + countError.getMessage());
            }

            if (count == 0) {
                System.out.println("👥 Criando usuários iniciais...");

                try {
                    if (!em.getTransaction().isActive()) {
                        em.getTransaction().begin();
                    }

                    User admin = new User();
                    admin.setUsername("admin");
                    admin.setSenha("123456");
                    admin.setNome("Administrador Master");
                    admin.setEmail("admin@energeticos.com.br");
                    admin.setTipo("ADMIN");
                    admin.setAtivo(true);
                    em.persist(admin);

                    User gerente = new User();
                    gerente.setUsername("gerente");
                    gerente.setSenha("123456");
                    gerente.setNome("Carlos Silva - Gerente");
                    gerente.setEmail("gerente@energeticos.com.br");
                    gerente.setTipo("GERENTE");
                    gerente.setAtivo(true);
                    em.persist(gerente);

                    User usuario = new User();
                    usuario.setUsername("usuario");
                    usuario.setSenha("123456");
                    usuario.setNome("João Santos - Vendedor");
                    usuario.setEmail("vendedor@energeticos.com.br");
                    usuario.setTipo("USUARIO");
                    usuario.setAtivo(true);
                    em.persist(usuario);

                    em.getTransaction().commit();

                    System.out.println("✅✅✅ USUÁRIOS INICIAIS CRIADOS COM SUCESSO!");
                    System.out.println("📋 USUÁRIOS DISPONÍVEIS:");
                    System.out.println("   👑 admin / 123456 (Administrador)");
                    System.out.println("   👔 gerente / 123456 (Gerente)");
                    System.out.println("   👤 usuario / 123456 (Usuário)");

                } catch (Exception txError) {
                    if (em.getTransaction().isActive()) {
                        em.getTransaction().rollback();
                    }
                    System.err.println("❌ ERRO na transação de criação de usuários: " + txError.getMessage());
                    txError.printStackTrace();
                }
            } else {
                System.out.println("✅ Usuários já existem no banco. Total: " + count);
            }
        } catch (Exception e) {
            System.err.println("❌ ERRO ao criar usuários iniciais: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void fechar() {
        if (em != null && em.isOpen()) {
            em.close();
            System.out.println("✅ EntityManager fechado");
        }
    }
}