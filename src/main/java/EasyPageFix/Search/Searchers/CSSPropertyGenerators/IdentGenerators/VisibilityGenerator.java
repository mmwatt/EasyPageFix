/*
 * Copyright (c) 2019 Matthew Watt & University of Sheffield. This project is based on ideas by Phil McMinn.
 */

package EasyPageFix.Search.Searchers.CSSPropertyGenerators.IdentGenerators;

import static org.w3c.dom.css.CSSPrimitiveValue.CSS_IDENT;
import static org.w3c.dom.css.CSSValue.CSS_PRIMITIVE_VALUE;

import EasyPageFix.Search.Searchers.CSSPropertyGenerators.CSSPropertyGeneratorBase;

public class VisibilityGenerator extends CSSPropertyGeneratorBase {

  private static String[] stringValues = {
      "visible",
      "hidden",
      "collapse"
  };

  public VisibilityGenerator() {
    super(CSS_PRIMITIVE_VALUE, CSS_IDENT, "visibility", stringValues);
  }
}
