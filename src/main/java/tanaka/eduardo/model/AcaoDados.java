package tanaka.eduardo.model;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;
import java.math.BigDecimal;

@Entity
@Table(name = "TB_ACAO_DADOS")
public class AcaoDados implements Serializable {

    @EmbeddedId
    private AcaoDadosId acaoDadosId;

    @Column(name = "VL_RENDIMENTO")
    private BigDecimal rendimento;

    public AcaoDadosId getAcaoDadosId() {
        return acaoDadosId;
    }

    public void setAcaoDadosId(AcaoDadosId acaoDadosId) {
        this.acaoDadosId = acaoDadosId;
    }


    public BigDecimal getRendimento() {
        return rendimento;
    }

    public void setRendimento(BigDecimal rendimento) {
        this.rendimento = rendimento;
    }
}
