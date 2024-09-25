/***************************************************************
Student Name: Allan Chung
File Name: App.java. Checks misspellings of words and output possible words for the user.
Assignment number: Project 2

Description: JavaFX SpellChecker Application. The spell checker option will identify misspelled words on the conditions that  
it has an extra letter, missing letter, or two letters swapped. After it identifies the misspelled words, it will 
output suggested words that the user possibly intended to spell. 
***************************************************************/
package uwf.project2;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashSet;
import java.util.Scanner;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;

public class App extends Application {

    TextArea textArea = new TextArea();
    HashSet<String> hashWords;
	File file;

    @Override
    public void start(Stage stage) throws Exception{        
        MenuBar menuBar = new MenuBar();
        
        Menu fileMenu = new Menu("File");
        Menu editMenu = new Menu("Edit");
        
        
        MenuItem open = createOpenItem(stage);
        MenuItem save = createSaveItem(stage);
        MenuItem exit = createExitItem();
        
		hashWords = new HashSet<String>();
		Scanner scanWords = null;
		file = new File("/Users/allanchung/Downloads/Words.txt");
		try {
			scanWords = new Scanner(file);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		while(scanWords.hasNextLine()) {
				hashWords.add(scanWords.nextLine());	
		}

		scanWords.close();
		
        MenuItem spellCheck = createSpellCheckItem(file);
        fileMenu.getItems().addAll(open, save, exit);
        menuBar.getMenus().addAll(fileMenu, editMenu);
        editMenu.getItems().addAll(spellCheck);
       
        textArea.setWrapText(true);
        textArea.getText();
        
        BorderPane root = new BorderPane();
        root.setTop(menuBar);
        root.setCenter(textArea);
        Scene scene = new Scene(root, 300, 250); 
        scene.getRoot();
        scene.getRoot().setStyle("-fx-font-family: 'serif'"); 
        
        stage.setScene(scene);        
        stage.setTitle("Title");
        stage.setScene(scene);
        stage.show();
    }

    private MenuItem createOpenItem(Stage stage) throws Exception{
    	MenuItem openItem = new MenuItem("Open");
        openItem.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
               	FileChooser fileChooser = new FileChooser();
            	FileChooser.ExtensionFilter extensionFilter = new FileChooser.ExtensionFilter("TXT files (*.txt)", "*.txt");
            	fileChooser.getExtensionFilters().add(extensionFilter);
            	File file = fileChooser.showOpenDialog(stage);
            	try(Scanner scnr = new Scanner(file)){
            		while (scnr.hasNextLine()) {
            			textArea.appendText(scnr.nextLine());
            		}
            	}
            	catch(FileNotFoundException e) {
            		e.getMessage();
            	}
            }
        });       
        return openItem;
    }
    
    private MenuItem createSaveItem(Stage stage) throws Exception{
    	MenuItem saveItem = new MenuItem("Save");
        saveItem.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
            	FileChooser fileChooser = new FileChooser();
            	fileChooser.setInitialDirectory(new File("."));
            	File file = fileChooser.showSaveDialog(null);
            }
        });   
    	return saveItem;
    }
    
    private MenuItem createExitItem() {
    	MenuItem exitItem = new MenuItem("Exit");
        exitItem.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                System.exit(0);
            }
        });
    	return exitItem;
    }
    
    private MenuItem createSpellCheckItem(File file) {
    	MenuItem spellCheckItem = new MenuItem("Spell Check");
        
    	spellCheckItem.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				String texts = new String(textArea.getText());
				String[] words = texts.split("[ ,.]+");	
				for(int i = 0; i < words.length; i++) {
					if(hashWords.contains(words[i].toLowerCase())) {
						continue;
					}
					else {
						HashSet<String> foundWords = new HashSet<String>();
						for(int z = 0; z <= words[i].length(); z++) {
							char letter = 'a';
							for(int j = 0; j < 26; j++) {
								StringBuffer stringBuffer = new StringBuffer(words[i]);
								stringBuffer.insert(z, letter);								
								if(hashWords.contains(stringBuffer.toString().toLowerCase()) && !foundWords.contains(stringBuffer.toString().toLowerCase())) {
									foundWords.add(stringBuffer.toString());
								}
								letter++;
							}	
						}
						

						for(int a = 0; a < words[i].length(); a++) {
								StringBuilder removeLetter = new StringBuilder(words[i]);
								removeLetter.deleteCharAt(a);
								if(hashWords.contains(removeLetter.toString().toLowerCase())) {
									foundWords.add(removeLetter.toString());
								}	
						}

						for(int q = 0; q < words[i].length()-1; q++) {
							char[] swapWordsArray = words[i].toCharArray();
							
							char temp = swapWordsArray[q];
							swapWordsArray[q] = swapWordsArray[q+1];
							swapWordsArray[q+1] = temp;
							String swappedWord = new String(swapWordsArray);
							if(hashWords.contains(swappedWord.toString().toLowerCase())) {
								foundWords.add(swappedWord.toString());
							}

						}
						
						String foundWordsString = foundWords.toString();
						if(foundWords.size() == 0) {
							foundWordsString = "not found";
						}
						Alert alert = new Alert(AlertType.INFORMATION);
						alert.setHeaderText("This word is misspelled");
						String s = "For " + words[i] + ", possible intended words are " + foundWordsString;
						alert.setContentText(s);
						System.out.println("For " + words[i] + ", possible intended words are " + foundWordsString);
						alert.show();					
					}
				}
		}
        });
    	return spellCheckItem;
    }
    
    public static void main(String[] args) {
        Application.launch(args);
    }

}