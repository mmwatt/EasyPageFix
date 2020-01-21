/*
 * Copyright (c) 2019 Matthew Watt & University of Sheffield. This project is based on ideas by Phil McMinn.
 */

package EasyPageFix.Search.Searchers.CSSPropertyGenerators.IdentGenerators;

import static org.w3c.dom.css.CSSPrimitiveValue.CSS_IDENT;
import static org.w3c.dom.css.CSSValue.CSS_VALUE_LIST;

import EasyPageFix.Search.Searchers.CSSPropertyGenerators.CSSPropertyGeneratorBase;
import com.steadystate.css.dom.CSSValueImpl;
import com.steadystate.css.dom.Property;
import org.w3c.dom.css.CSSValue;

public class MarginCenterGenerator extends CSSPropertyGeneratorBase {

  private static String[] stringValues = {
      "0px auto"
  };

  public MarginCenterGenerator() {
    super(CSS_VALUE_LIST, CSS_IDENT, "margin", stringValues);
  }

  @Override
  public Property randomValue() {
    CSSValueImpl value = new CSSValueImpl();
    value.setStringValue(getValueType(), stringValues[0]);
    return new Property(getName(), value, false);
  }

  @Override
  public Property[] possibleValues(CSSValue cssValue) {
    CSSValueImpl value = new CSSValueImpl();
    value.setStringValue(getValueType(), stringValues[0]);
    return new Property[]{new Property(getName(), value, false)};
  }
}
