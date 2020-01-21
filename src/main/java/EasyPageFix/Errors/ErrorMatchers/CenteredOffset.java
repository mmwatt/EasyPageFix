/*
 * Copyright (c) 2019 Matthew Watt & University of Sheffield. This project is based on ideas by Phil McMinn.
 */

package EasyPageFix.Errors.ErrorMatchers;

import EasyPageFix.Errors.ErrorType;
import EasyPageFix.Errors.PageError;
import java.text.ParseException;

public class CenteredOffset extends Centered {

  public CenteredOffset() {
    fullMatchStr = "\"\\S+\" is not centered \\S+ \\S+ \"\\S+\". Offset is (-)?\\d+px";
    relationExtractorStr = "centered \\S+ \\S+";
    relationElemExtractorStr = "\\S+";
    relationWordSkip = 1;
    compile();
  }

  @Override
  public PageError parseMessage(String message) throws ParseException {
    PageError p = super.parseMessage(message);

    int amount = ErrorMatchHelpers.pixelAmountMatch(message);

    return new PageError(p.elements, p.relations, ErrorType.CENTERING, amount, message);
  }
}
