/*
 * Copyright (c) 2019 Matthew Watt & University of Sheffield. This project is based on ideas by Phil McMinn.
 */

package EasyPageFix.Errors.ErrorMatchers;

import EasyPageFix.Errors.ErrorType;
import EasyPageFix.Errors.PageError;
import java.text.ParseException;

public class Visible extends ErrorMatcherBase {

  public Visible() {
    fullMatchStr = "\"\\S+\" is not visible on page";
    compile();
  }

  @Override
  public PageError parseMessage(String message) throws ParseException {
    String[] elems = ErrorMatchHelpers.basicElemMatch(message, ErrorType.VISIBLE);
    int amount = 100;
    return new PageError(elems, ErrorType.VISIBLE, amount, message);
  }
}
