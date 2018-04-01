public interface Dictionary {

    boolean putWord(String word);
    boolean containsWord(String word);
    int getIndex(String word);
    void printDictionary();
    public int getSize();
}
