package org.academiadecodigo.bootcamp;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;



public class WebServer {


    public void start() {

        int port = 8080;
        try {
            ServerSocket serversocket = new ServerSocket(port);
            System.out.println("Listening on port: " + port + " || " + serversocket);

            while(true) {

                Socket socket = serversocket.accept();
                System.out.println("Connection accepted || " + socket);

                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                DataOutputStream out = new DataOutputStream(socket.getOutputStream());

                String request = in.readLine();
                System.out.println(request);

                String filename = request.substring(request.indexOf("/"),request.lastIndexOf(" HTTP")); //keep the stuff between the firs / and -space-HTTP
                System.out.println("filename: " + filename);

                String statuscode;
                String type ;
                String extension = "jpg";
                long length;

                if(filename.equals("/")){
                    filename = "www/pjimage.jpg";
                } else {
                    filename = "www" + filename;
                }



                type = filename.substring(filename.lastIndexOf("."));
                System.out.println(type);

                switch(type){
                    case ".jpg": type = "image";
                                 break;

                    case ".png": type = "image";
                                 break;

                    case ".txt": type = "text";
                                 break;
                    default: type = "invalid type";
                                break;


                }



                File file = new File(filename);
                length = file.length();
                String header1 = null;
                FileInputStream inputStream;

            //SE O FILE EXISTE USA O FILE LENGTH SENAO EXISTIR VAI PRO 404
                if(!file.exists() || filename.equals("www/404.html")){
                    statuscode = "404";
                    header1 = "HTTP/1.0 " + statuscode + " Document Follows\r\n" +
                            "Content-Type: " + type + "/" + extension + " \r\n" +
                            "Content-Length: " + length +" \r\n" +
                            //"Content-Length: <99999999999> \r\n" +
                            "\r\n" ;

                    inputStream = new FileInputStream("www/404.html");
                } else{
                    statuscode = "200";
                }
                System.out.println(filename + " statuscode: " + statuscode);



                header1 = "HTTP/1.0 " + statuscode + " Document Follows\r\n" +
                        "Content-Type: " + type + "/" + extension + " \r\n" +       // adicionar varial pros char
                        "Content-Length: " + length +" \r\n" +
                        "\r\n" ;

                inputStream = new FileInputStream("www/pjimage.jpg");


                //out.write(header.getBytes());
                out.write(header1.getBytes());


                int  num=0;

                while(num != -1){
                    byte[] buffer = new byte[1024];   //the 1024 get stored in this buffer
                    num = inputStream.read(buffer);   //decodes de byte info
                    out.write(buffer, 0, buffer.length);

                }

                out.flush(); // flush sends
                socket.close();

            }



        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
