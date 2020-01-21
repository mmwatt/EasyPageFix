/*
 * Copyright (c) 2019 Matthew Watt & University of Sheffield. This project is based on ideas by Phil McMinn.
 */

package EasyPageFix.Search.Searchers.CSSPropertyGenerators.NumberGenerators;

import static org.w3c.dom.css.CSSPrimitiveValue.CSS_NUMBER;
import static org.w3c.dom.css.CSSValue.CSS_PRIMITIVE_VALUE;

import EasyPageFix.Search.Searchers.CSSPropertyGenerators.CSSPropertyGeneratorBase;
import EasyPageFix.Search.Searchers.CSSPropertyGenerators.PropertyGeneratorHelpers;
import com.steadystate.css.dom.CSSValueImpl;
import com.steadystate.css.dom.Property;
import java.util.Random;
import org.apache.commons.lang3.ArrayUtils;
import org.w3c.dom.css.CSSValue;

public class ZIndexGenerator extends CSSPropertyGeneratorBase {

  private int maxValue = 10;
  private int modifier = -2;
  private int genNo = 4;

  public ZIndexGenerator() {
    super(CSS_PRIMITIVE_VALUE, CSS_NUMBER, "z-index");
  }

  @Override
  public Property randomValue() {
    CSSValueImpl value = new CSSValueImpl();
    int rnd = new Random().nextInt(maxValue) - modifier;
    value.setCssText(Integer.toString(rnd));
    return new Property(getName(), value, false);
  }

  @Override
  public Property[] possibleValues(CSSValue cssValue) {
    int[] values = new int[genNo];

    for (int i = 0; i < values.length; i++) {
      values[i] = new Random().nextInt(maxValue) - modifier;
    }

    return PropertyGeneratorHelpers
        .constructPropertyArray(ArrayUtils.toObject(values), cssValue, getValueType(), getName(),
            "");
  }
}
