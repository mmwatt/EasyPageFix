/*
 * Copyright (c) 2019 Matthew Watt & University of Sheffield. This project is based on ideas by Phil McMinn.
 */

package EasyPageFix.Errors.ErrorMatchers;

import EasyPageFix.Errors.ErrorType;
import java.text.ParseException;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Matthew Watt Class of static methods to make the other error matchers more readable and
 *     future-proof.
 */
public final class ErrorMatchHelpers {

  private ErrorMatchHelpers() {
  }

  /**
   * Extracts the elements mentioned in an error string
   *
   * @param message The full error message String
   * @param type The type of error being parsed.
   * @return An array containing the names of the elements mentioned.
   * @throws ParseException Exception if there are no elements found.
   */
  public static String[] basicElemMatch(String message, ErrorType type) throws ParseException {
    Pattern elemExtractor = Pattern.compile("\"\\S+\"");
    Matcher elemMatch = elemExtractor.matcher(message);

    LinkedList<String> elements = new LinkedList<>();

    String matchError = "Could not find any matches for " + type + " elements extractor";

    try {
      while (elemMatch.find()) {
        String element = elemMatch.group().replace("\"", "");
        elements.add(element);
      }
    } catch (Exception e) {
      throw new ParseException(matchError, 0);
    }
    return elements.toArray(new String[0]);
  }

  /**
   * Matches a string of "... 15px" or similar, returning the value.
   *
   * @param message The full error message String
   * @return int, The pixel value extracted
   * @throws ParseException Exception if unable to find the pixel value.
   */
  public static int pixelAmountMatch(String message) throws ParseException {
    Matcher amountMatch = Pattern.compile("(-)?\\d+px").matcher(message);
    String amountErrorMessage = "Could not extract amount from the error message";
    try {
      if (amountMatch.find()) {
        String amountStr = amountMatch.group(0);
        amountStr = amountStr.replace("px", "");
        return Integer.parseInt(amountStr);
      } else {
        throw new ParseException(amountErrorMessage, 0);
      }
    } catch (Exception e) {
      throw new ParseException(amountErrorMessage, 0);
    }
  }

  /**
   * Matches a string of "...%" and returns the percentage as a Float
   *
   * @param message Full error message
   * @return percentage as a Float between 0 and 1.
   * @throws ParseException Exception if unable to find the percentage.
   */
  public static int percentAmountMatch(String message) throws ParseException {
    Matcher amountMatch = Pattern.compile("(\\d+|\\d+.\\d+)%").matcher(message);
    String amountErrorMessage = "Could not extract percentage from the error message";
    try {
      if (amountMatch.find()) {
        String amountStr = amountMatch.group(0);
        amountStr = amountStr.replace("%", "");
        //TODO Decide on low/high percentages
        float amountFloat = Float.parseFloat(amountStr);
        return Math.round(amountFloat);
      } else {
        throw new ParseException(amountErrorMessage, 0);
      }
    } catch (Exception e) {
      throw new ParseException(amountErrorMessage, 0);
    }
  }

  /**
   * Matches for multiple pixel values, returning them as a list.
   *
   * @return list of pixel values
   */
  public static List<Integer> multiPixelMatch(String message) throws ParseException {
    Matcher pixelMatcher = Pattern.compile("(-)?\\d+px").matcher(message);
    LinkedList<Integer> pixelValues = new LinkedList<>();

    while (pixelMatcher.find()) {
      String px = pixelMatcher.group().replace("px", "");
      pixelValues.add(Integer.parseInt(px));
    }

    if (pixelValues.isEmpty()) {
      throw new ParseException("Could not find any pixel values.", 0);
    }

    return pixelValues;
  }

  /**
   * Matches for multiple percent values, returning them as a list.
   *
   * @return list of percentages
   */
  public static List<Integer> multiPercentMatch(String message) throws ParseException {
    Matcher percentMatcher = Pattern.compile("\\d+%").matcher(message);
    LinkedList<Integer> percentValues = new LinkedList<>();

    while (percentMatcher.find()) {
      String px = percentMatcher.group().replace("%", "");
      percentValues.add(Integer.parseInt(px));
    }

    if (percentValues.isEmpty()) {
      throw new ParseException("Could not find any pixel values.", 0);
    }

    return percentValues;
  }

  /**
   * Matches integers with a space in front
   *
   * @return A list of integer values.
   */
  public static List<Integer> multiIntMatch(String message) throws ParseException {
    Matcher intMatcher = Pattern.compile(" (-)?\\d+").matcher(message);
    LinkedList<Integer> intValues = new LinkedList<>();

    while (intMatcher.find()) {
      String i = intMatcher.group();
      intValues.add(Integer.parseInt(i.replace(" ", "")));
    }

    if (intValues.isEmpty()) {
      throw new ParseException("Could not find any integer values.", 0);
    }

    return intValues;
  }

  /**
   * Simple method to return the nth word in a string.
   *
   * @return single word.
   */
  public static String getNthWord(int index, String message) throws IndexOutOfBoundsException {
    String[] words = message.split(" ");
    if (index >= words.length) {
      throw new IndexOutOfBoundsException("Index higher than number of words in the string.");
    }
    return words[index];
  }
}
