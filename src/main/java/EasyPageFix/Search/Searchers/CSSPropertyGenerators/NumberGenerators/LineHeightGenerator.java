/*
 * Copyright (c) 2019 Matthew Watt & University of Sheffield. This project is based on ideas by Phil McMinn.
 */

package EasyPageFix.Search.Searchers.CSSPropertyGenerators.NumberGenerators;

import EasyPageFix.Search.Searchers.CSSPropertyGenerators.CSSPropertyGeneratorFloat;

public class LineHeightGenerator extends CSSPropertyGeneratorFloat {

  public LineHeightGenerator() {
    super("line-height", 3, 4);
  }
}
