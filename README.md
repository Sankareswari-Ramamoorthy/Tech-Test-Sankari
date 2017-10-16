Given Problem :

Sample data  to represent the instructions sent by various clients to JP Morgan to execute in the international market
with columns Entity Buy/Sell AgreedFx Currency InstructionDate SettlementDate Units Price per unit

A work week starts Monday and ends Friday, unless the currency of the trade is AED or SAR, where the work week starts Sunday and ends Thursday. No other holidays to be taken into account.
A trade can only be settled on a working day.
If an instructed settlement date falls on a weekend, then the settlement date should be changed to the next working day.
USD amount of a trade = Price per unit * Units * Agreed Fx

Requirements :
Create a report that shows
Amount in USD settled incoming everyday
Amount in USD settled outgoing everyday
Ranking of entities based on incoming and outgoing amount. 
Eg: If entity foo instructs the highest amount for a buy instruction, then foo is rank 1 for outgoing

Approach :

*** Data.csv  file is created to hold the columns with sample data. *****
*** JPMorgReport.java***
This is the Java file created as a main class

This holds the below 4 functions which are called further to produce the report-

loadData()
settlementIncomingReport()
settlementOutgoingReport()
printRanking()

This Java  program also has a class InnerDataRow

*** Description of the parts of the program & its functionality ***

**********
loadData()
**********

This function is called to load the Data.csv file

It splits the csv bsed on the delimiter & loads into InnerDataRow's getters & setters.
This function calls calculateAmount(),selectSettlementDate() 
 to derive the USD Amount based on the given formula & to find the actual Settlement Date based on the given criteria .
Inturn this function uses two ArrayList with the class InnerDataRow s object one for Buy nother for Sell

List<InnerDataRow> dataListForBuy = new ArrayList<InnerDataRow>();
List<InnerDataRow> dataListForSell = new ArrayList<InnerDataRow>();



Finally printing  the Buy/Sell from CSV file to the console.

**************************
settlementIncomingReport()
**************************

This function uses Map <Date, Double> with stream() to collect,group & Sum Incoming trade Amount based on USD Amount & Actual settlement date
with collect() for the stream of sell & map derived amount to its corresponding actual settlement date.
This uses StringBuilder & Iterator to print the derivations to the console.

**************************
settlementOutgoingReport()
**************************
Similar to the above function,this function uses Map <Date, Double> with stream() to collect,group & Sum Outgoing trade Amount 
based on USD Amount & Actual settlement date with collect() for the stream of Buy & map derived amount to its corresponding actual settlement date.
This uses StringBuilder & Iterator to print the derivations to the console.


**************
printRanking() 
**************

This Function is used to rank the entities based on the highest amount for both Buy & Sell individually.

**********************
Rank for the incoming
**********************
This function uses Map <String, Double> with stream() to collect,group & Sum Incoming trade Amount based on  entity with collect()
for the stream of sell & pass this to sorted() to sort the amount from highest to lowest.
Similarlly it uses another Map <String, Double> with stream() to collect,group & Sum Outgoing trade Amount based on  entity with collect()
for the stream of Buy & pass this to sorted() to sort the amount from highest to lowest.

Finally it prints the Rank in the console.

*******************************************************
Output of this code with Data.csv in "F:\\Data.csv"
*******************************************************
Entity = foo Settlement Date = Mon Jan 04 00:00:00 GMT 2016 USD Amount = $10025.0

Entity = nil Settlement Date = Mon Jan 11 00:00:00 GMT 2016 USD Amount = $7751.700000000001

Entity = simbu Settlement Date = Fri Jan 15 00:00:00 GMT 2016 USD Amount = $28047.600000000002

Entity = sankari Settlement Date = Mon Jan 04 00:00:00 GMT 2016 USD Amount = $25507.0

Entity = shanky Settlement Date = Mon Jan 04 00:00:00 GMT 2016 USD Amount = $2469.3030000000003

Entity = gum Settlement Date = Thu Jan 07 00:00:00 GMT 2016 USD Amount = $30226.5

Entity = sarat Settlement Date = Thu Jan 07 00:00:00 GMT 2016 USD Amount = $159213.6

Entity = java Settlement Date = Mon Jan 04 00:00:00 GMT 2016 USD Amount = $211848.0

Entity = spring Settlement Date = Fri Oct 14 00:00:00 BST 2016 USD Amount = $46511.4

Entity = struts Settlement Date = Sun Oct 23 00:00:00 BST 2016 USD Amount = $15622.2

Entity = bar Settlement Date = Thu Jan 07 00:00:00 GMT 2016 USD Amount = $14899.5

Entity = vinod Settlement Date = Thu Jan 07 00:00:00 GMT 2016 USD Amount = $5330.474999999999

Entity = morhgan Settlement Date = Sun Oct 16 00:00:00 BST 2016 USD Amount = $31006.8

Entity = handy Settlement Date = Thu Jan 07 00:00:00 GMT 2016 USD Amount = $363343.68

Entity = hatty Settlement Date = Mon Jan 04 00:00:00 GMT 2016 USD Amount = $14878.5

Entity = kittu Settlement Date = Thu Jan 07 00:00:00 GMT 2016 USD Amount = $17087.868000000002

Entity = bittu Settlement Date = Mon Jan 04 00:00:00 GMT 2016 USD Amount = $4727.673

Entity = hari Settlement Date = Thu Jan 07 00:00:00 GMT 2016 USD Amount = $74712.45599999999

Entity = lalu Settlement Date = Mon Jan 04 00:00:00 GMT 2016 USD Amount = $1036932.0

Entity = hadoop Settlement Date = Fri Oct 14 00:00:00 BST 2016 USD Amount = $2004.0


*** REPORT :: INCOMING USD SETTLEMENT PER DAY ***
Thu Jan 07 00:00:00 GMT 2016="475373.97899999993", Fri Oct 14 00:00:00 BST 2016="2004.0", Sun Oct 16 00:00:00 BST 2016="31006.8", Mon Jan 04 00:00:00 GMT 2016="1056538.173"

*** REPORT :: OUTGOING USD SETTLEMENT PER DAY ***
Thu Jan 07 00:00:00 GMT 2016="189440.1", Fri Oct 14 00:00:00 BST 2016="46511.4", Fri Jan 15 00:00:00 GMT 2016="28047.600000000002", Sun Oct 23 00:00:00 BST 2016="15622.2", Mon Jan 11 00:00:00 GMT 2016="7751.700000000001", Mon Jan 04 00:00:00 GMT 2016="249849.303"

*** REPORT :: RANKING OF ENTITIES BASED ON INCOMING SETTLEMENT ***
1) Entity = lalu Settlement Date = Mon Jan 04 00:00:00 GMT 2016 USD Amount = $1036932.0

2) Entity = handy Settlement Date = Thu Jan 07 00:00:00 GMT 2016 USD Amount = $363343.68

3) Entity = hari Settlement Date = Thu Jan 07 00:00:00 GMT 2016 USD Amount = $74712.45599999999

4) Entity = morhgan Settlement Date = Sun Oct 16 00:00:00 BST 2016 USD Amount = $31006.8

5) Entity = kittu Settlement Date = Thu Jan 07 00:00:00 GMT 2016 USD Amount = $17087.868000000002

6) Entity = bar Settlement Date = Thu Jan 07 00:00:00 GMT 2016 USD Amount = $14899.5

7) Entity = hatty Settlement Date = Mon Jan 04 00:00:00 GMT 2016 USD Amount = $14878.5

8) Entity = vinod Settlement Date = Thu Jan 07 00:00:00 GMT 2016 USD Amount = $5330.474999999999

9) Entity = bittu Settlement Date = Mon Jan 04 00:00:00 GMT 2016 USD Amount = $4727.673

10) Entity = hadoop Settlement Date = Fri Oct 14 00:00:00 BST 2016 USD Amount = $2004.0


*** REPORT :: RANKING OF ENTITIES BASED ON OUTGOING SETTLEMENT ***
1) Entity = java Settlement Date = Mon Jan 04 00:00:00 GMT 2016 USD Amount = $211848.0

2) Entity = sarat Settlement Date = Thu Jan 07 00:00:00 GMT 2016 USD Amount = $159213.6

3) Entity = spring Settlement Date = Fri Oct 14 00:00:00 BST 2016 USD Amount = $46511.4

4) Entity = gum Settlement Date = Thu Jan 07 00:00:00 GMT 2016 USD Amount = $30226.5

5) Entity = simbu Settlement Date = Fri Jan 15 00:00:00 GMT 2016 USD Amount = $28047.600000000002

6) Entity = sankari Settlement Date = Mon Jan 04 00:00:00 GMT 2016 USD Amount = $25507.0

7) Entity = struts Settlement Date = Sun Oct 23 00:00:00 BST 2016 USD Amount = $15622.2

8) Entity = foo Settlement Date = Mon Jan 04 00:00:00 GMT 2016 USD Amount = $10025.0

9) Entity = nil Settlement Date = Mon Jan 11 00:00:00 GMT 2016 USD Amount = $7751.700000000001

10) Entity = shanky Settlement Date = Mon Jan 04 00:00:00 GMT 2016 USD Amount = $2469.3030000000003

