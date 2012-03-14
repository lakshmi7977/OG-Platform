/**
 * Copyright (C) 2009 - present by OpenGamma Inc. and the OpenGamma group of companies
 *
 * Please see distribution for license.
 */
package com.opengamma.master;

import java.util.Map;

import org.joda.beans.BeanBuilder;
import org.joda.beans.BeanDefinition;
import org.joda.beans.JodaBeanUtils;
import org.joda.beans.MetaProperty;
import org.joda.beans.impl.direct.DirectBean;
import org.joda.beans.impl.direct.DirectMetaBean;
import org.joda.beans.impl.direct.DirectMetaPropertyMap;

import com.opengamma.util.PublicSPI;

/**
 * Request for meta-data about a single master.
 * <p>
 * Some user interfaces require meta-data in order to operate, such as
 * a drop-down list of valid entries to select from. This abstract class
 * provides the basic ability to request such meta-data.
 */
@PublicSPI
@BeanDefinition
public abstract class AbstractMetaDataRequest extends DirectBean {

  /**
   * Creates an instance.
   */
  public AbstractMetaDataRequest() {
  }

  //------------------------- AUTOGENERATED START -------------------------
  ///CLOVER:OFF
  /**
   * The meta-bean for {@code AbstractMetaDataRequest}.
   * @return the meta-bean, not null
   */
  public static AbstractMetaDataRequest.Meta meta() {
    return AbstractMetaDataRequest.Meta.INSTANCE;
  }
  static {
    JodaBeanUtils.registerMetaBean(AbstractMetaDataRequest.Meta.INSTANCE);
  }

  @Override
  public AbstractMetaDataRequest.Meta metaBean() {
    return AbstractMetaDataRequest.Meta.INSTANCE;
  }

  @Override
  protected Object propertyGet(String propertyName, boolean quiet) {
    return super.propertyGet(propertyName, quiet);
  }

  @Override
  protected void propertySet(String propertyName, Object newValue, boolean quiet) {
    super.propertySet(propertyName, newValue, quiet);
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == this) {
      return true;
    }
    if (obj != null && obj.getClass() == this.getClass()) {
      return true;
    }
    return false;
  }

  @Override
  public int hashCode() {
    int hash = getClass().hashCode();
    return hash;
  }

  //-----------------------------------------------------------------------
  /**
   * The meta-bean for {@code AbstractMetaDataRequest}.
   */
  public static class Meta extends DirectMetaBean {
    /**
     * The singleton instance of the meta-bean.
     */
    static final Meta INSTANCE = new Meta();

    /**
     * The meta-properties.
     */
    private final Map<String, MetaProperty<?>> _metaPropertyMap$ = new DirectMetaPropertyMap(
        this, null);

    /**
     * Restricted constructor.
     */
    protected Meta() {
    }

    @Override
    public BeanBuilder<? extends AbstractMetaDataRequest> builder() {
      throw new UnsupportedOperationException("AbstractMetaDataRequest is an abstract class");
    }

    @Override
    public Class<? extends AbstractMetaDataRequest> beanType() {
      return AbstractMetaDataRequest.class;
    }

    @Override
    public Map<String, MetaProperty<?>> metaPropertyMap() {
      return _metaPropertyMap$;
    }

    //-----------------------------------------------------------------------
  }

  ///CLOVER:ON
  //-------------------------- AUTOGENERATED END --------------------------
}
