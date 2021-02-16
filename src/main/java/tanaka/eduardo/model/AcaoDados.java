package tanaka.eduardo.model;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;

@Entity
@Table(name = "TB_ACAO_DADOS")
public class AcaoDados implements Serializable {

    @EmbeddedId
    private AcaoDadosId acaoDadosId;

    @ManyToOne
    @JoinColumn(name = "CO_ACAO")
    private Acao acao;

    @Column(name = "VL_RENDIMENTO")
    private BigDecimal rendimento;

    public AcaoDadosId getAcaoDadosId() {
        return acaoDadosId;
    }

    public void setAcaoDadosId(AcaoDadosId acaoDadosId) {
        this.acaoDadosId = acaoDadosId;
    }

    public Acao getAcao() {
        return acao;
    }

    public void setAcao(Acao acao) {
        this.acao = acao;
    }

    public BigDecimal getRendimento() {
        return rendimento;
    }

    public void setRendimento(BigDecimal rendimento) {
        this.rendimento = rendimento;
    }
}
