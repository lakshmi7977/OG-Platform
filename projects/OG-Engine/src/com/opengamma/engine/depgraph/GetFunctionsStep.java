/**
 * Copyright (C) 2011 - present by OpenGamma Inc. and the OpenGamma group of companies
 * 
 * Please see distribution for license.
 */
package com.opengamma.engine.depgraph;

import java.util.Collections;
import java.util.Iterator;
import java.util.concurrent.atomic.AtomicReference;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.opengamma.engine.function.MarketDataSourcingFunction;
import com.opengamma.engine.function.ParameterizedFunction;
import com.opengamma.engine.value.ValueRequirement;
import com.opengamma.engine.value.ValueSpecification;
import com.opengamma.util.Cancellable;
import com.opengamma.util.tuple.Pair;

/* package */final class GetFunctionsStep extends ResolveTask.State {

  private static final Logger s_logger = LoggerFactory.getLogger(GetFunctionsStep.class);

  public GetFunctionsStep(final ResolveTask task) {
    super(task);
  }

  private static final class LiveDataResolvedValueProducer implements ResolvedValueProducer {

    private final ValueRequirement _valueRequirement;
    private final ResolvedValue _resolvedValue;

    public LiveDataResolvedValueProducer(final ValueRequirement valueRequirement, final ResolvedValue resolvedValue) {
      _valueRequirement = valueRequirement;
      _resolvedValue = resolvedValue;
    }

    @Override
    public Cancellable addCallback(final ResolvedValueCallback callback) {
      final AtomicReference<ResolvedValueCallback> callbackRef = new AtomicReference<ResolvedValueCallback>(callback);
      callback.resolved(_valueRequirement, _resolvedValue, new ResolutionPump() {
        @Override
        public void pump() {
          final ResolvedValueCallback callback = callbackRef.getAndSet(null);
          if (callback != null) {
            callback.failed(_valueRequirement);
          }
        }
      });
      return new Cancellable() {
        @Override
        public boolean cancel(final boolean mayInterruptIfRunning) {
          return callbackRef.getAndSet(null) != null;
        }
      };
    }

  }

  @Override
  protected void run(final DependencyGraphBuilder builder) {
    if (builder.getMarketDataAvailabilityProvider().isAvailable(getValueRequirement())) {
      s_logger.info("Found live data for {}", getValueRequirement());
      final MarketDataSourcingFunction function = new MarketDataSourcingFunction(getValueRequirement());
      final ResolvedValue result = createResult(function.getResult(), new ParameterizedFunction(function, function.getDefaultParameters()), Collections.<ValueSpecification>emptySet(), Collections
          .singleton(function.getResult()));
      builder.declareTaskProducing(function.getResult(), getTask(), new LiveDataResolvedValueProducer(getValueRequirement(), result));
      pushResult(result);
      if (!getTask().isFinished()) {
        // Wasn't immediately pumped, so declare a finished state 
        setTaskStateFinished();
      }
    } else {
      final Iterator<Pair<ParameterizedFunction, ValueSpecification>> itr = builder.getFunctionResolver().resolveFunction(getValueRequirement(), getComputationTarget());
      if (itr.hasNext()) {
        s_logger.debug("Found functions for {}", getValueRequirement());
        setRunnableTaskState(new NextFunctionStep(getTask(), itr), builder);
      } else {
        s_logger.info("No functions for {}", getValueRequirement());
        setTaskStateFinished();
      }
    }
  }

  @Override
  protected void pump() {
    // Might be pumped while pushing the live data result; this will go straight to the finished state
    setTaskStateFinished();
  }

  @Override
  public String toString() {
    return "GET_FUNCTIONS";
  }

}
