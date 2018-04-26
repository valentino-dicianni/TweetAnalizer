package Analisis_V1.tests;

public class TFIDFtest {
/*
    public static void main(String[] args) {
        int count = 0;
        TFIDFCalculation TfidfObj = new TFIDFCalculation();

        File folder = new File("/Users/mac/IdeaProjects/TwitterAnalizer/corpus/press");

        // loops over files available in the path except for hidden files.
        File[] listOfFiles = folder.listFiles(file -> !file.isHidden());
        int noOfDocs = 0;
        if (listOfFiles != null) {

            noOfDocs = listOfFiles.length;
            System.out.println(noOfDocs);

            DocumentProperties[] docProperties = new DocumentProperties[noOfDocs];

            for (File file : listOfFiles) {
                if (file.isFile()) {
                    docProperties[count] = new DocumentProperties();
                    HashMap<String,Integer> wordCount = TfidfObj.getTermsFromFile(file.getAbsolutePath(),count, folder);
                    docProperties[count].setWordCountMap(wordCount);
                    HashMap<String,Double> termFrequency = TfidfObj.calculateTermFrequency(docProperties[count].getWordCountMap());
                    docProperties[count].setTermFreqMap(termFrequency);
                    count++;
                }
            }

            //Calculatin IF
            HashMap<String,Double> inverseDocFreqMap = TfidfObj.calculateInverseDocFrequency(docProperties);


            //CalculatingIDF
            count = 0;
            for (File file : listOfFiles) {
                if (file.isFile()) {
                    HashMap<String,Double> tfIDF = new HashMap<>();
                    double tfIdfValue = 0.0;
                    double idfVal = 0.0;
                    HashMap<String,Double> tf = docProperties[count].getTermFreqMap();
                    for (Object o : tf.entrySet()) {
                        Map.Entry pair = (Map.Entry) o;
                        double tfVal = (Double) pair.getValue();
                        if (inverseDocFreqMap.containsKey((String) pair.getKey())) {
                            idfVal = inverseDocFreqMap.get((String) pair.getKey());
                        }
                        tfIdfValue = tfVal * idfVal;
                        tfIDF.put((pair.getKey().toString()), tfIdfValue);
                    }
                    int fileNameNumber = (count+1);
                    String OutPutPath = "/Users/mac/IdeaProjects/TwitterAnalizer/corpus/csvOutput/"+file.getName()+".csv";

                    try {
                        TfidfObj.outputAsCSV(tfIDF,OutPutPath);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    count++;
                }
            }
        }
        System.out.print("DONE");
    }
 */
}
