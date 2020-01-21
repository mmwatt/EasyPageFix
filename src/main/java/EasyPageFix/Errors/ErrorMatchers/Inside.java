/*
 * Copyright (c) 2019 Matthew Watt & University of Sheffield. This project is based on ideas by Phil McMinn.
 */

package EasyPageFix.Errors.ErrorMatchers;

import EasyPageFix.Errors.ErrorType;
import EasyPageFix.Errors.PageError;
import java.text.ParseException;

public class Inside extends ErrorMatcherBase {

  public Inside() {
    fullMatchStr = "\"\\S+\" is not completely inside. The offset is -?\\d+px.";
    compile();
  }

  @Override
  public PageError parseMessage(String message) throws ParseException {

    String[] elems = ErrorMatchHelpers.basicElemMatch(message, ErrorType.INSIDE);

    int amount = ErrorMatchHelpers.pixelAmountMatch(message);

    return new PageError(elems, ErrorType.INSIDE, amount, message);
  }
}
