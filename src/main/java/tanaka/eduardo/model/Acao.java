package tanaka.eduardo.model;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "TB_ACAO")
public class Acao implements Serializable {

    @Id
    @Column(name = "CO_ACAO")
    private String id;

    @Column(name = "DT_ACAO")
    private LocalDate data;

    @OneToMany
    private List<AcaoDados> acaoDadosList;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public LocalDate getData() {
        return data;
    }

    public void setData(LocalDate data) {
        this.data = data;
    }

    public List<AcaoDados> getAcaoDadosList() {
        return acaoDadosList;
    }

    public void setAcaoDadosList(List<AcaoDados> acaoDadosList) {
        this.acaoDadosList = acaoDadosList;
    }
}
