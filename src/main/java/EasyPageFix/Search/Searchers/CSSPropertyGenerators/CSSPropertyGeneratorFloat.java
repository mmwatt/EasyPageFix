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

public abstract class CSSPropertyGeneratorFloat extends CSSPropertyGeneratorBase {

  private int genNo;
  private float multiplier;

  public CSSPropertyGeneratorFloat(String name, float multiplier, int genNo) {
    super(CSS_PRIMITIVE_VALUE, CSS_NUMBER, name);
    this.multiplier = multiplier;
    this.genNo = genNo;
  }

  @Override
  public Property randomValue() {
    CSSValueImpl value = new CSSValueImpl();
    float rnd = new Random().nextFloat() * multiplier;
    value.setCssText(Float.toString(rnd));
    return new Property(getName(), value, false);
  }

  @Override
  public Property[] possibleValues(CSSValue cssValue) {
    float[] values = new float[genNo];

    for (int i = 0; i < values.length; i++) {
      values[i] = new Random().nextFloat() * multiplier;
    }

    return PropertyGeneratorHelpers
        .constructPropertyArray(ArrayUtils.toObject(values), cssValue, getValueType(), getName(),
            "");
  }
}
