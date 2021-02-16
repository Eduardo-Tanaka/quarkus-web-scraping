package tanaka.eduardo.repository;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import tanaka.eduardo.model.AcaoDados;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class AcaoDadosRepository implements PanacheRepository<AcaoDados> {
}
