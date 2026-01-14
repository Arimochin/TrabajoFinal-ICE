import java.util.Random;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
        String filePath1 = "resources/br17.atsp";
        String filePath2 = "resources/p43.atsp";
        int [][] m = ATSPReader.init(filePath1);
        TSP.init(m);

    }
}