/*
 * Copyright (c) 2019 Matthew Watt & University of Sheffield. This project is based on ideas by Phil McMinn.
 */

package EasyPageFix.Search.Searchers.CSSPropertyGenerators;

import com.steadystate.css.dom.Property;
import org.w3c.dom.css.CSSValue;

public interface CSSPropertyGenerator {

  /**
   * Gives the Type of this css value, same as the types in org.w3c.dom.css.CSSValue. This can be a
   * number from 0 to 3: 0: 'Inherit' - The value is inherited and the cssText contains 'inherit' 1:
   * 'Primitive' - Uses values defined in org.w3c.dom.css.CSSPrimitiveValue 2: 'Value List' - Uses a
   * list of values, e.g. for "border: 10px 12px 22px 2px" 3: 'Custom' - Uses a custom value (don't
   * touch)
   *
   * @return A code defining the type of the value. Defined in org.w3c.dom.css.CSSValue
   */
  short getPropertyType();

  /**
   * Gives the type of the primitive value returned by this Generator. Some useful values: 0:
   * unknown, returned if propertyType != 1 1: number 2: percentage 5: px 6: pt 19: string 20: URI
   * 21: identifier 25: RGB colour
   *
   * @return A code defining the type of the value. Defined in org.w3c.dom.css.CSSPrimitiveValue
   */
  short getValueType();

  /**
   * Gives the name of the property
   *
   * @return The property's name, as a String.
   */
  String getName();

  /**
   * Randomly generates a possible value for this property.
   *
   * @return A Property object
   */
  Property randomValue();

  /**
   * Randomly generates a number of possible values for this property, given the current value
   *
   * @return list of strings, in format "property: value"
   */
  Property[] possibleValues(CSSValue cssValue);
}
