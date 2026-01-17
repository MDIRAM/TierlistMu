package tierlist;

/**
 * Class ATier - Merepresentasikan tier A
 * Merupakan concrete implementation dari abstract class Tier
 * 
 * Konsep OOP: Inheritance
 */
public class ATier extends Tier {
    
    @Override
    public String getTierName() {
        return "A";
    }
}
