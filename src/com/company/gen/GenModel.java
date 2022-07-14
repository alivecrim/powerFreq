package com.company.gen;

import javax.swing.*;

public class GenModel {
    private final JComboBox<String> prefix;
    private final JTextField level;
    private final JTextField center;
    private final JTextField ip;
    private final JCheckBox rf;

    public GenModel(JTextField genCentFreq, JTextField genLevel, JComboBox<String> genPrefix, JCheckBox rfBox, JTextField genIp) {
        this.center = genCentFreq;
        this.level = genLevel;
        this.prefix = genPrefix;
        this.rf = rfBox;
        this.ip = genIp;
    }

    public double getCenterFreq() {
        return Double.parseDouble(this.center.getText()) * prefixResolver(prefix);
    }

    private double prefixResolver(JComboBox<String> comboPrefix) {
        int selectedIndex = comboPrefix.getSelectedIndex();
        switch (selectedIndex) {
            case 0:
                return 1;
            case 1:
                return 1000;
            case 2:
                return 1e6;
            case 3:
                return 1e9;
        }
        throw new IllegalArgumentException("Неверный префикс!");
    }

    public double getLevel() {
        return Double.parseDouble(this.level.getText());
    }

    public String getIp() {
        return this.ip.getText();
    }

    public boolean getRf() {
        return rf.isSelected();
    }
}
