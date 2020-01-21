/*
 * Copyright (c) 2019 Matthew Watt & University of Sheffield. This project is based on ideas by Phil McMinn.
 */

package EasyPageFix.Errors;

import EasyPageFix.Errors.ErrorMatchers.Aligned;
import EasyPageFix.Errors.ErrorMatchers.Centered;
import EasyPageFix.Errors.ErrorMatchers.CenteredOffset;
import EasyPageFix.Errors.ErrorMatchers.Contains;
import EasyPageFix.Errors.ErrorMatchers.DirectionPosition;
import EasyPageFix.Errors.ErrorMatchers.ErrorMatcher;
import EasyPageFix.Errors.ErrorMatchers.Inside;
import EasyPageFix.Errors.ErrorMatchers.Near;
import EasyPageFix.Errors.ErrorMatchers.NotAbsent;
import EasyPageFix.Errors.ErrorMatchers.Size;
import EasyPageFix.Errors.ErrorMatchers.Unfixable;
import EasyPageFix.Errors.ErrorMatchers.Visible;
import java.text.ParseException;


/**
 * @author Matthew Watt
 *     Used to interpret error strings and perform pattern matching, relating them to known error
 *     types.
 */
public class ErrorParser {

  private static ErrorMatcher[] errorMatchers = {
      new NotAbsent(),
      new Aligned(),
      new CenteredOffset(),
      new Centered(),
      new Contains(),
      new Inside(),
//      new ColorScheme(), unused
      new Size(),
      new DirectionPosition(),
      new Visible(),
      new Near(),
      new Unfixable()
  };


  public static PageError parse(String message) throws ParseException {
    for (ErrorMatcher errorMatcher : errorMatchers) {
      if (errorMatcher.matches(message)) {
        return errorMatcher.parseMessage(message);
      }
    }
    return null; //There are no parse-able errors
  }
}
