/*
 * Copyright (c) 2019 Matthew Watt & University of Sheffield. This project is based on ideas by Phil McMinn.
 */

package EasyPageFix.Search.Searchers.CSSPropertyGenerators;

import com.steadystate.css.dom.CSSValueImpl;
import com.steadystate.css.dom.Property;
import org.w3c.dom.css.CSSValue;

public abstract class CSSPropertyGeneratorBase implements CSSPropertyGenerator {

  private short propType;
  private short valType;
  private String name;

  private String[] stringValues;

  /**
   * Basic constructor for setting types
   *
   * @param propType types defined in interface and org.w3c.dom.css.CSSValue
   * @param valType types defined in interface and org.w3c.dom.css.CSSPrimitiveValue
   * @param name Name of the CSS Property
   */
  public CSSPropertyGeneratorBase(short propType, short valType, String name) {
    this.propType = propType;
    this.valType = valType;
    this.name = name;
  }


  /**
   * Similar to other constructor, also sets StringValues for use in basic method implementations
   *
   * @param propType types defined in interface and org.w3c.dom.css.CSSValue
   * @param valType types defined in interface and org.w3c.dom.css.CSSPrimitiveValue
   * @param name Name of the CSS Property
   * @param stringValues Array of permitted strings for the CSS Value
   */
  public CSSPropertyGeneratorBase(short propType, short valType, String name,
      String[] stringValues) {
    this(propType, valType, name);
    this.stringValues = stringValues;
  }

  @Override
  public short getPropertyType() {
    return propType;
  }

  @Override
  public short getValueType() {
    return valType;
  }

  @Override
  public String getName() {
    return name;
  }

  @Override
  public Property randomValue() {
    CSSValueImpl value = new CSSValueImpl();
    value.setStringValue(getValueType(), PropertyGeneratorHelpers.getRandomElement(stringValues));
    return new Property(getName(), value, false);
  }

  @Override
  public Property[] possibleValues(CSSValue cssValue) {
    return PropertyGeneratorHelpers
        .constructPropertyArray(stringValues, cssValue, getValueType(), getName());
  }
}
