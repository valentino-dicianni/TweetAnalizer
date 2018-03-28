import java.util.ArrayList;

public class TokenList {
    private ArrayList<String> tokens  = new ArrayList<>();

    public void addToken(String token) {
        tokens.add(token);
    }
    public String[] getTokens(){
        return (String[]) tokens.toArray();
    }

    @Override
    public String toString() {
        StringBuilder list = new StringBuilder("TOKEN LIST: ");
        for(String token : tokens){
            list.append(token).append(" | ");
        }
        return list.toString();
    }
}