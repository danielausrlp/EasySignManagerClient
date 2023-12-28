import javax.swing.*;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class configFile {

    //Get the current working directory
    public String currentDirectory = System.getProperty("user.dir");
    //Sets the config path
    public String configPath = currentDirectory + "/config.txt";
    //Converts String configPath to Path pConfigPath
    public Path pConfigPath = Paths.get(configPath);
    //FTP Parameters
    public String ftpAddress,ftpUsername,ftpPassword,roomId;


    //Calls both functions to initialize parameters properly
    configFile(){
        formatConfigAndInitializeParameters(loadConfigFromFile());
    }

    //Loads and returns the config file (String). If it doesn't exist, create new file. Returns null on failure
    private String loadConfigFromFile(){

        String temp;

        File cf = new File(configPath);

        try{

            if(cf.createNewFile()){
                JOptionPane.showMessageDialog(null,"A new config file was created. Please set it up correctly.");
                return null;
            }
            //reads the first line of the config
            temp = Files.readAllLines(pConfigPath).get(0);

        } catch (Exception ex){
            JOptionPane.showMessageDialog(null,"Couldn't read / create config file.");
            return null;
        }


        return temp;

    }

    //Initializes the FTP Parameters from the config file, doesn't do anything when config file is corrupt or whatever
    private void formatConfigAndInitializeParameters(String rawConfig) {

        if(rawConfig == null){
            return;
        }

        String[] temp;
        temp = rawConfig.split(";");

        if(temp.length != 4){
            JOptionPane.showMessageDialog(null,"Config file is corrupted or not set up correctly.");
            return;
        }

        ftpAddress = temp[0];
        ftpUsername = temp[1];
        ftpPassword = temp[2];
        roomId = temp[3];


    }


}
