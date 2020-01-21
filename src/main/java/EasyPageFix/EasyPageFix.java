/*
 * Copyright (c) 2019 Matthew Watt & University of Sheffield. This project is based on ideas by Phil McMinn.
 */

package EasyPageFix;

import EasyPageFix.Errors.PageError;
import EasyPageFix.Pages.BrowserTools;
import EasyPageFix.Pages.WebPage;
import EasyPageFix.Search.FixSearch;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

/**
 * @author Matthew Watt
 *     EasyPageFix is a tool which allows a developer to specify a webpage and its specification,
 *     and in return it will search for fixes to any layout issues which occur between the displayed
 *     page and its spec.
 *
 *     Command-line Arguments:
 *     0: Path to HTML file
 *     1: Path to CSS file
 *     2: Path to misc. resources for the page (page's root folder, normally)
 *     3: Path to galen spec
 *     4: Output path of fixed page
 *     5: Path to a chrome driver executable
 *     6: Chrome Window Width
 *     7: Chrome Window Height
 */
public class EasyPageFix {


  public static void main(String[] args) {

    long timeBefore = System.currentTimeMillis();
    //process args if present
    if (args.length == 8) {
      //1st Arg: HTML
      Path htmlPath = Paths.get(args[0]);
      Path cssPath = Paths.get(args[1]);
      Path resourcesPath = Paths.get(args[2]);
      String specPath = args[3];
      Path fixedPath = Paths.get(args[4]);
      String chromeDriverLocation = args[5];
      int windowWidth = Integer.parseInt(args[6]);
      int windowHeight = Integer.parseInt(args[7]);

      //Perform initialisation tasks
      BrowserTools browserTools = new BrowserTools(specPath, chromeDriverLocation,
          htmlPath.toString(), windowWidth, windowHeight);

      //Attempt to fix the page.
      WebPage oldPage;
      WebPage fixedPage;
      try {
        oldPage = new WebPage(htmlPath, cssPath, resourcesPath, browserTools);
        List<PageError> oldErrors = oldPage.errors;
        System.out.println(
            "Number of errors BEFORE: " + oldErrors.size() + ", Score: " + oldPage
                .fullErrorScore());

        fixedPage = FixSearch.fixPage(oldPage, browserTools);
        List<PageError> newErrors = fixedPage.errors;
        System.out.println(
            "Number of errors AFTER: " + newErrors.size() + ", Score: " + fixedPage
                .fullErrorScore());

        if (fixedPage.fullErrorScore() < oldPage.fullErrorScore()) {
          fixedPage.exportAll(fixedPath);
          System.out.println("Exported to: " + fixedPath.toAbsolutePath().toString());
        } else {
          System.err.println("Unable to fix any of the issues on the page.");
        }
      } catch (Exception e) {
        //Something's wrong
        e.printStackTrace();
      } finally {
        //Close web browser
        browserTools.exit();

        long timeAfter = System.currentTimeMillis();

        System.out.println("Execution time: " + (timeAfter - timeBefore) / 1000 + "s");
      }
    } else {
      if (args.length < 1) {
        System.err
            .println(
                "Not enough arguments provided. Use --help or --h to view correct arguments & order.");
        System.exit(1);
      } else if (args[0].equals("--help") || args[0].equals("--h")) {
        System.out.println("0: Path to HTML file\n"
            + "1: Path to CSS file\n"
            + "2: Path to misc. resources for the page\n"
            + "3: Path to galen spec\n"
            + "4: Path to output website\n"
            + "5: (Absolute) Path to a chrome driver executable\n"
            + "6: Chrome Window Width\n"
            + "7: Chrome Window Height\n"
            + "Dependencies: JSoup HTML parser, GalenFramework, cssparser (steadystate), W3C's SAC and java-string-similarity.\n"
            + "All should be included in EasyPageFix.jar");
        System.exit(0);
      }
    }

  }


}
