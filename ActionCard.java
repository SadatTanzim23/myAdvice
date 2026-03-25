import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import javax.swing.*;
import javax.swing.border.*;

public class ActionCard extends JPanel {//Each module showing the action available inside
    private Runnable onClickAction;

    public ActionCard(String title, String desc) {
        setLayout(new BorderLayout(0, 4));
        setOpaque(false);
        setBorder(new EmptyBorder(14, 16, 14, 16));

        JLabel t = new JLabel(title);
        t.setFont(new Font("Dialog", Font.BOLD, 13));
        t.setForeground(myAdvice.TEXT_LIGHT);

        JLabel d = new JLabel(desc);
        d.setFont(new Font("Dialog", Font.PLAIN, 11));
        d.setForeground(myAdvice.TEXT_MUTED);

        add(t, BorderLayout.NORTH);
        add(d, BorderLayout.CENTER);
        
        // Add click listener
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (onClickAction != null) {
                    onClickAction.run();
                }
            }
        });
    }
    
    public void setOnClickAction(Runnable action) {
        this.onClickAction = action;
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(myAdvice.CARD_BG);
        g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 10, 10));
        g2.dispose();
    }
}
