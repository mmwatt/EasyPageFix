/*
 * Copyright (c) 2019 Matthew Watt & University of Sheffield. This project is based on ideas by Phil McMinn.
 */

package EasyPageFix.Pages;

import static org.apache.commons.io.FileUtils.copyDirectory;

import EasyPageFix.Errors.PageError;
import com.galenframework.reports.model.LayoutReport;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.ParseException;
import java.util.List;
import org.apache.commons.io.FileUtils;
import org.w3c.dom.css.CSSRule;

public class WebPage {

  public PageHTML page;
  public PageCSS css;
  public List<PageError> errors;

  private Path resourcesPath;
  private Path tempPath;

  private BrowserTools browserTools;

  /**
   * Basic constructor with Java types
   */
  public WebPage(PageHTML page, PageCSS css, Path resourcesPath, List<PageError> errors,
      BrowserTools browserTools)
      throws IOException {
    this.page = page;
    this.css = css;
    this.resourcesPath = resourcesPath;
    this.errors = errors;
    this.browserTools = browserTools;
    this.tempPath = Files.createTempDirectory(resourcesPath.getParent(), "temp-page");
    tempPath.toFile().deleteOnExit();
    recursiveDeleteOnShutdown(tempPath);
    exportAll(tempPath);
  }

  public WebPage(PageHTML page, PageCSS css, Path resourcesPath, BrowserTools browserTools)
      throws IOException, ParseException {
    this.page = page;
    this.css = css;
    this.resourcesPath = resourcesPath;
    this.browserTools = browserTools;

    tempPath = Files.createTempDirectory(resourcesPath.getParent(), "temp-page");
    tempPath.toFile().deleteOnExit();
    recursiveDeleteOnShutdown(tempPath);
    exportAll(tempPath);
    refreshErrors();
  }

  /**
   * Constructor for passing string versions of paths to ease calls in other classes
   */
  public WebPage(String pagePath, String cssPath, String resourcesPath, BrowserTools browserTools)
      throws IOException, ParseException {
    this(Paths.get(pagePath), Paths.get(cssPath), Paths.get(resourcesPath), browserTools);
  }

  /**
   * A constructor which specifies the HTML and CSS file paths, and will look for accompanying
   * resources in the same directory as the page, ignoring the given HTML and CSS documents.
   */
  public WebPage(Path pagePath, Path cssPath, Path resourcesPath, BrowserTools browserTools)
      throws IOException, ParseException {
    page = new PageHTML(pagePath);
    css = new PageCSS(cssPath);
    this.resourcesPath = resourcesPath;
    this.browserTools = browserTools;
    tempPath = Files.createTempDirectory(resourcesPath.getParent(), "temp-page");
    tempPath.toFile().deleteOnExit();
    recursiveDeleteOnShutdown(tempPath);
    exportAll(tempPath);
    refreshErrors();
  }

  /**
   * Clones the current webpage (as best as it can). HTML is kept the same though.
   *
   * @return A clone of the current webpage
   */
  public WebPage duplicate() {
    PageCSS newCss;
    //These 'catch' blocks should never be reached, but they need to be here for Java reasons
    try {
      newCss = css.duplicate();
      return new WebPage(page, newCss, resourcesPath, browserTools);
    } catch (IOException | ParseException e) {
      e.printStackTrace();
      return null;
    }
  }

  /**
   * Checks the stored page again.
   */
  public void refreshErrors() throws IOException, ParseException {
    // if we have an index.html and index.css
    if (!pageAndStylesPresent()) {
      throw new FileNotFoundException(
          "Could not find index.html/css in temp directory: " + tempPath.toString());
    }

    //then check that page 'url' for errors

    LayoutReport report = browserTools
        .runLayoutCheck(Paths.get(tempPath.toAbsolutePath().toString(), "index.html"));

    //parse the errors and store them on the object
    errors = PageError.parseReport(report);

    //TODO remove debugging code?
    System.out.println("Current page errors: ");
    System.out.println(errors);
  }

  //Checks whether HTML and CSS files are in the temp. directory
  private boolean pageAndStylesPresent() {
    //TODO assume index.html?
    boolean htmlExists = Paths.get(tempPath.toString(), "index.html").toFile().exists();
    boolean cssExists = Paths.get(tempPath.toString(), "index.css").toFile().exists();
    return htmlExists && cssExists;
  }

  /**
   * Returns (optionally, updates) error list.
   *
   * @param checkFirst Should the errors be refreshed first?
   */
  public List<PageError> getErrors(boolean checkFirst) throws IOException, ParseException {
    if (checkFirst) {
      refreshErrors();
      return errors;
    }
    return errors;
  }

  /**
   * Adds the amounts from each of the page's errors
   */
  public int fullErrorScore() {
    int accumulator = 0;

    for (PageError err : errors) {
      accumulator += err.amount;
    }

    return accumulator;
  }

  /**
   * Used for adding rules to the page's stylesheet
   */
  public void addCSSRules(CSSRule[] rules) {
    if (rules == null) {
      return;
    }
    //TODO remove debugging code?
    System.out.print("Making following changes: ");
    for (CSSRule r : rules) {
      css.addRule(r);
      System.out.print(r + " ");
    }
    System.out.print("\n");
  }


  /**
   * Export the page to the temp directory
   */
  public void tempSave() throws IOException {
    exportAll(tempPath);
  }


  /**
   * Saves all webpage files to a certain directory.
   */
  public void exportAll(Path directory) throws IOException {
    copyResources(directory);
    exportPageAndStyle(directory);
    //TODO printing debug
    System.out.println("Exported page to " + tempPath.toString());
  }

  /**
   * Writes the HTML and CSS of the page to a certain location (parent directory)
   */
  public void exportPageAndStyle(Path directory) throws IOException {
    //TODO remove assumption of index.html and index.css
    String pageFilePathStr = directory.toString() + "/index.html";
    Path pageFilePath = Paths.get(pageFilePathStr);
    Files.write(pageFilePath, page.pageString().getBytes());

    String cssFilePathStr = directory.toString() + "/index.css";
    Path cssFilePath = Paths.get(cssFilePathStr);
    Files.write(cssFilePath, css.cssString().getBytes());
  }

  /**
   * Copy all resource files from the stored directory to the specified one.
   */
  public void copyResources(Path newDirectory) throws IOException {
    copyDirectory(resourcesPath.toFile(), newDirectory.toFile());
  }

  /**
   * Mainly just delete the temp path. May include other actions if necessary
   */
  public void cleanup() {
    recursiveDelete(tempPath.toFile()).run();
  }

  @Override
  public void finalize() {
    cleanup();
  }

  /**
   * Copied from https://stackoverflow.com/questions/15022219/does-files-createtempdirectory-remove-the-directory-after-jvm-exits-normally/20280989;
   */
  private static void recursiveDeleteOnShutdown(final Path path) {
    Runtime.getRuntime().addShutdownHook(new Thread(recursiveDelete(path.toFile())));
  }

  private static Runnable recursiveDelete(final File file) {
    return () -> {
      try {
        FileUtils.deleteDirectory(file);
//        System.out.println("Recursively deleted: " + file.toString());
      } catch (IOException e) {
        System.out.println("Couldn't delete " + file.toString() + " Error: ");
        e.printStackTrace();
      }
    };
  }
}
