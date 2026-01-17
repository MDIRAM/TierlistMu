package tierlist;

/**
 * Class TierListData - Merepresentasikan data satu tier list
 * Menyimpan informasi tier list dan manager untuk mengelola tier-tier
 * 
 * Konsep OOP yang digunakan:
 * - Encapsulation: Data disimpan private, akses melalui getter/setter
 * - Composition: Punya aggregation dengan TierManager (has-a relationship)
 * 
 * @author Tier List Maker
 * @version 1.0
 */
public class TierListData {

    // Data tier list
    private String name;
    private String description;
    
    // Manager untuk mengelola semua tier (composition)
    private TierManager manager;

    /**
     * Constructor - membuat tier list baru dengan nama dan deskripsi
     * Secara otomatis membuat TierManager untuk mengelola tiers
     * 
     * @param name Nama tier list (contoh: "Ranking Anime 2025")
     * @param description Deskripsi/tujuan tier list (contoh: "My favorite anime")
     */
    public TierListData(String name, String description) {
        this.name = name;
        this.description = description;
        this.manager = new TierManager();  // Inisialisasi manager
    }

    /**
     * Getter untuk nama tier list
     * @return Nama tier list
     */
    public String getName() {
        return name;
    }

    /**
     * Setter untuk mengubah nama tier list
     * @param name Nama baru
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Getter untuk deskripsi tier list
     * @return Deskripsi tier list
     */
    public String getDescription() {
        return description;
    }

    /**
     * Setter untuk mengubah deskripsi tier list
     * @param description Deskripsi baru
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Getter untuk tier manager
     * @return TierManager yang mengelola tiers
     */
    public TierManager getManager() {
        return manager;
    }
}
