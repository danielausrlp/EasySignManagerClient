import org.apache.commons.net.ftp.*;
import javax.swing.*;
import java.awt.*;
import java.io.*;
import javax.imageio.ImageIO;


public class ftpManager {

    public FTPClient client;
    configFile configFtp;
    ftpManager(configFile cf){
        configFtp = cf;
    }

    //Tries to open an FTP connection, returns 0 on success, -1 on failure
    private int openConnection(configFile cf) {

        client = new FTPClient();

        try{

            client.connect(cf.ftpAddress);
            client.login(cf.ftpUsername,cf.ftpPassword);
            System.out.println(client.getReplyString());

            if(client.getReplyString().contains("530")){
                JOptionPane.showMessageDialog(null,"Credentials to the FTP Server are incorrect.");
                client = null;
                return -1;
            }


        } catch (Exception ex){
            JOptionPane.showMessageDialog(null,"Couldn't connect to FTP Server. ");
            client = null;
            return -1;
        }

        return 0;

    }

    //Get the image from the FTP Server
    public Image getImageFromFtpServer(){

        Image temp;

        //Kill program if connection failed
        if(openConnection(configFtp) == -1)
            System.exit(-1);

        try{

            InputStream is = client.retrieveFileStream("/"+ configFtp.roomId + "/" + "bild.png");
            BufferedInputStream bs = new BufferedInputStream(is);
            temp = ImageIO.read(bs);
            is.close();
            bs.close();
            //WTF?
            System.gc();
            client.disconnect();

            if(temp == null){
                JOptionPane.showMessageDialog(null,"Error in saving image. " + client.getReplyString());
                client.disconnect();
                return null;
            }


        } catch (Exception ex){
            JOptionPane.showMessageDialog(null,"Couldn't download image from FTP Server. " + client.getReplyString());
            System.out.println(ex.getMessage());
            return null;
        }


        return temp;
    }

    //Get the date reserved image from the FTP Server
    public Image getReservedImageFromFtpServer(){

        Image temp;

        //Kill program if connection failed
        if(openConnection(configFtp) == -1)
            System.exit(-1);

        try{

            InputStream is = client.retrieveFileStream("/"+ configFtp.roomId + "/" + "bild2.png");
            BufferedInputStream bs = new BufferedInputStream(is);
            temp = ImageIO.read(bs);
            is.close();
            bs.close();
            //WTF?
            System.gc();
            client.disconnect();

            if(temp == null){
                JOptionPane.showMessageDialog(null,"Error in saving image. " + client.getReplyString());
                client.disconnect();
                return null;
            }


        } catch (Exception ex){
            JOptionPane.showMessageDialog(null,"Couldn't download image from FTP Server. " + client.getReplyString());
            System.out.println(ex.getMessage());
            return null;
        }


        return temp;
    }

    //Uploads the new server-sided config file after successful download of the date reserved image and deletes the old temp image and replaces the new one
    //Deletes the date from the server-sided config to uncheck the box in the main esm
    //TODO: implement
    public void cleanUpAfterReservedImageDownload(){

    }

    //Get the client config from the server and returns the string.
    public String getClientConfigFromFtpServer() {

        String temp;

        //Kill program if connection failed
        if(openConnection(configFtp) == -1){
            System.exit(-1);
        }

        try{

           String[] names = client.listNames("/"+ configFtp.roomId);

           for(String s : names){

               if(s.contains("config.txt")){
                   InputStream is = client.retrieveFileStream("/"+ configFtp.roomId + "/config.txt");
                   BufferedReader br = new BufferedReader(new InputStreamReader(is));

                   temp = br.readLine();
                   System.out.println(temp);

                   return temp;
               }

           }
        } catch (Exception ex){
            JOptionPane.showMessageDialog(null,ex.getMessage());
            return null;
        }


        return null;
    }

}
