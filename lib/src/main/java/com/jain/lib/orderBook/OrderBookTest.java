package com.jain.lib.orderBook;

import com.google.gson.Gson;
import com.jain.lib.ConstUtils;
import com.jain.lib.Sample;
import com.jain.lib.Slot;
import com.jain.lib.TimeUtils;
import com.jain.lib.model.VenueDetails;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.temporal.IsoFields;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.EnumSet;
import java.util.List;
import java.util.Locale;

import static com.jain.lib.Sample.DAYS.today;

public class OrderBookTest {
    //Will come from service hours or opening hour table
    private static final int orderPerHour = 20;
    private static final String[] openTimings = {"10:00", "16:00"};
    private static final String[] closeTimings = {"14:00", "23:30"};
    private static final DecimalFormat df = new DecimalFormat("0.00");
    //private static boolean isPending=false;
    private static Slot lastSelectedSlot;
    private static int nextAvailableSlotNo;
    private static final SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss", Locale .getDefault());
    private static final SimpleDateFormat formatter1 = new SimpleDateFormat("HH:mm", Locale.getDefault());
    private static final SimpleDateFormat dateFormatter = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
    private static final SimpleDateFormat dateFormatterFull = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());
    private static List<Slot> slotList;

    public static void main(String[] args) {

        VenueDetails venueDetail = new Gson().fromJson(Sample.venueDetails, VenueDetails.class);

//        10/03/2021 22:20
//        orderETADate=TimeUtils.getFormattedDate("10/03/2021 22:20","dd/MM/yyyy HH:mm",
//                "yyyy-MM-dd HH:mm:ss"
//        );

//        orderBookDate=TimeUtils.getFormattedDate("10/03/2021","dd/MM/yyyy",
//                "yyyy-MM-dd HH:mm:ss"
//        );



//        Sample.DAYS day= today;
//
//        MinMaxTime minMaxTime=getMinMaxTime(day);
//        System.out.println("Day: "+day+" ::minMaxTime: "+minMaxTime);

//        EnumSet.allOf(Sample.DAYS.class)
//                .forEach(season -> System.out.println(season));
//
//        // Convert enum to set and apply forEach()
//        Arrays.asList(seasons.values())
//                .forEach(season -> System.out.println(season));

//        for (Sample.DAYS s : Sample.DAYS.values()) {
//            Sample.DAYS day= s;
//
//            MinMaxTime minMaxTime=getMinMaxTime(day);
////            System.out.println("Day: "+day+" ::minMaxTime: "+minMaxTime);
//
////            System.out.println(s);
//        }


        int interval = 60 * 60 / orderPerHour;
        getSlotList(interval);
        System.out.println("Size:" + slotList.size());



        OrderDetails orderDetails = new Gson().fromJson(Sample.orderDetails, OrderDetails.class);
        orderDetails.getMetaData().setOrderBookDate("11/03/2021 13:40");
        orderDetails.getMetaData().setOrderEtaDate("11/03/2021 16:20");
        processOrder(orderDetails);


        OrderDetails orderDetails2 = new Gson().fromJson(Sample.orderDetails2, OrderDetails.class);
        orderDetails2.getMetaData().setOrderBookDate("11/03/2021 12:20");
        orderDetails2.getMetaData().setOrderEtaDate("11/03/2021 12:30");
        processOrder(orderDetails2);


        orderDetails.getMetaData().setOrderBookDate("11/03/2021 13:45");
        orderDetails.getMetaData().setOrderEtaDate("11/03/2021 16:22");
        processOrder(orderDetails);

        orderDetails2.getMetaData().setOrderBookDate("11/03/2021 10:20");
        orderDetails2.getMetaData().setOrderEtaDate("11/03/2021 12:35");
        processOrder(orderDetails2);




        //Next order is placed with in 2 minutes
//        Calendar cal = Calendar.getInstance();
//        cal.setTime(date);
//        cal.add(Calendar.MINUTE, 2);
//        date = cal.getTime();
//        newOrderTime = formatter.format(date);
//        System.out.println("Time 2 Order :" + newOrderTime);
//        getTimings(newOrderTime, cartItemList);
//
//        //Next order is placed with in second shift set lastselectedslot to null,
//        lastSelectedSlot = null;
//
//        cal = Calendar.getInstance();
//        cal.setTime(date);
//        cal.set(Calendar.HOUR_OF_DAY, 17);
//        cal.set(Calendar.MINUTE, 14);
//        cal.set(Calendar.SECOND, 0);
//
//        date = cal.getTime();
//        newOrderTime = formatter.format(date);
//
//        System.out.println("Time 3 Order :" + newOrderTime);
//        try {
//            getTimings(newOrderTime, cartItemList);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }


    }

    private static MinMaxTime getMinMaxTime(Sample.DAYS day) {
        MinMaxTime slot=new MinMaxTime();
        String minDate,maxDate;
        String yesterdayDate;
        switch (day){
            case today:
                minDate=TimeUtils.getFormattedDate(TimeUtils.getCurTimeString(),"yyyy-MM-dd HH:mm:ss",
                        "yyyy-MM-dd");
                minDate= String.format("%s 00:00:00", minDate);
                slot.setMinTime(minDate);
                slot.setMaxTime(TimeUtils.getCurTimeString());
                break;
            case yesterday:

                yesterdayDate = TimeUtils.getSelectedDate(-1, "yyyy-MM-dd");
                minDate= String.format("%s 00:00:00", yesterdayDate);
                maxDate= String.format("%s 23:59:59", yesterdayDate);
                slot.setMinTime(minDate);
                slot.setMaxTime(maxDate);


                break;
            case last7days:
                yesterdayDate = TimeUtils.getSelectedDate(-7, "yyyy-MM-dd");
                minDate= String.format("%s 00:00:00", yesterdayDate);
                slot.setMinTime(minDate);
                slot.setMaxTime(TimeUtils.getCurTimeString());

                break;
            case currentMonth:
                LocalDate today = LocalDate.now();
//                System.out.println("First day: " + today.withDayOfMonth(1));
//                System.out.println("Last day: " + today.withDayOfMonth(today.lengthOfMonth()));
                minDate=today.getYear()+"-"+today.getMonthValue()+"-01 00:00:00";
                slot.setMinTime(minDate);
                slot.setMaxTime(TimeUtils.getCurTimeString());
                break;
            case lastMonth:
                LocalDate now = LocalDate.now(); // 2015-11-24
                LocalDate earlier = now.minusMonths(1); // 2015-10-24
                minDate=earlier.getYear()+"-"+earlier.getMonthValue()+"-01 00:00:00";
                int daysInMonth = earlier.lengthOfMonth();
                maxDate=earlier.getYear()+"-"+earlier.getMonthValue()+"-"+daysInMonth+" 23:59:59";
                slot.setMinTime(minDate);
                slot.setMaxTime(maxDate);
                break;
            case quarterCurrent:
                LocalDate localDate = LocalDate.now();
                LocalDate firstDayOfQuarter = localDate.with(localDate.getMonth().firstMonthOfQuarter())
                        .with(TemporalAdjusters.firstDayOfMonth());
                minDate=firstDayOfQuarter+" 00:00:00";

                slot.setMinTime(minDate);
                slot.setMaxTime(TimeUtils.getCurTimeString());



//                LocalDate lastDayOfQuarter = firstDayOfQuarter.plusMonths(2)
//                        .with(TemporalAdjusters.lastDayOfMonth());

                break;
            case quarterLast:
                 today = LocalDate.now();
// get previous quarter
                LocalDate previousQuarter = today.minus(1, IsoFields.QUARTER_YEARS);
// get last day in previous quarter
                long lastDayOfQuarter = IsoFields.DAY_OF_QUARTER.rangeRefinedBy(previousQuarter).getMaximum();

// get the date corresponding to the last day of quarter
                LocalDate lastDayInPreviousQuarter = previousQuarter.with(IsoFields.DAY_OF_QUARTER, lastDayOfQuarter);
                LocalDate firstDayInPreviousQuarter  = lastDayInPreviousQuarter.minusDays(lastDayOfQuarter-1);
                minDate=firstDayInPreviousQuarter+" 00:00:00";
                slot.setMinTime(minDate);
                maxDate=lastDayInPreviousQuarter+" 23:59:59";
                slot.setMaxTime(maxDate);

                break;
            case quarter1:
//                Jan – Mar
                today = LocalDate.now();
                minDate=today.getYear()+"-01-01  00:00:00";
                slot.setMinTime(minDate);
                maxDate=today.getYear()+"-03-31  23:59:59";
                slot.setMaxTime(maxDate);
                break;
            case quarter2:
//                April – June
                today = LocalDate.now();
                minDate=today.getYear()+"-04-01  00:00:00";
                slot.setMinTime(minDate);
                maxDate=today.getYear()+"-06-30  23:59:59";
                slot.setMaxTime(maxDate);
                break;
            case quarter3:
//                July-September
                today = LocalDate.now();
                minDate=today.getYear()+"-07-01  00:00:00";
                slot.setMinTime(minDate);
                maxDate=today.getYear()+"-09-30  23:59:59";
                slot.setMaxTime(maxDate);
                break;
            case quarter4:
//                October – December
                today = LocalDate.now();
                minDate=today.getYear()+"-10-01  00:00:00";
                slot.setMinTime(minDate);
                maxDate=today.getYear()+"-12-31  23:59:59";
                slot.setMaxTime(maxDate);
                break;
            case currentYear:
                 localDate = LocalDate.now();
                minDate=localDate.getYear()+"-01-01  00:00:00";
                slot.setMinTime(minDate);

                slot.setMaxTime(TimeUtils.getCurTimeString());
                break;
            case lastYear:
                localDate = LocalDate.now();
                LocalDate lastYear = localDate.minusYears(1); // 2015-10-24
                minDate=lastYear.getYear()+"-01-01  00:00:00";
                maxDate=lastYear.getYear()+"-12-31  23:59:59";
                slot.setMinTime(minDate);
                slot.setMaxTime(maxDate);

                break;

        }
        System.out.println("day: "+day+" ::Slot :"+slot);

//        System.out.println("SELECT SUM(total_order_amount) as total_revenue,count(*) as total_order_count," +
//                " count( DISTINCT(customer_ref) ) as total_unique_customer,AVG(total_order_amount) as  average_order_value," +
//                " (count(*)/count( DISTINCT(customer_ref) ) ) as avg_order_per_customer ," +
//                " (SUM(total_order_amount) /count( DISTINCT(customer_ref) )) as avg_revenue_per_customer" +
//                " FROM order_info " +
//                "WHERE `order_book_date` BETWEEN '"+
//                slot.getMinTime()+"' AND '"+slot.getMaxTime()+"' and venue_ref=1;\n");
        return slot;
    }

    private static void processOrder(OrderDetails orderDetails) {
        String orderBookDateTime = orderDetails.getMetaData().getOrderBookDate();
        String orderBookDate =
                TimeUtils.getFormattedDate(orderBookDateTime, "dd/MM/yyyy HH:mm", "dd/MM/yyyy");
        long orderBookDateInMillis = TimeUtils.string2Milliseconds(orderBookDateTime, dateFormatterFull) / 1000L;
        System.out.println("orderBookDateTime: " + orderBookDateTime + " : orderBookDate : " +
                orderBookDate + " : orderEtaInMillis : " + orderBookDateInMillis);

        String orderDateTimeEta = orderDetails.getMetaData().getOrderEtaDate();
        String inquiredDate =
                TimeUtils.getFormattedDate(orderDateTimeEta, "dd/MM/yyyy HH:mm", "dd/MM/yyyy");
        String orderEtaTime=                TimeUtils.getFormattedDate(orderDateTimeEta, "dd/MM/yyyy HH:mm", "HH:mm:ss");

        long orderEtaInMillis = TimeUtils.string2Milliseconds(orderDateTimeEta, dateFormatterFull) / 1000L;
        System.out.println("orderDateTimeEta: " + orderDateTimeEta + " : inquiredDate : " +
                inquiredDate + " : orderEtaInMillis : " + orderEtaInMillis);


        Date apiTimeDate = null;
        Date currenTime = new Date();
        String currentDateString = dateFormatterFull.format(currenTime);
        try {
            currenTime = dateFormatterFull.parse(currentDateString);
        } catch (ParseException e) {
            e.printStackTrace();
        }


        try {
            apiTimeDate = dateFormatterFull.parse(orderDateTimeEta);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        if (apiTimeDate.before(currenTime)) {
            System.out.println("Do not book Order");
        } else {
            System.out.println("book Order");

        }
//
        long differenceByNow = TimeUtils.getRelativeIntervalByNow(orderDateTimeEta, ConstUtils.TimeUnit.MIN, dateFormatterFull);
        boolean currentTimePassed = false;
        if (differenceByNow < 0) {
            currentTimePassed = true;
        }
        System.out.println("differenceByNow: " + differenceByNow);
//
//    interval=    venueDetail.getCollectInterval()*60;
//

//        for (Slot slot : slotList) {
//            System.out.println("slot: " + slot);
//        }
//
//
//
        List<CartItem> cartItemList = orderDetails.getCartItems();

        Collections.sort(cartItemList, new Comparator<CartItem>() {
            @Override
            public int compare(CartItem t1, CartItem t2) {
                return t2.getPrepTimeInMins() - t1.getPrepTimeInMins();
            }
        });

//    List<CartItem> cartItemList2 = orderDetails.getCartItems();
//    List<CartItem> cartItemList3 = getCartList3();


//        Date date = new Date();
//        String newOrderTime = formatter.format(date);

        System.out.println("Order Time: "+ orderEtaTime);
        getTimings(orderEtaTime, cartItemList);


    }


    private static void getSlotList(int interval) {
        slotList = new ArrayList<>();
        int slotNumber = 1;

        for (int i = 0; i < openTimings.length; i++) {

            try {
                Date startTime = formatter1.parse(openTimings[i]);
                Date endTime = formatter1.parse(closeTimings[i]);

                while (startTime.before(endTime)) {
                    Slot tempSlot = new Slot();
                    tempSlot.setSlotNumber(slotNumber);
                    tempSlot.setTimeStamp(formatter.format(startTime));
                    //System.out.println("slotNumber:"+slotNumber+":: timeStamp"+formatter.format(startTime));

                    slotList.add(tempSlot);

                    Calendar cal = Calendar.getInstance();
                    cal.setTime(startTime);
                    cal.add(Calendar.SECOND, interval);
                    startTime = cal.getTime();
                    slotNumber++;
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
    }


//    private static void getSlotList(int interval, String dateString, boolean currentTimePassed) {
//        slotList = new ArrayList<>();
//        int slotNumber = 1;
//        Date inquiredDate = null;
//
//        try {
//            inquiredDate = dateFormatter.parse(dateString);
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
//
//        for (int i = 0; i < openTimings.length; i++) {
//
//            try {
//                Date startTime = formatter1.parse(openTimings[i]);
//                Date endTime = formatter1.parse(closeTimings[i]);
//
//                while (startTime.before(endTime)) {
//                    Slot tempSlot = new Slot();
//                    tempSlot.setSlotNumber(slotNumber);
//                    String formattedDate = dateString + " " + formatter.format(startTime);
////          System.out.println("formattedDate: "+formattedDate);
//                    try {
//                        dateFormatterFull.parse(formattedDate);
//                    } catch (ParseException e) {
//                        e.printStackTrace();
//                    }
//                    tempSlot.setTimeStampLong(inquiredDate.getTime() / 1000L);
//                    tempSlot.setTimeStamp(formatter.format(startTime));
//                    //System.out.println("slotNumber:"+slotNumber+":: timeStamp"+formatter.format(startTime));
//
//                    if (currentTimePassed) {
//                        long diff = TimeUtils.getRelativeIntervalByNow(formattedDate, ConstUtils.TimeUnit.MIN, dateFormatterFull);
//                        if (diff < 0) {
//                            System.out.println("Skip");
//                        } else {
////              System.out.println("tempSlot: "+tempSlot.toString());
////                System.out.println("INSERT INTO `1`.`service_slots` " +
////        "(`service_type`," +
////        "`day_type`," +
////        "`slot_time`," +
////        "`venue_ref`)" +
////        "VALUES" +
////        "('COLLECTION','sun',"+"'"+tempSlot.getTimeStamp()+"'"+",'1');\n");
//                            slotList.add(tempSlot);
//
//                        }
//
//                    } else {
////            System.out.println("tempSlot: "+tempSlot.toString());
////              System.out.println("INSERT INTO `1`.`service_slots` " +
////                      "(`service_type`," +
////                      "`day_type`," +
////                      "`slot_time`," +
////                      "`venue_ref`)" +
////                      "VALUES" +
////                      "('COLLECTION','sun',"+"'"+tempSlot.getTimeStamp()+"'"+",'1');\n");
//                        slotList.add(tempSlot);
//                    }
//
//                    Calendar cal = Calendar.getInstance();
//                    cal.setTime(startTime);
//                    cal.add(Calendar.SECOND, interval);
//                    startTime = cal.getTime();
//                    slotNumber++;
//                }
//            } catch (ParseException e) {
//                e.printStackTrace();
//            }
//        }
//    }

    private static void getTimings(String newOrderTime, List<CartItem> cartItemList) {
        for (int i = 0; i < cartItemList.size(); i++) {
            CartItem cartItem = cartItemList.get(i);

            Slot allotedSlot;

//            allotedSlot = getSlots(newOrderTime);
            allotedSlot = getNearestSlots(newOrderTime,cartItemList.size(),i);

            if (allotedSlot != null) {
                allotedSlot.setAllotted(true);
                allotedSlot.setOrderID(cartItem.getItemRef());
                cartItem.setAllotedSlotNo(allotedSlot.getSlotNumber());
                cartItem.setStartTime(allotedSlot.getTimeStamp());

                cartItem.setEndTime(getCompletionTime(allotedSlot.getTimeStamp(),
                        cartItem.getPrepTimeInMins()));

                cartItemList.set(i, cartItem);
                lastSelectedSlot = allotedSlot;
            }
        }

        System.out.println(cartItemList);

        nextAvailableSlotNo = lastSelectedSlot.getSlotNumber() /*+ cartItemList.size() - 1*/;
        System.out.println("nextAvailableSlotNo: " + nextAvailableSlotNo);
    }



    private static String getCompletionTime(String timeStamp, int prepTime) {
        Date startTime = null;
        try {
            startTime = formatter.parse(timeStamp);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Calendar cal = Calendar.getInstance();
        cal.setTime(startTime);
        cal.add(Calendar.MINUTE, prepTime);
        startTime = cal.getTime();
        return formatter.format(startTime);
    }

    private static Slot getNearestSlots(String newOrderTime, int size, int index) {
        for (int i = 0; i < slotList.size(); i++) {

            try {
                Date slotTime = formatter1.parse(slotList.get(i).getTimeStamp());
                Date orderTime = formatter1.parse(newOrderTime);

                if (orderTime.equals(slotTime)||orderTime.before(slotTime)) {
                    if(slotList.get(i).getOrderID()==null){
                        boolean allSlotAvailable=true;
                        for(int j=i;j<i+size-index;j++){
                            if(slotList.get(j).getOrderID()!=null){
                                System.out.println("allSlotAvailable: "+false);
                                allSlotAvailable=false;
                                break;
                            }
                        }
                        if(allSlotAvailable){
                            return slotList.get(i);
                        }
                    }

//                    lastSelectedSlot = slotList.get(i);
//                    break;
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

//    private static Slot getSlots(String newOrderTime) {
////        lastSelectedSlot, slotList, formatter1
//        if (lastSelectedSlot != null) {
//            int newSlotNumber = lastSelectedSlot.getSlotNumber();
//            lastSelectedSlot = slotList.get(newSlotNumber);
//            if(lastSelectedSlot!=null){
//                if(lastSelectedSlot.getOrderID()!=null){
//                    //order Id is allotted to slot try next slot
//                }else{
//                    //order Id is null assign it
//
//                }
//
//            }else{
//
//            }
//        } else {
//            for (int i = 0; i < slotList.size(); i++) {
//
//                try {
//                    Date slotTime = formatter1.parse(slotList.get(i).getTimeStamp());
//                    Date orderTime = formatter1.parse(newOrderTime);
//
//                    if (orderTime.equals(slotTime)) {
//                        lastSelectedSlot = slotList.get(i);
//                        break;
//                    } else if (orderTime.before(slotTime)) {
//                        lastSelectedSlot = slotList.get(i);
//
//                        break;
//                    }
//                } catch (ParseException e) {
//                    e.printStackTrace();
//                }
//            }
//        }
//
//        return lastSelectedSlot;
//    }


}
