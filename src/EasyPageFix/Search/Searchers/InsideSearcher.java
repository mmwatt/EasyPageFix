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
import org.w3c.dom.css.CSSRule;
import org.w3c.dom.css.CSSStyleRule;
import org.w3c.dom.css.CSSValue;

//"\"\\S+\" is not completely inside. The offset is -?\\d+px."
public class InsideSearcher extends BaseSearcher {

  public InsideSearcher() {
    type = ErrorType.INSIDE;
  }


  @Override
  public CSSStyleRule[] bestSingleFix(WebPage oldPage, PageError error, BrowserTools browserTools) {
    int bestScore;
    CSSStyleRule bestRule = null;
    WebPage currentPage = oldPage;

    //Try the whole process 3 times, iterating on the same page (or until score == 0 (perfect fix))
    for (int i = 0; i < 3; i++) {
      PageError currentError = SingleFixSearcher.findCorrespondingError(currentPage.errors, error);
      if (currentError == null) {
        System.out.println("COULD NOT FIND INSIDE ERROR. PROGRAM FLOW INCORRECT");
        return new CSSStyleRule[]{};
      }
      bestScore = computeOldScore(currentPage, currentError, browserTools);
      CSSStyleRule[] oldRules = elementRules(oldPage, currentError);
      //Try and find a change for each rule
      for (CSSStyleRule oldRule : oldRules) {
        PositionGenerator positionGen = new PositionGenerator();
        CSSPropertyGenerator leftGen = new LeftGenerator();
        CSSPropertyGenerator rightGen = new RightGenerator();
        CSSPropertyGenerator topGen = new TopGenerator();
        CSSPropertyGenerator bottomGen = new BottomGenerator();

        Property positionProp = positionGen.relative();

        CSSPropertyGenerator[] posGenerators = new CSSPropertyGenerator[]{leftGen, rightGen, topGen,
            bottomGen};

        for (CSSPropertyGenerator gen : posGenerators) {
          System.out.println("Running generator: " + gen.getName());
          Property[] possValues = gen.possibleValues(generateValue(currentError));

          for (Property possValue : possValues) {
            CSSStyleRule newRule = newRule(oldRule, new Property[]{positionProp, possValue});
            int newScore = Integer.MAX_VALUE;
            try {
              newScore = SingleFixSearcher
                  .eval(currentPage, currentError, browserTools, new CSSRule[]{newRule});
            } catch (IOException | ParseException e) {
              e.printStackTrace(); //hopefully shouldn't happen. don't add to list if it does
            }
            if (newScore == 0) {
              return new CSSStyleRule[]{newRule};
            } else if (newScore < bestScore) {
              bestRule = newRule;
              bestScore = newScore;
            }
          }
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

  private CSSValue generateValue(PageError error) {
    CSSValueImpl value = new CSSValueImpl();
    value.setCssText(error.amount + "px");
    return value;
  }
}
