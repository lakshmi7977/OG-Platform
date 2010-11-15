/**
 * Copyright (C) 2009 - 2010 by OpenGamma Inc.
 *
 * Please see distribution for license.
 */
package com.opengamma.web.exchange;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.ws.rs.core.UriInfo;

import org.joda.beans.BeanDefinition;
import org.joda.beans.MetaProperty;
import org.joda.beans.Property;
import org.joda.beans.PropertyDefinition;
import org.joda.beans.impl.BasicMetaBean;
import org.joda.beans.impl.direct.DirectBean;
import org.joda.beans.impl.direct.DirectMetaProperty;

import com.opengamma.id.UniqueIdentifier;
import com.opengamma.master.exchange.ExchangeDocument;
import com.opengamma.master.exchange.ExchangeMaster;

/**
 * Data class for web-based exchanges.
 */
@BeanDefinition
public class WebExchangeData extends DirectBean {

  /**
   * The exchange master.
   */
  @PropertyDefinition
  private ExchangeMaster _exchangeMaster;
  /**
   * The JSR-311 URI information.
   */
  @PropertyDefinition
  private UriInfo _uriInfo;
  /**
   * The exchange id from the input URI.
   */
  @PropertyDefinition
  private String _uriExchangeId;
  /**
   * The version id from the URI.
   */
  @PropertyDefinition
  private String _uriVersionId;
  /**
   * The exchange.
   */
  @PropertyDefinition
  private ExchangeDocument _exchange;
  /**
   * The versioned exchange.
   */
  @PropertyDefinition
  private ExchangeDocument _versioned;

  /**
   * Creates an instance.
   */
  public WebExchangeData() {
  }

  /**
   * Creates an instance.
   * @param uriInfo  the URI information
   */
  public WebExchangeData(final UriInfo uriInfo) {
    setUriInfo(uriInfo);
  }

  //-------------------------------------------------------------------------
  /**
   * Gets the best available exchange id.
   * @param overrideId  the override id, null derives the result from the data
   * @return the id, may be null
   */
  public String getBestExchangeUriId(final UniqueIdentifier overrideId) {
    if (overrideId != null) {
      return overrideId.toLatest().toString();
    }
    return getExchange() != null ? getExchange().getUniqueId().toLatest().toString() : getUriExchangeId();
  }

  //------------------------- AUTOGENERATED START -------------------------
  ///CLOVER:OFF
  /**
   * The meta-bean for {@code WebExchangeData}.
   * @return the meta-bean, not null
   */
  public static WebExchangeData.Meta meta() {
    return WebExchangeData.Meta.INSTANCE;
  }

  @Override
  public WebExchangeData.Meta metaBean() {
    return WebExchangeData.Meta.INSTANCE;
  }

  @Override
  protected Object propertyGet(String propertyName) {
    switch (propertyName.hashCode()) {
      case -652001691:  // exchangeMaster
        return getExchangeMaster();
      case -173275078:  // uriInfo
        return getUriInfo();
      case 772498730:  // uriExchangeId
        return getUriExchangeId();
      case 666567687:  // uriVersionId
        return getUriVersionId();
      case 1989774883:  // exchange
        return getExchange();
      case -1407102089:  // versioned
        return getVersioned();
    }
    return super.propertyGet(propertyName);
  }

  @Override
  protected void propertySet(String propertyName, Object newValue) {
    switch (propertyName.hashCode()) {
      case -652001691:  // exchangeMaster
        setExchangeMaster((ExchangeMaster) newValue);
        return;
      case -173275078:  // uriInfo
        setUriInfo((UriInfo) newValue);
        return;
      case 772498730:  // uriExchangeId
        setUriExchangeId((String) newValue);
        return;
      case 666567687:  // uriVersionId
        setUriVersionId((String) newValue);
        return;
      case 1989774883:  // exchange
        setExchange((ExchangeDocument) newValue);
        return;
      case -1407102089:  // versioned
        setVersioned((ExchangeDocument) newValue);
        return;
    }
    super.propertySet(propertyName, newValue);
  }

  //-----------------------------------------------------------------------
  /**
   * Gets the exchange master.
   * @return the value of the property
   */
  public ExchangeMaster getExchangeMaster() {
    return _exchangeMaster;
  }

  /**
   * Sets the exchange master.
   * @param exchangeMaster  the new value of the property
   */
  public void setExchangeMaster(ExchangeMaster exchangeMaster) {
    this._exchangeMaster = exchangeMaster;
  }

  /**
   * Gets the the {@code exchangeMaster} property.
   * @return the property, not null
   */
  public final Property<ExchangeMaster> exchangeMaster() {
    return metaBean().exchangeMaster().createProperty(this);
  }

  //-----------------------------------------------------------------------
  /**
   * Gets the JSR-311 URI information.
   * @return the value of the property
   */
  public UriInfo getUriInfo() {
    return _uriInfo;
  }

  /**
   * Sets the JSR-311 URI information.
   * @param uriInfo  the new value of the property
   */
  public void setUriInfo(UriInfo uriInfo) {
    this._uriInfo = uriInfo;
  }

  /**
   * Gets the the {@code uriInfo} property.
   * @return the property, not null
   */
  public final Property<UriInfo> uriInfo() {
    return metaBean().uriInfo().createProperty(this);
  }

  //-----------------------------------------------------------------------
  /**
   * Gets the exchange id from the input URI.
   * @return the value of the property
   */
  public String getUriExchangeId() {
    return _uriExchangeId;
  }

  /**
   * Sets the exchange id from the input URI.
   * @param uriExchangeId  the new value of the property
   */
  public void setUriExchangeId(String uriExchangeId) {
    this._uriExchangeId = uriExchangeId;
  }

  /**
   * Gets the the {@code uriExchangeId} property.
   * @return the property, not null
   */
  public final Property<String> uriExchangeId() {
    return metaBean().uriExchangeId().createProperty(this);
  }

  //-----------------------------------------------------------------------
  /**
   * Gets the version id from the URI.
   * @return the value of the property
   */
  public String getUriVersionId() {
    return _uriVersionId;
  }

  /**
   * Sets the version id from the URI.
   * @param uriVersionId  the new value of the property
   */
  public void setUriVersionId(String uriVersionId) {
    this._uriVersionId = uriVersionId;
  }

  /**
   * Gets the the {@code uriVersionId} property.
   * @return the property, not null
   */
  public final Property<String> uriVersionId() {
    return metaBean().uriVersionId().createProperty(this);
  }

  //-----------------------------------------------------------------------
  /**
   * Gets the exchange.
   * @return the value of the property
   */
  public ExchangeDocument getExchange() {
    return _exchange;
  }

  /**
   * Sets the exchange.
   * @param exchange  the new value of the property
   */
  public void setExchange(ExchangeDocument exchange) {
    this._exchange = exchange;
  }

  /**
   * Gets the the {@code exchange} property.
   * @return the property, not null
   */
  public final Property<ExchangeDocument> exchange() {
    return metaBean().exchange().createProperty(this);
  }

  //-----------------------------------------------------------------------
  /**
   * Gets the versioned exchange.
   * @return the value of the property
   */
  public ExchangeDocument getVersioned() {
    return _versioned;
  }

  /**
   * Sets the versioned exchange.
   * @param versioned  the new value of the property
   */
  public void setVersioned(ExchangeDocument versioned) {
    this._versioned = versioned;
  }

  /**
   * Gets the the {@code versioned} property.
   * @return the property, not null
   */
  public final Property<ExchangeDocument> versioned() {
    return metaBean().versioned().createProperty(this);
  }

  //-----------------------------------------------------------------------
  /**
   * The meta-bean for {@code WebExchangeData}.
   */
  public static class Meta extends BasicMetaBean {
    /**
     * The singleton instance of the meta-bean.
     */
    static final Meta INSTANCE = new Meta();

    /**
     * The meta-property for the {@code exchangeMaster} property.
     */
    private final MetaProperty<ExchangeMaster> _exchangeMaster = DirectMetaProperty.ofReadWrite(this, "exchangeMaster", ExchangeMaster.class);
    /**
     * The meta-property for the {@code uriInfo} property.
     */
    private final MetaProperty<UriInfo> _uriInfo = DirectMetaProperty.ofReadWrite(this, "uriInfo", UriInfo.class);
    /**
     * The meta-property for the {@code uriExchangeId} property.
     */
    private final MetaProperty<String> _uriExchangeId = DirectMetaProperty.ofReadWrite(this, "uriExchangeId", String.class);
    /**
     * The meta-property for the {@code uriVersionId} property.
     */
    private final MetaProperty<String> _uriVersionId = DirectMetaProperty.ofReadWrite(this, "uriVersionId", String.class);
    /**
     * The meta-property for the {@code exchange} property.
     */
    private final MetaProperty<ExchangeDocument> _exchange = DirectMetaProperty.ofReadWrite(this, "exchange", ExchangeDocument.class);
    /**
     * The meta-property for the {@code versioned} property.
     */
    private final MetaProperty<ExchangeDocument> _versioned = DirectMetaProperty.ofReadWrite(this, "versioned", ExchangeDocument.class);
    /**
     * The meta-properties.
     */
    private final Map<String, MetaProperty<Object>> _map;

    @SuppressWarnings({"unchecked", "rawtypes" })
    protected Meta() {
      LinkedHashMap temp = new LinkedHashMap();
      temp.put("exchangeMaster", _exchangeMaster);
      temp.put("uriInfo", _uriInfo);
      temp.put("uriExchangeId", _uriExchangeId);
      temp.put("uriVersionId", _uriVersionId);
      temp.put("exchange", _exchange);
      temp.put("versioned", _versioned);
      _map = Collections.unmodifiableMap(temp);
    }

    @Override
    public WebExchangeData createBean() {
      return new WebExchangeData();
    }

    @Override
    public Class<? extends WebExchangeData> beanType() {
      return WebExchangeData.class;
    }

    @Override
    public Map<String, MetaProperty<Object>> metaPropertyMap() {
      return _map;
    }

    //-----------------------------------------------------------------------
    /**
     * The meta-property for the {@code exchangeMaster} property.
     * @return the meta-property, not null
     */
    public final MetaProperty<ExchangeMaster> exchangeMaster() {
      return _exchangeMaster;
    }

    /**
     * The meta-property for the {@code uriInfo} property.
     * @return the meta-property, not null
     */
    public final MetaProperty<UriInfo> uriInfo() {
      return _uriInfo;
    }

    /**
     * The meta-property for the {@code uriExchangeId} property.
     * @return the meta-property, not null
     */
    public final MetaProperty<String> uriExchangeId() {
      return _uriExchangeId;
    }

    /**
     * The meta-property for the {@code uriVersionId} property.
     * @return the meta-property, not null
     */
    public final MetaProperty<String> uriVersionId() {
      return _uriVersionId;
    }

    /**
     * The meta-property for the {@code exchange} property.
     * @return the meta-property, not null
     */
    public final MetaProperty<ExchangeDocument> exchange() {
      return _exchange;
    }

    /**
     * The meta-property for the {@code versioned} property.
     * @return the meta-property, not null
     */
    public final MetaProperty<ExchangeDocument> versioned() {
      return _versioned;
    }

  }

  ///CLOVER:ON
  //-------------------------- AUTOGENERATED END --------------------------
}
