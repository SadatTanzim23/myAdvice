import java.awt.*;
import java.awt.geom.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;

public class ActionCard extends JPanel {
    private boolean hovered = false;
    private Runnable action;

    public ActionCard(String title, String desc, Runnable action) {
        this.action = action;
        setLayout(new BorderLayout(0, 4));
        setOpaque(false);
        setBorder(new EmptyBorder(14, 16, 14, 16));
        setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        JLabel t = new JLabel(title);
        t.setFont(new Font("Dialog", Font.BOLD, 13));
        t.setForeground(myAdvice.TEXT_LIGHT);

        JLabel d = new JLabel(desc);
        d.setFont(new Font("Dialog", Font.PLAIN, 11));
        d.setForeground(myAdvice.TEXT_MUTED);

        add(t, BorderLayout.NORTH);
        add(d, BorderLayout.CENTER);

        addMouseListener(new MouseAdapter() {
            @Override public void mouseEntered(MouseEvent e) { hovered = true; repaint(); }
            @Override public void mouseExited(MouseEvent e)  { hovered = false; repaint(); }
            @Override public void mouseClicked(MouseEvent e) { if (action != null) action.run(); }
        });
    }

    // Keep old constructor working (no action)
    public ActionCard(String title, String desc) {
        this(title, desc, null);
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(hovered ? myAdvice.CARD_HOVER : myAdvice.CARD_BG);
        g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 10, 10));
        g2.dispose();
    }
}
