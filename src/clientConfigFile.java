import javax.swing.*;

public class clientConfigFile {


    //Standard update interval
    public int updateInterval = 30;

    clientConfigFile(String s){

        if(s == null){
            return;
        }

        formatConfigAndInitializeParameters(s);

    }

    public void formatConfigAndInitializeParameters(String rawConfig) {

        if(rawConfig == null){
            return;
        }

        String[] temp;
        temp = rawConfig.split(";");

        if(temp.length != 1){
            JOptionPane.showMessageDialog(null,"Config file is corrupted or not set up correctly on the server.");
            return;
        }

        updateInterval = Integer.parseInt(temp[0]);



    }

}
