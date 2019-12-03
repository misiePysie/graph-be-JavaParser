package Data;
import SpringApplication.GraphApplication;
import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.ImportDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import com.github.javaparser.symbolsolver.JavaSymbolSolver;
import com.github.javaparser.symbolsolver.model.resolution.TypeSolver;
import com.github.javaparser.symbolsolver.resolution.typesolvers.CombinedTypeSolver;
import com.github.javaparser.symbolsolver.resolution.typesolvers.JarTypeSolver;
import com.github.javaparser.symbolsolver.resolution.typesolvers.JavaParserTypeSolver;
import com.github.javaparser.symbolsolver.resolution.typesolvers.ReflectionTypeSolver;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;


public class DataGuide {

    private JavaSymbolSolver javaSymbolSolver;
    private TypeSolver typeSolver;
    private TypeSolver reflectionTypeSolver;
    private CombinedTypeSolver combinedTypeSolver;
    private File mainFile;

    private static Set<File> clasesFiles;
    private static List<String> classesNames;
    private static List<String> methodsNames;
    private static Map<String,Integer> filesWeight;
    private static Map<String,Integer> methodsWeight;
    private static Map<String,Map<String,Integer>> fileOneFileTwoWeight;
    private static Map<String,Map<String,Integer>> methodOneMethodTwoWeight;


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

        clasesFiles = new HashSet<>();
        classesNames = new ArrayList<>();
        methodsNames = new ArrayList<>();
        filesWeight = new HashMap<>();
        fileOneFileTwoWeight = new HashMap<>();
        methodOneMethodTwoWeight = new HashMap<>();
        methodsWeight = new HashMap<>();

        Arrays.stream(mainFile.listFiles()).forEach(file -> {

            checkDirectory(file, clasesFiles);
        });

        clasesFiles.forEach(file -> {
            classesNames.add(file.getName().substring(0,file.getName().lastIndexOf(".java")));
        });



        FilesConnections();
        //MethodConnections();


    }
    // Historyjka 1
    // Połączenia pomiędzy plikami


    public Map<String, Map<String,Integer>> FilesConnections(){

        List<Integer> weigth = new ArrayList<>();

        clasesFiles.forEach(file -> {
            int fileWeigth = (int)file.length();
            weigth.add(fileWeigth);

            CompilationUnit cu = null;
            try {
                cu = StaticJavaParser.parse(file);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

            Map<String,Integer> fileTwoAndWeight = new HashMap<>();

            for(MethodCallExpr mce : cu.findAll(MethodCallExpr.class)){
                    if(classesNames.contains(mce.resolve().getClassName())){

                        if(fileTwoAndWeight.containsKey(mce.resolve().getClassName())){
                            int value = fileTwoAndWeight.get(mce.resolve().getClassName()) + 1;
                            fileTwoAndWeight.put(mce.resolve().getClassName(),value);
                        }else{
                            fileTwoAndWeight.put(mce.resolve().getClassName(),1);
                        }
                        System.out.println("File one : " + file.getName().substring(0, file.getName().lastIndexOf(".java")) + "\t Weight File One: " + fileWeigth + "\t File two : " + mce.resolve().getClassName() + "\t Method from file two : " + mce.resolve().getName() );
                    }
                fileOneFileTwoWeight.put(file.getName().substring(0, file.getName().lastIndexOf(".java")),fileTwoAndWeight);
            }

        });
        System.out.println(fileOneFileTwoWeight);
        return fileOneFileTwoWeight;
    }

    // Historyjka 2
    // Połączenia pomiędzy plikami
    public Map<String, Map<String,Integer>> MethodConnections(){

        clasesFiles.forEach(file -> {
            CompilationUnit cu = null;
            try {
                cu = StaticJavaParser.parse(file);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            for(MethodDeclaration md : cu.findAll(MethodDeclaration.class)){
                Map<String,Integer> methodTwoAndWeight = new HashMap<>();
                for(MethodCallExpr mce : md.findAll(MethodCallExpr.class)){
                    if(classesNames.contains(mce.resolve().getClassName())){
                        if(methodsWeight.containsKey(mce.resolve().getName())){
                            int value = methodsWeight.get(mce.resolve().getName()) + 1;
                            methodsWeight.put(mce.resolve().getName(),value);
                        }
                        else{
                            methodsWeight.put(mce.resolve().getName(),1);
                        }
                        if(methodTwoAndWeight.containsKey(mce.resolve().getName())){
                            int value = methodTwoAndWeight.get(mce.resolve().getName()) + 1;
                            methodTwoAndWeight.put(mce.resolve().getName(),value);
                        }
                        else{
                            methodTwoAndWeight.put(mce.resolve().getName(),1);
                        }
                        methodOneMethodTwoWeight.put(md.resolve().getName(),methodTwoAndWeight);
                    }
                }

            }
        });

        // Mapa methodsWeight zwraca metode i ilosc jej wywołan czyli wagę wezła
        // Zwraca HashMap<String metoda1,<String metoda 2,Integer waga_krawędzi)
        return methodOneMethodTwoWeight;
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




}

