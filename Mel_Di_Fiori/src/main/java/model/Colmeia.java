package model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "colmeias")
public class Colmeia {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String identificacao;

    @Column(nullable = false)
    private String localizacao;

    @Column(nullable = false)
    private String tipo;

    @Column(nullable = false)
    private String status;

    @Column(nullable = false)
    private LocalDate dataInstalacao;

    private int numeroQuadros;

    @Column(length = 2000)
    private String observacoes;

    // NOVOS CAMPOS PARA ENERGÉTICOS
    @Column(precision = 10, scale = 2)
    private BigDecimal precoCusto;

    @Column(precision = 10, scale = 2)
    private BigDecimal precoVenda;

    private String fornecedor;
    private String categoria;
    private LocalDate dataValidade;
    private String codigoBarras;
    private int estoqueAtual;
    private int estoqueMinimo;

    // Construtores
    public Colmeia() {
    }

    public Colmeia(String identificacao, String localizacao, String tipo, String status, 
                   LocalDate dataInstalacao, int numeroQuadros, String observacoes) {
        this.identificacao = identificacao;
        this.localizacao = localizacao;
        this.tipo = tipo;
        this.status = status;
        this.dataInstalacao = dataInstalacao;
        this.numeroQuadros = numeroQuadros;
        this.observacoes = observacoes;
    }

    // Getters e Setters ORIGINAIS
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getIdentificacao() { return identificacao; }
    public void setIdentificacao(String identificacao) { this.identificacao = identificacao; }

    public String getLocalizacao() { return localizacao; }
    public void setLocalizacao(String localizacao) { this.localizacao = localizacao; }

    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public LocalDate getDataInstalacao() { return dataInstalacao; }
    public void setDataInstalacao(LocalDate dataInstalacao) { this.dataInstalacao = dataInstalacao; }

    public int getNumeroQuadros() { return numeroQuadros; }
    public void setNumeroQuadros(int numeroQuadros) { this.numeroQuadros = numeroQuadros; }

    public String getObservacoes() { return observacoes; }
    public void setObservacoes(String observacoes) { this.observacoes = observacoes; }

    // NOVOS GETTERS E SETTERS
    public BigDecimal getPrecoCusto() { return precoCusto; }
    public void setPrecoCusto(BigDecimal precoCusto) { this.precoCusto = precoCusto; }

    public BigDecimal getPrecoVenda() { return precoVenda; }
    public void setPrecoVenda(BigDecimal precoVenda) { this.precoVenda = precoVenda; }

    public String getFornecedor() { return fornecedor; }
    public void setFornecedor(String fornecedor) { this.fornecedor = fornecedor; }

    public String getCategoria() { return categoria; }
    public void setCategoria(String categoria) { this.categoria = categoria; }

    public LocalDate getDataValidade() { return dataValidade; }
    public void setDataValidade(LocalDate dataValidade) { this.dataValidade = dataValidade; }

    public String getCodigoBarras() { return codigoBarras; }
    public void setCodigoBarras(String codigoBarras) { this.codigoBarras = codigoBarras; }

    public int getEstoqueAtual() { return estoqueAtual; }
    public void setEstoqueAtual(int estoqueAtual) { this.estoqueAtual = estoqueAtual; }

    public int getEstoqueMinimo() { return estoqueMinimo; }
    public void setEstoqueMinimo(int estoqueMinimo) { this.estoqueMinimo = estoqueMinimo; }

    @Override
    public String toString() {
        return "Colmeia [id=" + id + ", identificacao=" + identificacao + ", localizacao=" + localizacao + ", tipo="
                + tipo + ", status=" + status + ", dataInstalacao=" + dataInstalacao + ", numeroQuadros="
                + numeroQuadros + ", observacoes=" + observacoes + ", precoCusto=" + precoCusto + ", precoVenda="
                + precoVenda + ", fornecedor=" + fornecedor + ", categoria=" + categoria + ", estoqueAtual="
                + estoqueAtual + "]";
    }
}