/**
 * Copyright (C) 2012 - present by OpenGamma Inc. and the OpenGamma group of companies
 *
 * Please see distribution for license.
 */
package com.opengamma.analytics.financial.provider.calculator.discounting;

import com.opengamma.analytics.financial.forex.derivative.Forex;
import com.opengamma.analytics.financial.forex.derivative.ForexSwap;
import com.opengamma.analytics.financial.forex.provider.ForexDiscountingProviderMethod;
import com.opengamma.analytics.financial.forex.provider.ForexSwapDiscountingProviderMethod;
import com.opengamma.analytics.financial.interestrate.AbstractInstrumentDerivativeVisitor;
import com.opengamma.analytics.financial.interestrate.InstrumentDerivative;
import com.opengamma.analytics.financial.interestrate.annuity.derivative.Annuity;
import com.opengamma.analytics.financial.interestrate.annuity.derivative.AnnuityCouponFixed;
import com.opengamma.analytics.financial.interestrate.cash.derivative.Cash;
import com.opengamma.analytics.financial.interestrate.cash.derivative.DepositIbor;
import com.opengamma.analytics.financial.interestrate.cash.provider.CashDiscountingProviderMethod;
import com.opengamma.analytics.financial.interestrate.cash.provider.DepositIborDiscountingMethod;
import com.opengamma.analytics.financial.interestrate.fra.derivative.ForwardRateAgreement;
import com.opengamma.analytics.financial.interestrate.fra.provider.ForwardRateAgreementDiscountingProviderMethod;
import com.opengamma.analytics.financial.interestrate.payments.derivative.CouponFixed;
import com.opengamma.analytics.financial.interestrate.payments.derivative.CouponIbor;
import com.opengamma.analytics.financial.interestrate.payments.derivative.CouponIborGearing;
import com.opengamma.analytics.financial.interestrate.payments.derivative.CouponIborSpread;
import com.opengamma.analytics.financial.interestrate.payments.derivative.CouponOIS;
import com.opengamma.analytics.financial.interestrate.payments.derivative.Payment;
import com.opengamma.analytics.financial.interestrate.payments.derivative.PaymentFixed;
import com.opengamma.analytics.financial.interestrate.payments.provider.CouponFixedDiscountingProviderMethod;
import com.opengamma.analytics.financial.interestrate.payments.provider.CouponIborDiscountingProviderMethod;
import com.opengamma.analytics.financial.interestrate.payments.provider.CouponIborGearingDiscountingProviderMethod;
import com.opengamma.analytics.financial.interestrate.payments.provider.CouponIborSpreadDiscountingProviderMethod;
import com.opengamma.analytics.financial.interestrate.payments.provider.CouponOISDiscountingProviderMethod;
import com.opengamma.analytics.financial.interestrate.payments.provider.PaymentFixedDiscountingProviderMethod;
import com.opengamma.analytics.financial.interestrate.swap.derivative.Swap;
import com.opengamma.analytics.financial.interestrate.swap.derivative.SwapFixedCoupon;
import com.opengamma.analytics.financial.provider.description.MulticurveProviderInterface;
import com.opengamma.analytics.financial.provider.sensitivity.multicurve.MultipleCurrencyMulticurveSensitivity;
import com.opengamma.util.ArgumentChecker;

/**
 * Calculator of the present value curve sensitivity as multiple currency interest rate curve sensitivity.
 */
public final class PresentValueCurveSensitivityDiscountingCalculator extends AbstractInstrumentDerivativeVisitor<MulticurveProviderInterface, MultipleCurrencyMulticurveSensitivity> {

  /**
   * The unique instance of the calculator.
   */
  private static final PresentValueCurveSensitivityDiscountingCalculator INSTANCE = new PresentValueCurveSensitivityDiscountingCalculator();

  /**
   * Gets the calculator instance.
   * @return The calculator.
   */
  public static PresentValueCurveSensitivityDiscountingCalculator getInstance() {
    return INSTANCE;
  }

  /**
   * Constructor.
   */
  private PresentValueCurveSensitivityDiscountingCalculator() {
  }

  /**
   * The methods used by the different instruments.
   */
  private static final CashDiscountingProviderMethod METHOD_DEPOSIT = CashDiscountingProviderMethod.getInstance();
  private static final DepositIborDiscountingMethod METHOD_DEPOSIT_IBOR = DepositIborDiscountingMethod.getInstance();
  private static final PaymentFixedDiscountingProviderMethod METHOD_PAY_FIXED = PaymentFixedDiscountingProviderMethod.getInstance();
  private static final CouponFixedDiscountingProviderMethod METHOD_CPN_FIXED = CouponFixedDiscountingProviderMethod.getInstance();
  private static final CouponIborDiscountingProviderMethod METHOD_CPN_IBOR = CouponIborDiscountingProviderMethod.getInstance();
  private static final CouponIborSpreadDiscountingProviderMethod METHOD_CPN_IBOR_SPREAD = CouponIborSpreadDiscountingProviderMethod.getInstance();
  private static final CouponIborGearingDiscountingProviderMethod METHOD_CPN_IBOR_GEARING = CouponIborGearingDiscountingProviderMethod.getInstance();
  private static final CouponOISDiscountingProviderMethod METHOD_CPN_ON = CouponOISDiscountingProviderMethod.getInstance();
  private static final ForwardRateAgreementDiscountingProviderMethod METHOD_FRA = ForwardRateAgreementDiscountingProviderMethod.getInstance();
  private static final ForexDiscountingProviderMethod METHOD_FOREX = ForexDiscountingProviderMethod.getInstance();
  private static final ForexSwapDiscountingProviderMethod METHOD_FOREX_SWAP = ForexSwapDiscountingProviderMethod.getInstance();

  @Override
  public MultipleCurrencyMulticurveSensitivity visit(final InstrumentDerivative instrument, final MulticurveProviderInterface multicurve) {
    return instrument.accept(this, multicurve);
  }

  // -----     Deposit     ------

  @Override
  public MultipleCurrencyMulticurveSensitivity visitCash(final Cash cash, final MulticurveProviderInterface multicurve) {
    return METHOD_DEPOSIT.presentValueCurveSensitivity(cash, multicurve);
  }

  @Override
  public MultipleCurrencyMulticurveSensitivity visitDepositIbor(final DepositIbor deposit, final MulticurveProviderInterface multicurve) {
    return METHOD_DEPOSIT_IBOR.presentValueCurveSensitivity(deposit, multicurve);
  }

  // -----     Payment/Coupon     ------

  @Override
  public MultipleCurrencyMulticurveSensitivity visitFixedPayment(final PaymentFixed payment, final MulticurveProviderInterface multicurve) {
    return METHOD_PAY_FIXED.presentValueCurveSensitivity(payment, multicurve);
  }

  @Override
  public MultipleCurrencyMulticurveSensitivity visitCouponFixed(final CouponFixed payment, final MulticurveProviderInterface multicurve) {
    return METHOD_CPN_FIXED.presentValueCurveSensitivity(payment, multicurve);
  }

  @Override
  public MultipleCurrencyMulticurveSensitivity visitCouponIbor(final CouponIbor payment, final MulticurveProviderInterface multicurve) {
    return METHOD_CPN_IBOR.presentValueCurveSensitivity(payment, multicurve);
  }

  @Override
  public MultipleCurrencyMulticurveSensitivity visitCouponIborSpread(final CouponIborSpread payment, final MulticurveProviderInterface multicurve) {
    return METHOD_CPN_IBOR_SPREAD.presentValueCurveSensitivity(payment, multicurve);
  }

  @Override
  public MultipleCurrencyMulticurveSensitivity visitCouponIborGearing(final CouponIborGearing payment, final MulticurveProviderInterface multicurve) {
    return METHOD_CPN_IBOR_GEARING.presentValueCurveSensitivity(payment, multicurve);
  }

  @Override
  public MultipleCurrencyMulticurveSensitivity visitCouponOIS(final CouponOIS payment, final MulticurveProviderInterface multicurve) {
    return METHOD_CPN_ON.presentValueCurveSensitivity(payment, multicurve);
  }

  @Override
  public MultipleCurrencyMulticurveSensitivity visitForwardRateAgreement(final ForwardRateAgreement fra, final MulticurveProviderInterface multicurve) {
    return METHOD_FRA.presentValueCurveSensitivity(fra, multicurve);
  }

  // -----     Annuity     ------

  @Override
  public MultipleCurrencyMulticurveSensitivity visitGenericAnnuity(final Annuity<? extends Payment> annuity, final MulticurveProviderInterface multicurve) {
    ArgumentChecker.notNull(annuity, "Annuity");
    ArgumentChecker.notNull(multicurve, "multicurve");
    MultipleCurrencyMulticurveSensitivity cs = visit(annuity.getNthPayment(0), multicurve);
    for (int loopp = 1; loopp < annuity.getNumberOfPayments(); loopp++) {
      cs = cs.plus(visit(annuity.getNthPayment(loopp), multicurve));
    }
    return cs;
  }

  @Override
  public MultipleCurrencyMulticurveSensitivity visitFixedCouponAnnuity(final AnnuityCouponFixed annuity, final MulticurveProviderInterface multicurve) {
    return visitGenericAnnuity(annuity, multicurve);
  }

  // -----     Swap     ------

  @Override
  public MultipleCurrencyMulticurveSensitivity visitSwap(final Swap<?, ?> swap, final MulticurveProviderInterface multicurve) {
    final MultipleCurrencyMulticurveSensitivity sensitivity1 = visit(swap.getFirstLeg(), multicurve);
    final MultipleCurrencyMulticurveSensitivity sensitivity2 = visit(swap.getSecondLeg(), multicurve);
    return sensitivity1.plus(sensitivity2);
  }

  @Override
  public MultipleCurrencyMulticurveSensitivity visitFixedCouponSwap(final SwapFixedCoupon<?> swap, final MulticurveProviderInterface multicurve) {
    return visitSwap(swap, multicurve);
  }

  // -----     Forex     ------

  @Override
  public MultipleCurrencyMulticurveSensitivity visitForex(final Forex derivative, final MulticurveProviderInterface multicurves) {
    return METHOD_FOREX.presentValueCurveSensitivity(derivative, multicurves);
  }

  @Override
  public MultipleCurrencyMulticurveSensitivity visitForexSwap(final ForexSwap derivative, final MulticurveProviderInterface multicurves) {
    return METHOD_FOREX_SWAP.presentValueCurveSensitivity(derivative, multicurves);
  }

}