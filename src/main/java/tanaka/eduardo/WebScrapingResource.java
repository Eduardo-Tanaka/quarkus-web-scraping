package tanaka.eduardo;

import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.hibernate.validator.constraints.Length;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tanaka.eduardo.model.Acao;
import tanaka.eduardo.repository.AcaoRepository;
import tanaka.eduardo.service.WebScrapingService;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.validation.constraints.NotEmpty;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.stream.Collectors;

@Path("/web-scraping")
@ApplicationScoped
public class WebScrapingResource {

    private final Logger log = LoggerFactory.getLogger(WebScrapingResource.class);

    @ConfigProperty(name = "CHROME_DRIVER_PATH")
    String chromeDriver;

    @Inject
    AcaoRepository acaoRepository;

    @Inject
    WebScrapingService webScrapingService;

    @GET
    @Path("{codigo}")
    public Response scrapByCodigo(@PathParam("codigo") @Length(min = 6, max = 6) String codigo) throws Exception {
        codigo = codigo.toUpperCase();

        webScrapingService.lePaginaCodigo(codigo);

        return Response.ok().build();
    }

    @GET
    @Path("/resumo")
    public Response scrap(@QueryParam("codigo") @NotEmpty String codigoAcao) {
        codigoAcao = codigoAcao.toUpperCase();

        webScrapingService.lePaginaResumo(codigoAcao);

        return Response.ok().build();
    }

    @GET
    @Path("/resumo-db")
    public Response scrapDb() {
        List<Acao> acaoList = acaoRepository.findAll().list();
        List<String> codigos = acaoList.stream().map(Acao::getId).collect(Collectors.toList());

        String codigoAcao = String.join(",", codigos).toUpperCase();

        webScrapingService.lePaginaResumo(codigoAcao);

        return Response.ok().build();
    }

}