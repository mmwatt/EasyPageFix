/*
 * Copyright (c) 2019 Matthew Watt & University of Sheffield. This project is based on ideas by Phil McMinn.
 */

package EasyPageFix.Pages;

import com.galenframework.api.Galen;
import com.galenframework.reports.model.LayoutReport;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

public class BrowserTools {

  private final Path SPEC_PATH;
  private final Path CHROME_DRIVER_LOCATION;
  private Path htmlPath;
  private final WebDriver driver;

  public BrowserTools(String specPath, String driverLocation, String html, int width, int height) {
    this(Paths.get(specPath), Paths.get(driverLocation), Paths.get(html), width, height);
  }

  public BrowserTools(Path specPath, Path driverLocation, Path html, int width, int height) {
    SPEC_PATH = specPath;
    CHROME_DRIVER_LOCATION = driverLocation;
    htmlPath = html;

    System
        .setProperty("webdriver.chrome.driver", CHROME_DRIVER_LOCATION.toAbsolutePath().toString());
    final ChromeOptions options = new ChromeOptions();
    options.addArguments("--disable-gpu");
//    options.addArguments("--headless");
    options.addArguments("--window-size=" + width + "," + height);

    driver = new ChromeDriver(options);
  }

  public LayoutReport runLayoutCheck() throws IOException {
    return runLayoutCheck(htmlPath);
  }

  public LayoutReport runLayoutCheck(Path path) throws IOException {
    driver.get(path.toUri().toURL().toString());

    return Galen.checkLayout(driver, SPEC_PATH.toAbsolutePath().toString(), null);
  }

  public void exit() {
    driver.close();
  }

  @Override
  public String toString() {
    return "BrowserTools{" +
        "SPEC_PATH=" + SPEC_PATH +
        ", CHROME_DRIVER_LOCATION=" + CHROME_DRIVER_LOCATION +
        ", htmlPath=" + htmlPath +
        ", driver=" + driver +
        '}';
  }
}
