/**
 * Copyright (C) 2012 - present by OpenGamma Inc. and the OpenGamma group of companies
 * 
 * Please see distribution for license.
 */
package com.opengamma.engine.target;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicInteger;

import com.google.common.collect.MapMaker;
import com.opengamma.engine.ComputationTargetResolver;

/**
 * Base class for target resolver based object.
 */
/* package */abstract class TargetResolverObject implements Serializable {

  private static final AtomicInteger s_nextIdentifier = new AtomicInteger();
  private static final ConcurrentMap<ComputationTargetResolver, Integer> s_resolver2identifier = new MapMaker().weakKeys().makeMap();
  private static final ConcurrentMap<Integer, ComputationTargetResolver> s_identifier2resolver = new MapMaker().weakValues().makeMap();

  private transient ComputationTargetResolver _targetResolver;

  protected TargetResolverObject(final ComputationTargetResolver targetResolver) {
    _targetResolver = targetResolver;
  }

  protected ComputationTargetResolver getTargetResolver() {
    return _targetResolver;
  }

  private void writeObject(final ObjectOutputStream out) throws IOException {
    Integer id = s_resolver2identifier.get(getTargetResolver());
    if (id == null) {
      id = s_nextIdentifier.getAndIncrement();
      s_identifier2resolver.put(id, getTargetResolver());
      final Integer existing = s_resolver2identifier.putIfAbsent(getTargetResolver(), id);
      if (existing != null) {
        s_identifier2resolver.remove(id);
        id = existing;
      }
    }
    out.writeInt(id.intValue());
  }

  private void readObject(final ObjectInputStream in) throws IOException {
    final int targetResolver = in.readInt();
    _targetResolver = s_identifier2resolver.get(targetResolver);
    assert _targetResolver != null;
  }

}
