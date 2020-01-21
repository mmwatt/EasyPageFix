/*
 * Copyright (c) 2019 Matthew Watt & University of Sheffield. This project is based on ideas by Phil McMinn.
 */

package EasyPageFix.Errors.ErrorMatchers;

import EasyPageFix.Errors.PageError;
import java.text.ParseException;

public interface ErrorMatcher {

  /**
   * Takes a Galen error message and returns true if it matches this error type.
   *
   * @param message The Galen error message
   * @return boolean, True if a match is detected.
   */
  boolean matches(String message);

  /**
   * If the message string matches, then parse it into a PageError
   *
   * @param message The Galen error message
   * @return A PageError object.
   * @throws ParseException Thrown if the message was not parsable.
   */
  PageError parseMessage(String message) throws ParseException;
}
