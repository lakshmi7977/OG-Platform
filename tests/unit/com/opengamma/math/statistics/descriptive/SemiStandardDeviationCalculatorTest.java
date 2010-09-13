/**
 * Copyright (C) 2009 - 2010 by OpenGamma Inc.
 *
 * Please see distribution for license.
 */
package com.opengamma.math.statistics.descriptive;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.opengamma.math.function.Function1D;

/**
 * 
 */
public class SemiStandardDeviationCalculatorTest {
  private static final Function1D<double[], Double> UPSIDE1 = new SemiStandardDeviationCalculator(0, false);
  private static final Function1D<double[], Double> DOWNSIDE1 = new SemiStandardDeviationCalculator();
  private static final Function1D<double[], Double> UPSIDE2 = new SemiStandardDeviationCalculator(0.5, false);
  private static final Function1D<double[], Double> DOWNSIDE2 = new SemiStandardDeviationCalculator(-0.5, true);
  private static final int N = 100;
  private static final double[] POSITIVE_X = new double[N];
  private static final double[] NEGATIVE_X = new double[N];
  private static final double[] X1 = new double[N];
  private static final double[] X2 = new double[N];

  static {
    for (int i = 0; i < N; i++) {
      POSITIVE_X[i] = 0.5;
      NEGATIVE_X[i] = -0.5;
      if (i % 10 == 0) {
        X1[i] = 0.64;
        X2[i] = -0.6;
      }
    }
  }

  @Test(expected = IllegalArgumentException.class)
  public void testNullArray() {
    UPSIDE1.evaluate((double[]) null);
  }

  @Test
  public void test() {
    final double eps = 1e-15;
    assertEquals(UPSIDE1.evaluate(POSITIVE_X), 0.5, eps);
    assertEquals(DOWNSIDE1.evaluate(POSITIVE_X), 0, eps);
    assertEquals(UPSIDE1.evaluate(NEGATIVE_X), 0, eps);
    assertEquals(DOWNSIDE1.evaluate(NEGATIVE_X), 0.5, eps);
    assertEquals(UPSIDE2.evaluate(X1), 0.14, eps);
    assertEquals(DOWNSIDE2.evaluate(X2), 0.1, eps);
  }
}
