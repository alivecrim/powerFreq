package com.company.gen;

import com.company.RawTcpService;
import com.company.ScpiException;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class ApplyGenListener implements ActionListener {
    private final GenModel genModel;

    public ApplyGenListener(GenModel genModel) {
        this.genModel = genModel;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        try {
            saveConfig();
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        String host = this.genModel.getIp();
        int port = 5025;

        double centerFreq = this.genModel.getCenterFreq();
        double refLevel = this.genModel.getLevel();
        System.out.printf("center=%s ref=%s", centerFreq, refLevel);

        RawTcpService sender = null;
        try {
            sender = new RawTcpService(host, port);
        } catch (ScpiException ex) {
            ex.printStackTrace();
        }

        assert sender != null;
        sender.sendMessage(String.format(":POW %sDBM; *OPC?", refLevel));
        sender.sendMessage(String.format("FREQuency %s;*OPC?", centerFreq));
        sender.sendMessage(String.format("OUTP %s;*OPC?", this.genModel.getRf() ? 1 : 0));
        try {
            sender.closeSocket();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private void saveConfig() throws IOException {
        FileWriter fileWriter = new FileWriter("gen.config");
        PrintWriter pw = new PrintWriter(fileWriter);
        pw.println(this.genModel.getCenterFreq());
        pw.println(this.genModel.getLevel());
        pw.println(this.genModel.getRf() ? 1 : 0);
        pw.println(this.genModel.getIp());
        pw.close();
        fileWriter.close();
    }
}
