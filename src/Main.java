import java.util.concurrent.TimeUnit;

public class Main{


    public static configFile config = new configFile();
    public static ftpManager ftp = new ftpManager(config);

    //Manages the server sided config from the FTP Server
    public static clientConfigFile configFromFtp = new clientConfigFile(ftp.getClientConfigFromFtpServer());


    public static void main(String[] args){

        //Create the JFrame window
        displayWindow w1 = new displayWindow(ftp.getImageFromFtpServer());
        w1.setVisible(true);


        //Endless loop
        for(;;){

            try{
                w1.updateImage(ftp.getImageFromFtpServer());
                ftp.getClientConfigFromFtpServer();
                TimeUnit.SECONDS.sleep(configFromFtp.updateInterval);

            } catch (Exception ex){
                System.exit(-1);
            }


        }


    }

}