/*
 * Copyright (c) 2019 Matthew Watt & University of Sheffield. This project is based on ideas by Phil McMinn.
 */

package EasyPageFix.Errors.ErrorMatchers;

import EasyPageFix.Errors.ErrorType;
import EasyPageFix.Errors.PageError;
import java.text.ParseException;

public class Aligned extends ErrorMatcherBase {

  public Aligned() {
    fullMatchStr = "\"\\S+\" is not aligned \\S+ \\S* with \"\\S+\". Offset is -?\\d+px";
    this.compile();
  }

  @Override
  public PageError parseMessage(String message) throws ParseException {
    String[] elems = ErrorMatchHelpers.basicElemMatch(message, ErrorType.ALIGNMENT);
    int amount = ErrorMatchHelpers.pixelAmountMatch(message);
    return new PageError(elems, ErrorType.ALIGNMENT, amount, message);
  }
}
