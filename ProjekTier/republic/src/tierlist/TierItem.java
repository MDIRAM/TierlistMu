package tierlist;

/**
 * Class TierItem - Merepresentasikan satu item/object yang akan dirangking
 * Contoh: Anime, Game, Karakter, dll
 * 
 * Konsep OOP yang digunakan:
 * - Encapsulation: Properties di-private, akses melalui getter/setter
 * 
 * @author Tier List Maker
 * @version 1.0
 */
public class TierItem {

    // Private fields - data terlindungi
    private String name;
    private String imagePath;

    /**
     * Constructor dengan nama item saja
     * @param name Nama item (contoh: "One Piece", "Valorant", dll)
     */
    public TierItem(String name) {
        this.name = name;
        this.imagePath = null;
    }

    /**
     * Constructor dengan nama dan path gambar
     * @param name Nama item
     * @param imagePath Path file gambar item
     */
    public TierItem(String name, String imagePath) {
        this.name = name;
        this.imagePath = imagePath;
    }

    /**
     * Getter untuk nama item
     * @return Nama item
     */
    public String getName() {
        return name;
    }

    /**
     * Getter untuk path gambar item
     * @return Path gambar, atau null jika tidak ada
     */
    public String getImagePath() {
        return imagePath;
    }

    /**
     * Setter untuk mengubah path gambar item
     * @param imagePath Path file gambar baru
     */
    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }
}

