/*
 * Copyright (c) 2019 Matthew Watt & University of Sheffield. This project is based on ideas by Phil McMinn.
 */

package EasyPageFix.Search.Searchers;

import EasyPageFix.Errors.PageError;
import EasyPageFix.Pages.BrowserTools;
import EasyPageFix.Pages.WebPage;
import java.io.IOException;
import java.text.ParseException;
import java.util.List;
import org.w3c.dom.css.CSSRule;
import org.w3c.dom.css.CSSStyleRule;

public interface SingleFixSearcher {

  /**
   * Returns a score for a given error on a page when certain changes are applied (score if the
   * error has been fixed on the page) 0 = fixed (error could not be found) >0 = error is present,
   * lower is better.
   */
  static int eval(WebPage page, PageError error, BrowserTools browserTools,
      CSSRule[] changes)
      throws IOException, ParseException {
    WebPage newPage = page.duplicate();
    if (changes != null) {
      newPage.addCSSRules(changes);
      newPage.tempSave();
      newPage.refreshErrors();
    }

    PageError relevantError = findCorrespondingError(newPage.errors, error);

    if (relevantError == null) {
      newPage.cleanup();
      return 0; //if error can't be found, must be fixed
    } else {
      return relevantError.amount;
    }

  }

  static PageError findCorrespondingError(List<PageError> errors, PageError originalError) {
    for (PageError e : errors) {
      boolean sameElems = true;
      if (e.elements != null) {
        for (int i = 0; i < e.elements.length; i++) {
          if (originalError.elements.length < i + 1) {
            sameElems = false;
          } else {
            if (!e.elements[i].equals(originalError.elements[i])) {
              sameElems = false;
            }
          }
        }

        boolean sameType = (e.type == originalError.type);

        if (sameElems && sameType) {
          return e;
        }
      }
    }
    return null;
  }

  /**
   * Returns the best possible fix for the issues on the page. May use a combination of search-based
   * and knowledge-based fixes.
   *
   * @param oldPage webpage to be modified
   * @param error error for which a fix is being attempted.
   */
  CSSStyleRule[] bestSingleFix(WebPage oldPage, PageError error, BrowserTools browserTools);

  /**
   * Does the searcher match the specified error?
   */
  boolean matchesError(PageError error);

//TODO Full implementation for all errors
}


