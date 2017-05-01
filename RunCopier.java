import javax.swing.JFrame;
import javax.swing.UIManager;

public class RunCopier 
{
	private static final String TITLE = "Copier";
	
	public static void main(String[] args)
	{
		try
		{
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		GUI gui = new GUI();
		
		JFrame frame = new JFrame();
		frame.setSize(550,240);
		frame.setLocationRelativeTo(null);
		frame.setResizable(false);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setTitle(TITLE);
		frame.add(gui);
		frame.setVisible(true);
	}
}