package tierlist;

/**
 * Class STier - Merepresentasikan tier S (tier tertinggi)
 * Merupakan concrete implementation dari abstract class Tier
 * 
 * Konsep OOP: Inheritance - meng-extend Tier dan implement getTierName()
 */
public class STier extends Tier {
    
    /**
     * Override method abstrak getTierName dari parent class
     * @return String "S"
     */
    @Override
    public String getTierName() {
        return "S";
    }
}
