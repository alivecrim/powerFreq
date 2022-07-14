package com.company;

import com.company.fsw.ApplyFswListener;
import com.company.fsw.FswModel;
import com.company.gen.ApplyGenListener;
import com.company.gen.GenModel;

import javax.swing.*;
import java.io.*;

public class GenFsw {
    private JTabbedPane tabbedPane1;
    private JPanel mainPanel;

    private JButton applyFsw;
    private JTextField fswCenterFreq;
    private JTextField fswSpan;
    private JTextField fswRefLevel;
    private JComboBox<String> centerFreqPrefix;
    private JComboBox<String> spanFreqPrefix;

    private JButton applyGen;
    private JTextField genCentFreq;
    private JTextField genLevel;
    private JComboBox<String> genPrefix;
    private JTextField fswIp;
    private JTextField genIp;
    private JCheckBox rfBox;

    public static void main(String[] args) {
        JFrame frame = new JFrame("Analyzer_Generator");
        frame.setContentPane(new GenFsw().mainPanel);
        frame.pack();
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public GenFsw() {
        try {
            loadGenData();
        } catch (Exception ex) {
            System.out.println("No standard config");
        }

        try {
            loadFswData();
        } catch (Exception ex) {
            System.out.println("No standard config");
        }

        FswModel fswModel = new FswModel(fswCenterFreq, fswSpan, fswRefLevel, centerFreqPrefix, spanFreqPrefix, fswIp);
        this.applyFsw.addActionListener(new ApplyFswListener(fswModel));

        GenModel genModel = new GenModel(genCentFreq, genLevel, genPrefix, rfBox, genIp);
        this.applyGen.addActionListener(new ApplyGenListener(genModel));
    }

    private void loadGenData() throws IOException {
        FileReader fileReader = new FileReader("gen.config");
        BufferedReader bufferedReader = new BufferedReader(fileReader);
        String freq = bufferedReader.readLine();
        String power = bufferedReader.readLine();
        boolean rf = bufferedReader.readLine().equals("1");
        String ip = bufferedReader.readLine();
        this.genIp.setText(ip);
        this.genLevel.setText(power);
        this.genCentFreq.setText(freq);
        this.rfBox.setSelected(rf);
        bufferedReader.close();
        fileReader.close();
    }

    private void loadFswData() throws IOException {
        FileReader fileReader = new FileReader("fsw.config");
        BufferedReader bufferedReader = new BufferedReader(fileReader);
        String freq = bufferedReader.readLine();
        String span = bufferedReader.readLine();
        String level = bufferedReader.readLine();
        String ip = bufferedReader.readLine();
        this.fswCenterFreq.setText(freq);
        this.fswSpan.setText(span);
        this.fswRefLevel.setText(level);
        this.fswIp.setText(ip);
        bufferedReader.close();
        fileReader.close();
    }
}
