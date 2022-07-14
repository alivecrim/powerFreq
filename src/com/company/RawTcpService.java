package com.company;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

public class RawTcpService {
    private static Logger log = Logger.getLogger("TcpService");
    private BufferedReader br;
    private PrintWriter pw;
    private String host;
    private int port;
    private boolean connected;
    private Socket socket;

    public RawTcpService(String host, int port) throws ScpiException {
        this.host = host;
        this.port = port;
        this.connected = false;
    }

    public boolean isConnected() {
        return connected;
    }

    public void closeSocket() throws IOException {
        this.socket.close();
        this.connected = false;
    }

    private boolean connect() {
        try {
            socket = new Socket();
            socket.connect(new InetSocketAddress(host, port), 1000);
            socket.setSoTimeout(10000);
            this.br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.pw = new PrintWriter(socket.getOutputStream(), true);
            this.connected = true;
            return true;
        } catch (IOException e) {
            log.info(String.format("failed connect to device: %s:%s", host, port));
        }
        this.connected = false;
        return false;
    }

    public synchronized String sendMessage(String message) throws ScpiException {
        if (!message.contains("?") || message.contains("ESR")) {
            connected = false;
            this.pw.close();
            try {
                this.br.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        String result;
        result = sendMessageToDevice(message);
        return result;
    }

    private void connectionTry() throws ScpiException {
        int connectTry = 3;
        if (this.host == null || this.port == 0) throw new ScpiException(
                String.format("Неверное значение хоста или порта: [%s:%s]", host, port)
        );
        while (!isConnected()) {
            this.connect();
            connectTry--;
            if (connectTry == 0) throw new ScpiException(
                    String.format(
                            "Device by ip %s:%s unavailable!",
                            host,
                            port
                    )
            );
        }
    }

    private String sendMessageToDevice(String message) throws ScpiException {
        connectionTry();
        checkInputBufferForOldData();
        pw.println(message);
        if (!message.contains("?")) {
            return "";
        }
        try {
            String s = this.br.readLine();
            if (s == null) throw new IOException("Receive empty answer!");
            return s;
        } catch (IOException e) {
            this.connected = false;
            log.log(Level.INFO, String.format("[Send message error]: %s", message));
            throw new ScpiException(e.getMessage());
        }
    }

    private void checkInputBufferForOldData() throws ScpiException {
        try {
            while (br.ready()) {
                br.readLine();
            }
        } catch (IOException ex) {
            throw new ScpiException("Previous buffer not empty");
        }
    }
}
