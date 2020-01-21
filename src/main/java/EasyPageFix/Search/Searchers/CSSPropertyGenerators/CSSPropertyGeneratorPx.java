/*
 * Copyright (c) 2019 Matthew Watt & University of Sheffield. This project is based on ideas by Phil McMinn.
 */

package EasyPageFix.Search.Searchers.CSSPropertyGenerators;

import static org.w3c.dom.css.CSSPrimitiveValue.CSS_PX;
import static org.w3c.dom.css.CSSValue.CSS_PRIMITIVE_VALUE;

import com.steadystate.css.dom.CSSValueImpl;
import com.steadystate.css.dom.Property;
import java.util.Random;
import org.apache.commons.lang3.ArrayUtils;
import org.w3c.dom.css.CSSValue;

public abstract class CSSPropertyGeneratorPx extends CSSPropertyGeneratorBase {

  private int maxValue, genNo;

  public CSSPropertyGeneratorPx(String name, int maxValue, int genNo) {
    super(CSS_PRIMITIVE_VALUE, CSS_PX, name);
    this.maxValue = maxValue;
    this.genNo = genNo;
  }

  @Override
  public Property randomValue() {
    CSSValueImpl value = new CSSValueImpl();
    int rnd = new Random().nextInt(maxValue);
    value.setCssText(rnd + "px");
    return new Property(getName(), value, false);
  }

  @Override
  public Property[] possibleValues(CSSValue cssValue) {
    int currentInt = Integer.parseInt(cssValue.getCssText().replaceAll("[a-zA-Z[%]]", ""));
    return possibleValues(currentInt);
  }

  public Property[] possibleValues(int currentInt) {
    int[] variations = variations(currentInt);
    CSSValue value = new CSSValueImpl();
    value.setCssText(Integer.toString(currentInt));
    return PropertyGeneratorHelpers
        .constructPropertyArray(ArrayUtils.toObject(variations), value, getValueType(),
            getName(),
            "px");
  }

  private int[] variations(int startingValue) {
    int tenth = startingValue / 10;
    int val1 = startingValue + tenth;
    int val2 = startingValue + 2 * tenth;
    int val3 = startingValue - tenth;
    int val4 = startingValue - 2 * tenth;

    return new int[]{val1, val2, val3, val4};
  }
}
