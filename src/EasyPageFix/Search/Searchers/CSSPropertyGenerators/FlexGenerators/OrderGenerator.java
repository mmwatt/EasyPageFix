/*
 * Copyright (c) 2019 Matthew Watt & University of Sheffield. This project is based on ideas by Phil McMinn.
 */

package EasyPageFix.Search.Searchers.CSSPropertyGenerators.FlexGenerators;

import EasyPageFix.Search.Searchers.CSSPropertyGenerators.CSSPropertyGeneratorNumber;

public class OrderGenerator extends CSSPropertyGeneratorNumber {

  public OrderGenerator() {
    super("order", 10, 4);
  }
}
