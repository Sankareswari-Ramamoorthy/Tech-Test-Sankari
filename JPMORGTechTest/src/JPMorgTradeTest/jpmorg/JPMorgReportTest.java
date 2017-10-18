package JPMorgTradeTest.jpmorg;

import junit.framework.TestCase;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.ArrayList;
import java.util.HashMap;
import java.lang.reflect.*;

import JPMorgTrade.jpmorg.JPMorgReport;
import JPMorgTrade.jpmorg.JPMorgReport.InnerDataRow;

public class JPMorgReportTest extends TestCase {
  
  public void testCalculateAmountWhenAllString() {
    JPMorgReport jpMorg = new JPMorgReport();
    String data1 = new String ("100.25");
    String data2 = new String ("200");
    String data3 = new String ("0.50");
    Object o = null;
    try {
      Method method = JPMorgTrade.jpmorg.JPMorgReport.class.getDeclaredMethod("calculateAmount", 
                                                                             new Class[] {java.lang.String.class, 
        java.lang.String.class, 
                                                                               java.lang.String.class});
      method.setAccessible(true);
      o = method.invoke(jpMorg, data1, data2, data3);
    } catch (NoSuchMethodException nsme) {
      nsme.printStackTrace();
    } catch (IllegalAccessException iae) {
      iae.printStackTrace();
    } catch (InvocationTargetException ite) {
      ite.printStackTrace();
    }
    
    assertEquals (10025.0, (Double) o);    
  }
  
  public void testSelectSettlementDate() {
    JPMorgReport jpMorg = new JPMorgReport();
    String date = new String ("02 Jan 2016");
    String currency = new String ("SGP");
    Object o = null;
    try {
      Method method = JPMorgTrade.jpmorg.JPMorgReport.class.getDeclaredMethod("selectSettlementDate", 
                                                                             new Class[] {java.lang.String.class,                                                                                            
        java.lang.String.class});
      method.setAccessible(true);
      o = method.invoke(jpMorg, date, currency);
    } catch (NoSuchMethodException nsme) {
      nsme.printStackTrace();
    } catch (IllegalAccessException iae) {
      iae.printStackTrace();
    } catch (InvocationTargetException ite) {
      ite.printStackTrace();
    }
    
    assertEquals (new Date("Mon Jan 04 00:00:00 GMT 2016"), (Date) o);
  }
  
  public void testSettlementOutgoingReport() {
    List<JPMorgReport.InnerDataRow> lstIdr = new ArrayList<JPMorgReport.InnerDataRow>();
    JPMorgReport jpm = new JPMorgReport();
    JPMorgReport.InnerDataRow innerObj1 = jpm.new InnerDataRow();
    JPMorgReport.InnerDataRow innerObj2 = jpm.new InnerDataRow();
    JPMorgReport.InnerDataRow innerObj3 = jpm.new InnerDataRow();
    innerObj1.setEntity("foo");    
    innerObj2.setEntity("nil");
    innerObj3.setEntity("shanky");
    innerObj1.setActualSetlDate(new Date("Mon Jan 04 00:00:00 GMT 2016"));
    innerObj2.setActualSetlDate(new Date("Mon Jan 11 00:00:00 GMT 2016"));
    innerObj3.setActualSetlDate(new Date("Mon Jan 04 00:00:00 GMT 2016"));
    innerObj1.setUsdAmount(10025.0);
    innerObj2.setUsdAmount(7751.70);
    innerObj3.setUsdAmount(2469.30);
    lstIdr.add(innerObj1);
    lstIdr.add(innerObj2);
    lstIdr.add(innerObj3);
    Map<Date, Double> outputMap = jpm.settlementOutgoingReport(lstIdr);
    Map<Date, Double> compare = new HashMap<Date, Double>();
    compare.put(new Date("Mon Jan 11 00:00:00 GMT 2016"),7751.7);
    compare.put(new Date("Mon Jan 04 00:00:00 GMT 2016"),12494.3);
    assertEquals (compare, outputMap);
  }
  
  public void testSettlementIncomingReport() {
    List<JPMorgReport.InnerDataRow> lstIdr = new ArrayList<JPMorgReport.InnerDataRow>();
    JPMorgReport jpm = new JPMorgReport();
    JPMorgReport.InnerDataRow innerObj1 = jpm.new InnerDataRow();
    JPMorgReport.InnerDataRow innerObj2 = jpm.new InnerDataRow();
    JPMorgReport.InnerDataRow innerObj3 = jpm.new InnerDataRow();
    innerObj1.setEntity("bar");    
    innerObj2.setEntity("hatty");
    innerObj3.setEntity("bittu");
    innerObj1.setActualSetlDate(new Date("Thu Jan 07 00:00:00 GMT 2016"));
    innerObj2.setActualSetlDate(new Date("Mon Jan 04 00:00:00 GMT 2016"));
    innerObj3.setActualSetlDate(new Date("Mon Jan 04 00:00:00 GMT 2016"));
    innerObj1.setUsdAmount(14899.5);
    innerObj2.setUsdAmount(14878.5);
    innerObj3.setUsdAmount(4727.67);
    lstIdr.add(innerObj1);
    lstIdr.add(innerObj2);
    lstIdr.add(innerObj3);
    Map<Date, Double> outputMap = jpm.settlementIncomingReport(lstIdr);
    Map<Date, Double> compare = new HashMap<Date, Double>();
    compare.put(new Date("Thu Jan 07 00:00:00 GMT 2016"),14899.5);
    compare.put(new Date("Mon Jan 04 00:00:00 GMT 2016"),19606.17);
    assertEquals (compare, outputMap);
  }
}