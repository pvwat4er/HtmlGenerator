package com.jcourse.pvwat4er;

import java.io.*;
import java.util.*;

public class HtmlGenerator {
    public File file;
    public File[] s;
    public String[] href;


    HtmlGenerator(String fname) throws IOException {

        file = new File(fname);
        s = file.listFiles();

        Arrays.sort(s, new Comparator<File>() {
            public int compare(File o1, File o2) {
                if (o1.isDirectory() && o2.isDirectory()) {
                    return o1.getName().compareTo(o2.getName());
                } else if (o1.isDirectory()) {
                    return -1;
                } else if (o2.isDirectory()) {
                    return 1;
                } else {
                    return o1.getName().compareTo(o2.getName());
                }
            }
        });


    }


    public final static String HEADER = "<html>\n" +
            "<head>\n" +
            "    <title></title>\n" +
            "</head>\n" +
            "<body>\n" +
            "<table>\n" +
            "    <tr>\n" +
            "        <td>Name</td>\n" +
            "        <td>Size</td>\n" +
            "        <td>Last Modified</td>\n" +
            "    </tr>";
    public final static String ROW = "</table>\n" +
            "</body>\n" +
            "</html>";

    public static void main(String[] args) {
        try {
            HtmlGenerator index = new HtmlGenerator(args[0]);
            index.genHtml(null);
            index.createFile(args[0]);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String[] genHtml(String parent[]) {
        String parentDir;
        String subDir = "";

        int i = 1;

        if (parent != null) {
            int j= 0;
            for (String d : parent) {
                subDir += d + "/";

            }
            if (parent.length == 1) {
                parentDir = "/";
            } else parentDir = parent[parent.length - 2];

            href = new String[s.length + 3];
            System.out.println("CSubDirectory is " + subDir);

            if (parentDir != null) {
                i = 2;
                href[1] = ("<tr><td><a href = " + "'" + "http://localhost:8080/" + parentDir + "'>" + " .. </a></td><td></td><td>"
                        + (new Date(file.getParentFile().lastModified()).toString()) + "</td>");
            }

        } else href = new String[s.length + 2];

        href[0] = HEADER;

        String name;
        for (File srt : s) {
            if (parent != null) {
                System.out.println("srt.getName() is " + srt.getName());
                name = "http://localhost:8080/" + subDir + srt.getName();
                System.out.println("name is " + name);
            } else name = "http://localhost:8080/" + srt.getName();

            String time = new Date(srt.lastModified()).toString();


            if (srt.isDirectory()) {
                href[i] = ("<tr><td><a href = '" + name + "'>" + srt.getName() + "</a></td><td></td><td>" +
                        time + "</td>");
            } else {
                String size = srt.length() + "b";
                href[i] = ("<tr><td><a href = '" + name + "'>" + srt.getName() + "</a></td><td>" +
                        size + "</td><td>" + time + "</td>");
            }
            i++;
        }
        href[href.length - 1] = ROW;

        return href;
    }

    
    public void createFile(String args) throws IOException {

        FileOutputStream outputStream = null;
        try {
            File f = new File(args + File.separator + "index.html");
            if (!f.canExecute()) {
                outputStream = new FileOutputStream(args + File.separator + "index.html");
                PrintWriter outputWriter = new PrintWriter(new OutputStreamWriter(outputStream));
                //outputWriter.write(HEADER);
                for (String s : href) {
                    outputWriter.write(s + "\n");
                }
                //outputWriter.write(ROW);
                outputWriter.flush();
                System.out.println("File index.html is created.");
            } else System.out.println("File " + f.getAbsolutePath() + " was founded and be used on.");
        } catch (IOException e) {
            throw e;
        } finally {
            if (outputStream != null) outputStream.close();
        }
    }
}
