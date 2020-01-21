/*
 * Copyright (c) 2019 Matthew Watt & University of Sheffield. This project is based on ideas by Phil McMinn.
 */

package EasyPageFix.Search.Searchers.CSSPropertyGenerators.IdentGenerators;

import static org.w3c.dom.css.CSSPrimitiveValue.CSS_IDENT;
import static org.w3c.dom.css.CSSValue.CSS_PRIMITIVE_VALUE;

import EasyPageFix.Search.Searchers.CSSPropertyGenerators.CSSPropertyGeneratorBase;

public class DisplayGenerator extends CSSPropertyGeneratorBase {

  //TODO remove unnecessary or uncommon values
  private static String[] values = {
      "inline",
      "block",
      "contents",
      "flex",
      "grid",
      "inline-block",
      "inline-flex",
      "inline-grid",
      "inline-table",
      "list-item",
      "run-in",
      "table",
      "table-caption",
      "table-column-group",
      "table-header-group",
      "table-footer-group",
      "table-row-group",
      "table-cell",
      "table-column",
      "table-row",
      "none"
  };

  public DisplayGenerator() {
    super(CSS_PRIMITIVE_VALUE, CSS_IDENT, "display", values);
  }

}
