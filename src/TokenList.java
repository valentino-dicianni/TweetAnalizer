import java.util.ArrayList;

public class TokenList {
    private ArrayList<String> tokens  = new ArrayList<>();

    public void addToken(String token) {
        tokens.add(token);
    }
    public String[] getTokens(){
        String[] res= new String[tokens.size()];
        for (int i = 0; i<res.length; i++) {
            res[i] = tokens.get(i);
        }
        return res;
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