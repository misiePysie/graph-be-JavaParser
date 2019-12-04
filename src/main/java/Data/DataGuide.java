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
    private static Map<String, Integer> filesWeight;
    private static Map<String, Integer> methodsWeight;
    private static Map<String, Map<String, Integer>> fileOneFileTwoWeight;
    private static Map<String, Map<String, Integer>> methodOneMethodTwoWeight;


    public void findModuleDependencies(String rootPath) throws IOException, ClassNotFoundException, NoSuchFieldException {

        this.combinedTypeSolver = new CombinedTypeSolver();
        this.typeSolver = new JavaParserTypeSolver(rootPath);
        this.mainFile = new File(rootPath);
        reflectionTypeSolver = new ReflectionTypeSolver();
        combinedTypeSolver.add(reflectionTypeSolver);
        combinedTypeSolver.add(typeSolver);
        combinedTypeSolver.add(new JarTypeSolver("C:\\Users\\asus\\Desktop\\studia syf\\5. semestr\\IO\\io io\\javaparser\\javaparser-symbol-solver-core-3.15.5.jar"));
        combinedTypeSolver.add(new JarTypeSolver("C:\\Users\\asus\\Desktop\\studia syf\\5. semestr\\IO\\io io\\javaparser\\javaparser-symbol-solver-logic-3.15.5.jar"));
        combinedTypeSolver.add(new JarTypeSolver("C:\\Users\\asus\\Desktop\\studia syf\\5. semestr\\IO\\io io\\javaparser\\javaparser-symbol-solver-model-3.15.5.jar"));
        combinedTypeSolver.add(new JarTypeSolver("C:\\Users\\asus\\Desktop\\studia syf\\5. semestr\\IO\\io io\\javaparser\\javaparser-core-3.15.5.jar"));
        combinedTypeSolver.add(new JarTypeSolver("C:\\Users\\asus\\Desktop\\studia syf\\5. semestr\\IO\\io io\\javaparser\\gson-2.8.2.jar"));

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
            classesNames.add(file.getName().substring(0, file.getName().lastIndexOf(".java")));
        });


        FilesConnections();
        //MethodConnections();


    }
    //----------------------------------------------------------------------------------------------------------------------------
    // Historyjka 1
    // Połączenia pomiędzy plikami File_File


    public Map<String, Map<String, Integer>> FilesConnections() {
        //stworzenie listy plików;
        final ArrayList<JavaFile> listOfJavaFiles = new ArrayList<JavaFile>();
        addAllFilesToList(listOfJavaFiles);


        ArrayList<EdgeFile_File> listOfEdgesFile_File = new ArrayList<EdgeFile_File>();
        final JavaFile tempTwoJavaFile = new JavaFile();
        final JavaFile tempOneJavaFile = new JavaFile();

        clasesFiles.forEach(file -> {
            int value = 0;

            for (JavaFile f : listOfJavaFiles) {
                if (file.getName().substring(0, file.getName().lastIndexOf(".java")).equals(f.getJavaFileName())) {
                    tempOneJavaFile.setJavaFileName(f.getJavaFileName());
                    tempOneJavaFile.setSize(f.getSize());
                }
            }

            CompilationUnit cu = null;
            try {
                cu = StaticJavaParser.parse(file);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

            HashMap<String, Integer> fileTwoAndWeight = new HashMap<>();
            boolean isAlreadyInList = false;

            for (MethodCallExpr mce : cu.findAll(MethodCallExpr.class)) {

                if (classesNames.contains(mce.resolve().getClassName())) {

                    if (fileTwoAndWeight.containsKey(mce.resolve().getClassName())) {
                        value = fileTwoAndWeight.get(mce.resolve().getClassName()) + 1;
                        fileTwoAndWeight.put(mce.resolve().getClassName(), value);
                    } else {
                        fileTwoAndWeight.put(mce.resolve().getClassName(), 1);
                    }

                    //System.out.println("File one : " + file.getName().substring(0, file.getName().lastIndexOf(".java")) + "\t File two : " + mce.resolve().getClassName()  );
                }

                fileOneFileTwoWeight.put(file.getName().substring(0, file.getName().lastIndexOf(".java")), fileTwoAndWeight);
            }
            addAllEdgesToList(listOfEdgesFile_File, listOfJavaFiles, tempOneJavaFile, fileTwoAndWeight);

        });

        //todo: Dla Norbiego do przekazania chlopakom: listOfEdgesFile_File,listOfJavaFiles

        return fileOneFileTwoWeight;
    }

    public void setSizeForCircles(ArrayList<JavaFile> listOfJavaFiles) {
        double max = -1;
        int index = -1;
        for (int i = 0; i < listOfJavaFiles.size(); i++) {
            if (listOfJavaFiles.get(i).getSize() > max) {
                max = listOfJavaFiles.get(i).getSize();
                index = i;
            }
        }
        listOfJavaFiles.get(index).setSize(100);
        int val = 0;
        for (int i = 0; i < listOfJavaFiles.size(); i++) {
            val = (int) ((listOfJavaFiles.get(i).getSize() * 100) / max);
            listOfJavaFiles.get(i).setSize(val);
        }

    }

    public void addAllEdgesToList(ArrayList<EdgeFile_File> listOfEdgesFile_File, ArrayList<JavaFile> listOFJavaFiles, JavaFile fileOne, HashMap<String, Integer> fileTwoAndWeight) {

        listOFJavaFiles.forEach(f -> {
            if (fileTwoAndWeight.containsKey(f.getJavaFileName())) {
                int weight = fileTwoAndWeight.get(f.getJavaFileName()).intValue();
                listOfEdgesFile_File.add(new EdgeFile_File(fileOne, f, weight));
            }
        });
        listOfEdgesFile_File.forEach(e -> System.out.println(e));
    }

    public void addAllFilesToList(ArrayList<JavaFile> listOfJavaFiles) {

        clasesFiles.forEach(file -> {
            int fileWeight = (int) file.length();
            //   weight.add(fileWeight);
            JavaFile javaFile = new JavaFile(file.getName().substring(0, file.getName().lastIndexOf(".java")), fileWeight);
            listOfJavaFiles.add(javaFile);
        });
        //ustawienie wartosci pod kolka
        setSizeForCircles(listOfJavaFiles);

        listOfJavaFiles.forEach(f -> System.out.println(f));
    }

    //-------------------------------------------------------------------------------------------------------------------------------------
    // Historyjka 2
    // Połączenia pomiędzy metodami
    public Map<String, Map<String, Integer>> MethodConnections() {

        ArrayList<Method> methodsList = new ArrayList<>();
        ArrayList<EdgeMethod_Method> methodsEdgesList = new ArrayList<>();

        clasesFiles.forEach(file -> {

            CompilationUnit cu = null;
            try {
                cu = StaticJavaParser.parse(file);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            for (MethodDeclaration md : cu.findAll(MethodDeclaration.class)) {
                Map<String, Integer> methodTwoAndWeight = new HashMap<>();
                for (MethodCallExpr mce : md.findAll(MethodCallExpr.class)) {
                    if (classesNames.contains(mce.resolve().getClassName())) {
                        if (methodsWeight.containsKey(mce.resolve().getName())) {
                            int value = methodsWeight.get(mce.resolve().getName()) + 1;
                            methodsWeight.put(mce.resolve().getName(), value);
                        } else {
                            methodsWeight.put(mce.resolve().getName(), 1);
                        }
                        if (methodTwoAndWeight.containsKey(mce.resolve().getName())) {
                            int value = methodTwoAndWeight.get(mce.resolve().getName()) + 1;
                            methodTwoAndWeight.put(mce.resolve().getName(), value);
                        } else {
                            methodTwoAndWeight.put(mce.resolve().getName(), 1);
                        }
                        methodOneMethodTwoWeight.put(md.resolve().getName(), methodTwoAndWeight);
                    }
                }

            }
        });

        // Mapa methodsWeight zwraca metode i ilosc jej wywołan czyli wagę wezła
        // Zwraca HashMap<String metoda1,<String metoda 2,Integer waga_krawędzi)
        System.out.println("Lista metod: ");
        addMethodsToList(methodsList, methodOneMethodTwoWeight);
        //addMethodsToList(methodsList, methodsWeight);
        System.out.println("Lista krawedzi: ");
        addMethodsEdgesToList(methodsEdgesList,methodOneMethodTwoWeight);
        return methodOneMethodTwoWeight;
    }


    private void checkDirectory(File file, Set<File> list) {
        if (file.isDirectory()) {
            Arrays.stream(file.listFiles()).forEach(file2 -> {
                if (file2.isDirectory()) {
                    checkDirectory(file2, list);
                } else {
                    list.add(file2);
                }
            });
        } else {
            list.add(file);
        }
    }

    public void addMethodsToList(ArrayList<Method> methodsList, Map<String, Map<String, Integer>> methodOneMethodTwoWeight) {

        for (Map.Entry<String, Map<String, Integer>> entry : methodOneMethodTwoWeight.entrySet()) {
            String methodName = entry.getKey();
            Method method = new Method(methodName);
            methodsList.add(method);
            Map<String, Integer> temp = entry.getValue();
            for (Map.Entry<String, Integer> entr : temp.entrySet()) {
                String methodName1 = entr.getKey();
                Method method1 = new Method(methodName1);
                methodsList.add(method1);
            }
        }


        for (Method m : methodsList) {
            System.out.println(m);
        }
    }

    public void addMethodsEdgesToList(ArrayList<EdgeMethod_Method> methodsEdgesList, Map<String, Map<String, Integer>> methodOneMethodTwoWeight) {

        for (Map.Entry<String, Map<String, Integer>> entry : methodOneMethodTwoWeight.entrySet()) {
            Method methodFrom = new Method();
            methodFrom.setMethodName(entry.getKey());
            Map<String, Integer> temp = entry.getValue();
            for (Map.Entry<String, Integer> entr : temp.entrySet()) {
                Method methodTo=new Method();
                int edgeWeight;
                methodTo.setMethodName(entr.getKey());
                edgeWeight=entr.getValue();
                EdgeMethod_Method edge=new EdgeMethod_Method(methodFrom,methodTo,edgeWeight);
                methodsEdgesList.add(edge);
            }

        }
        for(EdgeMethod_Method emm:methodsEdgesList){
            System.out.println(emm);
        }


    }


}

