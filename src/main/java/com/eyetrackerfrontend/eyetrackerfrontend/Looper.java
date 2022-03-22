package com.eyetrackerfrontend.eyetrackerfrontend;

import javafx.stage.Screen;
import org.msgpack.core.MessagePack;
import org.msgpack.core.MessageUnpacker;
import org.zeromq.SocketType;
import org.zeromq.ZContext;
import org.zeromq.ZMQ;

import java.io.*;

public class Looper extends Thread{
    public volatile int x, y;
    private boolean end = false;
    private PrintWriter outX;
    private PrintWriter outY;

    public Looper() {
        try {
            outX = new PrintWriter(new BufferedWriter(new FileWriter("xValues.txt")));
            outY = new PrintWriter(new BufferedWriter(new FileWriter("yValues.txt")));
        } catch (IOException e) {
            e.printStackTrace();
        }
        outX.println("X values,");
        outY.println("Y values,");
    }

    public void run() {
        ZContext ctx = new ZContext();
        ZMQ.Socket pupil_remote = ctx.createSocket(SocketType.REQ);
        String address = "tcp://127.0.0.1";
        String port = "50020";

        pupil_remote.connect(address + ":" + port);

        pupil_remote.send("SUB_PORT");
        byte[] sub_portA = pupil_remote.recv();
        StringBuilder sub_portSB = new StringBuilder();
        for (byte b : sub_portA) {
            sub_portSB.append((char) b);
        }
        String sub_port = sub_portSB.toString();

        System.out.println(sub_port);

        ZMQ.Socket subscriber = ctx.createSocket(SocketType.SUB);
        subscriber.connect(address + ":" + sub_port);
        subscriber.subscribe("surfaces.Surface 1");

        while (!end) {
            System.out.println("t");
            byte[] topicA = subscriber.recv(0);
            StringBuilder topicASB = new StringBuilder();
            for (byte b : topicA) {
                topicASB.append((char) b);
            }
            String topic = topicASB.toString();
            System.out.println(topic);

            byte[] payload = subscriber.recv(0);
            MessageUnpacker message = MessagePack.newDefaultUnpacker(payload);
            try {
                message.unpackMapHeader();
            } catch (IOException e) {
                e.printStackTrace();
            }
            for (int i = 0; i < 6; i++) {
                try {
                    message.skipValue();
                    message.skipValue();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            String a;
            int nx, ny;
            try {
                a = message.unpackString();
                System.out.println(a);
                int arrayIdx = message.unpackArrayHeader();
                System.out.println(arrayIdx);
                int cnt = 0;
                long sumX = 0;
                long sumY = 0;
                for (int i = 0; i < arrayIdx; i++) {
                    message.unpackMapHeader();
                    message.skipValue(); message.skipValue(); message.skipValue();
                    message.unpackArrayHeader();
                    nx = (int) Math.round(message.unpackDouble() * MainApplication.screenWidth);
                    ny = (int) Math.round((1 - message.unpackDouble()) * MainApplication.screenHeight);
                    message.skipValue();
                    double confidence = message.unpackDouble();
                    message.skipValue();
                    boolean onSurf = message.unpackBoolean();
                    if (confidence > 0.8 && onSurf) {
                        System.out.println(nx + " " + ny);
                        sumX += nx;
                        sumY += ny;
                        cnt++;
                    }
                    for (int j = 0; j < 4; j++) message.skipValue();
                }
                if (cnt > 0) {
                    x = (int) (sumX / cnt);
                    y = (int) (sumY / cnt);
                    System.out.println(topic + ": " + x + " " + y);
                    MainApplication.rawX = x;
                    MainApplication.rawY = y;
                    if (MainApplication.logData) {
                        outX.println(x + ",");
                        outY.println(y + ",");
                    }
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    public void endProgram() {
        outX.close();
        outY.close();
        end = true;
    }
}
