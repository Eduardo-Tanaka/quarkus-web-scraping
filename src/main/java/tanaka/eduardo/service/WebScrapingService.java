package tanaka.eduardo.service;

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
import tanaka.eduardo.repository.AcaoDadosRepository;
import tanaka.eduardo.repository.AcaoRepository;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.openqa.selenium.support.ui.ExpectedConditions.presenceOfElementLocated;

@ApplicationScoped
public class WebScrapingService {

    private final Logger log = LoggerFactory.getLogger(WebScrapingService.class);

    @ConfigProperty(name = "CHROME_DRIVER_PATH")
    String chromeDriver;

    @Inject
    AcaoDadosRepository acaoDadosRepository;

    @Inject
    AcaoRepository acaoRepository;

    @Transactional
    public void salvaDados(AcaoDados acaoDados) {
        acaoDadosRepository.persist(acaoDados);
    }

    private WebDriver inicializaWebDriver() {
        // declare the chrome driver from the local machine location
        System.setProperty("webdriver.chrome.driver", chromeDriver);

        // create object of chrome options
        ChromeOptions options = new ChromeOptions();

        // add the headless argument
        options.addArguments("headless");

        // pass the options parameter in the Chrome driver declaration
        WebDriver driver = new ChromeDriver(options);

        return driver;
    }

    public void lePaginaCodigo(String codigo) throws Exception {
        // verifica se existe a acao gravada no banco
        Optional<Acao> acao = acaoRepository.find("id", codigo).firstResultOptional();

        if (!acao.isPresent()) {
            log.error("Ação não cadastrada");
            throw new Exception("Ação não cadastrada");
        }

        WebDriver driver = inicializaWebDriver();

        // Navigate to site url
        driver.get("https://fiis.com.br/" + codigo + "/");

        WebDriverWait wait = new WebDriverWait(driver, 10);
        WebElement firstResult = wait.until(presenceOfElementLocated(By.cssSelector("#last-revenues--table tbody")));
        List<WebElement> lista = firstResult.findElements(By.tagName("tr"));

        long inicio = System.currentTimeMillis();

        for (WebElement tr : lista) {
            List<WebElement> tds = tr.findElements(By.tagName("td"));

            AcaoDados acaoDados = new AcaoDados();
            AcaoDadosId acaoDadosId = new AcaoDadosId();
            acaoDados.setRendimento(new BigDecimal(tds.get(4).getText().replace("R$", "").replace(",", ".").trim()));
            acaoDadosId.setDataPagamento(LocalDate.parse(tds.get(1).getText(), DateTimeFormatter.ofPattern("dd/MM/yy")));
            acaoDadosId.setDataBase(LocalDate.parse(tds.get(0).getText(), DateTimeFormatter.ofPattern("dd/MM/yy")));
            acaoDadosId.setAcao(acao.get());

            try {
                acaoDados.setAcaoDadosId(acaoDadosId);

                salvaDados(acaoDados);
            } catch (Exception e) {
                log.error(e.getMessage(), e);
            }
        }

        long fim = System.currentTimeMillis();

        System.out.println("inicio -> " + inicio);
        System.out.println("fim -> " + fim);
        System.out.println("tempo -> " + (fim - inicio));

        driver.close();
    }

    public void lePaginaResumo(String codigoAcao) {
        // lista de ações q serão gravados
        String[] codigos = codigoAcao.split(",");

        WebDriver driver = inicializaWebDriver();

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
                                Optional<AcaoDados> ad = acaoDadosRepository.findByCodigoAndData(tds.get(0).getText());
                                if (!ad.isPresent()) {
                                    AcaoDados acaoDados = new AcaoDados();
                                    AcaoDadosId acaoDadosId = new AcaoDadosId();
                                    acaoDados.setRendimento(new BigDecimal(tds.get(1).getText().replace(",", ".").trim()));
                                    acaoDadosId.setDataPagamento(LocalDate.parse(tds.get(3).getText(), DateTimeFormatter.ofPattern("dd/MM/yy")));
                                    acaoDadosId.setDataBase(LocalDate.parse(tds.get(4).getText(), DateTimeFormatter.ofPattern("dd/MM/yy")));
                                    acaoDadosId.setAcao(acao.get());

                                    try {
                                        acaoDados.setAcaoDadosId(acaoDadosId);

                                        salvaDados(acaoDados);
                                    } catch (Exception e) {
                                        log.error(e.getMessage(), e);
                                    }
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
    }
}
