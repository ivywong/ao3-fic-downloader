import java.io.*;
import java.util.*;
import org.jsoup.*;
import org.apache.commons.io.FileUtils.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Downloader extends JFrame implements ActionListener{

    /* Notes: use file.getAbsolutePath() for path
     * JFileChooser chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
     * FileUtils.copyURLToFile(URL source, File destination,connectTO,readTimeout) (ms)
     * 
     */
    
    private static Downloader d;
    private JFileChooser chooser;
    private JTextField url, destination;
    private JButton browse, download, cancel;
    private Container bg;

 
    public static Downloader getInstance(){
        if(d == null){
            d = new Downloader();
        }
        return d;
    }

    public void init(){
        bg = this.getContentPane();
        chooser = new JFileChooser();
        url = new JTextField("URL here", 45);
        destination = new JTextField("Dest here", 40);
        download = new JButton("Download");
        browse = new JButton("Browse...");
        cancel = new JButton("Cancel");
        

    }
    
    public void setup(){
        d.setTitle("AO3 Downloader");
        d.setSize(600,400);
        d.setLocationRelativeTo(null);
        d.setDefaultCloseOperation(EXIT_ON_CLOSE);
        chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

        url.addActionListener(this);
        destination.addActionListener(this);
        download.addActionListener(this);
        browse.addActionListener(this);
        cancel.addActionListener(this);
        chooser.addActionListener(this);
        
        bg.setLayout(new FlowLayout());
        bg.add(new JLabel("URL:"));
        bg.add(url);
        bg.add(new JLabel("Destination:"));
        bg.add(destination);
        //bg.add(chooser);
        bg.add(browse);
        bg.add(download);
        bg.add(cancel);

    }

    public void run(){
        d.init();
        d.setup();
        System.out.println("Running...");
        d.setVisible(true);
    }
    

    public static void main(String[] args){
        Downloader.getInstance().run();
    }

    public void actionPerformed(ActionEvent e){
        String command = e.getActionCommand();
        System.out.println(e.getActionCommand());
        if(command.equals("Browse...")){
            //show file browser
            int result = chooser.showSaveDialog(this);
    
            //if 'Save', change text in dest. text field to chosen destination
            if(result == JFileChooser.APPROVE_OPTION){
                String dest = chooser.getSelectedFile().getAbsolutePath();
                System.out.println(dest);
                destination.setText(dest);
            }
        } else if(command.equals("Download")){
            String src = url.getText();
            String dest = destination.getText();
            //check if URL string is valid
            //then do HTML parsing stuff
            //download every work in series
        }
    }


}
