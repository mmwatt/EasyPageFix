/*
 * Copyright (c) 2019 Matthew Watt & University of Sheffield. This project is based on ideas by Phil McMinn.
 */

package EasyPageFix.Search.Searchers;

import EasyPageFix.Errors.ErrorType;
import EasyPageFix.Errors.PageError;
import EasyPageFix.Pages.BrowserTools;
import EasyPageFix.Pages.WebPage;
import EasyPageFix.Search.Searchers.CSSPropertyGenerators.CSSPropertyGenerator;
import EasyPageFix.Search.Searchers.CSSPropertyGenerators.PxGenerators.HeightGenerator;
import EasyPageFix.Search.Searchers.CSSPropertyGenerators.PxGenerators.WidthGenerator;
import com.steadystate.css.dom.CSSValueImpl;
import com.steadystate.css.dom.Property;
import java.io.IOException;
import java.text.ParseException;
import org.w3c.dom.css.CSSRule;
import org.w3c.dom.css.CSSStyleRule;
import org.w3c.dom.css.CSSValue;

//"free-pic-1" width is 250px instead of 150px
public class SizeSearcher extends BaseSearcher {

  public SizeSearcher() {
    type = ErrorType.SIZE;
  }

  @Override
  public CSSStyleRule[] bestSingleFix(WebPage oldPage, PageError error, BrowserTools browserTools) {
    int bestScore;
    CSSStyleRule bestRule = null;
    WebPage currentPage = oldPage;

    for (int i = 0; i < 3; i++) {
      PageError currentError = SingleFixSearcher.findCorrespondingError(currentPage.errors, error);
      if (currentError == null) {
        System.out.println("COULD NOT FIND SIZE ERROR. PROGRAM FLOW INCORRECT");
        return new CSSStyleRule[]{};
      }
      bestScore = computeOldScore(currentPage, currentError, browserTools);
      CSSStyleRule[] oldRules = elementRules(currentPage, currentError);

      //Assuming there is an old rule
      for (CSSStyleRule oldRule : oldRules) {
        CSSPropertyGenerator generator =
            (currentError.type == ErrorType.HEIGHT) ? new HeightGenerator() : new WidthGenerator();

        Property[] properties = generator.possibleValues(generateValue(oldRule, currentError));

        for (int j = 0; j < properties.length; j++) {
          CSSStyleRule newRule = newRule(oldRule, properties[j]);
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
            bestScore = newScore;
            bestRule = newRule;
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
    if (bestRule == null) {
      return new CSSStyleRule[]{};
    } else {
      return new CSSStyleRule[]{bestRule};
    }
  }

  private CSSValue generateValue(CSSStyleRule oldRule, PageError error) {
    if (oldRule == null) {
      CSSValue value = new CSSValueImpl();
      value.setCssText(error.amount + "px");
      return value;
    } else { //i.e. if there is an old rule
      CSSValue heightValue = oldRule.getStyle().getPropertyCSSValue("height");
      CSSValue widthValue = oldRule.getStyle().getPropertyCSSValue("width");
      if (heightValue == null && widthValue == null) {
        CSSValue value = new CSSValueImpl();
        value.setCssText(error.amount + "px");
        return value;
      } else {
        if (widthValue != null) {
          return widthValue;
        } else {
          return heightValue;
        }
      }
    }
  }

  @Override
  public boolean matchesError(PageError error) {
    return error.type == ErrorType.HEIGHT || error.type == ErrorType.WIDTH;
  }
}
