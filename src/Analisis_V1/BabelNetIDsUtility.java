package Analisis_V1;

import Analisis_V1.utils.Concept;
import Analisis_V1.utils.Language;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Vector;

public class BabelNetIDsUtility {
    //private String key = "441df9e4-7a0a-4d36-ad66-d38294c1dcd4";
    //private String key = "27ca9b69-a220-4173-be9d-ce8ff3826080";
    private String key = "c4bc4acd-eb8b-4794-88b8-b230156e6fe6";
    private String service_url = "https://babelfy.io/v1/disambiguate";
    private Language lang;

    public BabelNetIDsUtility(Language lang){
        this.lang = lang;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public void setLang(Language lang) {
        this.lang = lang;
    }

    /**
     * Send a GET request to Babelfy with an input text and returns a list of concepts
     * with the corresponding sysID.
     *
     * @param text in input to Babelfy
     * @return a vector of {@code Concept} from the text
     */
    public Vector<Concept> executePost(String text) {
        String urlParameters   = "text="+text+"&lang="+lang.toString()+"&key="+key;
        byte[] postData        = urlParameters.getBytes( StandardCharsets.UTF_8 );
        int    postDataLength  = postData.length;
        Vector<Concept> result = new Vector<>();
        HttpURLConnection conn;

        try {

            conn = connectToServer();
            conn.setRequestProperty( "Content-Length", Integer.toString( postDataLength ));
            DataOutputStream wr = new DataOutputStream( conn.getOutputStream());
            wr.write( postData );

            if (conn.getResponseCode() != 200){
                throw new Exception("Bad response code");
            }

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
        } catch (Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
            return null;
        }
        return result;
    }

    /**
     * ConnectToServer utility for connection to Babelfy.
     *
     * @return a {@code HttpURLConnection}
     * @throws IOException if connection doesn't work
     */
    private HttpURLConnection connectToServer() throws IOException {
        HttpURLConnection conn;
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

    /**
     * Analise a JSON string, creates the concepts and store them into a result vector.
     *
     * @param text the text analised by Babelnet
     * @param response the response in JSON format from Babelnet
     * @param res is the results vector
     */
    private void analizeJson(String text, String response, Vector<Concept> res){
        JSONArray jsonArray;
        try {
            jsonArray = new JSONArray(response);

            for(int i=0; i < jsonArray.length(); i++){
                JSONObject jsonObject = jsonArray.getJSONObject(i);

                int start = (int)(jsonObject.getJSONObject("charFragment").get("start"));
                int end = (int)(jsonObject.getJSONObject("charFragment").get("end")) + 1;

                String word = text.substring(start,end).toLowerCase();
                String sysid = jsonObject.getString("babelSynsetID");
                Concept concept = new Concept(word, sysid);
                if(!res.contains(concept))
                    res.add(concept);
            }
        } catch (JSONException e) {
           System.out.println("ERROR: "+ response);

        }
    }
}
