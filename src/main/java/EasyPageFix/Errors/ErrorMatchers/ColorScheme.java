/*
 * Copyright (c) 2019 Matthew Watt & University of Sheffield. This project is based on ideas by Phil McMinn.
 */

package EasyPageFix.Errors.ErrorMatchers;

import EasyPageFix.Errors.ColorError;
import EasyPageFix.Errors.ErrorType;
import EasyPageFix.Errors.PageError;
import java.awt.Color;
import java.lang.reflect.Field;
import java.text.ParseException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Matthew Watt This is unused but left for potential future extension.
 */
public class ColorScheme extends ErrorMatcherBase {

  public ColorScheme() {
    fullMatchStr = "color \\S+ on \"\\S+\" is \\d+% \\S+";
    compile();
  }

  @Override
  public PageError parseMessage(String message) throws ParseException {
    String[] elems = ErrorMatchHelpers.basicElemMatch(message, ErrorType.COLOR_SCHEME);

    int amount = ErrorMatchHelpers.percentAmountMatch(message);

    Matcher colorMatch = Pattern.compile("\\S+").matcher(message);
    colorMatch.find();
    colorMatch.find();

    Color c1, c2;
    try {
      Field f1 = Color.class.getField(colorMatch.group());
      c1 = (Color) f1.get(null);

      for (int i = 0; i < 5; i++) {
        colorMatch.find();
      }

      f1 = Color.class.getField(colorMatch.group());
      c2 = (Color) f1.get(null);
    } catch (Exception e) {
      throw new ParseException("Could not parse color: " + e.getMessage(), 1);
    }

    return new ColorError(elems, ErrorType.COLOR_SCHEME, amount, message, c1, c2);
  }

}
