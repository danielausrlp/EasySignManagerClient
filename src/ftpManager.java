import org.apache.commons.net.ftp.*;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import javax.imageio.ImageIO;


public class ftpManager {

    public FTPClient client;
    configFile configFtp;
    ftpManager(configFile cf){
        configFtp = cf;
    }

    //Tries to open an FTP connection, returns 0 on success, -1 on failure
    //make sure to disconnect after usage
    private int openConnection(configFile cf) {

        client = new FTPClient();

        try{

            client.connect(cf.ftpAddress);
            client.enterLocalPassiveMode();
            client.login(cf.ftpUsername,cf.ftpPassword);
            System.out.println(client.getReplyString());

            if(client.getReplyString().contains("530")){
                //JOptionPane.showMessageDialog(null,"Credentials to the FTP Server are incorrect.");
                System.out.println("Credentials to the FTP Server are incorrect.");
                client = null;
                return -1;
            }


        } catch (Exception ex){
            //JOptionPane.showMessageDialog(null,"Couldn't connect to FTP Server. ");
            System.out.println("Couldn't connect to FTP Server. ");
            //client = null;
            return -1;
        }

        return 0;

    }

    //Get the image from the FTP Server
    public BufferedImage getImageFromFtpServer(){

        BufferedImage temp;

        //do nothing
        if(openConnection(configFtp) == -1)
            return null;

        try{


            //utterly retarded
            client.setFileType(FTP.BINARY_FILE_TYPE);
            InputStream is = client.retrieveFileStream("/"+ configFtp.roomId + "/" + "bild.png");
            BufferedInputStream bs = new BufferedInputStream(is);
            temp = convertBufferedInputStreamToPng(bs);

            is.close();
            bs.close();
            System.gc();
            client.logout();
            client.disconnect();

            if(temp == null){
                System.out.println("Error in saving image. Why is img == null??");
                return null;
            }

        } catch (Exception ex){
            //JOptionPane.showMessageDialog(null,"Couldn't download image from FTP Server. " + ex.getMessage());
            System.out.println("Couldn't download image from FTP Server. " + ex.getMessage());

            try{
                client.logout();
                client.disconnect();
            } catch (IOException exx){
                System.exit(-1);
            }

            return null;
        }


        return temp;
    }

    //Get the date reserved image from the FTP Server NOT USED
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
            System.gc();
            client.logout();
            client.disconnect();

            if(temp == null){
                JOptionPane.showMessageDialog(null,"Error in saving image. " + client.getReplyString());
                client.logout();
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

    //whatever
    public byte[] getBytesFromReservedImage() {

        byte[] temp;

        //do nothing
        if(openConnection(configFtp) == -1)
            return null;

        try{

            client.setFileType(FTP.BINARY_FILE_TYPE);
            InputStream is = client.retrieveFileStream("/"+ configFtp.roomId + "/" + "bild2.png");
            BufferedInputStream bs = new BufferedInputStream(is);
            temp = bs.readAllBytes();
            is.close();
            bs.close();
            System.gc();
            client.logout();
            client.disconnect();

            if(temp == null){
                //JOptionPane.showMessageDialog(null,"Error in saving image. " + client.getReplyString());
                System.out.println("Error in saving image. " + client.getReplyString());
                return null;
            }


        } catch (Exception ex){
            //JOptionPane.showMessageDialog(null,"Couldn't download image from FTP Server. " + client.getReplyString());
            System.out.println("Couldn't download image from FTP Server. " + ex.getMessage());

            try{
                client.logout();
                client.disconnect();
            } catch (IOException exx){
                System.exit(-1);
            }

            return null;
        }

        return temp;
    }

    //Uploads the new server-sided config file after successful download of the date reserved image and deletes the old temp image and replaces the new one
    //Deletes the date from the server-sided config to uncheck the box in the main esm
    public void cleanUpAfterReservedImageDownload(clientConfigFile ccf){

        String remoteConfig = "/"+ configFtp.roomId + "/" + "config.txt";
        String remoteImage = "/" + configFtp.roomId + "/" + "bild.png";
        String remoteTempImage = "/" + configFtp.roomId + "/" + "bild2.png";

        if(openConnection(configFtp) == -1)
            return;

        byte[] data = getBytesFromReservedImage(); //bullshit
        data = convertByteArrayToPng(data);


        try{

            if(openConnection(configFtp) == -1)
                return;

            ByteArrayInputStream bis = new ByteArrayInputStream(data);
            client.setFileType(FTP.BINARY_FILE_TYPE);
            client.storeFile(remoteImage, bis);


            if(openConnection(configFtp) == -1)
                return;


            if(!client.deleteFile(remoteTempImage)){
                client.logout();
                client.disconnect();
                System.out.println("Couldn't delete the temporary image from the FTP Server.");
                throw new Exception();
            }
            client.logout();
            client.disconnect();



        } catch (Exception ex){
            //JOptionPane.showMessageDialog(null,"Couldn't change the temporary image." + client.getReplyString());
            System.out.println("Couldn't change the temporary image." + ex.getMessage());
            return;
        }


        //Rewriting the config file
        if(openConnection(configFtp) == -1)
            return;

        try {

            InputStream is = new ByteArrayInputStream(ccf.getClientConfigWithoutDate().getBytes());
            if(!client.storeFile(remoteConfig,is)){
                client.logout();
                client.disconnect();
                is.close();
                System.out.println("Couldn't store the server-sided config to the FTP Server.");
                throw new Exception();
            }
            client.logout();
            client.disconnect();
            is.close();

        } catch (Exception ex){
            //JOptionPane.showMessageDialog(null,"Couldn't change the server-sided config. " + client.getReplyString());
            System.out.println("Couldn't change the server-sided config. " + ex.getMessage());


        }




    }

    //Get the client config from the server and returns the string.
    public String getClientConfigFromFtpServer() {

        String temp;

        //do nothing
        if(openConnection(configFtp) == -1){
            return null;
        }

        try{

            if(client.getReplyString().contains("425")){
                JOptionPane.showMessageDialog(null,"wtf.");
                client = null;
                return null;
            }

            String[] names = client.listNames("/"+ configFtp.roomId);

           for(String s : names){

               if(s.contains("config.txt")){
                   InputStream is = client.retrieveFileStream("/"+ configFtp.roomId + "/config.txt");
                   BufferedReader br = new BufferedReader(new InputStreamReader(is));

                   if(client.getReplyString().contains("425")){
                       JOptionPane.showMessageDialog(null,"wtf.");
                       client = null;
                       return null;
                   }

                   temp = br.readLine();
                   System.out.println(temp);
                   client.logout();
                   client.disconnect();
                   return temp;
               }

           }
        } catch (Exception ex){
            JOptionPane.showMessageDialog(null,ex.getMessage());
            return null;
        }


        return null;
    }

    //converts a bufferedinputstream to a png format picture
    public BufferedImage convertBufferedInputStreamToPng(BufferedInputStream bs){

        try{
            BufferedImage temp;

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            temp = ImageIO.read(bs);
            ImageIO.write(temp,"png",baos);
            ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
            baos.close();
            bais.close();
            return ImageIO.read(bais);

        } catch (IOException ex){
            System.out.println( "Error converting BufferedInputStream to png."+ ex.getMessage());
            return null;
        }

    }

    //converts a byte[] to a byte[] with png formatting
    public byte[] convertByteArrayToPng(byte[] data){

        try{

            ByteArrayInputStream bais = new ByteArrayInputStream(data);
            BufferedImage temp = ImageIO.read(bais);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(temp,"png",baos);
            bais.close();
            baos.close();
            return baos.toByteArray();

        } catch (IOException ex) {
            System.out.println( "Error converting byte[] to png."+ ex.getMessage());
            return null;
        }



    }



}
