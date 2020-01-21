/*
 * Copyright (c) 2019 Matthew Watt & University of Sheffield. This project is based on ideas by Phil McMinn.
 */

package EasyPageFix.Errors.ErrorMatchers;

import EasyPageFix.Errors.ErrorType;
import EasyPageFix.Errors.PageError;
import java.text.ParseException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DirectionPosition extends ErrorMatcherBase {

  public DirectionPosition() {
    fullMatchStr = "\"\\S+\" is (\\S+ of|\\S+) \"\\S+\" \\d+(%|px)";
    compile();
  }

  @Override
  public PageError parseMessage(String message) throws ParseException {
    String relation = getRelation(message);

    String[] elems = ErrorMatchHelpers.basicElemMatch(message, ErrorType.DIRECTION);

    int amount;
    try {
      amount = ErrorMatchHelpers.percentAmountMatch(message);
    } catch (ParseException e) {
      //If this throws an error too, the parse exception should be passed up.
      amount = ErrorMatchHelpers.pixelAmountMatch(message);
    }

    return new PageError(elems, relation, ErrorType.DIRECTION, amount, message, amount < 0);
  }

  private String getRelation(String message) throws ParseException {
    Matcher m = Pattern.compile("\\S+").matcher(message);
    //3rd word
    for (int i = 0; i < 3; i++) {
      m.find();
    }
    //TODO Questionable programming
    return m.group();
  }
}
