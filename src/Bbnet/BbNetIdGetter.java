package Bbnet;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;

public class BbNetIdGetter {
    private String key = "441df9e4-7a0a-4d36-ad66-d38294c1dcd4";
    private String service_url = "https://babelfy.io/v1/disambiguate";
    private Language lang;


    BbNetIdGetter(Language lang){
        this.lang = lang;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public void setLang(Language lang) {
        this.lang = lang;
    }



    boolean executePost(String text) {
        String urlParameters   = "text="+text+"&lang="+lang.toString()+"&key="+key;
        byte[] postData        = urlParameters.getBytes( StandardCharsets.UTF_8 );
        int    postDataLength  = postData.length;
        HttpURLConnection conn = null;

        try {
            URL url = new URL( service_url );
            conn= (HttpURLConnection) url.openConnection();
            conn.setDoOutput( true );
            conn.setInstanceFollowRedirects( false );
            conn.setRequestMethod( "POST" );
            conn.setRequestProperty( "Content-Type", "application/x-www-form-urlencoded");
            conn.setRequestProperty( "charset", "utf-8");
            conn.setRequestProperty( "Content-Length", Integer.toString( postDataLength ));
            conn.setUseCaches( false );

            DataOutputStream wr = new DataOutputStream( conn.getOutputStream());
            wr.write( postData );
            int responseCode = conn.getResponseCode();
            System.out.println("\nSending 'GET' request to URL : " + service_url);
            System.out.println("Response Code : " + responseCode);
            System.out.println("Response Message : " +conn.getResponseMessage()+'\n');

            BufferedReader in = new BufferedReader(
                    new InputStreamReader(conn.getInputStream()));
            String inputLine;
            StringBuilder response = new StringBuilder();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            JSONArray jsonArray = new JSONArray(response.toString());
            for(int i=0 ; i< jsonArray.length(); i++){
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                System.out.println("SysID: " +jsonObject.getString("babelSynsetID"));
            }

        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }

        return true;
    }


}
