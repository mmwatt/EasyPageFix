/*
 * Copyright (c) 2019 Matthew Watt & University of Sheffield. This project is based on ideas by Phil McMinn.
 */

package EasyPageFix.Search.Searchers.CSSPropertyGenerators;

import static org.w3c.dom.css.CSSPrimitiveValue.CSS_CM;
import static org.w3c.dom.css.CSSPrimitiveValue.CSS_EMS;
import static org.w3c.dom.css.CSSPrimitiveValue.CSS_EXS;
import static org.w3c.dom.css.CSSPrimitiveValue.CSS_IN;
import static org.w3c.dom.css.CSSPrimitiveValue.CSS_MM;
import static org.w3c.dom.css.CSSPrimitiveValue.CSS_NUMBER;
import static org.w3c.dom.css.CSSPrimitiveValue.CSS_PC;
import static org.w3c.dom.css.CSSPrimitiveValue.CSS_PERCENTAGE;
import static org.w3c.dom.css.CSSPrimitiveValue.CSS_PT;
import static org.w3c.dom.css.CSSPrimitiveValue.CSS_PX;

import com.steadystate.css.dom.CSSValueImpl;
import com.steadystate.css.dom.Property;
import java.util.Random;
import org.apache.commons.lang3.ArrayUtils;
import org.w3c.dom.css.CSSValue;

/**
 * Class for functions which reduce duplicated code in the property generators
 */
public class PropertyGeneratorHelpers {

  final static int[] NUMERIC_TYPES = {
      CSS_NUMBER, CSS_PERCENTAGE, CSS_EMS, CSS_EXS, CSS_PX, CSS_CM, CSS_MM, CSS_IN, CSS_PT, CSS_PC
  };

  /**
   * Simply returns a random element from the list
   *
   * @param array Array of any type
   * @return A single item of the type of the array.
   */
  public static <T> T getRandomElement(T[] array) {
    int rnd = new Random().nextInt(array.length);
    return array[rnd];
  }

  /**
   * Gets a random element that is not the same as a given example.
   */
  public static String getRandomElement(String[] array, String string) {
    String newString;
    do {
      newString = getRandomElement(array);
    } while (string.contains(newString));
    return newString;
  }

  /**
   * Function that returns a copy of the array, excluding the specified element (if present)
   */
  public static String[] getAllStringsExcluding(String[] array, String string) {
    //Find whether the element is in the array
    boolean contains = false;
    for (String s : array) {
      if (s.equals(string)) {
        contains = true;
      }
    }

    //If the element is in the array, return a copy without it. If not, then return the array as-is
    if (contains) {
      String[] arr2 = ArrayUtils.clone(array);
      for (int i = 0; i < array.length; i++) {
        if (array[i].equals(string)) {
          return ArrayUtils.remove(arr2, i);
        }
      }
    }
    return array;
  }

  /**
   * Function returning a copy of the array, excluding the specified element (if present). Specific
   * to integers
   */
  public static <T> T[] getAllExcluding(T[] array, T elem) {
    boolean contains = ArrayUtils.contains(array, elem);
    if (contains) {
      T[] arr2 = ArrayUtils.clone(array);
      return ArrayUtils.remove(arr2, ArrayUtils.indexOf(arr2, elem));
    } else {
      return array;
    }
  }

  /**
   * Function for generating an array of properties based on parameters
   *
   * @param values The values to be used in each of the properties
   * @param cssValue A value to exclude
   * @param valueType The type from org.w3c.dom.css.CSSPrimitiveValue to be used
   * @param name The property's name, as a string.
   * @return A list of property objects with the specified values.
   */
  public static Property[] constructPropertyArray(String[] values, CSSValue cssValue,
      short valueType,
      String name) {
    String[] newValues = getAllStringsExcluding(values, cssValue.getCssText());

    Property[] properties = new Property[newValues.length];
    for (int i = 0; i < properties.length; i++) {
      CSSValueImpl value = new CSSValueImpl();
      value.setStringValue(valueType, newValues[i]);
      properties[i] = new Property(name, value, false);
    }
    return properties;
  }

  /**
   * Function for generating an array of properties based on parameters. Generates only a certain
   * number of values.
   *
   * @param values The values to be used Integer
   * @param cssValue A value to exclude
   * @param valueType The type from org.w3c.dom.css.CSSPrimitiveValue to be used
   * @param name The property's name, as a string.
   * @param ending An optional ending for the value;
   * @return A list of properties
   */
  public static Property[] constructPropertyArray(Integer[] values, CSSValue cssValue,
      short valueType,
      String name, String ending) {

    Integer oldValue;
    Integer[] newValues;
    //Extract float value
    if (cssValue.getCssValueType() == CSSValue.CSS_PRIMITIVE_VALUE) {
      //if the cssValue's type is numeric
      if (ArrayUtils.contains(NUMERIC_TYPES, valueType)) {
        oldValue = Integer.parseInt(cssValue.getCssText().replaceAll("[a-zA-Z]|%", ""));
        newValues = getAllExcluding(values, oldValue);
        return constructPropertyArray(newValues, valueType, name, ending);
      }
    }
    //TODO make this an actual error
    return null;
  }

  /**
   * Function for generating an array of properties based on parameters. Generates only a certain
   * number of values.
   *
   * @param values The values to be used (Float)
   * @param cssValue A value to exclude
   * @param valueType The type from org.w3c.dom.css.CSSPrimitiveValue to be used
   * @param name The property's name, as a string.
   * @param ending An optional ending for the value;
   * @return A list of properties
   */
  public static Property[] constructPropertyArray(Float[] values, CSSValue cssValue,
      short valueType,
      String name, String ending) {

    //Extract float value
    if (cssValue.getCssValueType() == CSSValue.CSS_PRIMITIVE_VALUE) {
      //if the cssValue's type is numeric
      if (ArrayUtils.contains(NUMERIC_TYPES, valueType)) {
        Float oldValue = Float.parseFloat(cssValue.getCssText().replaceAll("[a-zA-Z[%]]", ""));
        Float[] newValues = getAllExcluding(values, oldValue);
        return constructPropertyArray(newValues, valueType, name, ending);
      }
    }
    //TODO make this an actual error
    return null;
  }


  /**
   * More straightforward version of above functions
   *
   * @param values Values to be used (pref. numbers)
   * @param valueType The type from org.w3c.dom.css.CSSPrimitiveValue to be used
   * @param name The property's name, as a string.
   * @param ending An optional ending for the value;
   * @return Array of Properties
   */
  public static <T> Property[] constructPropertyArray(T[] values, short valueType, String name,
      String ending) {
    Property[] properties = new Property[values.length];
    for (int i = 0; i < properties.length; i++) {
      CSSValueImpl value = new CSSValueImpl();
      value.setCssText(values[i].toString() + ending);
      properties[i] = new Property(name, value, false);
    }
    return properties;
  }
}
