import java.io.*;
import java.util.*;
import java.net.*;

import org.jsoup.*;
import org.jsoup.nodes.*;
import org.jsoup.select.*;
import org.apache.commons.io.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.lang.System.*;

public class Downloader extends JFrame implements ActionListener{

    /*
    */


    private static Downloader d;
    private JFileChooser chooser;
    private JTextField url, destination;
    private JButton browse, check, download, cancel;
    private JLabel status, info;
    private Container bg;

    private String src, dest;
    private int counter;
    private boolean numberFics;


    public static Downloader getInstance(){
        if(d == null){
            d = new Downloader();
        }
        return d;
    }

    //GUI Setup

    public void init(){
        bg = this.getContentPane();
        //chooser = new JFileChooser(new File(System.getProperty("user.dir")));
        //TESTING DIRECTORY
        chooser = new JFileChooser(new File("C:\\Users\\home\\Dropbox\\Fanfics\\Testing"));
        url = new JTextField("http://archiveofourown.org/series/14493", 45);
        destination = new JTextField("C:\\Users\\home\\Dropbox\\Fanfics\\Testing", 40);
        download = new JButton("Download");
        browse = new JButton("Browse...");
        check = new JButton("Check");
        cancel = new JButton("Cancel");
        status = new JLabel();
        info = new JLabel();

        src = "";
        dest = "";
        numberFics = false;


    }

    public void setup(){
        d.setTitle("AO3 Downloader");
        d.setSize(600,300);
        d.setLocationRelativeTo(null);
        d.setDefaultCloseOperation(EXIT_ON_CLOSE);
        chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

        url.addActionListener(this);
        destination.addActionListener(this);
        download.addActionListener(this);
        browse.addActionListener(this);
        check.addActionListener(this);
        cancel.addActionListener(this);
        chooser.addActionListener(this);

        bg.setLayout(new FlowLayout());
        bg.add(new JLabel("URL:"));
        bg.add(url);
        bg.add(new JLabel("Destination:"));
        bg.add(destination);
        bg.add(browse);
        bg.add(check);
        bg.add(download);
        bg.add(cancel);
        bg.add(status);
        bg.add(info);

    }

    public void run(){
        d.init();
        d.setup();
        System.out.println("Running...");
        d.setVisible(true);
    }

    public void close(){
        //perhaps delete in-progress download here
        d.dispose();
        System.exit(0);
    }


    public static void main(String[] args){
        Downloader.getInstance().run();
    }

    public void actionPerformed(ActionEvent e){
        String command = e.getActionCommand();
        if(command.equals("Browse...")){
            //show file browser
            int result = chooser.showSaveDialog(this);

            //if 'Save', change text in dest. text field to chosen destination
            if(result == JFileChooser.APPROVE_OPTION){
                String dest = chooser.getSelectedFile().getAbsolutePath();
                System.out.println(dest);
                destination.setText(dest);
            }
        } else if(command.equals("Check")){
            src = url.getText();
            getSeriesInfo();
        } else if(command.equals("Download")){
            src = url.getText();
            dest = destination.getText();
            //check if URL string is valid
            //then do HTML parsing stuff
            //download every work in series
            //status.setText("<html>Downloading fic from " + src + "<br>" + "Destination: " + dest + "</html>");
            getSeriesInfo();
            downloadSeries();
        } else if(command.equals("Cancel")){
            Downloader.getInstance().close();
        }
    }

    //HTML handling

    // Get information about series.
    public void getSeriesInfo(){
        Document series;
        String ficInfo = "";
        try {
            series = Jsoup.connect(src).get();
            Elements links = series.select("a[href~=/works/\\d+$]"); //Find all links to work
            String title = series.select("h2.heading").first().text();
            ficInfo += ("Series Title: " + title + "\n");
            info.setText(ficInfo);
            //info.setText("<html>Downloading...</html>");
            System.out.println(ficInfo);
            
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    //Download series of fics with url.
    public void downloadSeries(){
        Document series;
        String ficInfo = "";
        try {
            series = Jsoup.connect(src).get();
            Elements links = series.select("a[href~=/works/\\d+$]"); //Find all links to work
            String title = series.select("h2.heading").first().text();
            ficInfo += ("Series Title: " + title + "\n");
            info.setText(ficInfo);
            if(links.size() > 1){
                dest += ("\\" + title + "\\");
                System.out.println("Destination: " + dest);
                numberFics = true;
            }

            for(Element link : links){
                counter++;
                String ficLink = "http://www.archiveofourown.org" + link.attr("href");
                downloadFic(ficLink);
            }

            System.out.println("Processed.");
            counter = 0;
 
        } catch (IOException e){
            e.printStackTrace();
        }

    }

    // Download single fic with url.
    public void downloadFic(String link){
        String ficInfo = "";
        String format = "epub";
        try {
            Document fic = Jsoup.connect(link+"?view_adult=true").get();
            String title = fic.select("h2.heading").first().text();
            //info.setText(title);
            Element dlink = fic.select("a[href~=/downloads.*" + format + "]").first();
            String dl = "http://www.archiveofourown.org" + dlink.attr("href");
            String filename = String.format("%s.%s", title, format);
            if(numberFics){
                filename = String.format("%03d-%s",counter,filename);
            }
            //System.out.println(filename);
            FileUtils.copyURLToFile(new URL(dl), new File(dest + filename), 15000, 30000); 
            System.out.println("Downloading " + filename);

        } catch (IOException e){
            e.printStackTrace();
        }
    }   

}
