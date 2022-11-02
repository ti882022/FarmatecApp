package com.farmatecalphaversion;

import org.json.JSONException;
import org.json.JSONObject;

public class ConvertJarrtoJobj {

    public static void main(String[] args) {
        String myJSON = "{\"name\":\"studytonight\",\"address\":\"Noida\"}";
        try {
            JSONObject jsonObject = new JSONObject(myJSON);
            System.out.println("JSON Object: " + jsonObject);
        } catch (JSONException e) {
            System.out.println("Error " + e.toString());
        }
    }
}