/*
 * Copyright (c) 2019 Matthew Watt & University of Sheffield. This project is based on ideas by Phil McMinn.
 */

package EasyPageFix.Errors;

import java.awt.Color;
import java.util.Arrays;

/**
 * @author Matthew Watt
 *     This is unused but left for potential future extension.
 */
public class ColorError extends PageError {

  Color desiredColor;
  Color actualColor;

  public ColorError(String[] elems, ErrorType type, int amount, String message, Color desiredColor,
      Color actualColor) {
    super(elems, type, amount, message);
    this.desiredColor = desiredColor;
    this.actualColor = actualColor;
  }

  @Override
  public String toString() {
    return "ColorError{" +
        "desiredColor=" + desiredColor +
        ", actualColor=" + actualColor +
        ", elements=" + Arrays.toString(elements) +
        ", relations=" + Arrays.toString(relations) +
        ", type=" + type +
        ", amount=" + amount +
        ", message='" + message + '\'' +
        '}';
  }
}
