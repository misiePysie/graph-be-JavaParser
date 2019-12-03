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
import sun.security.krb5.internal.PAData;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;


public class DataGuide {

    private JavaSymbolSolver javaSymbolSolver;
    private TypeSolver typeSolver;
    private TypeSolver reflectionTypeSolver;
    private CombinedTypeSolver combinedTypeSolver;
    private File mainFile;

    private static Set<File> clasesFiles;
    private static List<String> classesNames;
    private static List<String> methodsNames;
    private static HashMap<String,Integer> filesWeight;
    private static HashMap<String,Integer> methodsWeight;
    private static HashMap<String,HashMap<String,Integer>> fileOneFileTwoWeight;
    private static HashMap<String,HashMap<String,Integer>> methodOneMethodTwoWeight;
    private static HashMap<String,HashMap<String,Integer>> moduleOneModuleTwoWeight;
    private static HashMap<String,HashMap<String,Integer>> methodAndModuleWeight;
    private static HashMap<String,HashMap<String,Integer>> methodAndModuleWeightOne;
    private static HashMap<String,HashMap<String,Integer>> methodAndModuleWeightTwo;

    public void findModuleDependencies(String rootPath,AllData allData) throws IOException, ClassNotFoundException, NoSuchFieldException {

        this.combinedTypeSolver = new CombinedTypeSolver();
        this.typeSolver = new JavaParserTypeSolver(rootPath);
        this.mainFile = new File(rootPath);
        reflectionTypeSolver = new ReflectionTypeSolver();
        combinedTypeSolver.add(reflectionTypeSolver);
        combinedTypeSolver.add(typeSolver);
        combinedTypeSolver.add(new JarTypeSolver("F:/Java/Projects/IOIOIO/jary/javaparser-symbol-solver-core-3.15.5.jar"));
        combinedTypeSolver.add(new JarTypeSolver("F:/Java/Projects/IOIOIO/jary/javaparser-symbol-solver-logic-3.15.5.jar"));
        combinedTypeSolver.add(new JarTypeSolver("F:/Java/Projects/IOIOIO/jary/javaparser-symbol-solver-model-3.15.5.jar"));
        combinedTypeSolver.add(new JarTypeSolver("F:/Java/Projects/IOIOIO/jary/javaparser-core-3.15.5.jar"));
        combinedTypeSolver.add(new JarTypeSolver("F:/Java/Projects/IOIOIO/jary/javaparser-symbol-solver-model-3.15.5.jar"));
        combinedTypeSolver.add(new JarTypeSolver("F:/Java/Projects/IOIOIO/jary/gson-2.8.2.jar"));
        this.javaSymbolSolver = new JavaSymbolSolver(combinedTypeSolver);
        StaticJavaParser.getConfiguration().setSymbolResolver(javaSymbolSolver);

        clasesFiles = new HashSet<>();
        classesNames = new ArrayList<>();
        methodsNames = new ArrayList<>();
        filesWeight = new HashMap<>();
        fileOneFileTwoWeight = new HashMap<>();
        methodOneMethodTwoWeight = new HashMap<>();
        methodsWeight = new HashMap<>();
        moduleOneModuleTwoWeight = new HashMap<>();
        methodAndModuleWeightOne = new HashMap<>();
        methodAndModuleWeightTwo = new HashMap<>();
        methodAndModuleWeight = new HashMap<>();
        Arrays.stream(mainFile.listFiles()).forEach(file -> {

            checkDirectory(file, clasesFiles);
        });

        clasesFiles.forEach(file -> {
            classesNames.add(file.getName().substring(0,file.getName().lastIndexOf(".java")));
        });



//     FilesConnections(allData);
//     MethodConnections();
     ModuleConnections(allData);


    }
    //----------------------------------------------------------------------------------------------------------------------------
    // Historyjka 1
    // Połączenia pomiędzy plikami File_File


    public HashMap<String, HashMap<String,Integer>> FilesConnections(AllData allData){
        //stworzenie listy plików;
        final ArrayList<JavaFile> listOfJavaFiles=new ArrayList<JavaFile>();
        addAllFilesToList(listOfJavaFiles);


        ArrayList<EdgeFile_File> listOfEdgesFile_File=new ArrayList<EdgeFile_File>();
        final JavaFile tempTwoJavaFile=new JavaFile();
        final JavaFile tempOneJavaFile=new JavaFile();

        clasesFiles.forEach(file -> {
            int value=0;

            for (JavaFile f:listOfJavaFiles) {
                if(file.getName().substring(0, file.getName().lastIndexOf(".java")).equals(f.getJavaFileName())){
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

            HashMap<String,Integer> fileTwoAndWeight = new HashMap<>();
            boolean isAlreadyInList=false;

            for(MethodCallExpr mce : cu.findAll(MethodCallExpr.class)){

                    if(classesNames.contains(mce.resolve().getClassName())){

                        if(fileTwoAndWeight.containsKey(mce.resolve().getClassName())){
                            value = fileTwoAndWeight.get(mce.resolve().getClassName()) + 1;
                            fileTwoAndWeight.put(mce.resolve().getClassName(),value);
                        }else{
                            fileTwoAndWeight.put(mce.resolve().getClassName(),1);
                        }

                        //System.out.println("File one : " + file.getName().substring(0, file.getName().lastIndexOf(".java")) + "\t File two : " + mce.resolve().getClassName()  );
                    }

                fileOneFileTwoWeight.put(file.getName().substring(0, file.getName().lastIndexOf(".java")),fileTwoAndWeight);
            }
            addAllEdgesToList(listOfEdgesFile_File,listOfJavaFiles,tempOneJavaFile,fileTwoAndWeight);


        });
        allData.setListOfJavaFiles(listOfJavaFiles);
        allData.setListOfEdgesFile_File(listOfEdgesFile_File);
        return fileOneFileTwoWeight;
    }
    public void setJavaFilesSizeForCircles(ArrayList<JavaFile> listOfJavaFiles)
    {
        double max=-1;

        int index=-1;
        for (int i = 0; i< listOfJavaFiles.size(); i++)
        {
            if(listOfJavaFiles.get(i).getSize()>max) {
                max = listOfJavaFiles.get(i).getSize();
                index = i;
            }
        }
        listOfJavaFiles.get(index).setSize(100);
        int val=0;
        for(int i = 0; i< listOfJavaFiles.size(); i++)
        {
            val=(int)((listOfJavaFiles.get(i).getSize()*100)/max);
            listOfJavaFiles.get(i).setSize(val);
        }

    }
    public void addAllEdgesToList(ArrayList<EdgeFile_File> listOfEdgesFile_File,ArrayList<JavaFile> listOFJavaFiles,JavaFile fileOne,HashMap<String,Integer> fileTwoAndWeight){

        listOFJavaFiles.forEach(f->{
            if(fileTwoAndWeight.containsKey(f.getJavaFileName())){
                int weight=fileTwoAndWeight.get(f.getJavaFileName()).intValue();
                listOfEdgesFile_File.add(new EdgeFile_File(fileOne,f,weight));
            }
        });
        listOfEdgesFile_File.forEach(e-> System.out.println(e));
    }
    public void addAllFilesToList(ArrayList<JavaFile> listOfJavaFiles){

        clasesFiles.forEach(file -> {
                    int fileWeight = (int)file.length();
                    //   weight.add(fileWeight);
                    JavaFile javaFile=new JavaFile(file.getName().substring(0, file.getName().lastIndexOf(".java")),fileWeight);
                    listOfJavaFiles.add(javaFile);
                });
        //ustawienie wartosci pod kolka
        setJavaFilesSizeForCircles(listOfJavaFiles);

        listOfJavaFiles.forEach(f-> System.out.println(f));
    }
    //-------------------------------------------------------------------------------------------------------------------------------------
    // Historyjka 2
    // Połączenia pomiędzy metodami
    public HashMap<String, HashMap<String,Integer>> MethodConnections(){

        clasesFiles.forEach(file -> {
            CompilationUnit cu = null;
            try {
                cu = StaticJavaParser.parse(file);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            for(MethodDeclaration md : cu.findAll(MethodDeclaration.class)){
                HashMap<String,Integer> methodTwoAndWeight = new HashMap<>();
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
    // Historyjka 3
    // Połączenia pomiędzy metodami
    public HashMap<String, HashMap<String,Integer>> ModuleConnections(AllData allData){
        ArrayList<Package> listOfPackages=new ArrayList<>();

        addListOfPackages(listOfPackages);


        clasesFiles.forEach(file -> {

            CompilationUnit cu = null;
            try {
                cu = StaticJavaParser.parse(file);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

            HashMap<String,Integer> moduleTwoAndWeight = new HashMap<>();
            HashMap<String,Integer> methodTwoAndWeight = new HashMap<>();
            HashMap<String,Integer> methodTwoAndWeightSecond = new HashMap<>();
            for(MethodDeclaration md : cu.findAll(MethodDeclaration.class)){
                for(MethodCallExpr mce : md.findAll(MethodCallExpr.class)){
                    if(classesNames.contains(mce.resolve().getClassName())){
                        if(!moduleTwoAndWeight.containsKey(mce.resolve().getPackageName())){
                            moduleTwoAndWeight.put(mce.resolve().getPackageName(),1);
                        }
                        else{
                            int value = moduleTwoAndWeight.get(mce.resolve().getPackageName()) + 1;
                            moduleTwoAndWeight.put(mce.resolve().getPackageName(),value);
                        }

                        if(!methodTwoAndWeight.containsKey(mce.resolve().getName())){
                            methodTwoAndWeight.put(mce.resolve().getName(),1);
                        }
                        else{
                            int value = methodTwoAndWeight.get(mce.resolve().getName()) + 1;
                            methodTwoAndWeight.put(mce.resolve().getName(),value);
                        }

                        if(!methodTwoAndWeightSecond.containsKey(md.resolve().getName())){
                            methodTwoAndWeightSecond.put(md.resolve().getName(),1);
                        }
                        else{
                            int value = methodTwoAndWeightSecond.get(md.resolve().getName()) + 1;
                            methodTwoAndWeightSecond.put(md.resolve().getName(),value);
                        }
                        methodAndModuleWeightTwo.put(mce.resolve().getPackageName(),methodTwoAndWeight);
                        methodAndModuleWeightOne.put(md.resolve().getPackageName(),methodTwoAndWeightSecond);
                        moduleOneModuleTwoWeight.put(md.resolve().getPackageName(),moduleTwoAndWeight);

                    }
                }

            }

        });

        for(String key: methodAndModuleWeightOne.keySet() ){
                if(methodAndModuleWeightTwo.containsKey(key)){
                    methodAndModuleWeightTwo.get(key).forEach((k, v) -> methodAndModuleWeightOne.get(key).merge(k, v, Integer::sum));
                }else{
                    methodAndModuleWeightTwo.put(key,methodAndModuleWeightOne.get(key));
                }
        }
        methodAndModuleWeight.putAll(methodAndModuleWeightOne);
        // methodAndModuleWeight mapa zawierająca <Paczka,<Metoda,wagakrawedzi>>
        // moduleOneModuleTwoWeight mapa zawierajaca<Paczka1,<Paczka2, waga krawedzi>>
        System.out.println( "Method_Module: "+methodAndModuleWeight);
        System.out.println("Module_Module:"+moduleOneModuleTwoWeight);


       // setPackagesSizeForCircles(listOfPackages);
       // listOfPackages.forEach(x-> System.out.println(x));



        return moduleOneModuleTwoWeight;
    }
    public void setPackagesSizeForCircles(ArrayList<Package> listOfPackages)
    {
        int max=-1;
        int index=0;
        for (int i = 0; i< listOfPackages.size(); i++)
        {
            if(listOfPackages.get(i).getSize()>max) {
                max = listOfPackages.get(i).getSize();
                index = i;
            }
        }
        listOfPackages.get(index).setSize(500);
        int val=0;
        for(int i = 0; i< listOfPackages.size(); i++)
        {
            val=(listOfPackages.get(i).getSize()*500)/max;
            listOfPackages.get(i).setSize(val);
        }

    }
    public void addAllEdgesPackage_PackagesToList(ArrayList<Package> listOfPackages,ArrayList<EdgePackage_Package> listOfEdgesPackage_Package,HashMap<String,HashMap<String,Integer>> moduleOneModuleTwoWeight)
    {
        EdgePackage_Package edgePackage_package;
        Iterator firstIterator=moduleOneModuleTwoWeight.entrySet().iterator();
        Iterator secondIterator;
        while(firstIterator.hasNext()){
            Map.Entry mapElement=(Map.Entry)firstIterator.next();
            //Map.Entry mapInsideElement=(Map.Entry)

         // edgePackage_package=new EdgePackage_Package(mapElement.getKey().toString(),mapElement.getValue().toString());
        }
       // Package p=new Package(moduleOneModuleTwoWeight.ge)
    }
    public void addListOfPackages(ArrayList<Package> listOfPackages)
    {

        clasesFiles.forEach(file -> {
            boolean isAlreadyAtList=false;
            int ppackageWeight = (int) file.getParentFile().length();
            //   weight.add(fileWeight);
            Package aPackage = new Package(file.getParentFile().getName(), ppackageWeight);


            //if(!listOfPackages.contains(aPackage)) listOfPackages.add(aPackage);
          //  System.out.println(aPackage);
            for (Package p : listOfPackages) {
                if (p.getPackageName().equals(aPackage.getPackageName())) {
                    isAlreadyAtList = true;
                }
            }
            if (!isAlreadyAtList){
                listOfPackages.add(aPackage);
            }
        });

        listOfPackages.forEach(x-> System.out.println(x));
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

