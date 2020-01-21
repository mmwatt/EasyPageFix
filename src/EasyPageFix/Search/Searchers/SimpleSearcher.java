/*
 * Copyright (c) 2019 Matthew Watt & University of Sheffield. This project is based on ideas by Phil McMinn.
 */

package EasyPageFix.Search.Searchers;

import EasyPageFix.Errors.PageError;
import EasyPageFix.Pages.BrowserTools;
import EasyPageFix.Pages.WebPage;
import EasyPageFix.Search.Searchers.CSSPropertyGenerators.CSSPropertyGenerator;
import EasyPageFix.Search.Searchers.CSSPropertyGenerators.FlexGenerators.FlexBasisGenerator;
import EasyPageFix.Search.Searchers.CSSPropertyGenerators.FlexGenerators.FlexDirectionGenerator;
import EasyPageFix.Search.Searchers.CSSPropertyGenerators.FlexGenerators.FlexGrowGenerator;
import EasyPageFix.Search.Searchers.CSSPropertyGenerators.FlexGenerators.FlexShrinkGenerator;
import EasyPageFix.Search.Searchers.CSSPropertyGenerators.FlexGenerators.FlexWrapGenerator;
import EasyPageFix.Search.Searchers.CSSPropertyGenerators.FlexGenerators.JustifyContentGenerator;
import EasyPageFix.Search.Searchers.CSSPropertyGenerators.FlexGenerators.OrderGenerator;
import EasyPageFix.Search.Searchers.CSSPropertyGenerators.IdentGenerators.AlignContentGenerator;
import EasyPageFix.Search.Searchers.CSSPropertyGenerators.IdentGenerators.AlignItemsGenerator;
import EasyPageFix.Search.Searchers.CSSPropertyGenerators.IdentGenerators.AlignSelfGenerator;
import EasyPageFix.Search.Searchers.CSSPropertyGenerators.IdentGenerators.BackgroundPositionGenerator;
import EasyPageFix.Search.Searchers.CSSPropertyGenerators.IdentGenerators.BorderStyleGenerator;
import EasyPageFix.Search.Searchers.CSSPropertyGenerators.IdentGenerators.ColumnFillGenerator;
import EasyPageFix.Search.Searchers.CSSPropertyGenerators.IdentGenerators.ColumnSpanGenerator;
import EasyPageFix.Search.Searchers.CSSPropertyGenerators.IdentGenerators.DisplayGenerator;
import EasyPageFix.Search.Searchers.CSSPropertyGenerators.IdentGenerators.FloatGenerator;
import EasyPageFix.Search.Searchers.CSSPropertyGenerators.IdentGenerators.MarginCenterGenerator;
import EasyPageFix.Search.Searchers.CSSPropertyGenerators.IdentGenerators.PositionGenerator;
import EasyPageFix.Search.Searchers.CSSPropertyGenerators.IdentGenerators.TextAlignGenerator;
import EasyPageFix.Search.Searchers.CSSPropertyGenerators.IdentGenerators.VerticalAlignIdentGenerator;
import EasyPageFix.Search.Searchers.CSSPropertyGenerators.NumberGenerators.ColumnCountGenerator;
import EasyPageFix.Search.Searchers.CSSPropertyGenerators.NumberGenerators.FontWeightGenerator;
import EasyPageFix.Search.Searchers.CSSPropertyGenerators.NumberGenerators.LineHeightGenerator;
import EasyPageFix.Search.Searchers.CSSPropertyGenerators.PxGenerators.BorderWidthGenerator;
import EasyPageFix.Search.Searchers.CSSPropertyGenerators.PxGenerators.BottomGenerator;
import EasyPageFix.Search.Searchers.CSSPropertyGenerators.PxGenerators.ColumnWidthGenerator;
import EasyPageFix.Search.Searchers.CSSPropertyGenerators.PxGenerators.FontSizeGenerator;
import EasyPageFix.Search.Searchers.CSSPropertyGenerators.PxGenerators.HeightGenerator;
import EasyPageFix.Search.Searchers.CSSPropertyGenerators.PxGenerators.LeftGenerator;
import EasyPageFix.Search.Searchers.CSSPropertyGenerators.PxGenerators.MarginBottomGenerator;
import EasyPageFix.Search.Searchers.CSSPropertyGenerators.PxGenerators.MarginLeftGenerator;
import EasyPageFix.Search.Searchers.CSSPropertyGenerators.PxGenerators.MarginRightGenerator;
import EasyPageFix.Search.Searchers.CSSPropertyGenerators.PxGenerators.MarginTopGenerator;
import EasyPageFix.Search.Searchers.CSSPropertyGenerators.PxGenerators.MaxHeightGenerator;
import EasyPageFix.Search.Searchers.CSSPropertyGenerators.PxGenerators.MaxWidthGenerator;
import EasyPageFix.Search.Searchers.CSSPropertyGenerators.PxGenerators.MinHeightGenerator;
import EasyPageFix.Search.Searchers.CSSPropertyGenerators.PxGenerators.MinWidthGenerator;
import EasyPageFix.Search.Searchers.CSSPropertyGenerators.PxGenerators.PaddingBottomGenerator;
import EasyPageFix.Search.Searchers.CSSPropertyGenerators.PxGenerators.PaddingLeftGenerator;
import EasyPageFix.Search.Searchers.CSSPropertyGenerators.PxGenerators.PaddingRightGenerator;
import EasyPageFix.Search.Searchers.CSSPropertyGenerators.PxGenerators.PaddingTopGenerator;
import EasyPageFix.Search.Searchers.CSSPropertyGenerators.PxGenerators.RightGenerator;
import EasyPageFix.Search.Searchers.CSSPropertyGenerators.PxGenerators.TextIndentGenerator;
import EasyPageFix.Search.Searchers.CSSPropertyGenerators.PxGenerators.TopGenerator;
import EasyPageFix.Search.Searchers.CSSPropertyGenerators.PxGenerators.WidthGenerator;
import com.steadystate.css.dom.CSSStyleRuleImpl;
import com.steadystate.css.dom.Property;
import java.util.LinkedList;
import org.w3c.dom.css.CSSStyleRule;

/**
 * Searcher which takes in a type of error and attempts to brute-force a solution
 */
public class SimpleSearcher extends BaseSearcher {

  //All generators being used by the searcher
  CSSPropertyGenerator[] generators = {
      new AlignContentGenerator(),
      new AlignItemsGenerator(),
      new AlignSelfGenerator(),
      new BackgroundPositionGenerator(),
      new BorderStyleGenerator(),
      new ColumnFillGenerator(),
      new ColumnSpanGenerator(),
      new DisplayGenerator(),
      new FloatGenerator(),
      new MarginCenterGenerator(),
      new PositionGenerator(),
      new TextAlignGenerator(),
      new VerticalAlignIdentGenerator(),
      new ColumnCountGenerator(),
      new FontWeightGenerator(),
      new LineHeightGenerator(),
      new BorderWidthGenerator(),
      new BottomGenerator(),
      new ColumnWidthGenerator(),
      new FontSizeGenerator(),
      new HeightGenerator(),
      new LeftGenerator(),
      new MarginBottomGenerator(),
      new MarginLeftGenerator(),
      new MarginRightGenerator(),
      new MarginTopGenerator(),
      new MaxHeightGenerator(),
      new MaxWidthGenerator(),
      new MinHeightGenerator(),
      new MinWidthGenerator(),
      new PaddingBottomGenerator(),
      new PaddingLeftGenerator(),
      new PaddingRightGenerator(),
      new PaddingTopGenerator(),
      new RightGenerator(),
      new TextIndentGenerator(),
      new TopGenerator(),
      new WidthGenerator(),
      new ColumnFillGenerator(),
      new ColumnSpanGenerator(),
      new ColumnWidthGenerator(),
      new ColumnCountGenerator(),
      new DisplayGenerator(),
      new FlexBasisGenerator(),
      new FlexDirectionGenerator(),
      new FlexGrowGenerator(),
      new FlexShrinkGenerator(),
      new FlexWrapGenerator(),
      new JustifyContentGenerator(),
      new OrderGenerator()
  };

  public SimpleSearcher() {
  }

  @Override
  public CSSStyleRule[] bestSingleFix(WebPage oldPage, PageError error, BrowserTools browserTools) {
    //For now, shoves all changes into a list
    return mutations(oldPage, error);
  }

  private CSSStyleRule[] mutations(WebPage oldPage, PageError error) {
    LinkedList<CSSStyleRule> ruleList = new LinkedList<CSSStyleRule>();

    //For every element mentioned in the error
    for (String element : error.elements) {

      //Get all of the rules that apply to it, try ID, class, then just string match
      CSSStyleRule[] elementRules;
      elementRules = oldPage.css.getStyleRulesWithId(element);
      if (elementRules == null) {
        elementRules = oldPage.css.getStyleRulesWithClass(element);
        if (elementRules == null) {
          elementRules = oldPage.css.getStyleRulesWithString(element);
        }
      }

      //for every rule
      for (CSSStyleRule elemRule : elementRules) {
        CSSStyleRuleImpl elemRuleImpl = (CSSStyleRuleImpl) elemRule;
        //for every generator
        for (CSSPropertyGenerator gen : generators) {
          //check for the property name in the css text and generate a random value. notice the NOT
          if (!elemRuleImpl.getStyle().getPropertyValue(gen.getName()).equals("")) {
            Property p = gen.randomValue();
            CSSStyleRule newRule = newRule(elemRule, p);
            //add it to the list
            ruleList.add(newRule);
          }
        }
      }
    }

    System.out.println("Generated random changes:");
    System.out.println(ruleList);
    //a full list of random changes to make
    return ruleList.toArray(new CSSStyleRule[]{});
  }

  /**
   * Attempts to solve any error, so will pretend it can match them all.
   *
   * @param error Unused (but required by interface)
   * @return true (to match anything)
   */
  @Override
  public boolean matchesError(PageError error) {
    return true;
  }
}
