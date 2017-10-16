package Sankari.jpmorg;

import java.io.*;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Calendar;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.stream.Collectors;

public class JPMorgReport {
  
  private List<InnerDataRow> dataListBuy;
  private List<InnerDataRow> dataListSell;
  
  public static void main(String args[]) {
    
    JPMorgReport jpMorg = new JPMorgReport();
    try {
      jpMorg.loadData("F:\\Data.csv");
      jpMorg.settlementIncomingReport();
      jpMorg.settlementOutgoingReport();
      jpMorg.printRanking();
    } catch (IOException ioe){
      ioe.printStackTrace();
    } catch (ParseException pe){
      pe.printStackTrace();
    }
  }
  
  // Generate incoming settlement report
  private void settlementIncomingReport() {
    System.out.println("\n*** REPORT :: INCOMING USD SETTLEMENT PER DAY ***");
    Map <Date, Double> sumIncomingByDate = this.getDataListSell().stream().collect(
                                                                                   Collectors.groupingBy(InnerDataRow::getActualSetlDate, 
                                                                                                         Collectors.summingDouble(InnerDataRow::getUsdAmount)));
    StringBuilder sb = new StringBuilder();
    Iterator<Entry<Date, Double>> iter = sumIncomingByDate.entrySet().iterator();
    while (iter.hasNext()) {
      Entry<Date, Double> entry = iter.next();
      sb.append(entry.getKey());
      sb.append('=').append('"');
      sb.append(entry.getValue());
      sb.append('"');
      if (iter.hasNext()) {
        sb.append(',').append(' ');
      }
    }
    
    System.out.println(sb.toString());
  }
  
  // Generate outgoing settlement report
  private void settlementOutgoingReport() {
    System.out.println("\n*** REPORT :: OUTGOING USD SETTLEMENT PER DAY ***");
    Map <Date, Double> sumOutgoingByDate = this.getDataListBuy().stream().collect(
                                                                                  Collectors.groupingBy(InnerDataRow::getActualSetlDate, 
                                                                                                        Collectors.summingDouble(InnerDataRow::getUsdAmount)));
    StringBuilder sb = new StringBuilder();
    Iterator<Entry<Date, Double>> iter = sumOutgoingByDate.entrySet().iterator();
    while (iter.hasNext()) {
      Entry<Date, Double> entry = iter.next();
      sb.append(entry.getKey());
      sb.append('=').append('"');
      sb.append(entry.getValue());
      sb.append('"');
      if (iter.hasNext()) {
        sb.append(',').append(' ');
      }
    }
    System.out.println(sb.toString());
  }
  
  // Print ranking
  private void printRanking() {
    System.out.println("\n*** REPORT :: RANKING OF ENTITIES BASED ON INCOMING SETTLEMENT ***");
    Map <String, Double> sumIncomingByEntity = this.getDataListSell().stream().collect(
                                                                                       Collectors.groupingBy(InnerDataRow::getEntity, 
                                                                                                             Collectors.summingDouble(InnerDataRow::getUsdAmount)));
    List<InnerDataRow> sortedIncomingByEntity = this.getDataListSell().stream().sorted(
                                                                                       Comparator.comparing((InnerDataRow x) -> sumIncomingByEntity.get(x.getEntity()))
                                                                                         .reversed()).collect(Collectors.toList());
    int rank = 0;
    for (InnerDataRow i : sortedIncomingByEntity) {
      System.out.print(++rank + ") ");
      System.out.println(i);
    }
    
    System.out.println("\n*** REPORT :: RANKING OF ENTITIES BASED ON OUTGOING SETTLEMENT ***");
    Map <String, Double> sumOutgoingByEntity = this.getDataListBuy().stream().collect(
                                                                                      Collectors.groupingBy(InnerDataRow::getEntity, 
                                                                                                            Collectors.summingDouble(InnerDataRow::getUsdAmount)));
    List<InnerDataRow> sortedOutgoingByEntity = this.getDataListBuy().stream().sorted(
                                                                                      Comparator.comparing((InnerDataRow x) -> sumOutgoingByEntity.get(x.getEntity()))
                                                                                        .reversed()).collect(Collectors.toList());
    rank = 0;
    for (InnerDataRow i : sortedOutgoingByEntity) {
      System.out.print(++rank + ") ");
      System.out.println(i);
    }
  }
  
  // Determine settlement date
  private Date selectSettlementDate (String givenSettlementDate, String currencyCode) 
    throws ParseException {
    DateFormat format1 = new SimpleDateFormat("dd/MMM/yyyy");
    Date d1 = format1.parse(givenSettlementDate.replace(" ","/"));
    Calendar c1 = Calendar.getInstance();
    c1.setTime(d1);
    if (currencyCode.equalsIgnoreCase("AED") || currencyCode.equalsIgnoreCase("SAR")) {
      switch(c1.get(Calendar.DAY_OF_WEEK)) {
        case Calendar.FRIDAY: 
          c1.add(Calendar.DATE, 2);
          break;
        case Calendar.SATURDAY:
          c1.add(Calendar.DATE, 1);
          break;
        default:
          c1.add(Calendar.DATE, 0);
          break;
      }
    } else {
      switch(c1.get(Calendar.DAY_OF_WEEK)) {
        case Calendar.SATURDAY: 
          c1.add(Calendar.DATE, 2);
          break;
        case Calendar.SUNDAY:
          c1.add(Calendar.DATE, 1);
          break;
        default:
          c1.add(Calendar.DATE, 0);
          break;
      }
    }
    
    return c1.getTime();
  }
  
  // Calculate USD amount of a trade
  private double calculateAmount (String pricePerUnit, String units, String agreedFx) 
    throws NumberFormatException {
    return Double.parseDouble(pricePerUnit) * Double.parseDouble(units) * Double.parseDouble(agreedFx);
  }
  
  // Load data and populate arraylist
  private void loadData(String csvFile) throws IOException, ParseException{
    String csvSplitBy = ",";
    String line = "";
    String[] lineValue = new String[8];
    Date setlDate = null;
    double usdAmt = 0.000;
    InnerDataRow innerObj = null;
    DateFormat df = new SimpleDateFormat("dd/MMM/yyyy");
    List<InnerDataRow> dataListForBuy = new ArrayList<InnerDataRow>();
    List<InnerDataRow> dataListForSell = new ArrayList<InnerDataRow>();
    BufferedReader br = new BufferedReader(new FileReader(csvFile));
    br.readLine();
    while ((line = br.readLine()) != null) {
      lineValue = line.split(csvSplitBy);
      
      setlDate = selectSettlementDate (lineValue[5], lineValue[3]);
      usdAmt = calculateAmount(lineValue[7], lineValue[6], lineValue[2]);
      
      if (lineValue[1].equalsIgnoreCase("B")){
        innerObj = new InnerDataRow();
        innerObj.setEntity(lineValue[0]);
        innerObj.setBuySellIndicator(lineValue[1]);
        innerObj.setAgreedFx(Double.parseDouble(lineValue[2]));
        innerObj.setCurrency(lineValue[3]);
        innerObj.setInstrDate(df.parse(lineValue[4].replace(" ","/")));
        innerObj.setSetlDate(df.parse(lineValue[5].replace(" ","/")));
        innerObj.setUnits(Integer.parseInt(lineValue[6]));
        innerObj.setPricePerUnit(Double.parseDouble(lineValue[7]));
        innerObj.setActualSetlDate(setlDate);
        innerObj.setUsdAmount(usdAmt);
        
        dataListForBuy.add(innerObj);
      } else {
        innerObj = new InnerDataRow();
        innerObj.setEntity(lineValue[0]);
        innerObj.setBuySellIndicator(lineValue[1]);
        innerObj.setAgreedFx(Double.parseDouble(lineValue[2]));
        innerObj.setCurrency(lineValue[3]);
        innerObj.setInstrDate(df.parse(lineValue[4].replace(" ","/")));
        innerObj.setSetlDate(df.parse(lineValue[5].replace(" ","/")));
        innerObj.setUnits(Integer.parseInt(lineValue[6]));
        innerObj.setPricePerUnit(Double.parseDouble(lineValue[7]));
        innerObj.setActualSetlDate(setlDate);
        innerObj.setUsdAmount(usdAmt);
        
        dataListForSell.add(innerObj);
      }
      
    }
    
    this.setDataListBuy(dataListForBuy);
    this.setDataListSell(dataListForSell);
    
    // test the data load
    for (InnerDataRow i : this.getDataListBuy()){
      System.out.println(i);
    }
    
    for (InnerDataRow i : this.getDataListSell()){
      System.out.println(i);
    }
  }
  
  class InnerDataRow {
    
    private String entity;
    private String buySellIndicator;
    private double agreedFx;
    private String currency;
    private Date instrDate;
    private Date setlDate;
    private int units;
    private double pricePerUnit;
    private Date actualSetlDate;
    private double usdAmount;
    
    public String getEntity() {
      return entity;
    }
    public void setEntity(String entity) {
      this.entity = entity;
    }
    public String getBuySellIndicator() {
      return buySellIndicator;
    }
    public void setBuySellIndicator(String buySellIndicator) {
      this.buySellIndicator = buySellIndicator;
    }
    public double getAgreedFx() {
      return agreedFx;
    }
    public void setAgreedFx(double agreedFx) {
      this.agreedFx = agreedFx;
    }
    public String getCurrency() {
      return currency;
    }
    public void setCurrency(String currency) {
      this.currency = currency;
    }
    public Date getInstrDate() {
      return instrDate;
    }
    public void setInstrDate(Date instrDate) {
      this.instrDate = instrDate;
    }
    public Date getSetlDate() {
      return setlDate;
    }
    public void setSetlDate(Date setlDate) {
      this.setlDate = setlDate;
    }
    public int getUnits() {
      return units;
    }
    public void setUnits(int units) {
      this.units = units;
    }
    public double getPricePerUnit() {
      return pricePerUnit;
    }
    public void setPricePerUnit(double pricePerUnit) {
      this.pricePerUnit = pricePerUnit;
    }
    public Date getActualSetlDate() {
      return actualSetlDate;
    }
    public void setActualSetlDate(Date actualSetlDate) {
      this.actualSetlDate = actualSetlDate;
    }
    public double getUsdAmount() {
      return usdAmount;
    }
    public void setUsdAmount(double usdAmount) {
      this.usdAmount = usdAmount;
    }
    public String toString() {
      return ("Entity = " + this.getEntity() 
                + " Settlement Date = " + this.getActualSetlDate().toString() 
                + " USD Amount = $" + this.getUsdAmount() + "\n");
    }
  }
  
  public List<InnerDataRow> getDataListBuy() {
    return dataListBuy;
  }
  
  public void setDataListBuy(List<InnerDataRow> dataListBuy) {
    this.dataListBuy = dataListBuy;
  }
  
  public List<InnerDataRow> getDataListSell() {
    return dataListSell;
  }
  
  public void setDataListSell(List<InnerDataRow> dataListSell) {
    this.dataListSell = dataListSell;
  }
}