/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package myclientfloweralbumservice;

import java.awt.GridLayout;
import java.awt.Image;
import java.util.List;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 *
 * @author biar
 */
public class MyClientFlowerAlbumService {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        
        //all flowers
        List<Image> images = getThumbnails();
        
        //one flower at the time
        //List<Image> images = new ArrayList<Image>();
        //images.add(getFlower("rose"));

        JFrame frame = new JFrame();
        
        //JPanel mainPanel = new JPanel(new GridLayout(2, 2));
        JPanel mainPanel = new JPanel(new GridLayout(1, images.size()));
        
        for(Image i : images) {
            
            JLabel lblimage = new JLabel(new ImageIcon(i));
            
            mainPanel.add(lblimage, -1);
  
        }
        
        frame.add(mainPanel);
        //frame.setSize(300, 400);
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setVisible(true);
        
    }

    private static Image getFlower(java.lang.String name) {
        myclientfloweralbumservice.FlowerAlbumService_Service service = new myclientfloweralbumservice.FlowerAlbumService_Service();
        myclientfloweralbumservice.FlowerAlbumService port = service.getFlowerAlbumServicePort();
        return port.getFlower(name);
    }

    private static java.util.List<java.awt.Image> getThumbnails() {
        myclientfloweralbumservice.FlowerAlbumService_Service service = new myclientfloweralbumservice.FlowerAlbumService_Service();
        myclientfloweralbumservice.FlowerAlbumService port = service.getFlowerAlbumServicePort();
        return port.getThumbnails();
    }
    
}
