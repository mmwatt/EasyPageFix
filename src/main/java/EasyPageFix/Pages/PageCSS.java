/*
 * Copyright (c) 2019 Matthew Watt & University of Sheffield. This project is based on ideas by Phil McMinn.
 */

package EasyPageFix.Pages;

import com.steadystate.css.parser.CSSOMParser;
import com.steadystate.css.parser.SACParserCSS3;
import info.debatty.java.stringsimilarity.JaroWinkler;
import java.io.IOException;
import java.io.StringReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.LinkedList;
import org.w3c.css.sac.InputSource;
import org.w3c.dom.css.CSSRule;
import org.w3c.dom.css.CSSStyleRule;
import org.w3c.dom.css.CSSStyleSheet;

/**
 * @author Matthew Watt
 *     Allows easy modification of CSS with new values for a given selector/element.
 */
public class PageCSS {

  public CSSStyleSheet styleSheet;

  /**
   * Input the whole file as a string, and parse it
   *
   * @param file The filepath or file contents
   */
  public PageCSS(String file) throws IOException {
    styleSheet = parseCssFile(file);
  }

  /**
   * Input filepath, parse the file specified
   */
  public PageCSS(Path filepath) throws IOException {
    this(new String(Files.readAllBytes(filepath)));
  }

  /**
   * Used to duplicate the object
   */
  public PageCSS duplicate() throws IOException {
    return new PageCSS(cssString());
  }

  /**
   * Returns the full string of the CSS stylesheet
   */
  public String cssString() {
    CSSRule[] rules = new CSSRule[styleSheet.getCssRules().getLength()];
    for (int i = 0; i < rules.length; i++) {
      rules[i] = styleSheet.getCssRules().item(i);
    }
    return rulesToString(rules);
  }

  /**
   * Generic method for turning a list of rules into a string (for saving to a file)
   */
  public static String rulesToString(CSSRule[] rules) {
    StringBuilder cssString = new StringBuilder();
    for (int i = 0; i < rules.length; i++) {
      CSSRule rule = rules[i];
      String ruleText = rule.getCssText();
      //Make formatting nicer
      ruleText = ruleText.replace(";", ";\n");
      ruleText = ruleText.replace("{", "{\n");
      ruleText = ruleText.replace("}", "\n}");
      cssString.append(ruleText);
      cssString.append("\n");
    }
    return cssString.toString();
  }

  /**
   * Takes the name of an element and returns associated rules
   *
   * @param text The string to look for in style rules
   * @return An array of CSSStyleRules
   */
  public CSSStyleRule[] getStyleRulesWithString(String text) {
    LinkedList<CSSStyleRule> list = new LinkedList<>();
    CSSRule[] allRules = getAllRules();

    for (CSSRule rule : allRules) {
      try {
        CSSStyleRule styleRule = (CSSStyleRule) rule;
        if (styleRule.getCssText().contains(text)) {
          list.add(styleRule);
        }
      } catch (Exception e) {
        //don't add it to the list if you can't cast it as a style rule
        //(no fixing of media rules)
      }
    }

    return list.toArray(new CSSStyleRule[0]);
  }

  /**
   * Takes a string and tries to return some rules with similar selectors
   *
   * @param text A string (hopefully similar to a selector)
   * @return Array of CSSStyleRules
   */
  public CSSStyleRule[] getRulesWithSimilarSelectors(String text) {
    LinkedList<CSSStyleRule> list = new LinkedList<>();
    CSSRule[] allRules = getAllRules();

    for (CSSRule rule : allRules) {
      try {
        CSSStyleRule styleRule = (CSSStyleRule) rule;
        JaroWinkler jw = new JaroWinkler();
        double similarity = jw.similarity(text, ((CSSStyleRule) rule).getSelectorText());
        if (similarity > 0.8) {
          list.add(styleRule);
        }
      } catch (Exception e) {
        //don't add it to the list if you can't cast it as a style rule
        //(no fixing of media rules)
      }
    }

    return list.toArray(new CSSStyleRule[0]);
  }

  /**
   * Calls getStyleRulesWithString with added "."
   *
   * @param classText The name of the class
   * @return An array of CSSStyleRules
   */
  public CSSStyleRule[] getStyleRulesWithClass(String classText) {
    return getStyleRulesWithString("." + classText);
  }


  /**
   * Calls getStyleRulesWithString with added "#"
   *
   * @param idText The name of the id
   * @return An array of CSSStyleRules
   */
  public CSSStyleRule[] getStyleRulesWithId(String idText) {
    return getStyleRulesWithString("#" + idText);
  }

  /**
   * Returns all rules in the stylesheet as an Array
   */
  public CSSRule[] getAllRules() {
    CSSRule[] rules = new CSSRule[styleSheet.getCssRules().getLength()];
    for (int i = 0; i < styleSheet.getCssRules().getLength(); i++) {
      rules[i] = styleSheet.getCssRules().item(i);
    }
    return rules;
  }

  /**
   * Wrapper to modify the rule at a certain index.
   *
   * @param index The index in the CSSRuleList
   * @param rule The new rule to replace the one at the index
   */
  public void replaceRule(int index, CSSRule rule) throws ArrayIndexOutOfBoundsException {
    if (index >= styleSheet.getCssRules().getLength()) {
      throw new ArrayIndexOutOfBoundsException();
    } else {
      styleSheet.getCssRules().item(index).setCssText(rule.getCssText());
    }
  }

  /**
   * Takes a rule, finds it in stylesheet and replaces it with another.
   *
   * @param oldRule CSSRule that must be present in the stylesheet
   * @param newRule CSSRule that must be absent from the stylesheet
   */
  public void replaceRule(CSSRule oldRule, CSSRule newRule) {
    oldRule.setCssText(newRule.getCssText());
  }

  /**
   * Adds a new rule to the stylesheet
   */
  public void addRule(CSSRule newRule) {
    if (newRule == null) {
      return;
    }
    int index = styleSheet.getCssRules().getLength();
    styleSheet.insertRule(newRule.getCssText(), index);
  }

  private static CSSStyleSheet parseCssFile(String fileString) throws IOException {
    InputSource source = new InputSource(new StringReader(fileString));
    CSSOMParser parser = new CSSOMParser(new SACParserCSS3());
    return parser.parseStyleSheet(source, null, null);
  }
}