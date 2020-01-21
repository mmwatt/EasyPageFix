/*
 * Copyright (c) 2019 Matthew Watt & University of Sheffield. This project is based on ideas by Phil McMinn.
 */

package EasyPageFix.Pages;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * @author Matthew Watt Class for reconstructing pages (with fixes) for future testing with Galen.
 */
public class PageHTML {

  private Document page;

  /**
   * Blank Constructor
   */
  public PageHTML() {

  }

  /**
   * Input the whole file as a string, and parse it
   *
   * @param file The filepath or file contents
   */
  public PageHTML(String file) {
    page = parseHTML(file);
  }

  /**
   * Input a filepath, parse the file it points to
   */
  public PageHTML(Path filepath) throws IOException {
    this(new String(Files.readAllBytes(filepath)));
  }

  private static Document parseHTML(String file) {
    return Jsoup.parse(file);
  }

  /**
   * Fully formatted HTML of the page
   *
   * @return A string of the page's HTML
   */
  public String pageString() {
    return page.html();
  }

  /**
   * Fetches a list of the elements matching a certain class. Called on the member 'Document'
   *
   * @param className A string of the class (without a '.')
   * @return Elements, extension of an arrayList
   */
  public Elements getElementsWithClass(String className) {
    return page.getElementsByClass(className);
  }

  /**
   * Fetches a single element by its Id
   *
   * @param idName A string of the ID (without the '#')
   * @return The matching element
   */
  public Element getElementById(String idName) {
    return page.getElementById(idName);
  }
}
