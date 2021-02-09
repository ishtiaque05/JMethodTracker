package com.ishtiaque.Parser;

import com.github.javaparser.ParserConfiguration;
import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.ImportDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.visitor.VoidVisitor;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import com.ishtiaque.JTracker.Tracker;
import com.ishtiaque.Util.Util;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

public class JavaParser {
    public static void setupParser() {
        ParserConfiguration parserConfiguration = new ParserConfiguration()
                .setAttributeComments(false)
                .setLanguageLevel(ParserConfiguration.LanguageLevel.RAW);
        StaticJavaParser.setConfiguration(parserConfiguration);
    }

    public static void parseFile(String fileContent, String filepath, Tracker tracker) throws FileNotFoundException {
        CompilationUnit cu = StaticJavaParser.parse(fileContent);
        if (cu != null) {
            VoidVisitor<String> methodVisitor = new MethodLister(tracker);
            methodVisitor.visit(cu, filepath);
//            return methodListCollector;
        } else {
            System.out.println("NULL compilation unit");
//            return null;
        }
    }

    public static class MethodLister extends VoidVisitorAdapter<String> {
        public static Tracker tracker;
        public MethodLister(Tracker tracker) {
            this.tracker = tracker;
        }
        @Override
        public void visit(MethodDeclaration mt, String filepath) {
            super.visit(mt, filepath);
            //System.out.println(mt.getName());
            String methodSignature =  mt.getDeclarationAsString(true, false, true).replaceAll(" ", "");
            System.out.println(mt.getNameAsString());
            String methodIdentifier = Util.buildMethodIdentifier(filepath, mt.getNameAsString(), mt.getName().getBegin().get().line);
            String prevCommit = tracker.getPrevCommit();
            if(prevCommit != null) {
                if(!tracker.methodExistInPrevCommit(methodIdentifier)) {
                    tracker.addMethodToCurrentList(methodSignature, methodIdentifier);
                    tracker.addMethodToPrevCommitAsRmvList(methodSignature, methodIdentifier);
                }
            } else {
                tracker.addMethodToCurrentList(methodSignature, methodIdentifier);
            }

        }
    }
}

