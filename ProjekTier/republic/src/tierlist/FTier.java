package tierlist;

/**
 * Class FTier - Merepresentasikan tier F (tier terendah)
 * Merupakan concrete implementation dari abstract class Tier
 * 
 * Konsep OOP: Inheritance - meng-extend Tier dan implement getTierName()
 */
public class FTier extends Tier {
    
    /**
     * Override method abstrak getTierName dari parent class
     * @return String "F"
     */
    @Override
    public String getTierName() {
        return "F";
    }
}
