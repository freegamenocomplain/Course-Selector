import java.util.HashMap;

public class Club {
    boolean scheduled;
    int memberCount;
    double worstOverlap;
    String name;
    HashMap<String, Double> relations;

    Club(String name) {
        this.name = name;
        this.scheduled = false;
        this.relations = new HashMap<>();
    }
}
