/*
 * Copyright (c) 2019 Matthew Watt & University of Sheffield. This project is based on ideas by Phil McMinn.
 */

package EasyPageFix.Search.Searchers.CSSPropertyGenerators.IdentGenerators;

import static org.w3c.dom.css.CSSPrimitiveValue.CSS_IDENT;
import static org.w3c.dom.css.CSSValue.CSS_VALUE_LIST;

import EasyPageFix.Search.Searchers.CSSPropertyGenerators.CSSPropertyGeneratorBase;

public class BackgroundPositionGenerator extends CSSPropertyGeneratorBase {

  private static String[] stringValues = {
      "left top",
      "left center",
      "left bottom",
      "right top",
      "right center",
      "right bottom",
      "center top",
      "center center",
      "center bottom"
  };

  public BackgroundPositionGenerator() {
    super(CSS_VALUE_LIST, CSS_IDENT, "background-position", stringValues);
  }
}
