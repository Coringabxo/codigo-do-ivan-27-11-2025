package dao;

import java.util.List;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.persistence.TypedQuery;

public class DAO<E> {

    private static EntityManagerFactory emf;
    private EntityManager em;
    private Class<E> classe;

    static {
        try {
            emf = Persistence.createEntityManagerFactory("Mel_Di_Fiori");
        } catch (Exception e) {
            System.out.println("Erro ao criar EntityManagerFactory: " + e.getMessage());
        }
    }

    public DAO() {
        this(null);
    }

    public DAO(Class<E> classe) {
        this.classe = classe;
        em = emf.createEntityManager();
    }

    public DAO<E> abrirTransacao() {
        em.getTransaction().begin();
        return this;
    }

    public DAO<E> fecharTransacao() {
        em.getTransaction().commit(); 
        return this;
    }

    public DAO<E> incluirE(E entidade) {
        em.persist(entidade);
        return this;
    }

    public DAO<E> incluirTransacional(E entidade) {
        return this.abrirTransacao().incluirE(entidade).fecharTransacao();
    }

    public List<E> obterTodos(int quantidade, int deslocamento) {
        if (classe == null) {
            throw new UnsupportedOperationException("Classe nula");
        }
        String jpql = "select e from " + classe.getName() + " e";
        TypedQuery<E> query = em.createQuery(jpql, classe);
        query.setMaxResults(quantidade);
        query.setFirstResult(deslocamento);
        return query.getResultList();
    }

    public E obterPorID(Object id) {
        if (classe == null) {
            throw new UnsupportedOperationException("Classe nula");
        }
        return em.find(classe, id);
    }

    private DAO<E> removerPorId(Long id) {
        if (classe == null) {
            throw new UnsupportedOperationException("Classe nula");
        }
        E entidade = em.find(classe, id);
        if (entidade != null) {
            em.remove(entidade);
        }
        return this;
    }

    public DAO<E> removerPorIdTransacional(Long id) {
        return this.abrirTransacao().removerPorId(id).fecharTransacao();
    }

    private DAO<E> atualizar(E entidade) {
        em.merge(entidade);
        return this;
    }

    public DAO<E> atualizarTransacional(E entidade) {
        return this.abrirTransacao().atualizar(entidade).fecharTransacao();
    }

    public void fecharEm() {
        em.close();
    }
    
    public List<E> consultar(String jpql, Object parametro) {
        TypedQuery<E> query = em.createQuery(jpql, classe);
        query.setParameter(1, parametro);
        return query.getResultList();
    }

    public List<E> consultar(String jpql, Object... parametros) {
        TypedQuery<E> query = em.createQuery(jpql, classe);
        for (int i = 0; i < parametros.length; i++) {
            query.setParameter(i + 1, parametros[i]);
        }
        return query.getResultList();
    }
}