package com.ishtiaque.Parser;

import com.github.javaparser.ParserConfiguration;
import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.ImportDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.visitor.VoidVisitor;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

import java.io.File;
import java.io.FileNotFoundException;

public class JavaParser {
    public static void setupParser() {
        ParserConfiguration parserConfiguration = new ParserConfiguration()
                .setAttributeComments(false)
                .setLanguageLevel(ParserConfiguration.LanguageLevel.RAW);
        StaticJavaParser.setConfiguration(parserConfiguration);
    }

    public static void parseFile(String file) throws FileNotFoundException {
        CompilationUnit cu = StaticJavaParser.parse(file);
        if (cu != null) {
            VoidVisitor<String> methodVisitor = new MethodLister();
            methodVisitor.visit(cu, file);
        } else {
            System.out.println("NULL compilation unit");
        }
    }

    public static class MethodLister extends VoidVisitorAdapter<String> {
        @Override
        public void visit(MethodDeclaration mt, String file) {
            super.visit(mt, file);
            //System.out.println(mt.getName());
            System.out.println(mt.getNameAsString());
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

