package Analisis_V1;

import com.jcraft.jsch.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;


public class TTCSInterface {

    private static HashMap<String, Double> bids_to_ttcs_similarity = new HashMap<>();


    public static void launch(String couples) throws IOException {
        String user = "vdicianni";
        String password = "Tavol15pork1";
        String host = "ground.di.unito.it";
        int port=22;


        try {
            JSch jsch = new JSch();
            Session session = jsch.getSession(user, host, port);
            session.setPassword(password);
            session.setConfig("StrictHostKeyChecking", "no");
            session.connect();


            Channel channel = session.openChannel("exec");
            ((ChannelExec)channel).setCommand("cd /home/vdicianni/NL@DipInfo_project/NL@DipInfo/launchers/; ./VSP -similarity -cjson -metric=R0 -input=\""+couples+"\"");
            channel.connect();

            BufferedReader is = new BufferedReader(new InputStreamReader(channel.getInputStream()));
            String line;
            String json_distances = "";

            while ((line = is.readLine()) != null) {
                if (line.contains("JSON-OUTPUT => ")) {
                    json_distances = line.split("JSON-OUTPUT => ")[1].replaceAll("\\{", "").replaceAll("}", "");
                    break;
                }
            }

            String[] similarities = json_distances.split("],");
            for (String one_score : similarities) {
                one_score = one_score.replaceAll("\\[", "").replaceAll("]", "");
                String[] elements = one_score.split(",");
                //log.debug(Arrays.toString(elements));
                String bsi1 = elements[0];
                String bsi2 = elements[1];
                double score = 0.0;
                if (!elements[2].equals("undefined") && !elements[2].equals("unfair")) {
                    score = Double.parseDouble(elements[2]);
                }
                bids_to_ttcs_similarity.put(bsi1 + "_" + bsi2, score);
            }

            channel.disconnect();
            session.disconnect();

        } catch (JSchException e) {
            e.printStackTrace();
        }

    }

    public static void print() {
        for (String s : bids_to_ttcs_similarity.keySet()) {
            System.out.println(s.split("_")[0] + " " + s.split("_")[1] + " " + bids_to_ttcs_similarity.get(s));
        }
    }
}
