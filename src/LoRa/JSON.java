package LoRa;// 0182 00f1 019a   hex
//  386 241 410     decimal

import org.json.JSONObject;

public class JSON {

    public String[] getDataFromJSON (String json){
        JSONObject obj = new JSONObject(json);
        String EUI = obj.getString("EUI");
        String hexData = obj.getString("data");
        return getDecimalData(hexData);
    }

    public String[] getDecimalData(String hex){
        String toReturn[] = new String[3];

        int values[] = new int[6];
        String tempArray[] = new String[6];
        int tempInt = 0;

            for(int i=0;i<hex.length();i+=2){
                tempArray[i/2] = hex.charAt(i) + "" + hex.charAt(i+1);
        }
            for(int i=0;i<tempArray.length;i+=2){
                tempInt = Integer.parseInt((tempArray[i]+tempArray[i+1]),16);
                //System.out.println("DEBUG 3: " + tempInt);
                values[i/2] = tempInt;
            }

            /*
            for (i = 0; i < p.length; i+=2) {
                    temp = p[i] + "" + p[i+1];
                    tempInt = parseInt(temp, 16);

                  text += tempInt + " ";
                }
             */


        double humidity = values[0];
            humidity/=10;
        double temperature = values[1];
            temperature/=10;
        int co2 = values [2];

        toReturn[0]= ""+humidity;
        toReturn[1]= ""+temperature;
        toReturn[2]= ""+co2;

        return toReturn;
    }

}