package tierlist;

import javax.swing.*;
import javax.swing.border.*;
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.event.*;
import java.awt.dnd.*;
import java.awt.datatransfer.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;

/**
 * Class TierListGui - Tier List Maker GUI
 * Flow: Upload image → show di palette bawah → drag ke tier atau add
 * No nama item, hanya gambar
 */
public class TierListGui extends JFrame {

    private TierListData tierListData;
    private TierManager manager;
    
    private JPanel tierListPanel;
    private JPanel imagePalettePanel;
    private ArrayList<File> uploadedImages = new ArrayList<>();
    
    private String draggedItemName;
    private String draggedFromTier;
    private String draggedImagePath;
    
    private static final String PALETTE_SOURCE = "__PALETTE__";
    private static final String[] TIER_NAMES = {"S", "A", "B", "C", "D", "F"};
    
    private static final Color[] TIER_COLORS = {
        new Color(255, 100, 100),
        new Color(255, 200, 80),
        new Color(255, 255, 100),
        new Color(150, 255, 100),
        new Color(100, 200, 255),
        new Color(100, 255, 200)
    };

    public TierListGui() {
        this.tierListData = new TierListData("🎯 Buat Tierlist Mu", "Drag and drop items to rank them");
        this.manager = tierListData.getManager();
        setupUI();
    }

    private void setupUI() {
        setTitle(tierListData.getName());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1400, 900);
        setLocationRelativeTo(null);
        setResizable(true);

        JPanel mainContainer = new JPanel(new BorderLayout(0, 0));
        mainContainer.setBackground(new Color(30, 35, 50));

        // Top toolbar - hanya upload + add + delete + clear
        mainContainer.add(createTopToolbar(), BorderLayout.NORTH);

        // Center: Tier list
        JPanel centerContent = new JPanel(new BorderLayout(0, 0));
        centerContent.setBackground(new Color(30, 35, 50));
        
        tierListPanel = new JPanel();
        tierListPanel.setLayout(new BoxLayout(tierListPanel, BoxLayout.Y_AXIS));
        tierListPanel.setBackground(new Color(35, 40, 55));
        createTierPanels();
        
        JScrollPane scrollPane = new JScrollPane(tierListPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.getViewport().setBackground(new Color(35, 40, 55));
        centerContent.add(scrollPane, BorderLayout.CENTER);

        mainContainer.add(centerContent, BorderLayout.CENTER);
        add(mainContainer);
        setVisible(true);
    }

    /**
     * Top toolbar: Upload, Add, Delete, Clear All
     */
    private JPanel createTopToolbar() {
        JPanel toolbar = new JPanel(new BorderLayout(0, 0));
        toolbar.setBackground(new Color(40, 45, 65));
        toolbar.setBorder(new MatteBorder(0, 0, 2, 0, new Color(30, 130, 200)));
        toolbar.setPreferredSize(new Dimension(0, 50));

        // Left: Title
        JLabel titleLabel = new JLabel("🎯 Buat Tierlist Mu - Drag and drop items to rank them");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 14));
        titleLabel.setForeground(new Color(200, 200, 200));
        titleLabel.setBorder(new EmptyBorder(0, 15, 0, 0));
        toolbar.add(titleLabel, BorderLayout.WEST);

        // Right: Buttons
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        btnPanel.setBackground(new Color(40, 45, 65));

        JButton uploadBtn = new JButton("📤 Upload Image");
        uploadBtn.setFont(new Font("Arial", Font.PLAIN, 11));
        uploadBtn.addActionListener(e -> uploadImage());
        btnPanel.add(uploadBtn);

        JButton clearBtn = new JButton("🗑️ Clear All");
        clearBtn.setFont(new Font("Arial", Font.PLAIN, 11));
        clearBtn.addActionListener(e -> clearAllItems());
        btnPanel.add(clearBtn);

        toolbar.add(btnPanel, BorderLayout.CENTER);
        return toolbar;
    }

    /**
     * Membuat semua tier panels
     */
    private void createTierPanels() {
        tierListPanel.removeAll();
        
        for (int i = 0; i < TIER_NAMES.length; i++) {
            tierListPanel.add(createTierRow(i));
        }
        
        // Palette di bawah
        if (imagePalettePanel == null) {
            imagePalettePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 5));
            imagePalettePanel.setBackground(new Color(50, 55, 70));
            imagePalettePanel.setBorder(new TitledBorder("📌 Pin Images"));
            imagePalettePanel.setPreferredSize(new Dimension(1400, 130));
        }
        tierListPanel.add(imagePalettePanel);
        
        tierListPanel.revalidate();
        tierListPanel.repaint();
    }

    /**
     * Satu tier row
     */
    private JPanel createTierRow(int tierIndex) {
        String tierName = TIER_NAMES[tierIndex];
        JPanel tierRow = new JPanel(new BorderLayout(3, 0));
        tierRow.setMaximumSize(new Dimension(Integer.MAX_VALUE, 100));
        tierRow.setBorder(new MatteBorder(0, 0, 1, 0, new Color(60, 65, 80)));
        tierRow.setBackground(new Color(35, 40, 55));

        // Label tier
        JLabel tierLabel = new JLabel(tierName);
        tierLabel.setFont(new Font("Arial", Font.BOLD, 28));
        tierLabel.setHorizontalAlignment(JLabel.CENTER);
        tierLabel.setVerticalAlignment(JLabel.CENTER);
        tierLabel.setPreferredSize(new Dimension(80, 100));
        tierLabel.setBackground(TIER_COLORS[tierIndex]);
        tierLabel.setOpaque(true);
        tierLabel.setForeground(Color.BLACK);
        tierRow.add(tierLabel, BorderLayout.WEST);

        // Items area
        JPanel itemsArea = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 5));
        itemsArea.setBackground(new Color(45, 50, 70));
        
        Tier tier = manager.getTier(tierName);
        ArrayList<TierItem> items = tier.items;
        for (TierItem item : items) {
            itemsArea.add(createItemBox(item, tierName));
        }

        setupDropTarget(itemsArea, tierName);
        tierRow.add(itemsArea, BorderLayout.CENTER);

        return tierRow;
    }

    /**
     * Item box di tier
     */
    private JPanel createItemBox(TierItem item, String currentTier) {
        JPanel box = new JPanel(new BorderLayout());
        box.setPreferredSize(new Dimension(90, 90));
        box.setBorder(new LineBorder(Color.GRAY, 1));
        box.setBackground(new Color(200, 200, 200));

        if (item.getImagePath() != null && !item.getImagePath().isEmpty()) {
            try {
                BufferedImage img = ImageIO.read(new File(item.getImagePath()));
                Image scaledImg = img.getScaledInstance(90, 90, Image.SCALE_SMOOTH);
                JLabel imgLabel = new JLabel(new ImageIcon(scaledImg));
                box.add(imgLabel);
            } catch (Exception e) {
                JLabel err = new JLabel("❌");
                err.setHorizontalAlignment(JLabel.CENTER);
                box.add(err);
            }
        } else {
            box.setBackground(new Color(180, 180, 180));
        }

        box.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON3) {
                    showItemContextMenu(box, item, currentTier);
                }
            }
        });

        new DragSource().createDefaultDragGestureRecognizer(
            box, DnDConstants.ACTION_MOVE,
            dge -> {
                draggedItemName = item.getName();
                draggedFromTier = currentTier;
                draggedImagePath = item.getImagePath();
                dge.startDrag(DragSource.DefaultMoveDrop, new StringSelection(item.getName()));
            }
        );

        return box;
    }

    /**
     * Palette image box
     */
    private JPanel createPaletteImageBox(File imgFile) {
        JPanel box = new JPanel(new BorderLayout());
        box.setPreferredSize(new Dimension(90, 90));
        box.setBorder(new LineBorder(Color.GRAY, 2));
        box.setBackground(new Color(220, 220, 220));
        box.setCursor(new Cursor(Cursor.HAND_CURSOR));

        try {
            BufferedImage img = ImageIO.read(imgFile);
            Image scaledImg = img.getScaledInstance(90, 90, Image.SCALE_SMOOTH);
            JLabel imgLabel = new JLabel(new ImageIcon(scaledImg));
            box.add(imgLabel);
        } catch (Exception e) {
            JLabel err = new JLabel("Error");
            err.setHorizontalAlignment(JLabel.CENTER);
            box.add(err);
        }

        // Drag from palette
        new DragSource().createDefaultDragGestureRecognizer(
            box, DnDConstants.ACTION_COPY,
            dge -> {
                draggedItemName = removeExt(imgFile.getName());
                draggedFromTier = PALETTE_SOURCE;
                draggedImagePath = imgFile.getAbsolutePath();
                dge.startDrag(DragSource.DefaultCopyDrop, new StringSelection(imgFile.getAbsolutePath()));
            }
        );

        return box;
    }

    /**
     * Drop target
     */
    private void setupDropTarget(JPanel panel, String targetTier) {
        new DropTarget(panel, new DropTargetAdapter() {
            @Override
            public void drop(DropTargetDropEvent dtde) {
                try {
                    Transferable tr = dtde.getTransferable();
                    if (tr.isDataFlavorSupported(DataFlavor.stringFlavor)) {
                        dtde.acceptDrop(DnDConstants.ACTION_MOVE);
                        String data = (String) tr.getTransferData(DataFlavor.stringFlavor);
                        
                        if (PALETTE_SOURCE.equals(draggedFromTier)) {
                            String imagePath = data;
                            String name = removeExt(new File(imagePath).getName());
                            TierItem newItem = new TierItem(name, imagePath);
                            Tier t = manager.getTier(targetTier);
                            if (t != null) t.addItem(newItem);
                            refreshDisplay();
                        } 
                        else if (draggedFromTier != null && !draggedFromTier.equals(targetTier)) {
                            manager.moveItem(data, draggedFromTier, targetTier);
                            refreshDisplay();
                        }
                        dtde.dropComplete(true);
                        return;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                dtde.rejectDrop();
            }
        });
    }

    /**
     * Context menu untuk item di tier
     */
    private void showItemContextMenu(JPanel panel, TierItem item, String currentTier) {
        JPopupMenu menu = new JPopupMenu();

        JMenu moveMenu = new JMenu("Move to");
        for (String tier : TIER_NAMES) {
            if (!tier.equals(currentTier)) {
                JMenuItem mi = new JMenuItem(tier);
                mi.addActionListener(e -> {
                    manager.moveItem(item.getName(), currentTier, tier);
                    refreshDisplay();
                });
                moveMenu.add(mi);
            }
        }
        menu.add(moveMenu);
        menu.addSeparator();

        JMenuItem deleteItem = new JMenuItem("🗑️ Delete");
        deleteItem.addActionListener(e -> {
            manager.getTier(currentTier).removeItem(item);
            refreshDisplay();
        });
        menu.add(deleteItem);

        menu.show(panel, 0, panel.getHeight());
    }

    // ==================== OPERATIONS ====================

    /**
     * Upload satu atau banyak gambar
     */
    private void uploadImage() {
        JFileChooser chooser = new JFileChooser();
        chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        chooser.setMultiSelectionEnabled(true);
        chooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("Images", "jpg", "jpeg", "png", "gif"));
        
        if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            File[] selectedFiles = chooser.getSelectedFiles();
            for (File f : selectedFiles) {
                uploadedImages.add(f);
            }
            refreshPalette();
        }
    }

    /**
     * Add selected image dari palette ke tier
     */
    private void addToTier() {
        if (uploadedImages.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Upload image first!", "Info", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        // Dialog untuk pilih tier
        String[] options = TIER_NAMES;
        String tierChoice = (String) JOptionPane.showInputDialog(
            this,
            "Add to which tier?",
            "Select Tier",
            JOptionPane.QUESTION_MESSAGE,
            null,
            options,
            options[0]
        );

        if (tierChoice != null) {
            File lastImage = uploadedImages.get(uploadedImages.size() - 1);
            String imagePath = lastImage.getAbsolutePath();
            String name = removeExt(lastImage.getName());
            
            TierItem item = new TierItem(name, imagePath);
            Tier t = manager.getTier(tierChoice);
            if (t != null) {
                t.addItem(item);
                refreshDisplay();
            }
        }
    }

    /**
     * Delete selected image dari palette
     */
    private void deleteSelected() {
        if (uploadedImages.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No images to delete", "Info", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        uploadedImages.remove(uploadedImages.size() - 1);
        refreshPalette();
    }

    /**
     * Clear semua item dari tier
     */
    private void clearAllItems() {
        int confirm = JOptionPane.showConfirmDialog(this, "Clear all items from tiers?", "Confirm", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            manager = new TierManager();
            refreshDisplay();
        }
    }

    /**
     * Refresh palette
     */
    private void refreshPalette() {
        imagePalettePanel.removeAll();
        
        for (File f : uploadedImages) {
            imagePalettePanel.add(createPaletteImageBox(f));
        }
        
        imagePalettePanel.revalidate();
        imagePalettePanel.repaint();
    }

    /**
     * Refresh display
     */
    private void refreshDisplay() {
        createTierPanels();
    }

    private String removeExt(String filename) {
        int i = filename.lastIndexOf('.');
        return i > 0 ? filename.substring(0, i) : filename;
    }
}
