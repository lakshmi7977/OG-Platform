/**
 * Copyright (C) 2009 - present by OpenGamma Inc. and the OpenGamma group of companies
 *
 * Please see distribution for license.
 */
package com.opengamma.livedata.normalization;

import static org.testng.AssertJUnit.assertEquals;
import org.testng.annotations.Test;
import org.fudgemsg.FudgeContext;
import org.fudgemsg.MutableFudgeFieldContainer;
import com.opengamma.livedata.server.FieldHistoryStore;

/**
 * 
 */
public class MarketValueCalculatorTest {
  
  @Test
  public void bidAskLast() {
    MarketValueCalculator calculator = new MarketValueCalculator();
    
    MutableFudgeFieldContainer msg = FudgeContext.GLOBAL_DEFAULT.newMessage();
    msg.add(MarketDataRequirementNames.BID, 50.80);
    msg.add(MarketDataRequirementNames.ASK, 50.90);
    msg.add(MarketDataRequirementNames.LAST, 50.89);
    
    FieldHistoryStore store = new FieldHistoryStore();
    store.liveDataReceived(msg);
    
    MutableFudgeFieldContainer normalized = calculator.apply(msg, store);
    assertEquals(4, normalized.getAllFields().size());
    assertEquals(50.85, normalized.getDouble(MarketDataRequirementNames.MARKET_VALUE), 0.0001);
  }
  
  @Test
  public void bidAskOnly() {
    MarketValueCalculator calculator = new MarketValueCalculator();
    
    MutableFudgeFieldContainer msg = FudgeContext.GLOBAL_DEFAULT.newMessage();
    msg.add(MarketDataRequirementNames.BID, 50.80);
    msg.add(MarketDataRequirementNames.ASK, 50.90);
    
    FieldHistoryStore store = new FieldHistoryStore();
    store.liveDataReceived(msg);
    
    MutableFudgeFieldContainer normalized = calculator.apply(msg, store);
    assertEquals(3, normalized.getAllFields().size());
    assertEquals(50.85, normalized.getDouble(MarketDataRequirementNames.MARKET_VALUE), 0.0001);
  }
  
  @Test
  public void lastOnly() {
    MarketValueCalculator calculator = new MarketValueCalculator();
    
    MutableFudgeFieldContainer msg = FudgeContext.GLOBAL_DEFAULT.newMessage();
    msg.add(MarketDataRequirementNames.LAST, 50.89);
    
    FieldHistoryStore store = new FieldHistoryStore();
    store.liveDataReceived(msg);
    
    MutableFudgeFieldContainer normalized = calculator.apply(msg, store);
    assertEquals(2, normalized.getAllFields().size());
    assertEquals(50.89, normalized.getDouble(MarketDataRequirementNames.MARKET_VALUE), 0.0001);
  }
  
  @Test
  public void bigSpread() {
    MarketValueCalculator calculator = new MarketValueCalculator();
    
    MutableFudgeFieldContainer msg = FudgeContext.GLOBAL_DEFAULT.newMessage();
    msg.add(MarketDataRequirementNames.BID, 50.0);
    msg.add(MarketDataRequirementNames.ASK, 100.0);
    msg.add(MarketDataRequirementNames.LAST, 55.12);
    
    FieldHistoryStore store = new FieldHistoryStore();
    store.liveDataReceived(msg);
    
    MutableFudgeFieldContainer normalized = calculator.apply(msg, store);
    assertEquals(4, normalized.getAllFields().size());
    assertEquals(55.12, normalized.getDouble(MarketDataRequirementNames.MARKET_VALUE), 0.0001);
  }
  
  @Test
  public void bigSpreadHistory() {
    MarketValueCalculator calculator = new MarketValueCalculator();
    
    MutableFudgeFieldContainer historicalMsg = FudgeContext.GLOBAL_DEFAULT.newMessage();
    historicalMsg.add(MarketDataRequirementNames.LAST, 50.52);
    
    FieldHistoryStore store = new FieldHistoryStore();
    store.liveDataReceived(historicalMsg);
    
    MutableFudgeFieldContainer msg = FudgeContext.GLOBAL_DEFAULT.newMessage();
    msg.add(MarketDataRequirementNames.BID, 50.0);
    msg.add(MarketDataRequirementNames.ASK, 100.0);
    
    MutableFudgeFieldContainer normalized = calculator.apply(msg, store);
    assertEquals(3, normalized.getAllFields().size());
    assertEquals(50.52, normalized.getDouble(MarketDataRequirementNames.MARKET_VALUE), 0.0001);
  }
  
  @Test
  public void bigSpreadLowLast() {
    MarketValueCalculator calculator = new MarketValueCalculator();
    
    MutableFudgeFieldContainer msg = FudgeContext.GLOBAL_DEFAULT.newMessage();
    msg.add(MarketDataRequirementNames.BID, 50.0);
    msg.add(MarketDataRequirementNames.ASK, 100.0);
    msg.add(MarketDataRequirementNames.LAST, 44.50);
    
    FieldHistoryStore store = new FieldHistoryStore();
    store.liveDataReceived(msg);
    
    MutableFudgeFieldContainer normalized = calculator.apply(msg, store);
    assertEquals(4, normalized.getAllFields().size());
    assertEquals(50.0, normalized.getDouble(MarketDataRequirementNames.MARKET_VALUE), 0.0001);
  }
  
  @Test
  public void bigSpreadHighLast() {
    MarketValueCalculator calculator = new MarketValueCalculator();
    
    MutableFudgeFieldContainer msg = FudgeContext.GLOBAL_DEFAULT.newMessage();
    msg.add(MarketDataRequirementNames.BID, 50.0);
    msg.add(MarketDataRequirementNames.ASK, 100.0);
    msg.add(MarketDataRequirementNames.LAST, 120.0);
    
    FieldHistoryStore store = new FieldHistoryStore();
    store.liveDataReceived(msg);
    
    MutableFudgeFieldContainer normalized = calculator.apply(msg, store);
    assertEquals(4, normalized.getAllFields().size());
    assertEquals(100.0, normalized.getDouble(MarketDataRequirementNames.MARKET_VALUE), 0.0001);
  }
  
  @Test
  public void useHistoricalBidAsk() {
    MarketValueCalculator calculator = new MarketValueCalculator();
    
    MutableFudgeFieldContainer historicalMsg = FudgeContext.GLOBAL_DEFAULT.newMessage();
    historicalMsg.add(MarketDataRequirementNames.BID, 50.0);
    historicalMsg.add(MarketDataRequirementNames.ASK, 51.0);
    historicalMsg.add(MarketDataRequirementNames.MARKET_VALUE, 50.52);
    
    FieldHistoryStore store = new FieldHistoryStore();
    store.liveDataReceived(historicalMsg);
    
    MutableFudgeFieldContainer newMsg = FudgeContext.GLOBAL_DEFAULT.newMessage();
    newMsg.add(MarketDataRequirementNames.LAST, 50.89);
    
    MutableFudgeFieldContainer normalized = calculator.apply(newMsg, store);
    assertEquals(2, normalized.getAllFields().size());
    assertEquals(50.5, normalized.getDouble(MarketDataRequirementNames.MARKET_VALUE), 0.0001);
  }
  
  @Test
  public void useHistoricalMarketValueWithEmptyMsg() {
    MarketValueCalculator calculator = new MarketValueCalculator();
    
    MutableFudgeFieldContainer historicalMsg = FudgeContext.GLOBAL_DEFAULT.newMessage();
    historicalMsg.add(MarketDataRequirementNames.MARKET_VALUE, 50.52);
    
    FieldHistoryStore store = new FieldHistoryStore();
    store.liveDataReceived(historicalMsg);
    
    MutableFudgeFieldContainer newMsg = FudgeContext.GLOBAL_DEFAULT.newMessage();
    
    MutableFudgeFieldContainer normalized = calculator.apply(newMsg, store);
    assertEquals(1, normalized.getAllFields().size());
    assertEquals(50.52, normalized.getDouble(MarketDataRequirementNames.MARKET_VALUE), 0.0001);
  }
  
  @Test
  public void noMarketValueAvailable() {
    MarketValueCalculator calculator = new MarketValueCalculator();
    
    FieldHistoryStore store = new FieldHistoryStore();
    
    MutableFudgeFieldContainer newMsg = FudgeContext.GLOBAL_DEFAULT.newMessage();
    
    MutableFudgeFieldContainer normalized = calculator.apply(newMsg, store);
    assertEquals(0, normalized.getAllFields().size());
  }
  
  @Test
  public void zeroBid() {
    MarketValueCalculator calculator = new MarketValueCalculator();
    
    MutableFudgeFieldContainer msg = FudgeContext.GLOBAL_DEFAULT.newMessage();
    msg.add(MarketDataRequirementNames.BID, 0.0);
    msg.add(MarketDataRequirementNames.ASK, 1.0);
    msg.add(MarketDataRequirementNames.LAST, 0.57);
    
    FieldHistoryStore store = new FieldHistoryStore();
    store.liveDataReceived(msg);
    
    MutableFudgeFieldContainer normalized = calculator.apply(msg, store);
    assertEquals(4, normalized.getAllFields().size());
    assertEquals(0.5, normalized.getDouble(MarketDataRequirementNames.MARKET_VALUE), 0.0001);
  }

}
