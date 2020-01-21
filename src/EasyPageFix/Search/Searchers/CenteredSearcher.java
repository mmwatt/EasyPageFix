/*
 * Copyright (c) 2019 Matthew Watt & University of Sheffield. This project is based on ideas by Phil McMinn.
 */

package EasyPageFix.Search.Searchers;

import EasyPageFix.Errors.ErrorType;
import EasyPageFix.Errors.PageError;
import EasyPageFix.Pages.BrowserTools;
import EasyPageFix.Pages.WebPage;
import EasyPageFix.Search.Searchers.CSSPropertyGenerators.CSSPropertyGenerator;
import EasyPageFix.Search.Searchers.CSSPropertyGenerators.IdentGenerators.MarginCenterGenerator;
import EasyPageFix.Search.Searchers.CSSPropertyGenerators.PxGenerators.PaddingBottomGenerator;
import EasyPageFix.Search.Searchers.CSSPropertyGenerators.PxGenerators.PaddingTopGenerator;
import com.steadystate.css.dom.Property;
import java.io.IOException;
import java.text.ParseException;
import java.util.LinkedList;
import java.util.Random;
import org.w3c.dom.css.CSSRule;
import org.w3c.dom.css.CSSStyleRule;
import org.w3c.dom.css.CSSValue;

public class CenteredSearcher extends BaseSearcher {

  public CenteredSearcher() {
    type = ErrorType.CENTERING;
  }

  @Override //TODO try multiple times to find a fix for the issue
  public CSSStyleRule[] bestSingleFix(WebPage oldPage, PageError error, BrowserTools browserTools) {
    LinkedList<CSSStyleRule> fixesList = new LinkedList<>();
    //Find out what rules are associated with the error/element
    CSSStyleRule[] oldRules = elementRules(oldPage, error);
    //Evaluate the current page (for this error)
    int oldScore = computeOldScore(oldPage, error, browserTools);

    //Figure out whether the centering is horizontal or vertical
    if (error.relations[0].contains("horizontal") && oldRules != null) {
      //Try to apply centering margins to all the selectors/rules
      CSSPropertyGenerator marginCenterGenerator = new MarginCenterGenerator();
      for (CSSStyleRule rule : oldRules) {
        Property p = marginCenterGenerator.randomValue(); //not actually random in this case
        CSSStyleRule newRule = newRule(rule, p);
        try {
          int newScore = SingleFixSearcher
              .eval(oldPage, error, browserTools, new CSSRule[]{newRule});
          if (newScore == 0) {
            return new CSSStyleRule[]{newRule};
          } else if (newScore < oldScore) {
            fixesList.add(newRule);
          }
          //If the score is worse, we don't want to keep this rule.
        } catch (IOException | ParseException e) {
          //Don't have to stop program. Just don't add change to the list
          e.printStackTrace();
        }
      }
      //TODO add more horizontal centering code.

      return fixesList.toArray(new CSSStyleRule[0]);
    } else if (error.relations[0].contains("vertical") && oldRules != null) {
      //Add some padding
      Property[][] properties = generateVerticalPadding(oldRules);
      for (CSSStyleRule oldRule : oldRules) {
        for (int i = 0; i < properties[0].length; i++) {
          CSSStyleRule newRule1 = newRule(oldRule, properties[0][i]);
          CSSStyleRule newRule2 = newRule(oldRule, properties[1][i]);
          try {
            int newScore = SingleFixSearcher
                .eval(oldPage, error, browserTools, new CSSRule[]{newRule1, newRule2});
            if (newScore == 0) {
              return new CSSStyleRule[]{newRule1, newRule2};
            } else if (newScore < oldScore) {
              fixesList.add(newRule1);
              fixesList.add(newRule2);
            }
            //If the score is worse, we don't want to keep this rule.
          } catch (IOException | ParseException e) {
            //Don't have to stop program. Just don't add change to the list
            e.printStackTrace();
          }
        }
      }
      return fixesList.toArray(new CSSStyleRule[0]);
    }

    //TODO try adding some left/right/top/bottom values
    return null;
  }


  private Property[][] generateVerticalPadding(CSSStyleRule[] oldRules) {
    int startPadding = 0;
    //If we can find a value, use that as the starting point. If not, generate a random one
    int i = 0;
    while (i < oldRules.length && startPadding == 0) {
      if (oldRules[i].getStyle().getCssText().contains("padding")) {
        CSSValue existingValue;
        if (oldRules[i].getStyle().getPropertyCSSValue("padding") != null) {
          existingValue = oldRules[i].getStyle().getPropertyCSSValue("padding");
          startPadding = Integer.parseInt(existingValue.getCssText().replaceAll("\\D+", ""));
        } else if (oldRules[i].getStyle().getPropertyCSSValue("padding-top") != null) {
          existingValue = oldRules[i].getStyle().getPropertyCSSValue("padding-top");
          startPadding = Integer.parseInt(existingValue.getCssText().replaceAll("\\D+", ""));
        } else if (oldRules[i].getStyle().getPropertyCSSValue("padding-bottom") != null) {
          existingValue = oldRules[i].getStyle().getPropertyCSSValue("padding-bottom");
          startPadding = Integer.parseInt(existingValue.getCssText().replaceAll("\\D+", ""));
        } else {
          //Couldn't find any padding rules
        }
      }
      i++;
    }
    if (startPadding == 0) {
      startPadding = new Random().nextInt(150);
    }
    //Use this as the value for random generation
    Property[] paddingTop = new PaddingTopGenerator().possibleValues(startPadding);
    Property[] paddingBottom = new PaddingBottomGenerator().possibleValues(startPadding);
    return new Property[][]{paddingTop, paddingBottom};
  }

}
