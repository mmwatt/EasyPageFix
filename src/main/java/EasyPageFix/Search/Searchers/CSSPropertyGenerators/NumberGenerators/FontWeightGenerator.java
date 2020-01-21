/*
 * Copyright (c) 2019 Matthew Watt & University of Sheffield. This project is based on ideas by Phil McMinn.
 */

package EasyPageFix.Search.Searchers.CSSPropertyGenerators.NumberGenerators;

import EasyPageFix.Search.Searchers.CSSPropertyGenerators.CSSPropertyGeneratorNumber;

public class FontWeightGenerator extends CSSPropertyGeneratorNumber {

  public FontWeightGenerator() {
    super("font-weight", 900, 5);
  }
}
