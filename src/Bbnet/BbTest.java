package Bbnet;

public class BbTest {

    public static void main(String[] args) {
        BbNetIdGetter bbnet = new BbNetIdGetter(Language.IT);
        boolean result = bbnet.executePost("Donald Trump non mantiene le promesse e decreta nuovi dazi sul'importazionione di metalli e scorie radiattive molto pericolose per il nostro corpo");
    }

}
