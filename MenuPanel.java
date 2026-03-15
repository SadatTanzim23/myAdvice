import java.awt.*;
import javax.swing.*;
import javax.swing.border.*;

public class MenuPanel extends JPanel {//class for holding the 5 different module cards on the menu

    public MenuPanel(MainApp app) {
        setLayout(new BorderLayout());
        setOpaque(false);
        setBorder(new EmptyBorder(28, 40, 10, 40));

        JLabel prompt = new JLabel("Select a Module");
        prompt.setFont(new Font("Dialog", Font.BOLD, 13));
        prompt.setForeground(myAdvice.TEXT_MUTED);
        prompt.setBorder(new EmptyBorder(0, 4, 14, 0));
        add(prompt, BorderLayout.NORTH);

        JPanel grid = new JPanel(new GridBagLayout());
        grid.setOpaque(false);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1;
        gbc.weighty = 1;

        for (int i = 0; i < myAdvice.MODULE_META.length; i++) {
            final int idx = i;
            gbc.gridx = i % 2;
            gbc.gridy = i / 2;
            gbc.gridwidth = (i == 4) ? 2 : 1;
            if (i == 4) gbc.weightx = 0.5;

            MenuCard card = new MenuCard(///using a 2d array to place them
                myAdvice.MODULE_META[i][0],
                myAdvice.MODULE_META[i][1],
                myAdvice.MENU_DESCS[i]
            );
            card.addActionListener(e -> app.showModule(idx));
            grid.add(card, gbc);
        }
        add(grid, BorderLayout.CENTER);
    }
}
