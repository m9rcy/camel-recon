package io.m9rcy.recon.model;

import java.util.List;

public class JournalGroup {

    private ControlHeader controlHeader;
    private List<Transaction> transactions;
    private ControlTrailer controlTrailer;

    public JournalGroup() {
    }

    public ControlHeader getControlHeader() {
        return controlHeader;
    }

    public void setControlHeader(ControlHeader controlHeader) {
        this.controlHeader = controlHeader;
    }

    public List<Transaction> getTransactions() {
        return transactions;
    }

    public void setTransactions(List<Transaction> transactions) {
        this.transactions = transactions;
    }

    public ControlTrailer getControlTrailer() {
        return controlTrailer;
    }

    public void setControlTrailer(ControlTrailer controlTrailer) {
        this.controlTrailer = controlTrailer;
    }
}
