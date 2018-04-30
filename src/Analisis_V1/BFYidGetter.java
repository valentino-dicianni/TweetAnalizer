package Analisis_V1;

import Analisis_V1.utils.Language;
import Analisis_V1.utils.Term;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.Vector;

public class BFYidGetter {
    private String key = "441df9e4-7a0a-4d36-ad66-d38294c1dcd4";
    private String service_url = "https://babelfy.io/v1/disambiguate";
    private Language lang;


    public BFYidGetter(Language lang){
        this.lang = lang;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public void setLang(Language lang) {
        this.lang = lang;
    }

    public Vector<Term> executePost(String text) {
        String urlParameters   = "text="+text+"&lang="+lang.toString()+"&key="+key;
        byte[] postData        = urlParameters.getBytes( StandardCharsets.UTF_8 );
        int    postDataLength  = postData.length;
        HttpURLConnection conn;
        Vector<Term> result = new Vector<>();


        try {
            conn = connectToServer();
            conn.setRequestProperty( "Content-Length", Integer.toString( postDataLength ));

            DataOutputStream wr = new DataOutputStream( conn.getOutputStream());
            wr.write( postData );
            int responseCode = conn.getResponseCode();

            //just for check
            System.out.println("\nSending 'GET' request to URL : " + service_url);
            System.out.println("Response Code : " + responseCode);
            System.out.println("Response Message : " +conn.getResponseMessage()+'\n');

            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String inputLine;
            StringBuilder response = new StringBuilder();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
            analizeJson(text,response.toString(), result);

        } catch (IOException e) {
            e.printStackTrace();
        }

        //for testing...
        /*
        for (Term t : result) {
            System.out.println("TERM: " + t.getString() + " ID: " + t.getSysid());
        }*/

        return result;
    }

    private HttpURLConnection connectToServer() throws IOException {
        HttpURLConnection conn = null;
        URL url = new URL(service_url);
        conn = (HttpURLConnection) url.openConnection();
        conn.setDoOutput(true);
        conn.setInstanceFollowRedirects(false);
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        conn.setRequestProperty("charset", "utf-8");
        conn.setUseCaches(false);
        return conn;
    }

    private void analizeJson(String text, String response, Vector<Term> res){
        JSONArray jsonArray = null;
        try {
            jsonArray = new JSONArray(response);

            for(int i=0 ; i< jsonArray.length(); i++){
                JSONObject jsonObject = jsonArray.getJSONObject(i);

                int start = (int)(jsonObject.getJSONObject("charFragment").get("start"));
                int end = (int)(jsonObject.getJSONObject("charFragment").get("end")) + 1;

                String word = text.substring(start,end).toLowerCase();
                String sysid = jsonObject.getString("babelSynsetID");
                //System.out.println("SysID: " +jsonObject.getString("babelSynsetID")+" Fragment: "  + word);
                Term term = new Term(word, sysid);
                res.add(term);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }








}
