package UI;

import javax.swing.*;
import java.awt.*;

public class JuegoPanel extends JPanel {

    private final Image imagen;

    public JuegoPanel(){


        imagen = new ImageIcon(
                getClass().getResource("img/homeBackground.png")
        ).getImage();

        this.setBounds(0,0,800,600);

        this.setLayout(new FlowLayout());
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
