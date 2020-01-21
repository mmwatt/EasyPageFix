/*
 * Copyright (c) 2019 Matthew Watt & University of Sheffield. This project is based on ideas by Phil McMinn.
 */

package EasyPageFix.Errors.ErrorMatchers;

import EasyPageFix.Errors.ErrorType;
import EasyPageFix.Errors.PageError;
import java.text.ParseException;

public class NotAbsent extends ErrorMatcherBase {

  public NotAbsent() {
    fullMatchStr = "\"\\S+\" is not absent on page";
    this.compile();
  }

  @Override
  public PageError parseMessage(String message) throws ParseException {

    String[] elements = ErrorMatchHelpers.basicElemMatch(message, ErrorType.NOT_ABSENT);

    int amount = 100;

    return new PageError(elements, ErrorType.NOT_ABSENT, amount, message);
  }

}
