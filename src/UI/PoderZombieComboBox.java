package UI;

import javax.swing.*;

public class PoderZombieComboBox extends JComboBox {
    public PoderZombieComboBox(){
        super();
        for(int i=-2; i>=-5; i--) this.addItem(i);
    }
}
