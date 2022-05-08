package UI;

import javax.swing.*;
import java.awt.*;

public class ConfigPanel extends JPanel {

    private final Image imagen;

    public ConfigPanel() {
        imagen = new ImageIcon(
                getClass().getResource("img/configBackground.jpg")
        ).getImage();

        this.setBounds(0, 0, 200, 600);
        this.setLayout(new GridBagLayout());
    }

    @Override
    public void paint(Graphics g) {
        if (imagen != null) {
            g.drawImage(imagen, 0, 0, getWidth(), getHeight(),
                    this);

            setOpaque(false);
        } else {
            setOpaque(true);
        }

        super.paint(g);
    }
}
