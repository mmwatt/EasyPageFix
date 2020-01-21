/*
 * Copyright (c) 2019 Matthew Watt & University of Sheffield. This project is based on ideas by Phil McMinn.
 */

package EasyPageFix.Search.Searchers;

import EasyPageFix.Errors.ErrorType;
import EasyPageFix.Errors.PageError;
import EasyPageFix.Pages.BrowserTools;
import EasyPageFix.Pages.WebPage;
import EasyPageFix.Search.Searchers.CSSPropertyGenerators.CSSPropertyGenerator;
import EasyPageFix.Search.Searchers.CSSPropertyGenerators.IdentGenerators.DisplayGenerator;
import com.steadystate.css.dom.Property;
import java.io.IOException;
import java.text.ParseException;
import java.util.LinkedList;
import org.w3c.dom.css.CSSRule;
import org.w3c.dom.css.CSSStyleRule;

public class VisibleSearcher extends BaseSearcher {

  public VisibleSearcher() {
    type = ErrorType.VISIBLE;
  }

  @Override
  public CSSStyleRule[] bestSingleFix(WebPage oldPage, PageError error, BrowserTools browserTools) {
    int oldScore = computeOldScore(oldPage, error, browserTools);

    CSSStyleRule[] oldRules = elementRules(oldPage, error);
    LinkedList<CSSStyleRule> newRuleList = new LinkedList<>();

    CSSPropertyGenerator generator = new DisplayGenerator();
    //TODO add another path to account for occlusion by other objects (z index?)
    for (CSSStyleRule oldRule : oldRules) {
      if (oldRule.getCssText().contains("display: none")) {
        Property p = generator.randomValue();
        CSSStyleRule newRule = newRule(oldRule, p);
        int newScore = Integer.MAX_VALUE;
        try {
          newScore = SingleFixSearcher.eval(oldPage, error, browserTools, new CSSRule[]{newRule});
        } catch (IOException | ParseException e) {
          e.printStackTrace(); //hopefully shouldn't happen. don't add to list if it does
        }
        if (newScore == 0) {
          return new CSSStyleRule[]{newRule};
        } else if (newScore < oldScore) {
          newRuleList.add(newRule);
        }
      }
    }
    return newRuleList.toArray(new CSSStyleRule[0]);
  }
}
