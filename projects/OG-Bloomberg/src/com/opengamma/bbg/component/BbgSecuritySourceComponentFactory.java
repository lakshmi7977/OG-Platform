/**
 * Copyright (C) 2012 - present by OpenGamma Inc. and the OpenGamma group of companies
 * 
 * Please see distribution for license.
 */
package com.opengamma.bbg.component;

import java.util.LinkedHashMap;
import java.util.Map;

import org.joda.beans.BeanBuilder;
import org.joda.beans.BeanDefinition;
import org.joda.beans.JodaBeanUtils;
import org.joda.beans.MetaProperty;
import org.joda.beans.Property;
import org.joda.beans.PropertyDefinition;
import org.joda.beans.impl.direct.DirectBeanBuilder;
import org.joda.beans.impl.direct.DirectMetaProperty;
import org.joda.beans.impl.direct.DirectMetaPropertyMap;

import com.opengamma.bbg.BloombergSecuritySource;
import com.opengamma.bbg.ReferenceDataProvider;
import com.opengamma.component.ComponentInfo;
import com.opengamma.component.ComponentRepository;
import com.opengamma.component.factory.AbstractComponentFactory;
import com.opengamma.core.security.SecuritySource;
import com.opengamma.financial.timeseries.exchange.DefaultExchangeDataProvider;
import com.opengamma.financial.timeseries.exchange.ExchangeDataProvider;

/**
 * Component factory for the Bloomberg security source, a {@link SecuritySource} which backs directly on to Bloomberg. 
 */
@BeanDefinition
public class BbgSecuritySourceComponentFactory extends AbstractComponentFactory {

  /**
   * The classifier that the factory should publish under.
   */
  @PropertyDefinition(validate = "notNull")
  private String _classifier;
  /**
   * The reference data provider.
   */
  @PropertyDefinition(validate = "notNull")
  private ReferenceDataProvider _bbgReferenceData;

  //-------------------------------------------------------------------------
  @Override
  public void init(ComponentRepository repo, LinkedHashMap<String, String> configuration) throws Exception {
    ExchangeDataProvider exchangeDataProvider = initExchangeDataProvider();
    initBloombergSecuritySource(repo, exchangeDataProvider);
  }
  
  protected BloombergSecuritySource initBloombergSecuritySource(ComponentRepository repo, ExchangeDataProvider exchangeDataProvider) {
    BloombergSecuritySource bloombergSecuritySource = new BloombergSecuritySource(getBbgReferenceData(), exchangeDataProvider);
    
    ComponentInfo info = new ComponentInfo(BloombergSecuritySource.class, getClassifier());
    repo.registerComponent(info, bloombergSecuritySource);
    return bloombergSecuritySource;
  }
  
  protected ExchangeDataProvider initExchangeDataProvider() {
    return new DefaultExchangeDataProvider();
  }
  
  //------------------------- AUTOGENERATED START -------------------------
  ///CLOVER:OFF
  /**
   * The meta-bean for {@code BbgSecuritySourceComponentFactory}.
   * @return the meta-bean, not null
   */
  public static BbgSecuritySourceComponentFactory.Meta meta() {
    return BbgSecuritySourceComponentFactory.Meta.INSTANCE;
  }
  static {
    JodaBeanUtils.registerMetaBean(BbgSecuritySourceComponentFactory.Meta.INSTANCE);
  }

  @Override
  public BbgSecuritySourceComponentFactory.Meta metaBean() {
    return BbgSecuritySourceComponentFactory.Meta.INSTANCE;
  }

  @Override
  protected Object propertyGet(String propertyName, boolean quiet) {
    switch (propertyName.hashCode()) {
      case -281470431:  // classifier
        return getClassifier();
      case 789967022:  // bbgReferenceData
        return getBbgReferenceData();
    }
    return super.propertyGet(propertyName, quiet);
  }

  @Override
  protected void propertySet(String propertyName, Object newValue, boolean quiet) {
    switch (propertyName.hashCode()) {
      case -281470431:  // classifier
        setClassifier((String) newValue);
        return;
      case 789967022:  // bbgReferenceData
        setBbgReferenceData((ReferenceDataProvider) newValue);
        return;
    }
    super.propertySet(propertyName, newValue, quiet);
  }

  @Override
  protected void validate() {
    JodaBeanUtils.notNull(_classifier, "classifier");
    JodaBeanUtils.notNull(_bbgReferenceData, "bbgReferenceData");
    super.validate();
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == this) {
      return true;
    }
    if (obj != null && obj.getClass() == this.getClass()) {
      BbgSecuritySourceComponentFactory other = (BbgSecuritySourceComponentFactory) obj;
      return JodaBeanUtils.equal(getClassifier(), other.getClassifier()) &&
          JodaBeanUtils.equal(getBbgReferenceData(), other.getBbgReferenceData()) &&
          super.equals(obj);
    }
    return false;
  }

  @Override
  public int hashCode() {
    int hash = 7;
    hash += hash * 31 + JodaBeanUtils.hashCode(getClassifier());
    hash += hash * 31 + JodaBeanUtils.hashCode(getBbgReferenceData());
    return hash ^ super.hashCode();
  }

  //-----------------------------------------------------------------------
  /**
   * Gets the classifier that the factory should publish under.
   * @return the value of the property, not null
   */
  public String getClassifier() {
    return _classifier;
  }

  /**
   * Sets the classifier that the factory should publish under.
   * @param classifier  the new value of the property, not null
   */
  public void setClassifier(String classifier) {
    JodaBeanUtils.notNull(classifier, "classifier");
    this._classifier = classifier;
  }

  /**
   * Gets the the {@code classifier} property.
   * @return the property, not null
   */
  public final Property<String> classifier() {
    return metaBean().classifier().createProperty(this);
  }

  //-----------------------------------------------------------------------
  /**
   * Gets the reference data provider.
   * @return the value of the property, not null
   */
  public ReferenceDataProvider getBbgReferenceData() {
    return _bbgReferenceData;
  }

  /**
   * Sets the reference data provider.
   * @param bbgReferenceData  the new value of the property, not null
   */
  public void setBbgReferenceData(ReferenceDataProvider bbgReferenceData) {
    JodaBeanUtils.notNull(bbgReferenceData, "bbgReferenceData");
    this._bbgReferenceData = bbgReferenceData;
  }

  /**
   * Gets the the {@code bbgReferenceData} property.
   * @return the property, not null
   */
  public final Property<ReferenceDataProvider> bbgReferenceData() {
    return metaBean().bbgReferenceData().createProperty(this);
  }

  //-----------------------------------------------------------------------
  /**
   * The meta-bean for {@code BbgSecuritySourceComponentFactory}.
   */
  public static class Meta extends AbstractComponentFactory.Meta {
    /**
     * The singleton instance of the meta-bean.
     */
    static final Meta INSTANCE = new Meta();

    /**
     * The meta-property for the {@code classifier} property.
     */
    private final MetaProperty<String> _classifier = DirectMetaProperty.ofReadWrite(
        this, "classifier", BbgSecuritySourceComponentFactory.class, String.class);
    /**
     * The meta-property for the {@code bbgReferenceData} property.
     */
    private final MetaProperty<ReferenceDataProvider> _bbgReferenceData = DirectMetaProperty.ofReadWrite(
        this, "bbgReferenceData", BbgSecuritySourceComponentFactory.class, ReferenceDataProvider.class);
    /**
     * The meta-properties.
     */
    private final Map<String, MetaProperty<?>> _metaPropertyMap$ = new DirectMetaPropertyMap(
      this, (DirectMetaPropertyMap) super.metaPropertyMap(),
        "classifier",
        "bbgReferenceData");

    /**
     * Restricted constructor.
     */
    protected Meta() {
    }

    @Override
    protected MetaProperty<?> metaPropertyGet(String propertyName) {
      switch (propertyName.hashCode()) {
        case -281470431:  // classifier
          return _classifier;
        case 789967022:  // bbgReferenceData
          return _bbgReferenceData;
      }
      return super.metaPropertyGet(propertyName);
    }

    @Override
    public BeanBuilder<? extends BbgSecuritySourceComponentFactory> builder() {
      return new DirectBeanBuilder<BbgSecuritySourceComponentFactory>(new BbgSecuritySourceComponentFactory());
    }

    @Override
    public Class<? extends BbgSecuritySourceComponentFactory> beanType() {
      return BbgSecuritySourceComponentFactory.class;
    }

    @Override
    public Map<String, MetaProperty<?>> metaPropertyMap() {
      return _metaPropertyMap$;
    }

    //-----------------------------------------------------------------------
    /**
     * The meta-property for the {@code classifier} property.
     * @return the meta-property, not null
     */
    public final MetaProperty<String> classifier() {
      return _classifier;
    }

    /**
     * The meta-property for the {@code bbgReferenceData} property.
     * @return the meta-property, not null
     */
    public final MetaProperty<ReferenceDataProvider> bbgReferenceData() {
      return _bbgReferenceData;
    }

  }

  ///CLOVER:ON
  //-------------------------- AUTOGENERATED END --------------------------
}