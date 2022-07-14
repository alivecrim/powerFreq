package com.company.fsw;

import javax.swing.*;

public class FswModel {
    private final JComboBox<String> spanFreqPrefix;
    private final JComboBox<String> centerFreqPrefix;
    private final JTextField refLevelField;
    private final JTextField centerFreqField;
    private final JTextField spanField;
    private final JTextField ip;

    public FswModel(JTextField centerFreq, JTextField span, JTextField refLevel, JComboBox<String> centerFreqPrefix, JComboBox<String> spanFreqPrefix, JTextField fswIp) {
        this.centerFreqField = centerFreq;
        this.spanField = span;
        this.refLevelField = refLevel;
        this.centerFreqPrefix = centerFreqPrefix;
        this.spanFreqPrefix = spanFreqPrefix;
        this.ip = fswIp;
    }

    public double getCenterFreq() {
        return Double.parseDouble(this.centerFreqField.getText()) * prefixResolver(centerFreqPrefix);
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

    public double getSpan() {
        return Double.parseDouble(this.spanField.getText()) * prefixResolver(spanFreqPrefix);
    }

    public double getRefLevel() {
        return Double.parseDouble(this.refLevelField.getText());
    }

    public String getIp() {
        return this.ip.getText();
    }
}
