package tierlist;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * Abstract class Tier - Merepresentasikan satu level/rank dalam tier list
 * Setiap tier dapat menampung banyak item yang akan dirangking
 * 
 * Konsep OOP yang digunakan:
 * - Abstraction: Class ini abstract dan memiliki method abstract getTierName()
 * - Inheritance: Setiap tier (S, A, B, C, D, F) akan extend class ini
 * 
 * @author Tier List Maker
 * @version 1.0
 */
public abstract class Tier {

    // List untuk menyimpan semua item yang ada di tier ini
    public ArrayList<TierItem> items = new ArrayList<>();

    /**
     * Menambahkan item baru ke dalam tier
     * @param item Item yang akan ditambahkan
     */
    public void addItem(TierItem item) {
        items.add(item);
    }

    /**
     * Mencari item berdasarkan nama
     * @param name Nama item yang dicari
     * @return Item jika ditemukan, null jika tidak ada
     */
    public TierItem findItem(String name) {
        // Menggunakan for-each loop untuk iterasi items
        for (TierItem item : items) {
            if (item.getName().equalsIgnoreCase(name)) {
                return item;
            }
        }
        return null;
    }

    /**
     * Menghapus item dari tier
     * @param item Item yang akan dihapus
     */
    public void removeItem(TierItem item) {
        items.remove(item);
    }

    /**
     * Menampilkan semua item dalam tier ke console
     * Format: [TIER_NAME] : [item1, item2, ...]
     */
    public void showItems() {
        System.out.print(getTierName() + " : ");
        if (items.isEmpty()) {
            System.out.println("-");
        } else {
            // Menggunakan Iterator untuk iterasi yang aman saat modifikasi
            Iterator<TierItem> it = items.iterator();
            while (it.hasNext()) {
                System.out.print(it.next().getName());
                if (it.hasNext()) {
                    System.out.print(", ");
                }
            }
            System.out.println();
        }
    }

    /**
     * Abstract method - harus di-implement oleh subclass
     * @return Nama tier (S, A, B, C, D, F)
     */
    public abstract String getTierName();
}

