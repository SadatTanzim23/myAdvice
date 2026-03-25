import java.awt.*;
import javax.swing.*;
import javax.swing.border.*;

public class MainApp extends JFrame {
    final CardLayout centerLayout = new CardLayout();
    final JPanel     centerPanel  = new JPanel(centerLayout);
    final CardLayout southLayout  = new CardLayout();
    final JPanel     southPanel   = new JPanel(southLayout);
    JLabel headerSubLabel;

    public MainApp() {
        setTitle("myAdvice - Student Advising System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        setSize(800, 650);
        setLocationRelativeTo(null);
        getContentPane().setBackground(myAdvice.BG);
        setLayout(new BorderLayout());
        add(buildHeader(), BorderLayout.NORTH);

        centerPanel.setOpaque(false);
        centerPanel.add(new MenuPanel(this), "MENU");
        for (int i = 0; i < myAdvice.MODULE_META.length; i++) {//showing per module screen
            centerPanel.add(new ModuleScreen(i), myAdvice.MODULE_META[i][0]);
        }
        add(centerPanel, BorderLayout.CENTER);//having the main menu at the center

        //putting the footer on every screen
        southPanel.setOpaque(false);
        southPanel.add(buildFooter(),  "FOOTER");
        southPanel.add(buildBackBar(), "BACK");
        add(southPanel, BorderLayout.SOUTH);
    }

    public void showMenu() {//the navigation layout
        headerSubLabel.setText("Student Advising System  |  2026W");
        centerLayout.show(centerPanel, "MENU");
        southLayout .show(southPanel,  "FOOTER");
    }

    public void showModule(int idx) {
        headerSubLabel.setText(myAdvice.MODULE_META[idx][1] + "  |  " + myAdvice.MODULE_META[idx][2]);
        // If navigating to Curriculum Advising, refresh that panel first
        if (idx == 0) {
            for (Component c : centerPanel.getComponents()) {
                if (c instanceof ModuleScreen ms && ms.getModuleIdx() == 0) {
                    ms.refreshCurriculumIfNeeded();
                    break;
                }
            }
        }
        centerLayout.show(centerPanel, myAdvice.MODULE_META[idx][0]);
        southLayout .show(southPanel,  "BACK");
    }

    private JPanel buildHeader() {//created a header that is always visible along with the Jlabels for
        JPanel p = new JPanel() {
            @Override protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setPaint(new GradientPaint(0, 0, new Color(0x0A1628),
                                              getWidth(), getHeight(), new Color(0x132233)));
                g2.fillRect(0, 0, getWidth(), getHeight());
                g2.setColor(myAdvice.ACCENT);
                g2.setStroke(new BasicStroke(2f));
                g2.drawLine(40, getHeight() - 1, getWidth() - 40, getHeight() - 1);
                g2.dispose();
            }
        };
        p.setOpaque(false);
        p.setPreferredSize(new Dimension(780, 118));
        p.setLayout(new BorderLayout());
        p.setBorder(new EmptyBorder(22, 44, 14, 44));

        //the 3 labels at the top 
        JLabel logo = new JLabel("myAdvice", SwingConstants.CENTER);
        logo.setFont(myAdvice.FONT_TITLE);
        logo.setForeground(myAdvice.TEXT_LIGHT);

        JLabel badge = new JLabel("UWindsor  |  School of Computer Science", SwingConstants.CENTER);
        badge.setFont(myAdvice.FONT_SUBTITLE);
        badge.setForeground(myAdvice.ACCENT);

        headerSubLabel = new JLabel("Student Advising System  |  2026W", SwingConstants.CENTER);
        headerSubLabel.setFont(myAdvice.FONT_SUBTITLE);
        headerSubLabel.setForeground(myAdvice.TEXT_MUTED);

        JPanel stack = new JPanel(new GridLayout(3, 1, 0, 4));
        stack.setOpaque(false);
        stack.add(logo);
        stack.add(badge);
        stack.add(headerSubLabel);

        p.add(stack, BorderLayout.CENTER);
        return p;
    }

    private JPanel buildFooter() {//the footer at the bottom of every screen
        JPanel p = new JPanel(new BorderLayout());
        p.setOpaque(false);
        p.setBorder(new EmptyBorder(8, 44, 16, 44));

        JSeparator sep = new JSeparator();
        sep.setForeground(myAdvice.DIVIDER);
        p.add(sep, BorderLayout.NORTH);

        JLabel lbl = new JLabel(
            "University of Windsor");
        lbl.setFont(new Font("Dialog", Font.PLAIN, 11));
        lbl.setForeground(myAdvice.TEXT_MUTED);
        lbl.setBorder(new EmptyBorder(6, 0, 0, 0));
        p.add(lbl, BorderLayout.WEST);
        return p;
    }

    private JPanel buildBackBar() {//the back button inside each module at the bottom
        JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT, 16, 8));
        p.setBackground(myAdvice.PANEL_BG);
        p.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, myAdvice.DIVIDER));

        BackButton back = new BackButton("<- BACK");
        back.addActionListener(e -> showMenu());
        p.add(back);
        return p;
    }
}
