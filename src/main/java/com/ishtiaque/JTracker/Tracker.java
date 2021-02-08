package com.ishtiaque.JTracker;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Tracker {
    private String currentCommit;
    private String prevCommit;
    private static Map<String, ArrayList<String>> data = new HashMap<String, ArrayList<String>>();

    public Tracker(String currentCommit, String prevCommit){
        this.currentCommit = currentCommit;
        this.prevCommit = prevCommit;
    }

    public void setCurrentCommit(String currentCommit) {
        this.currentCommit = currentCommit;
    }

    public String getCurrentCommit() { return this.currentCommit; }

    public void setPrevCommit(String prevCommit) {
        this.prevCommit = prevCommit;
    }

    public String getPrevCommit() { return  this.prevCommit; }

    public Map<String, ArrayList<String>>  getData() { return this.data; }



}
