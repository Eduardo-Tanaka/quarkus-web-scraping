package tanaka.eduardo;

import tanaka.eduardo.model.Acao;
import tanaka.eduardo.repository.AcaoDadosRepository;
import tanaka.eduardo.repository.AcaoRepository;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/acoes")
@ApplicationScoped
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class AcaoResource {

    @Inject
    AcaoRepository acaoRepository;

    @Inject
    AcaoDadosRepository acaoDadosRepository;

    @GET
    public Response getAcoes() {
        return Response.ok().entity(acaoRepository.listAll()).build();
    }

    @GET
    @Path("/dados")
    public Response getAcoesDados() {
        return Response.ok().entity(acaoDadosRepository.listAll()).build();
    }

    @POST
    @Transactional
    public Response postAcao(Acao acao) {
        acaoRepository.persist(acao);
        return Response.ok().entity(acao).build();
    }
}
