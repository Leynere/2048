package gaphique;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;

import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * This class define a graphique case.
 * @author sivanantham
 *
 */
class Case {
	
	/**
	 * A panel of a case.
	 */
	private final JPanel panneau;
	
	/**
	 * The value of the case who is printed.
	 */
	private final JLabel value;
	
	/**
	 *Level of the case who is used for determine the value.
	 */
	private int lvl;
	
	
	/**
	 * Constructor.
	 */
	public Case() {
		this.panneau = new JPanel(new BorderLayout());
		this.value = new JLabel();
		this.value.setHorizontalAlignment((int) Component.CENTER_ALIGNMENT);
		this.value.setVerticalAlignment((int) Component.CENTER_ALIGNMENT);
		this.value.setForeground(Color.black);
		final Font police = new Font("Arial", Font.BOLD, 38);

		this.value.setFont(police);
		this.panneau.add(this.value, BorderLayout.CENTER);

		this.lvl = 0;
		this.update();
	}
	
	/**
	 * Get the panel with his items.
	 * @return : the panel of the case.
	 */
	public JPanel getPanneau(){
		return this.panneau;
	}
	
	/**
	 * Determine the color of the case with his level.
	 * @return : the associated color.
	 */
	private Color getColor(){
		switch (this.lvl){
		case 0:
			return new Color(187,173,160);
		case 2:
			return new Color(238,228,218);
		case 4:
			return new Color(237,224,200);
		case 8:
			return new Color(242,177,121);
		case 16:
			return new Color(245,149,99);
		case 32:
			return new Color(246,124,95);
		case 64:
			return new Color(246,94,59);
		case 128:
			return new Color(237, 207,114);
		case 256:
			return new Color(237, 204,97);
		case 512:
			return new Color(237,200,80);
		case 1024:
			return new Color(237,197, 63);
		case 2048:
			return new Color(237,194,46);
		default:
			return Color.blue;

		}
	}
	
	/**
	 * Update this case with the level.
	 */
	private void update(){
		this.panneau.setBackground(getColor());
		if(this.lvl != 0)
			this.value.setText(this.lvl+"");
		else
			this.value.setText("");
	}
	/**
	 * Update the level.
	 * @param lvl : the new level to set.
	 */
	void setLvl(int lvl){
		this.lvl = lvl;
		this.update();
	}
}
