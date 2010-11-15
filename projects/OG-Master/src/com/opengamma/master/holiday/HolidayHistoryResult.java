/**
 * Copyright (C) 2009 - 2010 by OpenGamma Inc.
 *
 * Please see distribution for license.
 */
package com.opengamma.master.holiday;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.joda.beans.BeanDefinition;
import org.joda.beans.MetaProperty;

import com.opengamma.OpenGammaRuntimeException;
import com.opengamma.master.AbstractHistoryResult;

/**
 * Result providing the history of a holiday.
 * <p>
 * The returned documents may be a mixture of versions and corrections.
 * The document instant fields are used to identify which are which.
 * See {@link HolidayHolidayRequest} for more details.
 */
@BeanDefinition
public class HolidayHistoryResult extends AbstractHistoryResult<HolidayDocument> {

  /**
   * Creates an instance.
   */
  public HolidayHistoryResult() {
  }

  /**
   * Creates an instance.
   * 
   * @param coll  the collection of documents to add, not null
   */
  public HolidayHistoryResult(Collection<HolidayDocument> coll) {
    super(coll);
  }

  //-------------------------------------------------------------------------
  /**
   * Gets the returned holidays from within the documents.
   * 
   * @return the holidays, not null
   */
  public List<ManageableHoliday> getHolidays() {
    List<ManageableHoliday> result = new ArrayList<ManageableHoliday>();
    if (getDocuments() != null) {
      for (HolidayDocument doc : getDocuments()) {
        result.add(doc.getHoliday());
      }
    }
    return result;
  }

  /**
   * Gets the first holiday, or null if no documents.
   * 
   * @return the first holiday, null if none
   */
  public ManageableHoliday getFirstHoliday() {
    return getDocuments().size() > 0 ? getDocuments().get(0).getHoliday() : null;
  }

  /**
   * Gets the single result expected from a query.
   * <p>
   * This throws an exception if more than 1 result is actually available.
   * Thus, this method implies an assumption about uniqueness of the queried holiday.
   * 
   * @return the matching holiday, not null
   * @throws IllegalStateException if no holiday was found
   */
  public ManageableHoliday getSingleHoliday() {
    if (getDocuments().size() != 1) {
      throw new OpenGammaRuntimeException("Expecting zero or single resulting match, and was " + getDocuments().size());
    } else {
      return getDocuments().get(0).getHoliday();
    }
  }

  //------------------------- AUTOGENERATED START -------------------------
  ///CLOVER:OFF
  /**
   * The meta-bean for {@code HolidayHistoryResult}.
   * @return the meta-bean, not null
   */
  @SuppressWarnings("unchecked")
  public static HolidayHistoryResult.Meta meta() {
    return HolidayHistoryResult.Meta.INSTANCE;
  }

  @Override
  public HolidayHistoryResult.Meta metaBean() {
    return HolidayHistoryResult.Meta.INSTANCE;
  }

  @Override
  protected Object propertyGet(String propertyName) {
    switch (propertyName.hashCode()) {
    }
    return super.propertyGet(propertyName);
  }

  @Override
  protected void propertySet(String propertyName, Object newValue) {
    switch (propertyName.hashCode()) {
    }
    super.propertySet(propertyName, newValue);
  }

  //-----------------------------------------------------------------------
  /**
   * The meta-bean for {@code HolidayHistoryResult}.
   */
  public static class Meta extends AbstractHistoryResult.Meta<HolidayDocument> {
    /**
     * The singleton instance of the meta-bean.
     */
    static final Meta INSTANCE = new Meta();

    /**
     * The meta-properties.
     */
    private final Map<String, MetaProperty<Object>> _map;

    @SuppressWarnings({"unchecked", "rawtypes" })
    protected Meta() {
      LinkedHashMap temp = new LinkedHashMap(super.metaPropertyMap());
      _map = Collections.unmodifiableMap(temp);
    }

    @Override
    public HolidayHistoryResult createBean() {
      return new HolidayHistoryResult();
    }

    @Override
    public Class<? extends HolidayHistoryResult> beanType() {
      return HolidayHistoryResult.class;
    }

    @Override
    public Map<String, MetaProperty<Object>> metaPropertyMap() {
      return _map;
    }

    //-----------------------------------------------------------------------
  }

  ///CLOVER:ON
  //-------------------------- AUTOGENERATED END --------------------------
}
