package tanaka.eduardo.repository;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import tanaka.eduardo.model.AcaoDados;

import javax.enterprise.context.ApplicationScoped;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@ApplicationScoped
public class AcaoDadosRepository implements PanacheRepository<AcaoDados> {

    public Optional<AcaoDados> findByCodigoAndData(String codigo) {
        Map<String, Object> params = new HashMap<>();
        params.put("id", codigo);
        params.put("data", LocalDate.now().format(DateTimeFormatter.ofPattern("MM/yyyy")));

        return find("acao.id = :id and to_char(acaoDadosId.dataPagamento, 'MM/YYYY') = :data", params).firstResultOptional();
    }
}
