/*
 * Copyright (c) 2019 Matthew Watt & University of Sheffield. This project is based on ideas by Phil McMinn.
 */

package EasyPageFix.Errors.ErrorMatchers;

import EasyPageFix.Errors.ErrorType;
import EasyPageFix.Errors.PageError;
import java.text.ParseException;

public class Contains extends ErrorMatcherBase {

  public Contains() {
    fullMatchStr = "\"\\S+\" is outside \"\\S+\"";
    compile();
  }

  @Override
  public PageError parseMessage(String message) throws ParseException {

    String[] elements = ErrorMatchHelpers.basicElemMatch(message, ErrorType.CONTAINS);

    int amount = 100;

    return new PageError(elements, ErrorType.CONTAINS, amount, message);
  }
}
