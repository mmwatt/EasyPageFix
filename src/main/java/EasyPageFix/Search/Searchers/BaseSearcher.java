/*
 * Copyright (c) 2019 Matthew Watt & University of Sheffield. This project is based on ideas by Phil McMinn.
 */

package EasyPageFix.Search.Searchers;

import EasyPageFix.Errors.ErrorType;
import EasyPageFix.Errors.PageError;
import EasyPageFix.Pages.BrowserTools;
import EasyPageFix.Pages.WebPage;
import com.steadystate.css.dom.CSSStyleRuleImpl;
import com.steadystate.css.dom.Property;
import java.io.IOException;
import java.text.ParseException;
import org.w3c.dom.css.CSSStyleRule;

public abstract class BaseSearcher implements SingleFixSearcher {

  ErrorType type;

  @Override
  public boolean matchesError(PageError error) {
    return error.type == this.type;
  }

  /**
   * Create a new version of a rule with an existing selector from an old rule
   */
  static CSSStyleRule newRule(CSSStyleRule oldRule, Property newProperty) {
    String text = oldRule.getSelectorText() + " {\n" + newProperty.getCssText() + "\n}";
    CSSStyleRuleImpl newRule = new CSSStyleRuleImpl();
    newRule.setCssText(text);
    return newRule;
  }

  /**
   * Create a new version of a rule with an existing selector from an old rule and multiple new
   * properties
   */
  static CSSStyleRule newRule(CSSStyleRule oldRule, Property[] newProperties) {
    StringBuilder text = new StringBuilder(oldRule.getSelectorText() + "{\n");
    for (Property p : newProperties) {
      text.append(p.getCssText()).append(";\n");
    }
    text.append("\n}");

    CSSStyleRuleImpl newRule = new CSSStyleRuleImpl();
    newRule.setCssText(text.toString());
    return newRule;
  }

  static CSSStyleRule[] elementRules(WebPage oldPage, PageError error) {
    //Try with ID

    for (String e : error.elements) {
      CSSStyleRule[] rules = oldPage.css.getStyleRulesWithId(error.elements[0]);
      if (rules.length == 0) {
        rules = oldPage.css.getStyleRulesWithClass(e);
        if (rules.length == 0) {
          rules = oldPage.css.getStyleRulesWithString(e);
          if (rules.length == 0) {
            rules = oldPage.css.getRulesWithSimilarSelectors(e);
          }
        }
      }
      if (rules.length > 0) {
        return rules;
      }
    }
    return new CSSStyleRule[]{};
  }

  /**
   * Gets the score for an unmodified page (nothing in 'catch' block)
   */
  static int computeOldScore(WebPage oldPage, PageError error, BrowserTools browserTools) {
    try {
      return SingleFixSearcher.eval(oldPage, error, browserTools, null);
    } catch (IOException | ParseException e) {
      e.printStackTrace();
      //This shouldn't happen, the page hasn't been modified yet.
      return Integer.MAX_VALUE;
    }
  }
}
