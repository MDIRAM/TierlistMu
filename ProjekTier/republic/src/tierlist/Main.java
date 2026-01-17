package tierlist;

/**
 * Class Main - Entry point aplikasi Tier List Maker
 * Menjalankan GUI tier list dengan Swing
 * 
 * @author Tier List Maker
 * @version 1.0
 */
public class Main {
    
    /**
     * Main method - dijalankan pertama kali saat aplikasi start
     * Menggunakan SwingUtilities.invokeLater untuk thread safety di GUI
     * 
     * @param args Command line arguments (tidak digunakan)
     */
    public static void main(String[] args) {
        // Jalankan GUI Tier List di Event Dispatch Thread
        javax.swing.SwingUtilities.invokeLater(() -> new TierListGui());
    }
}
