package com.company.fsw;

import com.company.RawTcpService;
import com.company.ScpiException;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class ApplyFswListener implements ActionListener {
    private final FswModel fswModel;

    public ApplyFswListener(FswModel fswModel) {
        this.fswModel = fswModel;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        try {
            saveConfig();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        double centerFreq = this.fswModel.getCenterFreq();
        double span = this.fswModel.getSpan();
        double refLevel = this.fswModel.getRefLevel();
        System.out.printf("center=%s span=%s ref=%s", centerFreq, span, refLevel);


        String host = this.fswModel.getIp();
        int port = 5025;
        RawTcpService sender = null;
        try {
            sender = new RawTcpService(host, port);
        } catch (ScpiException ex) {
            ex.printStackTrace();
        }

        assert sender != null;
        sender.sendMessage(String.format(":FREQ:SPAN %s;*OPC?", span));
        sender.sendMessage(String.format(":FREQ:CENT %s;*OPC?", centerFreq));
        sender.sendMessage(String.format(":DISPlay:WINDow:TRACe:Y:RLEVel %s; *OPC?", refLevel));

        sender.sendMessage(":CALC:MARK1:MODE POSition; *OPC?");
        sender.sendMessage(String.format(":CALC:MARK1:X %s; *OPC?", centerFreq));
        sender.sendMessage("CALC:MARK1:FUNC BPOW; *OPC?");
        sender.sendMessage(String.format("CALC:MARK1:FUNC:BAND:SPAN %s; *OPC?", span));
        try {
            sender.closeSocket();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private void saveConfig() throws IOException {
        FileWriter fileWriter = new FileWriter("fsw.config");
        PrintWriter pw = new PrintWriter(fileWriter);
        pw.println(this.fswModel.getCenterFreq());
        pw.println(this.fswModel.getSpan());
        pw.println(this.fswModel.getRefLevel());
        pw.println(this.fswModel.getIp());
        pw.close();
        fileWriter.close();
    }
}
