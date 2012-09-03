/**
 * Copyright (C) 2012 - present by OpenGamma Inc. and the OpenGamma group of companies
 *
 * Please see distribution for license.
 */
package com.opengamma.financial.convention;

import static org.testng.AssertJUnit.assertEquals;

import javax.time.calendar.LocalDate;

import org.testng.annotations.Test;

import com.opengamma.financial.convention.calendar.Calendar;
import com.opengamma.financial.convention.calendar.MondayToFridayCalendar;

/**
 *
 */
public class BondFutureOptionExpiryCalculatorTest {
  private static final BondFutureOptionExpiryCalculator CALCULATOR = BondFutureOptionExpiryCalculator.getInstance();
  static final Calendar WEEKEND_CALENDAR = new MondayToFridayCalendar("a");
  private static final Calendar CALENDAR = new MyCalendar();
  private static final LocalDate AUGUST = LocalDate.of(2012, 8, 1);
  private static final LocalDate SEPTEMBER_START = LocalDate.of(2012, 9, 1);
  private static final LocalDate SEPTEMBER_END = LocalDate.of(2012, 9, 29);

  @Test(expectedExceptions = IllegalArgumentException.class)
  public void testNegativeN() {
    CALCULATOR.getExpiry(-1, AUGUST, CALENDAR);
  }

  @Test(expectedExceptions = IllegalArgumentException.class)
  public void testZeroN() {
    CALCULATOR.getExpiry(0, AUGUST, CALENDAR);
  }

  @Test(expectedExceptions = IllegalArgumentException.class)
  public void testNullDate() {
    CALCULATOR.getExpiry(1, null, CALENDAR);
  }

  @Test(expectedExceptions = IllegalArgumentException.class)
  public void testNullCalendar() {
    CALCULATOR.getExpiry(2, AUGUST, null);
  }

  @Test
  public void testExpiryMonthBeforeExpiry() {
    assertEquals(LocalDate.of(2012, 9, 21), CALCULATOR.getExpiry(1, SEPTEMBER_START, WEEKEND_CALENDAR));
    assertEquals(LocalDate.of(2012, 9, 21), CALCULATOR.getExpiry(1, SEPTEMBER_START, CALENDAR));
    assertEquals(LocalDate.of(2012, 10, 26), CALCULATOR.getExpiry(2, SEPTEMBER_START, WEEKEND_CALENDAR));
    assertEquals(LocalDate.of(2012, 10, 26), CALCULATOR.getExpiry(2, SEPTEMBER_START, CALENDAR));
    assertEquals(LocalDate.of(2012, 11, 23), CALCULATOR.getExpiry(3, SEPTEMBER_START, WEEKEND_CALENDAR));
    assertEquals(LocalDate.of(2012, 11, 22), CALCULATOR.getExpiry(3, SEPTEMBER_START, CALENDAR));
    assertEquals(LocalDate.of(2012, 12, 28), CALCULATOR.getExpiry(4, SEPTEMBER_START, WEEKEND_CALENDAR));
    assertEquals(LocalDate.of(2012, 12, 21), CALCULATOR.getExpiry(4, SEPTEMBER_START, CALENDAR));
    assertEquals(LocalDate.of(2013, 1, 25), CALCULATOR.getExpiry(5, SEPTEMBER_START, WEEKEND_CALENDAR));
    assertEquals(LocalDate.of(2013, 1, 25), CALCULATOR.getExpiry(5, SEPTEMBER_START, CALENDAR));
    assertEquals(LocalDate.of(2013, 2, 22), CALCULATOR.getExpiry(6, SEPTEMBER_START, WEEKEND_CALENDAR));
    assertEquals(LocalDate.of(2013, 2, 22), CALCULATOR.getExpiry(6, SEPTEMBER_START, CALENDAR));
    assertEquals(LocalDate.of(2013, 4, 26), CALCULATOR.getExpiry(8, SEPTEMBER_START, WEEKEND_CALENDAR));
    assertEquals(LocalDate.of(2013, 4, 19), CALCULATOR.getExpiry(8, SEPTEMBER_START, CALENDAR));
  }

  @Test
  public void testExpiryMonthAfterExpiry() {
    assertEquals(LocalDate.of(2012, 10, 26), CALCULATOR.getExpiry(1, SEPTEMBER_END, WEEKEND_CALENDAR));
    assertEquals(LocalDate.of(2012, 10, 26), CALCULATOR.getExpiry(1, SEPTEMBER_END, CALENDAR));
    assertEquals(LocalDate.of(2012, 11, 23), CALCULATOR.getExpiry(2, SEPTEMBER_END, WEEKEND_CALENDAR));
    assertEquals(LocalDate.of(2012, 11, 22), CALCULATOR.getExpiry(2, SEPTEMBER_END, CALENDAR));
    assertEquals(LocalDate.of(2012, 12, 28), CALCULATOR.getExpiry(3, SEPTEMBER_END, WEEKEND_CALENDAR));
    assertEquals(LocalDate.of(2012, 12, 21), CALCULATOR.getExpiry(3, SEPTEMBER_END, CALENDAR));
    assertEquals(LocalDate.of(2013, 1, 25), CALCULATOR.getExpiry(4, SEPTEMBER_END, WEEKEND_CALENDAR));
    assertEquals(LocalDate.of(2013, 1, 25), CALCULATOR.getExpiry(4, SEPTEMBER_END, CALENDAR));
    assertEquals(LocalDate.of(2013, 2, 22), CALCULATOR.getExpiry(5, SEPTEMBER_END, WEEKEND_CALENDAR));
    assertEquals(LocalDate.of(2013, 2, 22), CALCULATOR.getExpiry(5, SEPTEMBER_END, CALENDAR));
    assertEquals(LocalDate.of(2013, 3, 22), CALCULATOR.getExpiry(6, SEPTEMBER_END, WEEKEND_CALENDAR));
    assertEquals(LocalDate.of(2013, 3, 22), CALCULATOR.getExpiry(6, SEPTEMBER_END, CALENDAR));
    assertEquals(LocalDate.of(2013, 4, 26), CALCULATOR.getExpiry(7, SEPTEMBER_END, WEEKEND_CALENDAR));
    assertEquals(LocalDate.of(2013, 4, 19), CALCULATOR.getExpiry(7, SEPTEMBER_END, CALENDAR));

  }

  private static class MyCalendar implements Calendar {
    private static final LocalDate HOLIDAY1 = LocalDate.of(2012, 12, 28);
    private static final LocalDate HOLIDAY2 = LocalDate.of(2012, 11, 23);
    private static final LocalDate HOLIDAY3 = LocalDate.of(2013, 2, 2);
    private static final LocalDate HOLIDAY4 = LocalDate.of(2013, 4, 29);

    public MyCalendar() {
    }

    @Override
    public boolean isWorkingDay(final LocalDate date) {
      if (date.equals(HOLIDAY1) || date.equals(HOLIDAY2) || date.equals(HOLIDAY3) || date.equals(HOLIDAY4)) {
        return false;
      }
      return WEEKEND_CALENDAR.isWorkingDay(date);
    }

    @Override
    public String getConventionName() {
      return null;
    }

  }
}