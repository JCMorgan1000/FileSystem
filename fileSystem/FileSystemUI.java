/*
 * Author: Jason C. Morgan
 * Date: November 30, 2016
 * Purpose: This program is a Java implementation of a basic file system.
 */

package fileSystem;

import java.awt.FlowLayout;
import java.awt.event.KeyEvent;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class FileSystemUI extends javax.swing.JFrame {

	private static final long serialVersionUID = 1L;
	
	private javax.swing.JTextArea displayArea;
    private javax.swing.JLabel displayLabel;
    private javax.swing.JTextField inputField;
    private javax.swing.JLabel inputLabel;
    private javax.swing.JScrollPane jScrollPane1;
    private CustomFileSystem fileSystem;
	
	public FileSystemUI() {
		
		super("File System");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
		
		displayLabel = new javax.swing.JLabel("Display:");
		displayArea = new javax.swing.JTextArea();
		jScrollPane1 = new javax.swing.JScrollPane();        
        inputLabel = new javax.swing.JLabel("Input:");
        inputField = new javax.swing.JTextField(10);

        jScrollPane1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        displayArea.setEditable(false);
        displayArea.setColumns(60);
        displayArea.setRows(35);
        jScrollPane1.setViewportView(displayArea);
        
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JPanel middlePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        topPanel.add(displayLabel);
        middlePanel.add(jScrollPane1);
		bottomPanel.add(inputLabel);
		bottomPanel.add(inputField);
		add(topPanel);
		add(middlePanel);
		add(bottomPanel);
		
		pack();
        
        inputField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                inputFieldKeyPressed(evt);
            }
        });
       
        setVisible(true);
        fileSystem = new CustomFileSystem(this);
        fileSystem.printMenu();
    }                                              

	public void inputFieldKeyPressed(java.awt.event.KeyEvent evt) {                                      
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
        	setMenuOption(Integer.parseInt(inputField.getText()));
        }
    } 
    
    public void setMenuOption(int i) {
    	fileSystem.runFileSystem(i);
	}

	//print any string to the text area
    public void appendTextArea(String s) {
    	displayArea.append(s);
    }

    public static void main(String args[]) {
       
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(FileSystemUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(FileSystemUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(FileSystemUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(FileSystemUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }

        /* Create and display the GUI */
        new FileSystemUI();
    }                   
}
