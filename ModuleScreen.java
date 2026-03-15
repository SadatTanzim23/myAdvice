import java.awt.*;
import javax.swing.*;
import javax.swing.border.*;

public class ModuleScreen extends JPanel {//the module screens you go in through the main screen 

    public ModuleScreen(int idx) {
        setLayout(new BorderLayout());
        setOpaque(false);

        add(buildSubHeader(idx),                               BorderLayout.NORTH);
        add(buildActionsPanel(myAdvice.MODULE_ACTIONS[idx]),   BorderLayout.CENTER);
    }

    private JPanel buildSubHeader(int idx) {//the subheading under the main heading panel with the module name and subhead
        JPanel p = new JPanel() {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setColor(myAdvice.PANEL_BG);
                g2.fillRect(0, 0, getWidth(), getHeight());
                g2.setColor(myAdvice.DIVIDER);
                g2.drawLine(0, getHeight() - 1, getWidth(), getHeight() - 1);
                g2.dispose();
            }
        };
        p.setOpaque(false);
        p.setPreferredSize(new Dimension(780, 54));
        p.setLayout(new BorderLayout(0, 2));
        p.setBorder(new EmptyBorder(8, 32, 8, 32));

        JLabel titleLbl = new JLabel(myAdvice.MODULE_META[idx][1]);
        titleLbl.setFont(new Font("Dialog", Font.BOLD, 17));
        titleLbl.setForeground(myAdvice.TEXT_LIGHT);

        JLabel subtitleLbl = new JLabel(myAdvice.MODULE_META[idx][2]);
        subtitleLbl.setFont(new Font("Dialog", Font.PLAIN, 12));
        subtitleLbl.setForeground(myAdvice.TEXT_MUTED);

        JPanel stack = new JPanel(new GridLayout(2, 1, 0, 2));
        stack.setOpaque(false);
        stack.add(titleLbl);
        stack.add(subtitleLbl);
        p.add(stack, BorderLayout.CENTER);
        return p;
    }

    //2-column grid of ActionCard panels
    private JPanel buildActionsPanel(String[][] items) {
        JPanel p = new JPanel(new GridBagLayout());
        p.setBackground(myAdvice.BG);
        p.setBorder(new EmptyBorder(30, 40, 30, 40));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets  = new Insets(8, 8, 8, 8);
        gbc.fill    = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1;

        JLabel heading = new JLabel("Available Actions");
        heading.setFont(new Font("Dialog", Font.BOLD, 13));
        heading.setForeground(myAdvice.TEXT_MUTED);
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        p.add(heading, gbc);

        for (int i = 0; i < items.length; i++) {
            gbc.gridy = i / 2 + 1;
            gbc.gridx = i % 2;
            gbc.gridwidth = 1;
            p.add(new ActionCard(items[i][0], items[i][1]), gbc);
        }
        return p;
    }
}
