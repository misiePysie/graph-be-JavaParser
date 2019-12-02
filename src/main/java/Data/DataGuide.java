package Data;
import SpringApplication.GraphApplication;
import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.ImportDeclaration;
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
    private static Map<String,Integer> filesWeight;


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
        filesWeight = new HashMap<>();

        Arrays.stream(mainFile.listFiles()).forEach(file -> {
            checkDirectory(file, clasesFiles);
        });

        clasesFiles.forEach(file -> {
            classesNames.add(file.getName().substring(0,file.getName().lastIndexOf(".java")));
        });

        FilesConnections();
    }
    // Historyjka 1
    // Połączenia pomiędzy plikami

    public Map<String, Map<String,Integer>> FilesConnections(){

        Map<String, Map<String, Integer>> filesInformation = new HashMap<>();
        List<Integer> weigth = new ArrayList<>();

        clasesFiles.forEach(file -> {

            String firstFileName = file.getName().substring(0,file.getName().lastIndexOf(".java"));
            int fileWeigth = (int)file.length();
            weigth.add(fileWeigth);

            CompilationUnit cu = null;
            try {
                cu = StaticJavaParser.parse(file);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

            for(ImportDeclaration id : cu.getImports() ){
               // System.out.println("File name: " + file.getName().substring(0, file.getName().lastIndexOf(".java")) + " Imports: " + id.getName().getIdentifier());
            }

            for(MethodCallExpr mce : cu.findAll(MethodCallExpr.class)){
                    if(classesNames.contains(mce.resolve().getClassName())){

                        System.out.println("File one : " + file.getName().substring(0, file.getName().lastIndexOf(".java")) + "\t Weight File One: " + fileWeigth + "\t File two : " + mce.resolve().getClassName() + "\t Method from file two : " + mce.resolve().getName() );
                    }
            }

        });

        return filesInformation;
    }

        public static class MethodExprVisitor extends VoidVisitorAdapter
    {
        @Override
        public void visit(MethodCallExpr n, Object arg)
        {
            super.visit(n,arg);
        }

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

