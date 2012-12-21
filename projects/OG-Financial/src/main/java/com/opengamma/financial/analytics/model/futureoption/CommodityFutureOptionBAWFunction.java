/**
 * Copyright (C) 2012 - present by OpenGamma Inc. and the OpenGamma group of companies
 *
 * Please see distribution for license.
 */
package com.opengamma.financial.analytics.model.futureoption;

import com.opengamma.engine.ComputationTargetType;
import com.opengamma.engine.value.ValueProperties;
import com.opengamma.engine.value.ValuePropertyNames;
import com.opengamma.engine.value.ValueRequirement;
import com.opengamma.engine.value.ValueRequirementNames;
import com.opengamma.financial.analytics.model.InstrumentTypeProperties;
import com.opengamma.financial.analytics.model.volatility.surface.black.BlackVolatilitySurfacePropertyNamesAndValues;
import com.opengamma.financial.security.FinancialSecurity;
import com.opengamma.financial.security.FinancialSecurityUtils;
import com.opengamma.id.UniqueId;
import com.opengamma.util.money.Currency;

/**
 *
 */
public abstract class CommodityFutureOptionBAWFunction extends FutureOptionFunction {
  /** The calculation method name */
  public static final String BAW_METHOD = "BaroneAdesiWhaleyMethod";

  /**
   * @param valueRequirementName The value requirement name
   */
  public CommodityFutureOptionBAWFunction(final String... valueRequirementName) {
    super(valueRequirementName, BAW_METHOD);
  }

  @Override
  protected ValueRequirement getVolatilitySurfaceRequirement(final FinancialSecurity security, final String surfaceName, final String smileInterpolator) {
    final Currency currency = FinancialSecurityUtils.getCurrency(security);
    final ValueProperties properties = ValueProperties.builder()
      .with(ValuePropertyNames.SURFACE, surfaceName)
      .with(BlackVolatilitySurfacePropertyNamesAndValues.PROPERTY_SMILE_INTERPOLATOR, smileInterpolator)
      .with(InstrumentTypeProperties.PROPERTY_SURFACE_INSTRUMENT_TYPE, InstrumentTypeProperties.COMMODITY_FUTURE_OPTION)
      .get();
    final UniqueId surfaceId = currency.getUniqueId();
    return new ValueRequirement(ValueRequirementNames.BLACK_VOLATILITY_SURFACE, ComputationTargetType.PRIMITIVE, surfaceId, properties);
  }

}