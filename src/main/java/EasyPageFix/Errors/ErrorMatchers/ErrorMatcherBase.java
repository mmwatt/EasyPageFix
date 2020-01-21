/*
 * Copyright (c) 2019 Matthew Watt & University of Sheffield. This project is based on ideas by Phil McMinn.
 */

package EasyPageFix.Errors.ErrorMatchers;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Simple abstract class for the Error Matchers. Subclasses expected to set their matching strings
 * and compile the patterns (through the provided method or their own implementation). Subclasses
 * should also implement parseMessage for their error type.
 */
public abstract class ErrorMatcherBase implements ErrorMatcher {

  String fullMatchStr = "";

  Pattern fullMatch;

  /**
   * Compiles the regex into the patterns
   */
  protected void compile() {
    fullMatch = Pattern.compile(fullMatchStr);
  }

  @Override
  public boolean matches(String message) {
    Matcher m = fullMatch.matcher(message);
    return m.matches();
  }

}
