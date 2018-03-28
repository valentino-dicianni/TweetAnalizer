import java.util.HashSet;

public interface TextReader {
    public HashSet<String> parseFile(String path);
}