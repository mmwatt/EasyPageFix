/*
 * Copyright (c) 2019 Matthew Watt & University of Sheffield. This project is based on ideas by Phil McMinn.
 */

package EasyPageFix.Search.Searchers;

import EasyPageFix.Errors.ErrorType;
import EasyPageFix.Errors.PageError;
import EasyPageFix.Pages.BrowserTools;
import EasyPageFix.Pages.WebPage;
import EasyPageFix.Search.Searchers.CSSPropertyGenerators.CSSPropertyGenerator;
import EasyPageFix.Search.Searchers.CSSPropertyGenerators.IdentGenerators.PositionGenerator;
import EasyPageFix.Search.Searchers.CSSPropertyGenerators.PxGenerators.BottomGenerator;
import EasyPageFix.Search.Searchers.CSSPropertyGenerators.PxGenerators.LeftGenerator;
import EasyPageFix.Search.Searchers.CSSPropertyGenerators.PxGenerators.RightGenerator;
import EasyPageFix.Search.Searchers.CSSPropertyGenerators.PxGenerators.TopGenerator;
import com.steadystate.css.dom.CSSValueImpl;
import com.steadystate.css.dom.Property;
import java.io.IOException;
import java.text.ParseException;
import java.util.Random;
import org.w3c.dom.css.CSSRule;
import org.w3c.dom.css.CSSStyleRule;
import org.w3c.dom.css.CSSValue;

public class NearSearcher extends BaseSearcher {

  public NearSearcher() {
    type = ErrorType.NEAR;
  }

  @Override //TODO make it check multiple times
  public CSSStyleRule[] bestSingleFix(WebPage oldPage, PageError error, BrowserTools browserTools) {
    int bestScore;
    CSSStyleRule bestRule = null;
    WebPage currentPage = oldPage;

    //Try the whole process 3 times, iterating on the same page (or until score == 0 (perfect fix))
    for (int i = 0; i < 3; i++) {
      PageError currentError = SingleFixSearcher.findCorrespondingError(currentPage.errors, error);
      if (currentError == null) {
        //If the error has gone, the change responsible should have been returned in prev. loop
        System.out.println("COULD NOT FIND NEAR ERROR. PROGRAM FLOW INCORRECT");
        return new CSSStyleRule[]{};
      }
      bestScore = computeOldScore(currentPage, currentError, browserTools);
      CSSStyleRule[] oldRules = elementRules(currentPage, currentError);

      for (CSSStyleRule oldRule : oldRules) {
        CSSPropertyGenerator gen = getGenerator(currentError);

        PositionGenerator positionGen = new PositionGenerator();
        Property positionProp = positionGen.relative();

        Property[] properties = gen.possibleValues(generateValue(oldRule, currentError));
        CSSStyleRule[] newRules = new CSSStyleRule[properties.length];
        for (int j = 0; j < properties.length; j++) {
          newRules[j] = newRule(oldRule, new Property[]{positionProp, properties[j]});
          int newScore = Integer.MAX_VALUE;
          try {
            newScore = SingleFixSearcher
                .eval(currentPage, currentError, browserTools, new CSSRule[]{newRules[j]});
          } catch (IOException | ParseException e) {
            e.printStackTrace(); //hopefully shouldn't happen. don't add to list if it does
          }
          if (newScore == 0) {
            return new CSSStyleRule[]{newRules[j]};
          } else if (newScore < bestScore) {
            bestRule = newRules[j];
            bestScore = newScore;
          }
          //if the score is worse, throw changes away
        }
        currentPage = oldPage.duplicate();
        try {
          currentPage.addCSSRules(new CSSRule[]{bestRule});
          currentPage.tempSave();
          currentPage.refreshErrors();
        } catch (IOException | ParseException e) {
          e.printStackTrace();
          currentPage = oldPage; // should never occur
        }
      }
    }
    if (bestRule == null) {
      return new CSSStyleRule[]{};
    } else {
      return new CSSStyleRule[]{bestRule};
    }
  }

  private CSSValue generateValue(CSSStyleRule oldRule, PageError error) {
//    if (oldRule == null) {
    CSSValueImpl value = new CSSValueImpl();
    value.setCssText(error.amount + "px");
    return value;
//    }
    //TODO actually do something for an else block, using the old rule
  }

  //Assumes that there is no existing rule,
  private CSSPropertyGenerator getGenerator(PageError error) {
    if (error.relations == null || error.relations.length == 0) {
      int r = new Random().nextInt(4);
      switch (r) {
        case 0:
          return new TopGenerator();
        case 1:
          return new RightGenerator();
        case 2:
          return new BottomGenerator();
        case 3:
          return new LeftGenerator();
      }
    } else {
      //find out whether the amount is negative
      if (error.negative) {
        switch (error.relations[0]) {
          case "left":
            return new RightGenerator();
          case "right":
            return new LeftGenerator();
          case "bottom":
            return new TopGenerator();
          case "top":
            return new BottomGenerator();
        }
      } else {
        switch (error.relations[0]) {
          case "right":
            return new RightGenerator();
          case "left":
            return new LeftGenerator();
          case "top":
            return new TopGenerator();
          case "bottom":
            return new BottomGenerator();
        }
      }
    }
    //Should never execute
    return new LeftGenerator();
  }

  @Override
  public boolean matchesError(PageError error) {
    return error.type == type || error.type == ErrorType.DIRECTION
        || error.type == ErrorType.ALIGNMENT;
  }
}
