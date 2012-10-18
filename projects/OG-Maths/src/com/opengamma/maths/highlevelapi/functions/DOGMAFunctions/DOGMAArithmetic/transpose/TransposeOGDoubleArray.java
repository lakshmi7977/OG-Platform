/**
 * Copyright (C) 2012 - present by OpenGamma Inc. and the OpenGamma group of companies
 * 
 * Please see distribution for license.
 */
package com.opengamma.maths.highlevelapi.functions.DOGMAFunctions.DOGMAArithmetic.transpose;

import com.opengamma.maths.highlevelapi.datatypes.primitive.OGArraySuper;
import com.opengamma.maths.highlevelapi.datatypes.primitive.OGDoubleArray;
import com.opengamma.maths.lowlevelapi.functions.checkers.Catchers;

/**
 * Transposes an OGDoubleArray
 * TODO: At some point consider COW or at least state propagated permutations for things like this?!
 */
public final class TransposeOGDoubleArray extends TransposeAbstract<OGDoubleArray> {
  private static TransposeOGDoubleArray s_instance = new TransposeOGDoubleArray();

  public static TransposeOGDoubleArray getInstance() {
    return s_instance;
  }

  private TransposeOGDoubleArray() {
  }

  @SuppressWarnings("unchecked")
  @Override
  public OGArraySuper<Number> transpose(OGDoubleArray array1) {
    Catchers.catchNullFromArgList(array1, 1);
    int rowsArray1 = array1.getNumberOfRows();
    int columnsArray1 = array1.getNumberOfColumns();
    int retRows = columnsArray1, retCols = rowsArray1;
    double[] data = array1.getData();
    double[] tmp = new double[rowsArray1 * columnsArray1];

    int ir;
    for (int i = 0; i < columnsArray1; i++) {
      ir = i * rowsArray1;
      for (int j = 0; j < rowsArray1; j++) {
        tmp[j * columnsArray1 + i] = data[ir + j];
      }
    }
    return new OGDoubleArray(tmp, retRows, retCols);
  }

}