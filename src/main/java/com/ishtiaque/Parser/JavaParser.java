package com.ishtiaque.Parser;

import com.github.javaparser.ParserConfiguration;
import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.ImportDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.visitor.VoidVisitor;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
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

    public static void parseFile(String fileContent, String filepath, List<String> methodListCollector) throws FileNotFoundException {
        CompilationUnit cu = StaticJavaParser.parse(fileContent);
        if (cu != null) {
            VoidVisitor<List<String>> methodVisitor = new MethodLister(filepath);
            methodVisitor.visit(cu, methodListCollector);
//            return methodListCollector;
        } else {
            System.out.println("NULL compilation unit");
//            return null;
        }
    }

    public static class MethodLister extends VoidVisitorAdapter<List<String>> {
        public static String filepath;
        public MethodLister(String filepath) {
            this.filepath = filepath;
        }
        @Override
        public void visit(MethodDeclaration mt, List<String> collector) {
            super.visit(mt, collector);
            //System.out.println(mt.getName());
            System.out.println(mt.getNameAsString());
            collector.add(Util.buildMethodIdentifier(this.filepath, mt.getNameAsString(), mt.getName().getBegin().get().line));
//            Method method = new Method();
//            method.name = mt.getNameAsString();
//            method.path = file.replace(Main.BASE_PATH + Main.PROJECT + "/", "");
//            method.lineNumber = mt.getName().getBegin().get().line;
//
//            if (!(method.path.toLowerCase().contains("test") || method.name.toLowerCase().contains("test"))) {
//                // we ignore all methods that has the word test in the path name or even in the method namee..
//                //            System.out.println(method.path);
//                Main.uniquePath.put(method.path, 1);
//                methods.add(method);
//
//            }

        }
    }
}

