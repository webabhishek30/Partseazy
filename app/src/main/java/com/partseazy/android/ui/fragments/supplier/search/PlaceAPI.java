package com.partseazy.android.ui.fragments.supplier.search;

import com.partseazy.android.Logger.CustomLogger;
import com.partseazy.android.network.request.WebServiceConstants;
import com.partseazy.android.ui.model.supplier.placeAutocomplete.PlacesResult;
import com.partseazy.android.ui.model.supplier.placeAutocomplete.Prediction;
import com.google.gson.Gson;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by naveen on 6/9/17.
 */

public class PlaceAPI {


    private static final String TAG = PlaceAPI.class.getSimpleName();


    public PlacesResult searchPlaces(String input)
    {
        PlacesResult placesResult = null;
        try
        {
            String callingUrl = WebServiceConstants.AUTOCOMPLETE_PLACE;
            callingUrl = callingUrl+"&input="+ URLEncoder.encode(input, "utf8");
            StringBuilder sb = new StringBuilder(callingUrl);
            sb.append("&types=(regions)");
            sb.append("&components=country:ind");
            CustomLogger.d("Place Url  :: "+sb.toString() );
            String resultjson = HttpGetRequest(sb.toString());
            //  CustomLogger.d("resultJson ::"+resultjson);
            placesResult = new Gson().fromJson(resultjson, PlacesResult.class);
        }catch (Exception e)
        {
            CustomLogger.e("Exception:-"+e);
        }
        return placesResult;
    }

    public List<Prediction> autoSuggestionPlaces(String input )
    {
        List<Prediction> resultList = new ArrayList<>();
        PlacesResult placesResult = new PlacesResult();
        placesResult  = searchPlaces(input);
        {
            if(placesResult!=null && placesResult.predictions!=null && placesResult.predictions.size()>0)
            {
                resultList.addAll(placesResult.predictions);
            }
        }
        return  resultList;
    }



    public static String HttpGetRequest(String urlString) throws IOException {
        HttpURLConnection conn = null;
        StringBuilder jsonResults = new StringBuilder();
        try {
            URL url = new URL(urlString);
            conn = (HttpURLConnection) url.openConnection();
            InputStreamReader in = new InputStreamReader(conn.getInputStream());
            int read;
            char[] buff = new char[1024];
            while ((read = in.read(buff)) != -1) {
                jsonResults.append(buff, 0, read);
            }
        } catch (IOException e) {
            CustomLogger.e("Exception :- ",e);
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }
        return jsonResults.toString();
    }
}
