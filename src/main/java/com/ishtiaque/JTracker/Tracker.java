package com.ishtiaque.JTracker;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Tracker {
    private static String currentCommit;
    private static String prevCommit;
    private static HashMap<String, HashMap<String, ArrayList<String>>> data = new HashMap<String, HashMap<String, ArrayList<String>>>();
    private static HashMap<String, ArrayList<String>> currentMethodHash = new HashMap<String, ArrayList<String>>();
    private static ArrayList<String> currentMethodList = new ArrayList<String>();
    private static ArrayList<String> methodsRemoveAtPrevCommit = new ArrayList<String>();

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

    public HashMap<String,  HashMap<String, ArrayList<String>>>  getData() { return this.data; }

    public void addData(String currentCommit) {
        currentMethodHash.put("methodList", currentMethodList);
        data.put(currentCommit, currentMethodHash);
    }



    public HashMap<String, ArrayList<String>> getCurrentMethodHash() {
        return currentMethodHash;
    }

    public void addMethodListToCurrentCommit(String key, ArrayList<String> methodList) {
        currentMethodHash.put(key, methodList);
    }

    private void addMethodsRemoveAtPrevCommit(String key, ArrayList<String> methodList) {
        currentMethodHash.put(key, methodList);
    }

    public void addMethodToCurrentList(String method) {
        currentMethodList.add(method);
    }

    public ArrayList<String> getCurrentMethodList() {
        return currentMethodList;
    }

    public void resetCurrentMethodVars() {
        currentMethodList = new ArrayList<String>();
        methodsRemoveAtPrevCommit = new ArrayList<String>();
        currentMethodHash = new HashMap<String, ArrayList<String>>();
    }

    public Boolean methodExistInPrevCommit(String methodIdentifier) {
        ArrayList<String> prevMethodList = data.get(prevCommit).get("methodList");
        return prevMethodList.contains(methodIdentifier);
    }

    public void addMethodToPrevCommitAsRmvList(String methodIdentifier) {
        methodsRemoveAtPrevCommit.add(methodIdentifier);
        data.get(prevCommit).put("removedMethod", methodsRemoveAtPrevCommit);
    }
}
