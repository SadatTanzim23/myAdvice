import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import javax.swing.*;

// ── Clickable card button used on the main menu grid ─────────────────────────
public class MenuCard extends JButton {

    private boolean hovered = false;
    private final String num;
    private final String title;
    private final String desc;

    public MenuCard(String num, String title, String desc) {
        this.num   = num;
        this.title = title;
        this.desc  = desc;

        setOpaque(false);
        setContentAreaFilled(false);
        setBorderPainted(false);
        setFocusPainted(false);
        setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        setPreferredSize(new Dimension(310, 100));

        addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) { hovered = true;  repaint(); }
            public void mouseExited (MouseEvent e) { hovered = false; repaint(); }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        //card background on each
        g2.setColor(hovered ? myAdvice.CARD_HOVER : myAdvice.CARD_BG);
        g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 14, 14));

        //left bar with the accent
        //g2.setColor(myAdvice.ACCENT);
        //g2.fill(new RoundRectangle2D.Float(0, 0, 4, getHeight(), 4, 4));

        //number badge for each 
        // g2.setColor(hovered ? myAdvice.ACCENT : myAdvice.ACCENT2);
        // g2.fill(new RoundRectangle2D.Float(16, 14, 26, 26, 8, 8));
        // g2.setColor(myAdvice.BG);
        // g2.setFont(new Font("Dialog", Font.BOLD, 12));
        // FontMetrics fm = g2.getFontMetrics();
        // g2.drawString(num,
        //     16 + (26 - fm.stringWidth(num)) / 2,
        //     14 + (26 + fm.getAscent() - fm.getDescent()) / 2 - 1);

        //the title of each module
        g2.setFont(myAdvice.FONT_BTN);
        g2.setColor(myAdvice.TEXT_LIGHT);
        g2.drawString(title, 18, 56);

        //description on each module
        g2.setFont(myAdvice.FONT_BTN_SUB);
        g2.setColor(myAdvice.TEXT_MUTED);
        g2.drawString(desc, 18, 74);

        //hoverarrow indicator incase if we want it
        // if (hovered) {
        //     g2.setColor(myAdvice.ACCENT);
        //     g2.setFont(new Font("Dialog", Font.BOLD, 24));
        //     //g2.drawString("->", getWidth() - 30, getHeight() - 12);//incase if we do want this we could have it
        // }

        g2.dispose();
    }
}
