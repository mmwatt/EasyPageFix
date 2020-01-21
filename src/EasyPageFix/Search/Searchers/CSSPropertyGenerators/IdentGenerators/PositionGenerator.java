/*
 * Copyright (c) 2019 Matthew Watt & University of Sheffield. This project is based on ideas by Phil McMinn.
 */

package EasyPageFix.Search.Searchers.CSSPropertyGenerators.IdentGenerators;

import static org.w3c.dom.css.CSSPrimitiveValue.CSS_IDENT;
import static org.w3c.dom.css.CSSValue.CSS_PRIMITIVE_VALUE;

import EasyPageFix.Search.Searchers.CSSPropertyGenerators.CSSPropertyGeneratorBase;
import com.steadystate.css.dom.CSSValueImpl;
import com.steadystate.css.dom.Property;

public class PositionGenerator extends CSSPropertyGeneratorBase {

  private static String[] stringValues = {
      "static",
      "absolute",
      "fixed",
      "relative",
      "sticky"
  };

  public PositionGenerator() {
    super(CSS_PRIMITIVE_VALUE, CSS_IDENT, "position", stringValues);
  }

  public Property relative() {
    CSSValueImpl value = new CSSValueImpl();
    value.setStringValue(getValueType(), "relative");
    return new Property(getName(), value, false);
  }

  public Property absolute() {
    CSSValueImpl value = new CSSValueImpl();
    value.setStringValue(getValueType(), "absolute");
    return new Property(getName(), value, false);
  }
}
