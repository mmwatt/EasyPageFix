/*
 * Copyright (c) 2019 Matthew Watt & University of Sheffield. This project is based on ideas by Phil McMinn.
 */

package EasyPageFix.Search.Searchers.CSSPropertyGenerators;

import static org.w3c.dom.css.CSSPrimitiveValue.CSS_NUMBER;
import static org.w3c.dom.css.CSSValue.CSS_PRIMITIVE_VALUE;

import com.steadystate.css.dom.CSSValueImpl;
import com.steadystate.css.dom.Property;
import java.util.Random;
import org.apache.commons.lang3.ArrayUtils;
import org.w3c.dom.css.CSSValue;

public abstract class CSSPropertyGeneratorNumber extends CSSPropertyGeneratorBase {

  private int maxValue, genNo;

  public CSSPropertyGeneratorNumber(String name, int maxValue, int genNo) {
    super(CSS_PRIMITIVE_VALUE, CSS_NUMBER, name);
    this.maxValue = maxValue;
    this.genNo = genNo;
  }

  @Override
  public Property randomValue() {
    CSSValueImpl value = new CSSValueImpl();
    int rnd = new Random().nextInt(maxValue);
    value.setCssText(Integer.toString(rnd));
    return new Property(getName(), value, false);
  }

  @Override
  public Property[] possibleValues(CSSValue cssValue) {
    int[] values = new int[genNo];

    for (int i = 0; i < values.length; i++) {
      values[i] = new Random().nextInt(maxValue);
    }

    return PropertyGeneratorHelpers
        .constructPropertyArray(ArrayUtils.toObject(values), cssValue, getValueType(), getName(),
            "");
  }

}
