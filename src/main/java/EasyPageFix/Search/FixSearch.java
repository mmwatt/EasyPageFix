/*
 * Copyright (c) 2019 Matthew Watt & University of Sheffield. This project is based on ideas by Phil McMinn.
 */

package EasyPageFix.Search;

import EasyPageFix.Errors.ErrorType;
import EasyPageFix.Errors.PageError;
import EasyPageFix.Pages.BrowserTools;
import EasyPageFix.Pages.WebPage;
import EasyPageFix.Search.Searchers.CenteredSearcher;
import EasyPageFix.Search.Searchers.InsideSearcher;
import EasyPageFix.Search.Searchers.NearSearcher;
import EasyPageFix.Search.Searchers.NotAbsentSearcher;
import EasyPageFix.Search.Searchers.SingleFixSearcher;
import EasyPageFix.Search.Searchers.SizeSearcher;
import EasyPageFix.Search.Searchers.VisibleSearcher;
import java.io.IOException;
import java.text.ParseException;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import org.w3c.dom.css.CSSRule;

/**
 * @author Matthew Watt Implementation of standard search techniques to find fixes for the errors
 *     found by Galen.
 */
public class FixSearch {

  private static SingleFixSearcher[] searchers = new SingleFixSearcher[]{
      new CenteredSearcher(),
      new InsideSearcher(),
      new NearSearcher(),
      new NotAbsentSearcher(),
      new SizeSearcher(),
      new VisibleSearcher(),
//      new SimpleSearcher() //Don't know whether to include this or not
  };

  public static WebPage fixPage(WebPage oldPage, BrowserTools browserTools)
      throws IOException, ParseException {
    List<CSSRule[]> candidateFixes = new LinkedList<>();

    //Don't bother fixing a good page.
    if (oldPage.errors.size() == 0) {
      return oldPage;
    }

    for (PageError error : oldPage.errors) {
      if (error.type != ErrorType.UNFIXABLE) {
        for (SingleFixSearcher s : searchers) {
          if (s.matchesError(error)) {
            CSSRule[] fixes = s.bestSingleFix(oldPage, error, browserTools);
            candidateFixes.add(fixes);
          }
        }
      }
    }
    //Try and combine the results returned by the searchers
    return combineRules(oldPage, candidateFixes, browserTools);
  }

  private static WebPage combineRules(WebPage oldPage, List<CSSRule[]> candidateFixes,
      BrowserTools browserTools)
      throws IOException, ParseException {
    if (candidateFixes.size() == 0) {
      System.out.println("Could not find any candidate fixes.");
      return oldPage;
    }

    List<List<CSSRule[]>> permutations = new LinkedList<>();

    int loopNumber =
        (candidateFixes.size() < 4) ? candidateFixes.size() : candidateFixes.size() * 2;

    //Approximate shuffling number, double the number of fixes (reduce computation)
    for (int i = 0; i < loopNumber; i++) {
      Collections.shuffle(candidateFixes);
      permutations.add(candidateFixes);
    }

//    Check errors for each permutation.
    int bestScore = Integer.MAX_VALUE;
    int bestChangesIndex = 0;
    for (int i = 0; i < permutations.size(); i++) {
      CSSRule[][] fixes = permutations.get(i).toArray(new CSSRule[0][0]);

//      copy the page to a new directory
      WebPage testPage = oldPage.duplicate();
//      score page
      int testPageScore = evalPage(testPage, browserTools, fixes);

      //if there are no errors, return page w/ changes
      if (testPageScore == 0) {
        for (CSSRule[] fix : fixes) {
          testPage.addCSSRules(fix);
        }
        testPage.tempSave();
        testPage.refreshErrors();
        return testPage;
      } else if (testPageScore < bestScore) {
        //log best (lowest) error score and store the page
        bestScore = testPageScore;
        bestChangesIndex = i;
      }
      testPage.cleanup();
    }

    //apply fixes from best performing page and return page.
    WebPage bestPage = oldPage.duplicate();
    for (CSSRule[] rules : permutations.get(bestChangesIndex)) {
      bestPage.addCSSRules(rules);
    }
    bestPage.tempSave();
    bestPage.refreshErrors();
    return bestPage;
  }


  private static int evalPage(WebPage page, BrowserTools browserTools, CSSRule[][] changes)
      throws IOException, ParseException {
    WebPage newPage = page.duplicate();
    if (changes != null) {
      for (CSSRule[] change : changes) {
        newPage.addCSSRules(change);
      }
      newPage.tempSave();
      newPage.refreshErrors();
    }

    int score = newPage.fullErrorScore();
    newPage.cleanup();
    return score;
  }
}

