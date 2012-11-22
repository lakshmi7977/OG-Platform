/**
 * Copyright (C) 2012 - present by OpenGamma Inc. and the OpenGamma group of companies
 *
 * Please see distribution for license.
 */
package com.opengamma.web.analytics;

import com.opengamma.engine.value.ValueSpecification;

/**
 * The row and column structure of a grid that displays analytics data.
 */
public interface GridStructure {

  /**
   * @return The number of rows in the grid
   */
  int getRowCount();

  /**
   * @return The number of columns in the grid
   */
  int getColumnCount();

  /**
   * @return Meta data for the grid's columns
   */
  AnalyticsColumnGroups getColumnStructure();

  /**
   * @param row The row index
   * @param col The column index
   * @return The {@link ValueSpecification} for the cell or {@code null} if the cell's value isn't calculated
   * by the engine
   */
  ValueSpecification getValueSpecificationForCell(int row, int col);
}