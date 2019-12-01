package Data;

import com.github.javaparser.ParseResult;
import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.PackageDeclaration;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.TypeDeclaration;
import com.github.javaparser.ast.expr.BinaryExpr;
import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.ast.type.ClassOrInterfaceType;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import com.github.javaparser.symbolsolver.JavaSymbolSolver;
import com.github.javaparser.symbolsolver.javaparser.Navigator;
import com.github.javaparser.symbolsolver.javaparsermodel.JavaParserFacade;
import com.github.javaparser.symbolsolver.model.resolution.TypeSolver;
import com.github.javaparser.symbolsolver.resolution.typesolvers.CombinedTypeSolver;
import com.github.javaparser.symbolsolver.resolution.typesolvers.JarTypeSolver;
import com.github.javaparser.symbolsolver.resolution.typesolvers.JavaParserTypeSolver;
import com.github.javaparser.symbolsolver.resolution.typesolvers.ReflectionTypeSolver;
import com.github.javaparser.utils.SourceRoot;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.instrument.ClassDefinition;
import java.util.*;
import java.util.stream.Collectors;

public class DataGuide {

    private JavaSymbolSolver javaSymbolSolver;
    private TypeSolver typeSolver;
    private TypeSolver reflectionTypeSolver;
    private CombinedTypeSolver combinedTypeSolver;
    private File mainFile;

    //Set z wszystkimi plikami
    private static Set<File> clasesFiles;

    //new
    private static Map<String,Integer> filesWeight;
    private static List<String> classesNames;

    private static Map<String,Map<String,Integer>> twoPackagesAndWeight;
    private static Map<String,String> classAndPackage;
    private static Map<String,ArrayList<String>> classAndMethods;
    private static Map<String,String> methodAndPackageName;
    private static Set<String> listOfDeclaredMethods;
    private static Map<String,String> packagePackage;
    private static Map<String,Integer> methodWeight;


    public void findModuleDependencies(String rootPath) throws IOException, ClassNotFoundException, NoSuchFieldException {

        this.combinedTypeSolver = new CombinedTypeSolver();
        this.typeSolver = new JavaParserTypeSolver(rootPath);
        this.mainFile = new File(rootPath);
        reflectionTypeSolver = new ReflectionTypeSolver();
        combinedTypeSolver.add(reflectionTypeSolver);
        combinedTypeSolver.add(typeSolver);
        combinedTypeSolver.add(new JarTypeSolver("/Users/dominikstrama/Desktop/jar_files/javaparser-symbol-solver-core-3.15.5.jar"));
        combinedTypeSolver.add(new JarTypeSolver("/Users/dominikstrama/Desktop/jar_files/javaparser-symbol-solver-logic-3.15.5.jar"));
        combinedTypeSolver.add(new JarTypeSolver("/Users/dominikstrama/Desktop/jar_files/javaparser-symbol-solver-model-3.15.5.jar"));
        combinedTypeSolver.add(new JarTypeSolver("/Users/dominikstrama/Desktop/jar_files/javaparser-core-3.15.5.jar"));
        combinedTypeSolver.add(new JarTypeSolver("/Users/dominikstrama/Desktop/jar_files/javaparser-symbol-solver-model-3.15.5.jar"));
        combinedTypeSolver.add(new JarTypeSolver("/Users/dominikstrama/Desktop/jar_files/gson-2.8.2.jar"));
        this.javaSymbolSolver = new JavaSymbolSolver(combinedTypeSolver);
        StaticJavaParser.getConfiguration().setSymbolResolver(javaSymbolSolver);

        twoPackagesAndWeight = new HashMap<>();
        clasesFiles = new HashSet<>();
        listOfDeclaredMethods = new HashSet<>();
        packagePackage = new HashMap<>();
        classAndMethods = new HashMap<>();
        classAndPackage = new HashMap<>();
        methodWeight = new HashMap<>();

        //new
        filesWeight = new HashMap<>();
        classesNames = new ArrayList<>();

        Arrays.stream(mainFile.listFiles()).forEach(file -> {
            checkDirectory(file, clasesFiles);
        });

        clasesFiles.forEach(file -> {
            classesNames.add(file.getName().substring(0,file.getName().lastIndexOf(".java")));
        });

        // PackageInformation();
        FilesConnections();
    }

    // Połączenia pomiędzy plikami zwracamy mape <nazwa pliku1,<nazwa pliku2, waga pliku>>
    public Map<String, Map<String,Integer>> FilesConnections(){
        Map<String, Map<String, Integer>> filesInformation = new HashMap<>();
        List<Integer> weigth = new ArrayList<>();

        clasesFiles.forEach(file -> {
            String firstFileName = file.getName().substring(0,file.getName().lastIndexOf(".java"));
            int fileWeigth = (int)file.length();
            weigth.add(fileWeigth);


        });

        return filesInformation;
    }


    public Map<String, Map<String, Integer>> PackageInformation(){
        Map<String, Map<String, Integer>> packageInformation = new HashMap<>();

        clasesFiles.forEach( file -> {
            CompilationUnit u  = null;
            try{
                u = StaticJavaParser.parse(file);

            }catch (FileNotFoundException e){
                System.out.println(e.fillInStackTrace());
            }

            u.findAll(MethodDeclaration.class).stream().forEach(obj -> {
                listOfDeclaredMethods.add(obj.resolve().getClassName());
            });
            new MethodVisitor().visit(u,null);

        });

        classAndPackage.keySet().retainAll(listOfDeclaredMethods);

        System.out.println(classAndPackage);
        System.out.println(methodWeight);
        return packageInformation;

    }

    private void checkDirectory (File file, Set<File> list){
        if(file.isDirectory()){
            Arrays.stream(file.listFiles()).forEach( file2 -> {
                if(file2.isDirectory()){
                    checkDirectory(file2,list);
                }
                else{
                    list.add(file2);
                }
            });
        }else{
            list.add(file);
        }
    }

    public static class MethodExprVisitor extends VoidVisitorAdapter
    {
        @Override
        public void visit(MethodCallExpr n, Object arg)
        {
            super.visit(n,arg);
        }

    }
    public static class MethodVisitor extends VoidVisitorAdapter
    {
        @Override
        public void visit(MethodDeclaration n, Object arg)
        {
            super.visit(n,arg);
            n.findAll(MethodCallExpr.class).stream().forEach(obj ->{
                System.out.println("Paczka 1: " + n.resolve().getPackageName() +" | Klasa paczki 1: " + n.resolve().getClassName() + " |  Nazwa metody 1: " + n.getNameAsString() + " |  Nazwa metody 2: " + obj.getNameAsString() + " |  Nazwa Paczki metody 2: " + obj.resolve().getPackageName() +"Klasa paczki 2: " + obj.resolve().getClassName() );

                if(methodWeight.containsKey(n.getNameAsString())){
                    methodWeight.replace(n.getNameAsString(),methodWeight.get(n.getNameAsString()) + 1);
                }
                else if(methodWeight.containsKey(obj.getNameAsString())){
                    methodWeight.replace(obj.getNameAsString(),methodWeight.get(obj.getNameAsString()) + 1);
                }
                else{
                    methodWeight.put(n.getNameAsString(),1);
                    methodWeight.put(obj.getNameAsString(),1);
                }

                classAndPackage.put(n.resolve().getClassName(),n.resolve().getPackageName());
                classAndPackage.put(obj.resolve().getClassName(),obj.resolve().getPackageName());


            });
        }
    }
    public static class ClassName extends VoidVisitorAdapter
    {
        @Override
        public void visit(ClassOrInterfaceDeclaration n, Object arg)
        {
            super.visit(n,arg);
        }
    }
}

