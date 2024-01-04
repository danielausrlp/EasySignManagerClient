import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.lang.management.MemoryUsage;
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

                int megabyte = 1024*1024;

                MemoryMXBean mB = ManagementFactory.getMemoryMXBean();
                MemoryUsage mU = mB.getHeapMemoryUsage();
                long maxMemory = mU.getMax() / megabyte;
                long usedMemory = mU.getUsed() / megabyte;

                if(configFromFtp.isTimeOver()){ //it fucking works
                    ftp.cleanUpAfterReservedImageDownload(configFromFtp);
                    w1.updateImage(ftp.getImageFromFtpServer());
                    System.out.println("Max Memory: " + maxMemory + " Used Memory: " + usedMemory);

                } else {
                    w1.updateImage(ftp.getImageFromFtpServer());
                    configFromFtp = new clientConfigFile(ftp.getClientConfigFromFtpServer());
                    System.out.println("Max Memory: " + maxMemory + " Used Memory: " + usedMemory);
                    //Just in case, i don't trust java
                    System.gc();
                }

                TimeUnit.SECONDS.sleep(configFromFtp.updateInterval);

            } catch (Exception ex){
                System.out.println(ex.getMessage());
                System.exit(-1);
            }


        }


    }

}