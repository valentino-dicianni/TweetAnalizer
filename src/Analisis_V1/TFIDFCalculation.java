package Analisis_V1;

import Analisis_V1.utils.DocumentProperties;

import java.io.*;
import java.util.*;


public class TFIDFCalculation {
    private SortedSet<String> wordList = new TreeSet<>(String.CASE_INSENSITIVE_ORDER);

    /**
     * Calculates the Term Frequency taking the counter map of terms in all documents.
     *
     * @param inputMap the input map of terms
     * @return a new HashMap with the term frequency
     */
    public HashMap<String,Double> calculateTermFrequency(HashMap<String, Integer> inputMap) {
        HashMap<String ,Double> termFreqMap = new HashMap<>();
        double sum = 0.0;
        //Get the sum of all elements in hashmap
        for (float val : inputMap.values()) {
            sum += val;
        }

        //create a new hashMap with Tf values in it.
        for (Map.Entry<String, Integer> pair : inputMap.entrySet()) {
            double tf = pair.getValue() / sum;
            termFreqMap.put((pair.getKey()), tf);
        }
        return termFreqMap;
    }

    /**
     * Calculate the Inverse Doc Frequency
     * @param docProperties
     * @return
     */
    public HashMap<String,Double> calculateInverseDocFrequency(DocumentProperties[] docProperties) {
      HashMap<String,Double> InverseDocFreqMap = new HashMap<>();
      int size = docProperties.length;
      double wordCount;
      
      for (String word : wordList) {
          wordCount = 0;
          for (DocumentProperties docProperty : docProperties) {
              HashMap<String, Integer> tempMap = docProperty.getWordCountMap();
              if (tempMap.containsKey(word)) {
                  wordCount++;
              }
          }
          double temp = size/ wordCount;
          double idf = 1 + Math.log(temp);
          InverseDocFreqMap.put(word,idf);
      }
      return InverseDocFreqMap;
    }

    // Converts the input text file to hashmap and even dumps the final output as CSV files
    public HashMap<String, Integer> getTermsFromFile(String Filename, int count, File folder) {
        HashMap<String,Integer> WordCount = new HashMap<>();
        BufferedReader reader = null;
        HashMap<String, Integer> finalMap = new HashMap<>();
        try {
            reader = new BufferedReader(new FileReader(Filename));
            String line = reader.readLine();
            while(line!=null) {
                String[] words = line.toLowerCase().split(" ");
                for(String term : words) {

                    wordList.add(term);
                    if(WordCount.containsKey(term)) {
                        WordCount.put(term,WordCount.get(term)+1);
                    }
                    else {
                        WordCount.put(term,1);
                    }
                }
                line = reader.readLine();
            }
            // sorting the hashmap
            Map<String, Integer> treeMap = new TreeMap<>(WordCount);
            finalMap = new HashMap<>(treeMap);
        }
        catch(IOException e) {
            e.printStackTrace();
        }
        return finalMap;
    }

    //Writes the contents of hashmap to CSV file
    public void outputAsCSV(HashMap<String, Double> treeMap, String OutputPath) throws IOException {
        StringBuilder builder = new StringBuilder();
        for (Map.Entry<String, Double> keymap : treeMap.entrySet()) {
            builder.append(keymap.getKey());
            builder.append(",");
            builder.append(keymap.getValue());
            builder.append("\r\n");
        }
        String content = builder.toString().trim();
        BufferedWriter writer = new BufferedWriter(new FileWriter(OutputPath));
        writer.write(content);
        writer.close();
    }
}
