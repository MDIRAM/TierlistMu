package tierlist;

/**
 * Class TierManager - Memanage/mengelola semua tier dalam tier list
 * Bertanggung jawab untuk:
 * - Menyimpan referensi ke semua tier (S, A, B, C, D, F)
 * - Mendapatkan tier berdasarkan nama
 * - Memindahkan item antar tier
 * - Menampilkan semua tier
 * 
 * Konsep OOP yang digunakan:
 * - Composition: Punya multiple Tier objects (has-a relationship)
 * - Polymorphism: Treat semua tier sebagai Tier type
 * 
 * @author Tier List Maker
 * @version 1.0
 */
public class TierManager {

    // Reference ke masing-masing tier (6 tier: S, A, B, C, D, F)
    private Tier s = new STier();
    private Tier a = new ATier();
    private Tier b = new BTier();
    private Tier c = new CTier();
    private Tier d = new DTier();
    private Tier f = new FTier();

    /**
     * Mendapatkan tier berdasarkan nama/huruf
     * @param name Nama tier (S, A, B, C, D, atau F)
     * @return Tier object yang sesuai, atau null jika nama tidak valid
     */
    public Tier getTier(String name) {
        switch (name.toUpperCase()) {
            case "S": return s;
            case "A": return a;
            case "B": return b;
            case "C": return c;
            case "D": return d;
            case "F": return f;
            default: return null;
        }
    }

    /**
     * Menampilkan semua tier dan item-itemnya ke console
     */
    public void showAll() {
        System.out.println("\n===== TIER LIST =====");
        s.showItems();
        a.showItems();
        b.showItems();
        c.showItems();
        d.showItems();
        f.showItems();
    }

    /**
     * Memindahkan item dari satu tier ke tier lain
     * @param itemName Nama item yang akan dipindahkan
     * @param fromTier Tier sumber (nama tier asal)
     * @param toTier Tier tujuan (nama tier tujuan)
     * @return true jika berhasil, false jika gagal
     */
    public boolean moveItem(String itemName, String fromTier, String toTier) {
        // Dapatkan reference ke tier sumber dan tier tujuan
        Tier from = getTier(fromTier);
        Tier to = getTier(toTier);

        // Validasi tier valid
        if (from == null || to == null) {
            return false;
        }

        // Cari item di tier sumber
        TierItem item = from.findItem(itemName);
        if (item == null) {
            return false;
        }

        // Pindahkan item
        from.removeItem(item);
        to.addItem(item);
        return true;
    }
}
