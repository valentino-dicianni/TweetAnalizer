package Analisis_V1;

import com.jcraft.jsch.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Scanner;


public class CoverInterface {
    private static HashMap<String, Double> bids_to_ttcs_similarity = new HashMap<>();

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

    public static void getConceptNetVector(String words){
        String cmd = "cd /home/vdicianni/conceptNet/; " + "java -jar jarTest.jar "+ words;
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
        System.out.print(json_vector);
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

    public static void resetTable(){
        bids_to_ttcs_similarity.clear();
    }

}
