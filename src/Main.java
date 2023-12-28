import java.util.concurrent.TimeUnit;

public class Main{


    public static configFile config = new configFile();
    public static ftpManager ftp = new ftpManager(config);



    public static void main(String[] args){

        displayWindow w1 = new displayWindow(ftp.getImageFromFtpServer());
        w1.setVisible(true);

        for(;;){

            try{
                w1.updateImage(ftp.getImageFromFtpServer());
                TimeUnit.SECONDS.sleep(30);
            } catch (Exception ex){
                System.exit(-1);
            }


        }


    }

}