package UI;

import javax.swing.*;
import java.awt.*;

public class ConfigLabel extends JLabel {
    public ConfigLabel(String text, int horizontalAlignment) {
        super(text,horizontalAlignment);
        this.setFont(new Font("Roboto", Font.BOLD, 14));
        this.setForeground(Color.white);
    }
}
