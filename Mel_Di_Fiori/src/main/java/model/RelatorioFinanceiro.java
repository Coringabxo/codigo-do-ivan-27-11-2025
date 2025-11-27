// RelatorioFinanceiro.java
package model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "relatorios_financeiros")
public class RelatorioFinanceiro {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String titulo;

    @Column(nullable = false)
    private LocalDate periodoInicio;

    @Column(nullable = false)
    private LocalDate periodoFim;

    @Column(nullable = false)
    private String tipoRelatorio;

    @Column(precision = 15, scale = 2)
    private BigDecimal receitaTotal;

    @Column(precision = 15, scale = 2)
    private BigDecimal custoTotal;

    @Column(precision = 15, scale = 2)
    private BigDecimal lucroBruto;

    @Column(precision = 5, scale = 2)
    private BigDecimal margemLucro;

    private int totalVendas;
    private int totalEnergéticosVendidos;

    @Column(length = 4000)
    private String resumoExecutivo;

    @Column(nullable = false)
    private LocalDateTime dataCriacao;

    private String criadoPor;

    // Construtores
    public RelatorioFinanceiro() {
        this.dataCriacao = LocalDateTime.now();
    }

    public RelatorioFinanceiro(String titulo, LocalDate periodoInicio, LocalDate periodoFim, String tipoRelatorio) {
        this();
        this.titulo = titulo;
        this.periodoInicio = periodoInicio;
        this.periodoFim = periodoFim;
        this.tipoRelatorio = tipoRelatorio;
    }

    // Getters e Setters (gerar todos)
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }

    public LocalDate getPeriodoInicio() { return periodoInicio; }
    public void setPeriodoInicio(LocalDate periodoInicio) { this.periodoInicio = periodoInicio; }

    public LocalDate getPeriodoFim() { return periodoFim; }
    public void setPeriodoFim(LocalDate periodoFim) { this.periodoFim = periodoFim; }

    public String getTipoRelatorio() { return tipoRelatorio; }
    public void setTipoRelatorio(String tipoRelatorio) { this.tipoRelatorio = tipoRelatorio; }

    public BigDecimal getReceitaTotal() { return receitaTotal; }
    public void setReceitaTotal(BigDecimal receitaTotal) { this.receitaTotal = receitaTotal; }

    public BigDecimal getCustoTotal() { return custoTotal; }
    public void setCustoTotal(BigDecimal custoTotal) { this.custoTotal = custoTotal; }

    public BigDecimal getLucroBruto() { return lucroBruto; }
    public void setLucroBruto(BigDecimal lucroBruto) { this.lucroBruto = lucroBruto; }

    public BigDecimal getMargemLucro() { return margemLucro; }
    public void setMargemLucro(BigDecimal margemLucro) { this.margemLucro = margemLucro; }

    public int getTotalVendas() { return totalVendas; }
    public void setTotalVendas(int totalVendas) { this.totalVendas = totalVendas; }

    public int getTotalEnergéticosVendidos() { return totalEnergéticosVendidos; }
    public void setTotalEnergéticosVendidos(int totalEnergéticosVendidos) { this.totalEnergéticosVendidos = totalEnergéticosVendidos; }

    public String getResumoExecutivo() { return resumoExecutivo; }
    public void setResumoExecutivo(String resumoExecutivo) { this.resumoExecutivo = resumoExecutivo; }

    public LocalDateTime getDataCriacao() { return dataCriacao; }
    public void setDataCriacao(LocalDateTime dataCriacao) { this.dataCriacao = dataCriacao; }

    public String getCriadoPor() { return criadoPor; }
    public void setCriadoPor(String criadoPor) { this.criadoPor = criadoPor; }

    @Override
    public String toString() {
        return "RelatorioFinanceiro{" +
                "id=" + id +
                ", titulo='" + titulo + '\'' +
                ", periodoInicio=" + periodoInicio +
                ", periodoFim=" + periodoFim +
                ", tipoRelatorio='" + tipoRelatorio + '\'' +
                ", receitaTotal=" + receitaTotal +
                ", lucroBruto=" + lucroBruto +
                ", dataCriacao=" + dataCriacao +
                '}';
    }
}