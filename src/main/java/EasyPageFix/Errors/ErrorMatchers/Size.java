/*
 * Copyright (c) 2019 Matthew Watt & University of Sheffield. This project is based on ideas by Phil McMinn.
 */

package EasyPageFix.Errors.ErrorMatchers;

import EasyPageFix.Errors.ErrorType;
import EasyPageFix.Errors.PageError;
import java.text.ParseException;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Size extends ErrorMatcherBase {

  private final String[] amountMatchStrings = {
      "\\d+% [-?\\d+px]",
      "\\d+%",
      "-?\\d+px"
  };

  private Pattern[] amountMatchers;


  public Size() {
    fullMatchStr = "\"\\S+\" \\S+ is (\\d+% \\[-?\\d+px\\]|\\d+%|-?\\d+px) (instead of|but it should be) ((less than )?|(greater than )?)(\\d+% \\[-?\\d+px\\]|\\d+%|-?\\d+px)";
    compile();
  }

  @Override
  public PageError parseMessage(String message) throws ParseException {
    ErrorType type = getType(message);

    String[] elems = ErrorMatchHelpers.basicElemMatch(message, ErrorType.SIZE);

    int amount = calcAmount(message);

    String relation = getRelation(message);

    if (relation == null) {
      return new PageError(elems, type, amount, message);
    } else {
      return new PageError(elems, relation, type, amount, message);
    }

  }

  private String getRelation(String message) {
    if (message.contains("less than")) {
      return "less than";
    }
    if (message.contains("greater than")) {
      return "greater than";
    }
    return null;
  }

  @Override
  protected void compile() {
    super.compile();
    amountMatchers = new Pattern[amountMatchStrings.length];
    for (int i = 0; i < amountMatchStrings.length; i++) {
      amountMatchers[i] = Pattern.compile(amountMatchStrings[i]);
    }
  }

  private ErrorType getType(String message) throws ParseException {
    Matcher m = Pattern.compile("\\S+").matcher(message);
    m.find();
    m.find();

    if (m.group().equals("height")) {
      return ErrorType.HEIGHT;
    } else if (m.group().equals("width")) {
      return ErrorType.WIDTH;
    } else {
      throw new ParseException("Could not parse type of size error.", 1);
    }
  }


  private int calcAmount(String message) throws ParseException {

    final int type = getAmountType(message);

    List<Integer> intList;

    switch (type) {
      case 0:
      case 1:
        intList = ErrorMatchHelpers.multiPercentMatch(message);
        break;
      case 2:
        intList = ErrorMatchHelpers.multiPixelMatch(message);
        break;
      default:
        throw new ParseException("Invalid amount type in Size error", 3);
    }

    int num1 = 0, num2 = 0;
    for (int i = 0; i < intList.size(); i++) {
      if (i == 0) {
        num1 = intList.get(i);
      }

      if (i == intList.size() - 1) {
        num2 = intList.get(i);
      }
    }

    return num1 - num2;
  }

  /**
   * Gets the amount type based on the strings list
   *
   * @return The index of the matching string.
   */
  private int getAmountType(String message) throws ParseException {
    for (int i = 0; i < amountMatchers.length; i++) {
      Matcher m = amountMatchers[i].matcher(message);
      if (m.find()) {
        return i;
      }
    }
    throw new ParseException("Could not extract amount type of the Size error string", 3);
  }


}
