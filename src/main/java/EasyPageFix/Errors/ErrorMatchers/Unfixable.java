/*
 * Copyright (c) 2019 Matthew Watt & University of Sheffield. This project is based on ideas by Phil McMinn.
 */

package EasyPageFix.Errors.ErrorMatchers;

import EasyPageFix.Errors.ErrorType;
import EasyPageFix.Errors.PageError;
import java.text.ParseException;

public class Unfixable implements ErrorMatcher {

  @Override
  public boolean matches(String message) {
    return true;
  }

  @Override
  public PageError parseMessage(String message) throws ParseException {
    PageError p = new PageError();
    p.type = ErrorType.UNFIXABLE;
    p.message = message;
    return p;
  }
}
