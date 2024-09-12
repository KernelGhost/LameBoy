import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JPanel;
import javax.swing.Timer;

public class GUIPanel extends JPanel implements ActionListener {
	private static final long serialVersionUID = 3471473953294856384L;
	private boolean[] boolGraphics = new boolean[2048];
	private Timer timer;
	
	public GUIPanel() {
		for (int intCtr = 0; intCtr < boolGraphics.length; intCtr++) {
			this.boolGraphics[intCtr] = false;
		}
		
		// Begin Timer
		timer = new Timer(6, this);
		timer.start();
	}
	
	public void UpdateGraphics(byte[] byteGraphics) {
		for (int intCtr = 0; intCtr < byteGraphics.length; intCtr++) {
			boolGraphics[7 + (intCtr * 8)] = (((byteGraphics[intCtr] >>> 7) & 0b00000001) != 0);
			boolGraphics[6 + (intCtr * 8)] = (((byteGraphics[intCtr] >>> 6) & 0b00000001) != 0);
			boolGraphics[5 + (intCtr * 8)] = (((byteGraphics[intCtr] >>> 5) & 0b00000001) != 0);
			boolGraphics[4 + (intCtr * 8)] = (((byteGraphics[intCtr] >>> 4) & 0b00000001) != 0);
			boolGraphics[3 + (intCtr * 8)] = (((byteGraphics[intCtr] >>> 3) & 0b00000001) != 0);
			boolGraphics[2 + (intCtr * 8)] = (((byteGraphics[intCtr] >>> 2) & 0b00000001) != 0);
			boolGraphics[1 + (intCtr * 8)] = (((byteGraphics[intCtr] >>> 1) & 0b00000001) != 0);
			boolGraphics[0 + (intCtr * 8)] = ((byteGraphics[intCtr] & 0b00000001) != 0);
		}
	}
	
	public void paint(Graphics g) {
		final int intScreenScale = 8;
		
        for (int intCtrY = 0; intCtrY < 32; intCtrY++) {
            for (int intCtrX = 0; intCtrX < 64; intCtrX++) {	
            	if (this.boolGraphics[intCtrX + (intCtrY * 64)] == true) {
            		g.setColor(Color.WHITE);
            	} else {
            		g.setColor(Color.BLACK);
            	}
            	g.fillRect(intCtrX * intScreenScale, intCtrY * intScreenScale, intScreenScale, intScreenScale);
            }
        }
		
		// Dispose
		g.dispose();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		timer.start();
		repaint();
	}
}