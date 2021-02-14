package tanaka.eduardo;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.io.IOException;
import java.net.URLEncoder;
import java.time.Duration;

import static org.openqa.selenium.support.ui.ExpectedConditions.presenceOfElementLocated;

@Path("/hello-resteasy")
public class GreetingResource {

    @Inject
    ScraperService scraperService;

    @ConfigProperty(name = "CHROME_DRIVER_PATH")
    String chromeDriver;

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String hello() {
        return "Hello RESTEasy";
    }

    @GET
    @Path("/teste")
    @Produces(MediaType.TEXT_PLAIN)
    public String teste() throws IOException, InterruptedException {
        //declare the chrome driver from the local machine location
        System.setProperty("webdriver.chrome.driver", chromeDriver);

        //create object of chrome options
        ChromeOptions options = new ChromeOptions();

        //add the headless argument
        options.addArguments("headless");

        //pass the options parameter in the Chrome driver declaration
        WebDriver driver = new ChromeDriver(options);

        //Navigate to toolsQA site url
        driver.get("https://fiis.com.br/resumo/");
        String s = driver.getTitle();
        String ss = driver.getPageSource();

        //Print the Title of the Page
        System.out.println("Title of the page is -> " + s);
        //Close the driver
        driver.close();

        //WebElement firstResult = wait.until(presenceOfElementLocated(By.cssSelector("#filter--result-table-resumo")));
        return chromeDriver + ss;



    }
}