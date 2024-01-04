import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;


//Display class for displaying the picture, make sure to call setVisible()
public class displayWindow extends JFrame {

    //JLabel to display the picture
    JLabel lPicture;

    //Get the dimensions of the screen to scale image to full screen
    Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    int width = (int)screenSize.getWidth();
    int height = (int)screenSize.getHeight();



    Icon ic;


    //Main constructor
    displayWindow(BufferedImage img){

        //Maximize the JFrame and disabling bar + border
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setUndecorated(true);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        hideCursor();

        //stupid linux full screen
        GraphicsEnvironment graphics = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice device = graphics.getDefaultScreenDevice();

        assert(img != null);
        //Image s_img = img.getScaledInstance(width,height,Image.SCALE_SMOOTH);

        BufferedImage resizedImage = new BufferedImage(width,height,BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = resizedImage.createGraphics();
        g2d.drawImage(img,0,0,width,height,null);
        g2d.dispose();


        ic = new ImageIcon(resizedImage);
        lPicture = new JLabel(ic);
        add(lPicture);

        device.setFullScreenWindow(this);

    }

    //Updates the image and the display window HOPEFULLY
    public void updateImage(Image img){

        //Image s_img = img.getScaledInstance(width,height,Image.SCALE_SMOOTH);
        BufferedImage resizedImage = new BufferedImage(width,height,BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = resizedImage.createGraphics();
        g2d.drawImage(img,0,0,width,height,null);
        g2d.dispose();
        ic = new ImageIcon(resizedImage);
        lPicture.setIcon(ic);
        System.gc();

    }

    //Hides the cursor
    private void hideCursor() {
        BufferedImage cursorImg = new BufferedImage(16,16,BufferedImage.TYPE_INT_ARGB);
        Cursor blankCursor = Toolkit.getDefaultToolkit().createCustomCursor(cursorImg, new Point(0,0), "blank cursor");
        getContentPane().setCursor(blankCursor);
    }


}
