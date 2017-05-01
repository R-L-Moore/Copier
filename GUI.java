import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import javax.swing.border.SoftBevelBorder;

public class GUI extends Panel implements ActionListener
{
    BevelBorder raisedBevel = new BevelBorder(BevelBorder.RAISED);
    BevelBorder loweredBevel = new BevelBorder(BevelBorder.LOWERED);
    SoftBevelBorder soft = new SoftBevelBorder(SoftBevelBorder.LOWERED);
		
	private static final long serialVersionUID = 1L;
	private String source, target;
	
	List<String> toCopy = new ArrayList<String>();
	boolean compared = false;
	boolean merged = false;
	int copyNum = 0;
	
	private Label sourceLabel = new Label("Source:");
	private JTextArea sourceField = new JTextArea(1,40);
	private JButton sourceBrowse = new JButton("...");
	private JFileChooser sourcePick = new JFileChooser();
	private JButton clearSource = new JButton("Clear Source");
	
	private Label targetLabel = new Label("Target:");
	private JTextArea targetField = new JTextArea(1, 40);
	private JButton targetBrowse = new JButton("...");
	private JFileChooser targetPick = new JFileChooser();
	private JButton clearTarget = new JButton ("Clear Target");
	
	private JButton compareButton = new JButton ("Compare");
	private JButton mergeButton = new JButton    ("Merge");
	
	private static JTextArea statusText = new JTextArea(8, 38);
	
	public GUI()
	{
		try
		{
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
			
		//Panels
		JPanel sourcePanel = new JPanel();
		JPanel targetPanel = new JPanel();
		JPanel actionsPanel = new JPanel(new BorderLayout());
		
		//SourcePanel
		sourcePanel.add(sourceLabel);
		sourceField.setEditable(false);
		JScrollPane sourcePane = new JScrollPane(sourceField);
		sourcePane.setHorizontalScrollBar(null);
		sourcePanel.add(sourcePane);
		sourcePanel.add(sourceBrowse);
		sourceBrowse.addActionListener(this);
		sourcePanel.add(clearSource);
		clearSource.addActionListener(this);
		
		//TargetPanel
		targetPanel.add(targetLabel);
		targetField.setEditable(false);
		JScrollPane targetPane = new JScrollPane(targetField);
		targetPane.setHorizontalScrollBar(null);
		targetPanel.add(targetPane);
		targetPanel.add(targetBrowse);
		targetBrowse.addActionListener(this);
		targetPanel.add(clearTarget);
		clearTarget.addActionListener(this);
				
		//StatusAreaPanel
		statusText.setBorder(loweredBevel);
		statusText.setLineWrap(true);
		statusText.setEditable(false);
		JScrollPane statusPanel = new JScrollPane (statusText);
		statusPanel.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		
		add(sourcePanel);
		add(targetPanel);
		
		actionsPanel.add(compareButton, BorderLayout.NORTH);
		compareButton.addActionListener(this);
		actionsPanel.add(mergeButton, BorderLayout.SOUTH);
		mergeButton.addActionListener(this);
		add(actionsPanel);	
		add(statusPanel);
		
		Font font = new Font ("Verdana", Font.PLAIN, 10);
		statusText.setFont(font);
		statusText.setText("");
		statusText.append(" 1) Select a source and target destination above\n");
		statusText.append(" 2) Click \"Compare\" to check for missing files\n");
		statusText.append(" 3) Click \"Merge\" to copy missing files from the source to the target\n");		
	}
	
	public void actionPerformed (ActionEvent e)
	{		
		if (e.getSource() == sourceBrowse)
		{
			sourcePick.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
			sourcePick.setDialogTitle("Source Selection");
			sourcePick.setApproveButtonText("Select Folder");
			sourcePick.setApproveButtonToolTipText("Confirm Selected Source Folder");
			if (sourcePick.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) 
			{
				File rootDir = sourcePick.getCurrentDirectory();
				File selectedDir = sourcePick.getSelectedFile();
				source = rootDir.getAbsolutePath() + sourcePick.getName(selectedDir);
				sourceField.setText(source);
			}
		}
		
		if (e.getSource() == targetBrowse)
		{
			targetPick.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
			targetPick.setDialogTitle("Target Selection");
			targetPick.setApproveButtonText("Select Folder");
			targetPick.setApproveButtonToolTipText("Confirm Selected Target Folder");
			if (targetPick.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) 
			{
				File rootDir = targetPick.getCurrentDirectory();
				File selectedDir = targetPick.getSelectedFile();
				target = rootDir.getAbsolutePath() + targetPick.getName(selectedDir);
				targetField.setText(target);
			}
		}
		
		if(e.getSource() == clearSource)
		{
			source = null;
			compared = false;
			sourceField.setText("");
		}
		if(e.getSource() == clearTarget)
		{
			target = null;
			compared = false;
			targetField.setText("");
		}
		
		if(e.getSource() == compareButton)
		{			
			if(source == null || target == null)
			{
				statusText.setText("");
				statusText.append(" Cannot compare - select a source and target\n");
				statusText.append(" Source: " + source + "\n Target: " + target + "\n");
			}
			else if (compared == false)
			{
				//run comparisons
				toCopy.clear();

				toCopy.addAll(FileManager.compare(source, target));
				
				for (@SuppressWarnings("unused") String file : toCopy){copyNum++;}
				statusText.setText("");
				if (copyNum == 0){statusText.append(copyNum + " file(s) to merge"); compared = true;}
				else
				{
					statusText.append(copyNum + " file(s) to merge :-\n");
					for (String file : toCopy)
					{
						statusText.append(" " + file + "\n");
					}
					statusText.setCaretPosition(0);
					compared = true;
					merged = false;
				}
			}
		}
		
		if(e.getSource() == mergeButton)
		{			
			if(compared == false && merged == false)
			{
				statusText.setText("");
				statusText.append(" Unable to merge - locations not compared");
			}
			else if (copyNum == 0)
			{
				statusText.setText("");
				statusText.append("0 file(s) to merge");
				compared = false;
				merged = true;
			}
			else
			{
				statusText.setText("");
				statusText.append("Merging files... \n");
				for (String file : toCopy)
				{
					statusText.append(" " + file + "\n");
				}
				FileManager.merge(toCopy, source, target);
				copyNum = 0;
				compared = false;
				merged = true;
				statusText.append(" All missing files merged");
			}
		}
	}
}