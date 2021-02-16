package tanaka.eduardo;

import com.google.common.collect.Lists;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tanaka.eduardo.model.Acao;
import tanaka.eduardo.model.AcaoDados;
import tanaka.eduardo.model.AcaoDadosId;
import tanaka.eduardo.repository.AcaoRepository;
import tanaka.eduardo.service.WebScrapingService;

import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.validation.constraints.NotEmpty;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.openqa.selenium.support.ui.ExpectedConditions.presenceOfElementLocated;

@Path("/web-scraping")
public class WebScrapingResource {

    private final Logger log = LoggerFactory.getLogger(WebScrapingResource.class);

    @ConfigProperty(name = "CHROME_DRIVER_PATH")
    String chromeDriver;

    @Inject
    AcaoRepository acaoRepository;

    @Inject
    WebScrapingService webScrapingService;

    @GET
    @Transactional
    public Response teste(@QueryParam("codigo") @NotEmpty String codigoAcao) {
        // lista de ações q serão gravados
        String[] codigos = codigoAcao.split(",");

        // declare the chrome driver from the local machine location
        System.setProperty("webdriver.chrome.driver", chromeDriver);

        // create object of chrome options
        ChromeOptions options = new ChromeOptions();

        // add the headless argument
        options.addArguments("headless");

        // pass the options parameter in the Chrome driver declaration
        WebDriver driver = new ChromeDriver(options);

        // Navigate to site url
        driver.get("https://fiis.com.br/resumo/");

        WebDriverWait wait = new WebDriverWait(driver, 10);
        WebElement firstResult = wait.until(presenceOfElementLocated(By.cssSelector("#filter--result-table-resumo")));
        List<WebElement> lista = firstResult.findElements(By.tagName("tr"));

        long inicio = System.currentTimeMillis();

        for (List<WebElement> l : Lists.partition(lista, 15)) {
            for (WebElement tr : l) {
                List<WebElement> tds = tr.findElements(By.tagName("td"));

                if (tds.size() > 0) {
                    try {
                        // verifica se a linha pertence a lista de acoes q serao gravadas
                        if (Arrays.stream(codigos).anyMatch(tds.get(0).getText()::equals)) {
                            // verifica se existe a acao gravada no banco
                            Optional<Acao> acao = acaoRepository.find("id", tds.get(0).getText()).firstResultOptional();

                            if (acao.isPresent()) {
                                AcaoDados acaoDados = new AcaoDados();
                                AcaoDadosId acaoDadosId = new AcaoDadosId();
                                acaoDados.setRendimento(Double.parseDouble(tds.get(1).getText().replace(",", ".")));
                                acaoDadosId.setDataPagamento(LocalDate.parse(tds.get(3).getText(), DateTimeFormatter.ofPattern("dd/MM/yy")));
                                acaoDadosId.setDataBase(LocalDate.parse(tds.get(4).getText(), DateTimeFormatter.ofPattern("dd/MM/yy")));

                                try {
                                    acaoDados.setAcaoDadosId(acaoDadosId);
                                    acaoDados.setAcao(acao.get());

                                    webScrapingService.salvaDados(acaoDados);
                                } catch (Exception e) {
                                    log.error(e.getMessage(), e);
                                }
                            }
                        }
                    } catch (Exception e) {
                        log.error(e.getMessage(), e);
                    }
                }

            }
        }

        long fim = System.currentTimeMillis();

        System.out.println("inicio -> " + inicio);
        System.out.println("fim -> " + fim);
        System.out.println("tempo -> " + (fim - inicio));

        // Close the driver
        driver.close();

        return Response.ok().build();
    }

}