/*
 * Copyright (c) 2019 Matthew Watt & University of Sheffield. This project is based on ideas by Phil McMinn.
 */

package EasyPageFix.Errors;

import com.galenframework.reports.model.LayoutReport;
import com.galenframework.validation.ValidationResult;
import java.text.ParseException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * @author Matthew Watt
 *     A Java representation of parsed error messages from Galen.
 */
public class PageError {

  //TODO do these need to be public? Setters/Getters?
  public String[] elements;
  public String[] relations;

  public ErrorType type;
  public int amount;

  public String message;

  public boolean negative;

  public PageError() {
    elements = null;
    type = null;
    amount = 0;
  }

  /**
   * Constructor for a single element and error amount.
   */
  public PageError(String element, ErrorType type, int amount, String message) {
    this(new String[]{element}, new String[]{}, type, amount, message, false);
  }

  /**
   * Constructor for multiple elements
   */
  public PageError(String[] elements, ErrorType type, int amount, String message) {
    this(elements, new String[]{}, type, amount, message, false);
  }


  /**
   * Constructor for multiple elements and a single relation word.
   */
  public PageError(String[] elements, String relation, ErrorType type, int amount, String message) {
    this(elements, new String[]{relation}, type, amount, message, false);
  }

  /**
   * Special case for full constructor w/o boolean for negative
   */
  public PageError(String[] elements, String[] relations, ErrorType type, int amount,
      String message) {
    this(elements, relations, type, amount, message, false);
  }

  /**
   * Special case with boolean for negative
   */
  public PageError(String[] elements, String relation, ErrorType type, int amount, String message,
      boolean negative) {
    this(elements, new String[]{relation}, type, amount, message, negative);
  }

  /**
   * Constructor for multiple elements, and a definition of a relation between them.
   */
  public PageError(String[] elements, String[] relations, ErrorType type, int amount,
      String message, boolean negative) {
    this.elements = elements;
    this.relations = relations;
    this.type = type;
    this.amount = amount;
    this.message = message;
    this.negative = negative;
  }

  /**
   * Checks if equal to another PageError, all parameters
   */
  public boolean equals(PageError otherError) {
    boolean elemEq = Arrays.deepEquals(elements, otherError.elements);
    boolean relEq = Arrays.deepEquals(elements, otherError.elements);
    boolean errEq = type == otherError.type;
    boolean amtEq = amount == otherError.amount;
    boolean msgEq = message.equals(otherError.message);

    return elemEq && relEq && errEq && amtEq && msgEq;
  }

  /**
   * Checks only the type, relations and elements involved in an error
   */
  public boolean shallowEquals(PageError otherError) {
    boolean elemEq = Arrays.deepEquals(elements, otherError.elements);
    boolean relEq = Arrays.deepEquals(elements, otherError.elements);
    boolean errEq = type == otherError.type;

    return elemEq && relEq && errEq;
  }

  /**
   * Generates a list of parsed errors for a given LayoutReport.
   *
   * @param report, obtained from Galen.
   * @return List of PageErrors
   */
  public static List<PageError> parseReport(LayoutReport report)
      throws IllegalStateException, ParseException {
    LinkedList<PageError> errors = new LinkedList<>();

    for (ValidationResult result : report.getValidationErrorResults()) {
      errors.addAll(parseResult(result));
    }

    return errors;
  }

  /**
   * Converts a ValidationResult into a list (normally 1 item, can be more) of PageErrors
   *
   * @param result From a Galen LayoutReport
   * @return A list of PageErrors
   */
  private static List<PageError> parseResult(ValidationResult result)
      throws IllegalStateException, ParseException {
    LinkedList<PageError> errors = new LinkedList<PageError>();
    for (String message : result.getError().getMessages()) {
      PageError error = ErrorParser.parse(message);
      if (error != null) {
        errors.add(error);
      }
    }
    return errors;
  }

  @Override
  public String toString() {
    return "PageError{" +
        "elements=" + Arrays.toString(elements) +
        ", relations=" + Arrays.toString(relations) +
        ", type=" + type +
        ", amount=" + amount +
        ", message='" + message + '\'' +
        '}';
  }
}
