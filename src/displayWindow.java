import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;


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
    displayWindow(Image img){

        //Maximize the JFrame and disabling bar + border
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setUndecorated(true);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        hideCursor();

        Image s_img = img.getScaledInstance(width,height,Image.SCALE_SMOOTH);

        ic = new ImageIcon(s_img);
        lPicture = new JLabel(ic);
        add(lPicture);

    }

    //Updates the image and the display window HOPEFULLY
    public void updateImage(Image img){

        Image s_img = img.getScaledInstance(width,height,Image.SCALE_SMOOTH);
        ic = new ImageIcon(s_img);
        lPicture.setIcon(ic);
        System.gc();

    }

    //Hides the cursor
    private void hideCursor() {
        BufferedImage cursorImg = new BufferedImage(16,16,BufferedImage.TYPE_INT_ARGB);
        Cursor blankCursor = Toolkit.getDefaultToolkit().createCustomCursor(cursorImg, new Point(0,0), "blank cursor");
        getContentPane().setCursor(blankCursor);
    }

    //Debug constructor
    displayWindow(int i){

        try{
            setExtendedState(JFrame.MAXIMIZED_BOTH);
            setUndecorated(true);
            setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

            Image img = ImageIO.read(new File("C:\\Users\\Daniel\\Desktop\\E8e_HQ5X0AgS509.png"));
            Image s_img = img.getScaledInstance(width,height,Image.SCALE_SMOOTH);

            Icon ic = new ImageIcon(s_img);
            lPicture = new JLabel(ic);
            add(lPicture);

        } catch (IOException ex){
            JOptionPane.showMessageDialog(null,"Error loading the Picture.");
            return;
        }





    }

}
