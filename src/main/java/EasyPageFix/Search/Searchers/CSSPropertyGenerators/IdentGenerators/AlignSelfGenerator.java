/*
 * Copyright (c) 2019 Matthew Watt & University of Sheffield. This project is based on ideas by Phil McMinn.
 */

package EasyPageFix.Search.Searchers.CSSPropertyGenerators.IdentGenerators;

import static org.w3c.dom.css.CSSPrimitiveValue.CSS_IDENT;
import static org.w3c.dom.css.CSSValue.CSS_PRIMITIVE_VALUE;

import EasyPageFix.Search.Searchers.CSSPropertyGenerators.CSSPropertyGeneratorBase;

public class AlignSelfGenerator extends CSSPropertyGeneratorBase {

  private static String[] values = {
      "auto",
      "stretch",
      "center",
      "flex-start",
      "flex-end",
      "baseline"
  };

  public AlignSelfGenerator() {
    super(CSS_PRIMITIVE_VALUE, CSS_IDENT, "align-self", values);
  }

}
