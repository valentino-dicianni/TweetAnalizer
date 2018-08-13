package Analisis_V1;

import com.jcraft.jsch.*;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Scanner;
import java.util.Vector;
import java.util.stream.Collectors;


public class CoverInterface {
    private static HashMap<String, Double> bids_to_ttcs_similarity = new HashMap<>();
    private static HashMap<String, Integer> lostWordsPerSentence = new HashMap<>();

    private static String launch(String cmd) {
        String usr = "vdicianni";
        String psswd = "Tavol15pork1";
        String host = "ground.di.unito.it";
        int port = 22;

        StringBuilder sb = null;
        try {
            JSch jsch = new JSch();
            Session session = jsch.getSession(usr, host, port);
            session.setPassword(psswd);
            session.setConfig("StrictHostKeyChecking", "no");
            session.connect();

            Channel channel = session.openChannel("exec");
            ((ChannelExec) channel).setCommand(cmd);
            channel.connect();

            BufferedReader reader = new BufferedReader(new InputStreamReader(channel.getInputStream()));
            sb = new StringBuilder();

            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line).append("\n");
            }

            reader.close();
            channel.disconnect();
            session.disconnect();

        } catch (JSchException | IOException e) {
            e.printStackTrace();
        }

        return sb != null ? sb.toString() : null;
    }

    public static Vector<Double> getConceptNetVector(String sentence){
        String args = sentence.replaceAll(" ", ",");

        String cmd = "cd /home/vdicianni/conceptNet/; " + "java -jar jarTest.jar "+ args;
        String json_vector = null;
        String result = launch(cmd);

        assert result != null;
        Scanner scanner = new Scanner(result);

        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            if (line.contains("[{\"")) {
                json_vector = line;
                break;
            }
        }
        scanner.close();
        return calculateVectorMedia(json_vector, sentence);
    }

    private static Vector<Double> calculateVectorMedia(String json_vector, String sentence){
        Vector<Double> tempVector = new Vector<>();
        Vector<Double> conceptNetVector = null;
        int lostWords = 0;

        try {
            JSONArray jsonArray = new JSONArray(json_vector);

            for(int i=0 ; i< jsonArray.length() - 1; i++){
                JSONObject jsonObject = jsonArray.getJSONObject(i);

                if(jsonObject.get("vector")  != JSONObject.NULL) {
                    JSONArray cnVector = (JSONArray) jsonObject.get("vector");
                    for(int j = 0; j < cnVector.length(); j++){
                        if(tempVector.size() > j)
                            tempVector.set(j, tempVector.get(j) + (double)cnVector.get(j));

                        else tempVector.add(j, (double)cnVector.get(j));
                    }
                }
                else{
                    lostWords++;
                }
            }

            //stats for lost words not in conceptNet
            int finalLostWords = lostWords;
            lostWordsPerSentence.put(sentence,lostWords);

            //mapping vector media on jsonArray.length() - (lostWords + 1)
            conceptNetVector = tempVector.stream()
                    .map(n -> n/(jsonArray.length() - (finalLostWords + 1)))
                    .collect(Collectors.toCollection(Vector::new));


        } catch (JSONException e) {
            e.printStackTrace();
        }

        assert conceptNetVector != null;
        return conceptNetVector;
    }

    /**
     * Calculate the concept similarity between the couples in input and stores the
     * results in the HashMap {@code bids_to_ttcs_similarity}
     *
     * @param couples list of bbNet_sysID to compare for concept similarity in Cover
     */

    public static void calculateConceptSimilarity(String couples){
        String json_distances = "";
        String command = "cd /home/vdicianni/NL@DipInfo_project/NL@DipInfo/launchers/; " +
                "./VSP -similarity -cjson -metric=R0 -input=\""+couples+"\"";


        String result = launch(command);
        assert result != null;
        Scanner scanner = new Scanner(result);

        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            if (line.contains("JSON-OUTPUT => ")) {
                json_distances = line.split("JSON-OUTPUT => ")[1].replaceAll("\\{", "").replaceAll("}", "");
                break;
            }
        }
        scanner.close();

        String[] similarities = json_distances.split("],");
        for (String one_score : similarities) {
            one_score = one_score.replaceAll("\\[", "").replaceAll("]", "");
            String[] elements = one_score.split(",");
            String bsi1 = elements[0];
            String bsi2 = elements[1];
            double score = 0.0;
            if (!elements[2].equals("undefined") && !elements[2].equals("unfair")) {
                score = Double.parseDouble(elements[2]);
            }
            bids_to_ttcs_similarity.put(bsi1 + "_" + bsi2, score);
        }

    }

    public static double getScore(String key){
        return bids_to_ttcs_similarity.get(key);
    }

    public static double getLostWords(String key){
        return lostWordsPerSentence.get(key);
    }

    public static void resetSimTable(){
        bids_to_ttcs_similarity.clear();
    }

}
