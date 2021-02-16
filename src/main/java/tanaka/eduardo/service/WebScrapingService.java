package tanaka.eduardo.service;

import tanaka.eduardo.model.AcaoDados;
import tanaka.eduardo.repository.AcaoDadosRepository;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;

@ApplicationScoped
public class WebScrapingService {

    @Inject
    AcaoDadosRepository acaoDadosRepository;

    @Transactional
    public void salvaDados(AcaoDados acaoDados) {
        acaoDadosRepository.persist(acaoDados);
    }
}
