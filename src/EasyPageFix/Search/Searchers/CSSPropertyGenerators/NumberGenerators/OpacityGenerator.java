/*
 * Copyright (c) 2019 Matthew Watt & University of Sheffield. This project is based on ideas by Phil McMinn.
 */

package EasyPageFix.Search.Searchers.CSSPropertyGenerators.NumberGenerators;

import EasyPageFix.Search.Searchers.CSSPropertyGenerators.CSSPropertyGeneratorFloat;

public class OpacityGenerator extends CSSPropertyGeneratorFloat {

  public OpacityGenerator() {
    super("opacity", 1, 5);
  }
}
