package tanaka.eduardo.model;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

@Embeddable
public class AcaoDadosId implements Serializable {

    @ManyToOne
    @JoinColumn(name = "CO_ACAO")
    private Acao acao;

    @Column(name = "DT_PAGAMENTO")
    private LocalDate dataPagamento;

    @Column(name = "DT_BASE")
    private LocalDate dataBase;

    public Acao getAcao() {
        return acao;
    }

    public void setAcao(Acao acao) {
        this.acao = acao;
    }

    public LocalDate getDataPagamento() {
        return dataPagamento;
    }

    public void setDataPagamento(LocalDate dataPagamento) {
        this.dataPagamento = dataPagamento;
    }

    public LocalDate getDataBase() {
        return dataBase;
    }

    public void setDataBase(LocalDate dataBase) {
        this.dataBase = dataBase;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AcaoDadosId that = (AcaoDadosId) o;
        return Objects.equals(acao, that.acao) && Objects.equals(dataPagamento, that.dataPagamento) && Objects.equals(dataBase, that.dataBase);
    }

    @Override
    public int hashCode() {
        return Objects.hash(acao, dataPagamento, dataBase);
    }
}
