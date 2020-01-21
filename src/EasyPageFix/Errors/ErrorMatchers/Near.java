/*
 * Copyright (c) 2019 Matthew Watt & University of Sheffield. This project is based on ideas by Phil McMinn.
 */

package EasyPageFix.Errors.ErrorMatchers;

import EasyPageFix.Errors.ErrorType;
import EasyPageFix.Errors.PageError;
import java.text.ParseException;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Near implements ErrorMatcher {

  //Do not change order of these without modifying other methods.
  String[] fullMatchStrings = {
      "\"\\S+\" is -?\\d+px \\S+ instead of \\S+px",
      "\"\\S+\" is -?\\d+px \\S+ which is not in range of \\d+ to -?\\d+px",
      "\"\\S+\" is -?\\d+px \\S+ and -?\\d+px \\S+ instead of -?\\d+px",
      "\"\\S+\" is -?\\d+px \\S+ instead of -?\\d+px and -?\\d+px \\S+ which is not in range of \\d+ to -?\\d+px",
      "\"\\S+\" is \\d+% \\[-?\\d+px\\] \\S+ instead of \\d+% \\[-?\\d+px\\]",
      "\"\\S+\" is \\d+% \\[-?\\d+px\\] \\S+ which is not in range of \\d+ to \\d+% \\[\\d+ to -?\\d+px\\]",
      "\"\\S+\" is -?\\d+px \\S+ and -?\\d+px \\S+ which is not in range of \\d+ to \\d+px",
      "\"\\S+\" is -?\\d+px (above|below) \"\\S+\" instead of -?\\d+px",
      "\"\\S+\" is -?\\d+px (above|below) \"\\S+\" which is not in range of \\d+ to \\d+px"
  };
//  "icon-list" is 0px top and 0px left which is not in range of 10 to 50px

  Pattern[] fullMatchPatterns;

  Matcher[] fullMatchers;

  public Near() {
    compilePatterns();
  }

  @Override
  public boolean matches(String message) {
    compileMatchers(message);
    for (Matcher m : fullMatchers) {
      if (m.matches()) {
        return true;
      }
    }
    return false;
  }

  @Override
  public PageError parseMessage(String message) throws ParseException {
    for (int i = 0; i < fullMatchers.length; i++) {
      if (fullMatchers[i].matches()) {
        return callParser(i, message);
      }
    }
    throw new ParseException("Unable to match the message to any patterns (NEAR)", 0);
  }

  //'Switchboard' for the different parsing methods.
  private PageError callParser(int parseType, String message)
      throws ParseException {
    switch (parseType) {
      case 0:
        return parser0(message);
      case 1:
        return parser1(message);
      case 2:
        return parser2(message);
      case 3:
        return parser3(message);
      case 4:
        return parser4(message);
      case 5:
        return parser5(message);
      case 6:
        return parser6(message);
      case 7:
        return parser7(message);
      case 8:
        return parser8(message);
      default:
        throw new IndexOutOfBoundsException("Could not find parser with that index. (NEAR)");
    }
  }

  //"\"\\w+\" is (-)?\\d+px \\w+ instead of \\w+px"
  private PageError parser0(String message) throws ParseException {
    String[] elems = ErrorMatchHelpers.basicElemMatch(message, ErrorType.NEAR);

    List<Integer> pixelList = ErrorMatchHelpers.multiPixelMatch(message);
    int amount = pixelList.get(0) - pixelList.get(1);

    String direction = ErrorMatchHelpers.getNthWord(3, message);

    return new PageError(elems, direction, ErrorType.NEAR, Math.abs(amount), message, amount < 0);
  }

  //"\"\\w+\" is (-)?\\d+px \\w+ which is not in range of \\d+ to (-)?\\d+px"
  private PageError parser1(String message) throws ParseException {
    String[] elems = ErrorMatchHelpers.basicElemMatch(message, ErrorType.NEAR);

    List<Integer> intList = ErrorMatchHelpers.multiIntMatch(message);
    int amount = intList.get(0) - intList.get(1);

    String direction = ErrorMatchHelpers.getNthWord(3, message);

    return new PageError(elems, direction, ErrorType.NEAR, Math.abs(amount), message, amount < 0);
  }

  //"\"\\w+\" is (-)?\\d+px \\w+ and (-)?\\d+px \\w+ instead of (-)?\\d+px",
  private PageError parser2(String message) throws ParseException {
    String[] elems = ErrorMatchHelpers.basicElemMatch(message, ErrorType.NEAR);

    List<Integer> intList = ErrorMatchHelpers.multiPixelMatch(message);
    int amount1 = intList.get(0) - intList.get(2);
    int amount2 = intList.get(1) - intList.get(2);

    String direction1 = ErrorMatchHelpers.getNthWord(3, message);
    String direction2 = ErrorMatchHelpers.getNthWord(6, message);

    return new PageError(elems, new String[]{direction1, direction2}, ErrorType.NEAR,
        Math.abs(amount1 + amount2),
        message, amount1 < 0 || amount2 < 0);
  }

  //  "\"\\w+\" is (-)?\\d+px \\w+ instead of (-)?\\d+px and (-)?\\d+px \\w+ which is not in range of \\d+ to (-)?\\d+px",
  private PageError parser3(String message) throws ParseException {
    String[] elems = ErrorMatchHelpers.basicElemMatch(message, ErrorType.NEAR);

    List<Integer> intList = ErrorMatchHelpers.multiIntMatch(message);
    //TODO clean up this mess.
    int amount1 = intList.get(0) - intList.get(1);
    int amount2 = intList.get(2) - intList.get(3);

    String direction1 = ErrorMatchHelpers.getNthWord(3, message);
    String direction2 = ErrorMatchHelpers.getNthWord(9, message);

    return new PageError(elems, new String[]{direction1, direction2}, ErrorType.NEAR,
        Math.abs(amount1 + amount2),
        message, amount1 < 0 || amount2 < 0);
  }

  //"\"\\w+\" is \\d+% [(-)?\\d+px] \\w+ instead of \\d+% [(-)?\\d+px]"
  private PageError parser4(String message) throws ParseException {
    String[] elems = ErrorMatchHelpers.basicElemMatch(message, ErrorType.NEAR);

    List<Integer> percentList = ErrorMatchHelpers.multiPercentMatch(message);

    int amount = percentList.get(0) - percentList.get(1);

    String direction = ErrorMatchHelpers.getNthWord(4, message);

    return new PageError(elems, direction, ErrorType.NEAR, Math.abs(amount), message, amount < 0);
  }

  //"\"\\w+\" is \\d+% [(-)?\\d+px] \\w+ which is not in range of \\d+ to \\d+% [\\d+ to (-)?\\d+px]"
  private PageError parser5(String message) throws ParseException {
    String[] elems = ErrorMatchHelpers.basicElemMatch(message, ErrorType.NEAR);

    List<Integer> intList = ErrorMatchHelpers.multiIntMatch(message);

    int amount = intList.get(0) - Math.round((intList.get(3) + intList.get(2)) / 2.0f);

    String direction = ErrorMatchHelpers.getNthWord(4, message);

    return new PageError(elems, direction, ErrorType.NEAR, Math.abs(amount), message, amount < 0);
  }

  private void compilePatterns() {
    LinkedList<Pattern> pList = new LinkedList<>();
    for (String fullMatchString : fullMatchStrings) {
      pList.add(Pattern.compile(fullMatchString));
    }
    fullMatchPatterns = pList.toArray(new Pattern[0]);
  }

  //"\"\\S+\" is (-)?\\d+px \\S+ and (-)?\\d+px \\S+ which is not in range of \\d+ to \\d+px"
  private PageError parser6(String message) throws ParseException {
    String[] elems = ErrorMatchHelpers.basicElemMatch(message, ErrorType.NEAR);

    List<Integer> intList = ErrorMatchHelpers.multiIntMatch(message);
    int amount1 = intList.get(0) - intList.get(1);
    int amount2 = intList.get(2) - intList.get(3);

    String direction1 = ErrorMatchHelpers.getNthWord(3, message);
    String direction2 = ErrorMatchHelpers.getNthWord(5, message);

    return new PageError(elems, new String[]{direction1, direction2}, ErrorType.NEAR,
        Math.abs(amount1 + amount2), message, amount1 < 0 || amount2 < 0);
  }

  //"\"\\S+\" is -?\\d+px (above|below) \"\\S+\" instead of -?\\d+px",
  private PageError parser7(String message) throws ParseException {
    String[] elems = ErrorMatchHelpers.basicElemMatch(message, ErrorType.NEAR);

    List<Integer> intList = ErrorMatchHelpers.multiIntMatch(message);
    int amount = intList.get(0) - intList.get(1);

    String direction1 = ErrorMatchHelpers.getNthWord(3, message);

    if (direction1.equals("above")) {
      direction1 = "top";
    }
    if (direction1.equals("below")) {
      direction1 = "bottom";
    }

    return new PageError(elems, new String[]{direction1}, ErrorType.NEAR,
        Math.abs(amount), message, amount < 0);
  }

  //  "\"\\S+\" is -?\\d+px (above|below) \"\\S+\" which is not in range of \\d+ to \\d+px"
  private PageError parser8(String message) throws ParseException {
    String[] elems = ErrorMatchHelpers.basicElemMatch(message, ErrorType.NEAR);

    List<Integer> intList = ErrorMatchHelpers.multiIntMatch(message);
    int amount1 = intList.get(0) - intList.get(1);
    int amount2 = intList.get(0) - intList.get(2);

    int amount = amount1 > amount2 ? amount2 : amount1;

    String direction1 = ErrorMatchHelpers.getNthWord(3, message);

    if (direction1.equals("above")) {
      direction1 = "top";
    }
    if (direction1.equals("below")) {
      direction1 = "bottom";
    }

    return new PageError(elems, direction1, ErrorType.NEAR,
        Math.abs(amount), message, amount < 0);
  }

  private void compileMatchers(String message) {
    LinkedList<Matcher> mList = new LinkedList<>();
    for (Pattern p : fullMatchPatterns) {
      mList.add(p.matcher(message));
    }
    fullMatchers = mList.toArray(new Matcher[0]);
  }
}
