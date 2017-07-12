package com.example.pujan.bag.printPackage;

import java.util.ArrayList;

/**
 * Created by Pujan on 1/6/2017.
 */
public class SetData {

    private static int[] id = {1,2,3,4,5};
    private  static String products[] = {"BD107","BFG123","BG678","BGT100","BUS016"};
    private static int quantity[] = {5,10,8,20,9};
    private static int price[] = {1000,5000,2500,1500,2700};

    public static ArrayList<PrintEntity> setItems()
    {
        ArrayList<PrintEntity> data = new ArrayList<>();

        for(int i=0;i<5;i++)
        {
            PrintEntity printEntity = new PrintEntity();

            data.add(printEntity);
        }

        return data;
    }
}
