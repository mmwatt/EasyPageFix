/*
 * Copyright (c) 2019 Matthew Watt & University of Sheffield. This project is based on ideas by Phil McMinn.
 */

package EasyPageFix.Errors.ErrorMatchers;

import EasyPageFix.Errors.ErrorType;
import EasyPageFix.Errors.PageError;
import java.text.ParseException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Centered extends ErrorMatcherBase {

  String relationExtractorStr;
  String relationElemExtractorStr;

  private Pattern relationExtractor;
  private Pattern relationElemExtractor;

  int relationWordSkip;

  public Centered() {
    fullMatchStr = "\"\\S+\" is centered but not \\S+ \\S+ \"\\S+\"";
    relationExtractorStr = "centered but not \\S+ \\S+";
    relationElemExtractorStr = "\\S+";
    relationWordSkip = 3;
    compile();
  }

  @Override
  protected void compile() {
    super.compile();
    relationExtractor = Pattern.compile(relationExtractorStr);
    relationElemExtractor = Pattern.compile(relationElemExtractorStr);
  }

  @Override
  public PageError parseMessage(String message) throws ParseException {
    Matcher relationMatch = relationExtractor.matcher(message);
    Matcher relationElemMatch;

    String relationErrorMessage = "Could not extract relation from error message";

    int amount = 100;

    String[] elems = ErrorMatchHelpers.basicElemMatch(message, ErrorType.CENTERING);

    String[] relations = new String[2];
    try {
      if (relationMatch.find()) {
        relationElemMatch = relationElemExtractor.matcher(relationMatch.group());
        for (int i = 0; i < relationWordSkip; i++) {
          relationElemMatch.find();
        }
        relationElemMatch.find();
        relations[0] = relationElemMatch.group();

        relationElemMatch.find();
        relations[1] = relationElemMatch.group();
      } else {
        throw new ParseException(relationErrorMessage, 0);
      }
    } catch (Exception e) {
      throw new ParseException(relationErrorMessage, 0);
    }

    return new PageError(elems, relations, ErrorType.CENTERING, amount, message);
  }
}
