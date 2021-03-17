package com.jain.lib;


import com.google.gson.Gson;
import com.jain.lib.model.CartInfo;
import com.jain.lib.model.MenuItem;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

public class MyClasss {
    //Will come from service hours or opening hour table
    private static final int orderPerHour = 20;
    private static final String[] openTimings = { "10:00", "16:00" };
    private static final String[] closeTimings = { "14:00", "23:59" };
    //private static boolean isPending=false;
    private static Slot lastSelectedSlot;
    private static int nextAvailableSlotNo;
    private static SimpleDateFormat formatter;
    private static SimpleDateFormat formatter1;
    private static List<Slot> slotList;

    private static DecimalFormat df = new DecimalFormat("0.00");
    public static void main(String[] args) {

        formatter = new SimpleDateFormat("HH:mm:ss");
        formatter1 = new SimpleDateFormat("HH:mm");
        int interval = 60 * 60 / orderPerHour;

        getSlotList(interval);
        System.out.println("Size:" + slotList.size());

        List<CartItem> cartItemList = getCartList();
        List<CartItem> cartItemList2 = getCartList2();
        List<CartItem> cartItemList3 = getCartList3();


        Date date = new Date();
        String newOrderTime = formatter.format(date);

        System.out.println("Time 1 Order :" + newOrderTime);
        getTimings(newOrderTime, cartItemList);

        //Next order is placed with in 2 minutes
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.MINUTE, 2);
        date = cal.getTime();
        newOrderTime = formatter.format(date);
        System.out.println("Time 2 Order :" + newOrderTime);
        getTimings(newOrderTime, cartItemList2);

        //Next order is placed with in second shift set lastselectedslot to null,
//        lastSelectedSlot = null;



        cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.HOUR_OF_DAY, 23);
        cal.set(Calendar.MINUTE, 13);
        cal.set(Calendar.SECOND, 0);

        date = cal.getTime();
        newOrderTime = formatter.format(date);

//        if(lastSelectedSlot.getTimeStamp())

        System.out.println("Time 3 Order :" + newOrderTime);
        try {
          getTimings(newOrderTime, cartItemList3);
        } catch (Exception e) {
          e.printStackTrace();
        }


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

    private static void getTimings(String newOrderTime, List<CartItem> cartItemList) {
        for (int i = 0; i < cartItemList.size(); i++) {
            CartItem cartItem = cartItemList.get(i);

            Slot allotedSlot;

            allotedSlot = getSlots(newOrderTime);

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
            @Override public int compare(CartItem t1, CartItem t2) {
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
            @Override public int compare(CartItem t1, CartItem t2) {
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
            @Override public int compare(CartItem t1, CartItem t2) {
                return t2.getPrepTime() - t1.getPrepTime();
            }
        });

        return cartItemList;
    }

    private static Slot getSlots(String newOrderTime) {

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
