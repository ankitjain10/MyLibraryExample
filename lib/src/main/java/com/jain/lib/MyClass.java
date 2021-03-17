package com.jain.lib;

import com.google.gson.Gson;
import com.jain.lib.model.VenueDetails;

import java.sql.Time;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

public class MyClass {
    //Will come from service hours or opening hour table
    private static final int orderPerHour = 20;
    private static final String[] openTimings = {"01:30", "16:00"};
    private static final String[] closeTimings = {"11:30", "23:30"};
    //private static boolean isPending=false;
    private static Slot lastSelectedSlot;
    private static int nextAvailableSlotNo;
    private static SimpleDateFormat formatter;
    private static SimpleDateFormat formatter1;
    private static SimpleDateFormat dateFormatter;
    private static SimpleDateFormat dateFormatterFull;
    private static List<Slot> slotList;


    private static final DecimalFormat df = new DecimalFormat("0.00");

    public static void main(String[] args) {

        VenueDetails venueDetail = new Gson().fromJson(Sample.venueDetails, VenueDetails.class);
//    df.setRoundingMode(RoundingMode.UP);
//
//    CartInfo cartInfo=new Gson().fromJson(Sample.cartData,CartInfo.class);
//    double itemValue=0.00;
//    double itemVAT=0.00;
//    double addonValue=0.00;
//    double addonTax=0.00;
//
//    double itemTotal=.00;
//
//    if(cartInfo!=null&&cartInfo.getCartItems()!=null&&cartInfo.getCartItems().size()>0){
//      for(MenuItem item: cartInfo.getCartItems()){
//        double addOnValues=item.getAddOnBaseTotal();
//        double addOnTax=item.getAddOnTax();
//
//
//
//        double subtotal=item.getItemBaseCost();
//        double vatValue=item.getItemVatAmount();
//        System.out.println("Item: "+item.getItemName()+" : subtotal: " +subtotal+
//            " : vatValue: "+vatValue+" :addOnValues :"+addOnValues+ " :addOnTax: "+addOnTax);
//        itemValue+=(subtotal+addOnValues);
//        itemVAT+=(vatValue+addOnTax);
//      }
//
//      itemTotal=itemValue+itemVAT;
//
//
//      System.out.println("Pre itemValue: "+itemValue+
//              " : itemVAT: "+itemVAT+": itemTotal: "+itemTotal);
//
//
//      BigDecimal bd = new BigDecimal(itemValue).setScale(2, RoundingMode.HALF_UP);
//      double salary = bd.doubleValue();
//      System.out.println("itemValue : " + salary);
//       bd = new BigDecimal(itemVAT).setScale(2, RoundingMode.HALF_UP);
//      salary = bd.doubleValue();
//      System.out.println("itemVAT : " + salary);
//       bd = new BigDecimal(itemTotal).setScale(2, RoundingMode.HALF_UP);
//       salary = bd.doubleValue();
//      System.out.println("itemTotal : " + salary);
//
//
//      //BigDecimal bd = new BigDecimal(itemTotal).setScale(2, RoundingMode.HALF_UP);
//      //double salary = bd.doubleValue();
//
//
//      System.out.println("itemValue: "+df.format(itemValue)+
//          " : itemVAT: "+df.format(itemVAT)+": itemTotal: "+df.format(itemTotal));
//
//      //long valueEMPWrong= (long) (itemTotal*100);
////      System.out.println("No round off gives wrong result valueEMP: "+valueEMPWrong);
//      long valueEMP=Math.round(itemTotal*100);
//      System.out.println("After round off/ceil valueEMP: "+valueEMP);
//
//    }

        //HashMap<String,String> debugKeys=new HashMap<>();
        //debugKeys.put("1","abc1");
        //debugKeys.put("2","abc2");
        //debugKeys.put("3","abc3");
        //debugKeys.put("4","abc4");
        //debugKeys.put("5","abc5");
        //
        //String value=debugKeys.get("4");
        //System.out.println("key value for 4: "+value);
        //
        // value=debugKeys.get("6");
        //System.out.println("key value for 6: "+value);
        //
        //
        //HashMap<String,String> releaseKeys=new HashMap<>();
        //debugKeys.put("1","xyz1");
        //debugKeys.put("2","xyz2");
        //debugKeys.put("3","xyz3");
        //debugKeys.put("4","xyz4");
        //debugKeys.put("5","xyz5");
        //
        //
        formatter = new SimpleDateFormat("HH:mm");
        formatter1 = new SimpleDateFormat("HH:mm");
        dateFormatterFull = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        dateFormatter = new SimpleDateFormat("dd/MM/yyyy");


        String bookingDateTime=TimeUtils.getCurTimeString(dateFormatter);
        long differenceByNow = TimeUtils.getRelativeIntervalByNow(bookingDateTime, ConstUtils.TimeUnit.DAY, dateFormatter);
        boolean currentTimePassed = false;
        if (differenceByNow <= 0) {
            currentTimePassed = true;
        }
        //In Seconds
        int interval = venueDetail.getCollectInterval() * 60;
        try {
            getSlotList(interval, bookingDateTime, currentTimePassed);
        } catch (Exception e) {
            e.printStackTrace();
        }
//
//
//    System.out.println("Size:" + slotList.size());
////
//    List<CartItem> cartItemList = getCartList();
//    List<CartItem> cartItemList2 = getCartList2();
//    List<CartItem> cartItemList3 = getCartList3();
//
//
//    Date date = new Date();
//    String newOrderTime = formatter.format(date);
//
//    System.out.println("Time 1 Order :" + newOrderTime);
//    getTimings(newOrderTime, cartItemList);
////
//    //Next order is placed with in 2 minutes
//    Calendar cal = Calendar.getInstance();
//    cal.setTime(date);
//    cal.add(Calendar.MINUTE, 2);
//    date = cal.getTime();
//    newOrderTime = formatter.format(date);
//    System.out.println("Time 2 Order :" + newOrderTime);
//    getTimings(newOrderTime, cartItemList2);
//
//    //Next order is placed with in second shift set lastselectedslot to null,
//    lastSelectedSlot = null;
//
//    cal = Calendar.getInstance();
//    cal.setTime(date);
//    cal.set(Calendar.HOUR_OF_DAY, 17);
//    cal.set(Calendar.MINUTE, 14);
//    cal.set(Calendar.SECOND, 0);
//
//    date = cal.getTime();
//    newOrderTime = formatter.format(date);
//
//    System.out.println("Time 3 Order :" + newOrderTime);
//    try {
//      getTimings(newOrderTime, cartItemList3);
//    } catch (Exception e) {
//      e.printStackTrace();
//    }


    }

    private static void getSlotList(int interval, String dateString, boolean currentDate) {
        slotList = new ArrayList<>();
        int slotNumber = 1;
        Date inquiredDate = null;

        try {
            inquiredDate = dateFormatter.parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        for (int i = 0; i < openTimings.length; i++) {

            try {
                Date startTime = formatter1.parse(openTimings[i]);
                Date endTime = formatter1.parse(closeTimings[i]);

                while (startTime.before(endTime)) {
                    Slot tempSlot = new Slot();
                    tempSlot.setSlotNumber(slotNumber);
                    String formattedDate = dateString + " " + formatter.format(startTime);
//          System.out.println("formattedDate: "+formattedDate);
                    try {
                        dateFormatterFull.parse(formattedDate);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    tempSlot.setTimeStampLong(inquiredDate.getTime() / 1000L);
                    tempSlot.setTimeStamp(formatter.format(startTime));
                    //System.out.println("slotNumber:"+slotNumber+":: timeStamp"+formatter.format(startTime));

                    if (currentDate) {
                        long diff = TimeUtils.getRelativeIntervalByNow(formattedDate, ConstUtils.TimeUnit.MIN, dateFormatterFull);
                        if (diff < 0) {
                            System.out.println("Skip");
                        } else {
                            System.out.println("tempSlot: " + tempSlot.toString());
//                System.out.println("INSERT INTO `1`.`service_slots` " +
//        "(`service_type`," +
//        "`day_type`," +
//        "`slot_time`," +
//        "`venue_ref`)" +
//        "VALUES" +
//        "('COLLECTION','sun',"+"'"+tempSlot.getTimeStamp()+"'"+",'1');\n");
                            slotList.add(tempSlot);

                        }

                    } else {
                        System.out.println("tempSlot: " + tempSlot.toString());
//              System.out.println("INSERT INTO `1`.`service_slots` " +
//                      "(`service_type`," +
//                      "`day_type`," +
//                      "`slot_time`," +
//                      "`venue_ref`)" +
//                      "VALUES" +
//                      "('COLLECTION','sun',"+"'"+tempSlot.getTimeStamp()+"'"+",'1');\n");
                        slotList.add(tempSlot);
                    }

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

    private static void getTimings(String newOrderTime, List<CartItem> cartItemList) {
        for (int i = 0; i < cartItemList.size(); i++) {
            CartItem cartItem = cartItemList.get(i);

            Slot allotedSlot;

            allotedSlot = getSlots(newOrderTime, lastSelectedSlot, slotList, formatter1);

            if (allotedSlot != null) {
                cartItem.setAllotedSlotNo(allotedSlot.getSlotNumber());
                cartItem.setStartTime(allotedSlot.getTimeStamp());

                cartItem.setEndTime(getCompletionTime(allotedSlot.getTimeStamp(),
                        cartItem.getPrepTime()));

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

    private static List<CartItem> getCartList() {
        List<CartItem> cartItemList = new ArrayList<>();
        cartItemList.add(new CartItem(1, 20));
        cartItemList.add(new CartItem(2, 45));
        cartItemList.add(new CartItem(3, 15));

        Collections.sort(cartItemList, new Comparator<CartItem>() {
            @Override
            public int compare(CartItem t1, CartItem t2) {
                return t2.getPrepTime() - t1.getPrepTime();
            }
        });

        return cartItemList;
    }

    private static List<CartItem> getCartList2() {
        List<CartItem> cartItemList = new ArrayList<>();
        cartItemList.add(new CartItem(1, 10));
        cartItemList.add(new CartItem(2, 5));
        cartItemList.add(new CartItem(3, 15));
        cartItemList.add(new CartItem(4, 5));

        //sort according prep time in descending order
        Collections.sort(cartItemList, new Comparator<CartItem>() {
            @Override
            public int compare(CartItem t1, CartItem t2) {
                return t2.getPrepTime() - t1.getPrepTime();
            }
        });

        return cartItemList;
    }

    private static List<CartItem> getCartList3() {
        List<CartItem> cartItemList = new ArrayList<>();
        cartItemList.add(new CartItem(2, 10));
        cartItemList.add(new CartItem(3, 45));
        cartItemList.add(new CartItem(1, 10));
        cartItemList.add(new CartItem(4, 5));

        //sort according prep time in descending order
        Collections.sort(cartItemList, new Comparator<CartItem>() {
            @Override
            public int compare(CartItem t1, CartItem t2) {
                return t2.getPrepTime() - t1.getPrepTime();
            }
        });

        return cartItemList;
    }

    private static Slot getSlots(String newOrderTime,
                                 Slot lastSelectedSlot, List<Slot> slotList, SimpleDateFormat formatter1) {

        if (lastSelectedSlot != null) {
            int newSlotNumber = lastSelectedSlot.getSlotNumber();
            lastSelectedSlot = slotList.get(newSlotNumber);
        } else {
            for (int i = 0; i < slotList.size(); i++) {

                try {
                    Date slotTime = formatter1.parse(slotList.get(i).getTimeStamp());
                    Date orderTime = formatter1.parse(newOrderTime);

                    if (orderTime.equals(slotTime)) {
                        lastSelectedSlot = slotList.get(i);
                        break;
                    } else if (orderTime.before(slotTime)) {
                        lastSelectedSlot = slotList.get(i);

                        break;
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        }

        return lastSelectedSlot;
    }


}
