package tanaka.eduardo.repository;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import tanaka.eduardo.model.Acao;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class AcaoRepository implements PanacheRepository<Acao> {
}
