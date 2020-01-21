/*
 * Copyright (c) 2019 Matthew Watt & University of Sheffield. This project is based on ideas by Phil McMinn.
 */

package EasyPageFix.Search.Searchers;

import EasyPageFix.Errors.ErrorType;
import EasyPageFix.Errors.PageError;
import EasyPageFix.Pages.BrowserTools;
import EasyPageFix.Pages.WebPage;
import com.steadystate.css.dom.CSSValueImpl;
import com.steadystate.css.dom.Property;
import java.util.LinkedList;
import org.w3c.dom.css.CSSStyleRule;
import org.w3c.dom.css.CSSValue;

public class NotAbsentSearcher extends BaseSearcher {

  public NotAbsentSearcher() {
    type = ErrorType.NOT_ABSENT;
  }

  @Override
  public CSSStyleRule[] bestSingleFix(WebPage oldPage, PageError error, BrowserTools browserTools) {
    CSSStyleRule[] oldRules = elementRules(oldPage, error);
    LinkedList<CSSStyleRule> newRuleList = new LinkedList<>();
    for (CSSStyleRule oldRule : oldRules) {
      CSSValue value = new CSSValueImpl();
      value.setCssText("display: none");
      Property p = new Property();
      p.setValue(value);
      newRuleList.add(newRule(oldRule, p));
    }
    return newRuleList.toArray(new CSSStyleRule[0]);
  }
}
