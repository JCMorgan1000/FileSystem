/*
 * Author: Jason C. Morgan
 * Date: November 30, 2016
 * Purpose: This program is a Java implementation of a basic file system.
 */

package fileSystem;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Paths;
import java.util.ArrayList;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

public class CustomFileSystem {
	private ArrayList<File> fileList;
	private FilenameFilter displayFilter;
	private File[] files;
	private String[] childFiles;
	private FileSystemUI fSUI;
	private File currentFile;
	private String[] menuOptionTitle = {"0 – Exit", "1 – Select directory", "2 – List directory content (first level)", "3 – List directory content (all levels)", "4 – Delete file", 
			"5 – Encrypt file (XOR with password)", "6 – Decrypt file (XOR with password)", "7 - Display Menu", "Select option: "};
	
	public CustomFileSystem(FileSystemUI fSUI) {
		this.fSUI = fSUI;
		currentFile = null;
	}
	
	public void runFileSystem(int menuOption) {
		switch (menuOption) {
		case 1:
			outPrintLn("1 – Select directory:\n");
			String fileName = JOptionPane.showInputDialog("Enter a complete directory path.");
			if(fileName == null)
				break;
			try {
				currentFile = Paths.get(fileName).toFile();
				outPrintLn("The directory you have selected is: " + currentFile.toString() + "\n");
			} catch (NullPointerException e1) {
				JOptionPane.showMessageDialog(null, "The path entered does NOT match any directory on the system!\nPlease try again.", 
						"Error", JOptionPane.ERROR_MESSAGE);
			}
			break;
			
		case 2:
			outPrintLn("2 – List directory content (first level):\n");			
			try {
				files = currentFile.listFiles();
				for (File file: files) {
					if (file.isDirectory()) {
						outPrint("directory:");
					} else {
						outPrint("     file:");
					}
					outPrintLn(file.getCanonicalPath());
				}
			}  catch (IOException e) {
				JOptionPane.showMessageDialog(null, "The file name entered does NOT match any file on the system!\nPlease try again.", 
						"Error", JOptionPane.ERROR_MESSAGE);
				break;
			} catch (NullPointerException e) {
				JOptionPane.showMessageDialog(null, "Either no directory was selected, or the path is incomplete!\nPlease try again.", 
						"Error", JOptionPane.ERROR_MESSAGE);
				break;
			}
			outPrintLn("");
			printMenu();
			break;
			
		case 3:
			outPrintLn("3 – List directory content (all levels):\n");
			fileList = new ArrayList<File>();
			printFiles(listf(currentFile, fileList));
			printMenu();
			break;
			
		case 4:
			outPrintLn("4 – Delete file:\n");
			String fName = JOptionPane.showInputDialog("Enter a file name.\n");
			if(fName == null)
				break;
			File deleteable = null;
			try {
				deleteable = findFile(fName);
			} catch (FileNotFoundException e) {
				JOptionPane.showMessageDialog(null, "The directory does not contain the file specified!\nPlease try again.", 
						"Error", JOptionPane.ERROR_MESSAGE);
				e.printStackTrace();
			} catch (NullPointerException e) {
				JOptionPane.showMessageDialog(null, "The file name entered does NOT match any file on the system!\nPlease try again.", 
						"Error", JOptionPane.ERROR_MESSAGE);
			}
			boolean deleted = deleteable.delete();
			if(deleted)
				outPrintLn("File: " + deleteable + " has been deleted.\n");
			break;
			
		case 5:
			outPrintLn("5 – Encrypt file (XOR with password):\n");
			String getEncryptFileName = JOptionPane.showInputDialog("Enter a file name.");
			if(getEncryptFileName == null)
				break;
			File getEncryptFile = null;
			try {
				getEncryptFile = findFile(getEncryptFileName);
			} catch (FileNotFoundException e) {
				JOptionPane.showMessageDialog(null, "The directory does not contain the file specified!\nPlease try again.", 
						"Error", JOptionPane.ERROR_MESSAGE);
				e.printStackTrace();
			} catch (NullPointerException e) {
				JOptionPane.showMessageDialog(null, "The file name entered does NOT match any file on the system!\nPlease try again.", 
						"Error", JOptionPane.ERROR_MESSAGE);
			}				
			String getPassword = JOptionPane.showInputDialog("Enter your password.");
			if(getPassword == null)
				break;
			encryptDecrypt(getEncryptFile, getPassword.toCharArray());
			break;
			
		case 6:
			outPrintLn("6 – Decrypt file (XOR with password):\n");
			String getDecryptFileName = JOptionPane.showInputDialog("Enter a file name.");
			if(getDecryptFileName == null)
				break;
			File getDecryptFile = null;
			try {
				getDecryptFile = findFile(getDecryptFileName);
			} catch (FileNotFoundException e) {
				JOptionPane.showMessageDialog(null, "The directory does not contain the file specified!\nPlease try again.", 
						"Error", JOptionPane.ERROR_MESSAGE);
				e.printStackTrace();
			} catch (NullPointerException e) {
				JOptionPane.showMessageDialog(null, "The file name entered does NOT match any file on the system!\nPlease try again.", 
						"Error", JOptionPane.ERROR_MESSAGE);
			}				
			String getDcrptPassword = JOptionPane.showInputDialog("Enter your password.");
			if(getDcrptPassword == null)
				break;
			encryptDecrypt(getDecryptFile, getDcrptPassword.toCharArray());
			break;
			
		case 7:
			printMenu();
			break;
			
		case 0:
			System.exit(0);
			break;
			
		default:
			outPrintLn("Invalid choice.\n");
			printMenu();
		}		
	}//end runProgram
	
	public void setMenuOption(int i) {
    	runFileSystem(i);
	}
	
	public File findFile(String st) throws java.io.FileNotFoundException {
		displayFilter = new FilenameFilter() {
		     public boolean accept
		     (File f, String name) {
		        return name.startsWith(st);
		        }
		     };
		     childFiles = currentFile.list(displayFilter);
		  if(childFiles.length > 1) {
			  outPrintLn("Your search returned these files:");
			  for (int i=0; i < childFiles.length; i++) {
				  String filename = childFiles[i];
				  outPrintLn(filename);
			  }
			  outPrintLn("Please be more specific\n");
		  }	      
	      return Paths.get(childFiles[0]).toAbsolutePath().toFile();      
	}
	
	public ArrayList<File> listf(File directory, ArrayList<File> fileList) {
	    // get all the files from a directory
		try {
			File[] fList = directory.listFiles();
			for (File file : fList) {
		        if (file.isFile()) {
		            fileList.add(file);
		        } else if (file.isDirectory()) {
		        	fileList.add(file);
		            listf(file, fileList);
		        }
		    }
		} catch(NullPointerException e) {
			JOptionPane.showMessageDialog(null, "Either no directory was selected, or the path is incomplete!\nPlease try again.", 
					"Error", JOptionPane.ERROR_MESSAGE);
		}
		return fileList;
	}
	//encrypts and decrypts a file using XOR encryption algorithm
	private void encryptDecrypt(File cryptedFile, char[] key) {
		byte bytes;
		int pos = 0;
		OutputStream oStream;
		BufferedReader bR;
		try {
			oStream = new FileOutputStream(getSaveToFile());
			bR = new BufferedReader(new FileReader(cryptedFile));
			while(bR.ready()) {
				bytes = (byte) bR.read();
				oStream.write((byte) (bytes ^ key[pos++]));
				if(pos == 7) pos = 0;
			}
			bR.close();
			oStream.close();
		} catch (FileNotFoundException e) {
			JOptionPane.showMessageDialog(null, "The directory does not contain the file specified!\nPlease try again.", 
					"Error", JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null, "An error occured while attempting to save a file!\nPlease try again.", 
					"Error", JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
		}
	}	
	//open "save as" dialog and return a file
	private File getSaveToFile() {
		JFileChooser fc = new JFileChooser();
        int returnVal = fc.showSaveDialog(null);
        if(returnVal == JFileChooser.APPROVE_OPTION) {
            if(fc.getSelectedFile() == null) {
                JOptionPane.showOptionDialog(null,
                    "You did not select a file",
                    "File Open Error",
                    JOptionPane.DEFAULT_OPTION,
                    JOptionPane.WARNING_MESSAGE, null,
                    null, null
                );
                getSaveToFile();
            }
        }
        return new File(fc.getSelectedFile().getPath());
	}	
	//print menue
    public void printMenu() {
    	for (String i: menuOptionTitle)
    		fSUI.appendTextArea(i + "\n");
    	fSUI.appendTextArea("\n");
    }
    //print list of files to the text area
    public void printFiles(ArrayList<File> files) {
    	for (File file: files) {
			if (file.isDirectory()) {
				outPrint("directory:");
			} else {
				outPrint("     file:");
			}
			try {
				outPrintLn(file.getCanonicalPath());
			} catch (IOException e) {
				JOptionPane.showMessageDialog(null, "Either no directory was selected, or the path is incomplete!\nPlease try again.", 
						"Error", JOptionPane.ERROR_MESSAGE);
				e.printStackTrace();
			}
		}
    	outPrintLn("");
    }
    
    //print any string to console
    public void outPrintLn(String s) {
    	fSUI.appendTextArea(s + "\n");
    }
    
    //print any string to console
    public void outPrint(String s) {
    	fSUI.appendTextArea(s);
    }
    
}