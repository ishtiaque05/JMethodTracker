package com.ishtiaque.JTracker;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class Tracker {
    private static String currentCommit;
    private static String prevCommit;
    // this store commit: { methodList: {key: val}}
    private static LinkedHashMap<String, LinkedHashMap <String, LinkedHashMap <String, String>>> data = new LinkedHashMap <String, LinkedHashMap <String, LinkedHashMap <String, String>>>();
    private static LinkedHashMap <String, LinkedHashMap <String, String>> currentMethodHash = new LinkedHashMap <String, LinkedHashMap <String, String>>();
    private static LinkedHashMap <String, String> currentMethodList = new LinkedHashMap <String, String>();
    private static LinkedHashMap <String, String> methodsRemoveAtPrevCommit = new LinkedHashMap <String, String>();

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

    public LinkedHashMap <String, LinkedHashMap <String, LinkedHashMap <String, String>>>  getData() { return this.data; }

    public void addData(String currentCommit) {
        currentMethodHash.put("methodList", currentMethodList);
        data.put(currentCommit, currentMethodHash);
    }

    public void addMethodToCurrentList(String methodSignature,String methodInfo) {
        currentMethodList.put(methodSignature, methodInfo);
    }

    public void resetCurrentMethodVars() {
        currentMethodList = new LinkedHashMap <String, String>();
        methodsRemoveAtPrevCommit = new LinkedHashMap <String, String>();
        currentMethodHash = new LinkedHashMap<String, LinkedHashMap <String, String>>();
    }

    public Boolean methodExistInPrevCommit(String methodIdentifier) {
        return data.get(prevCommit).get("methodList").containsKey(methodIdentifier);
    }

    public void addMethodToPrevCommitAsRmvList(String methodSignature, String methodIdentifier) {
        methodsRemoveAtPrevCommit.put(methodSignature, methodIdentifier);
        data.get(prevCommit).put("removedMethod", methodsRemoveAtPrevCommit);
    }
}
