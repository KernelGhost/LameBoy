import java.awt.EventQueue;
import javax.swing.JFrame;

public class Main extends JFrame {
	private static final long serialVersionUID = -3579328058618770565L;
	public static frmWindow frmMainWindow;
	
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					frmMainWindow = new frmWindow();
					frmMainWindow.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
}