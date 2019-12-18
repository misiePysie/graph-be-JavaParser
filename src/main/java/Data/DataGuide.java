package Data;
import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.expr.MethodCallExpr;
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
    private static String path;
    private JavaSymbolSolver javaSymbolSolver;
    private TypeSolver typeSolver;
    private TypeSolver reflectionTypeSolver;
    private CombinedTypeSolver combinedTypeSolver;
    private File mainFile;
    private AllData allData;

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
    private static HashMap<String, Set<String>> methodAndFile;
    public void findModuleDependencies(AllData temp) throws IOException, ClassNotFoundException, NoSuchFieldException {

        this.combinedTypeSolver = new CombinedTypeSolver();
        this.typeSolver = new JavaParserTypeSolver(path);
        this.mainFile = new File(path);
        reflectionTypeSolver = new ReflectionTypeSolver();
        combinedTypeSolver.add(reflectionTypeSolver);
        combinedTypeSolver.add(typeSolver);
        combinedTypeSolver.add(new JarTypeSolver("jar/javaparser-symbol-solver-core-3.15.5.jar"));
        combinedTypeSolver.add(new JarTypeSolver("jar/javaparser-symbol-solver-logic-3.15.5.jar"));
        combinedTypeSolver.add(new JarTypeSolver("jar/javaparser-symbol-solver-model-3.15.5.jar"));
        combinedTypeSolver.add(new JarTypeSolver("jar/javaparser-core-3.15.5.jar"));
        combinedTypeSolver.add(new JarTypeSolver("jar/gson-2.8.2.jar"));
        combinedTypeSolver.add(new JarTypeSolver("jar/spring-boot-2.2.2.RELEASE.jar"));
        combinedTypeSolver.add(new JarTypeSolver("jar/java-xmlbuilder-1.2.jar"));
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
        methodAndFile = new HashMap<>();
        Arrays.stream(mainFile.listFiles()).forEach(file -> {

            checkDirectory(file, clasesFiles);
        });

        System.out.println(clasesFiles);
        ArrayList<File> tempFile = new ArrayList<>();
        clasesFiles.forEach(file -> {
            classesNames.add(file.getName().substring(0, file.getName().lastIndexOf(".java")));
        });



        this.FilesConnections(temp);
        this.MethodConnections(temp);
        this.ModuleConnections(temp);
        this.MethodFileConnections();

    }
    //----------------------------------------------------------------------------------------------------------------------------
    // Historyjka 1
    // Połączenia pomiędzy plikami File_File


    public HashMap<String, HashMap<String,Integer>> FilesConnections(AllData temp){
        //stworzenie listy plików;
        final ArrayList<JavaFile> listOfJavaFiles = new ArrayList<JavaFile>();
        addAllFilesToList(listOfJavaFiles);


        ArrayList<EdgeFile_File> listOfEdgesFile_File = new ArrayList<EdgeFile_File>();
        // final JavaFile tempTwoJavaFile = new JavaFile();
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


        });
        addAllEdgesToList(listOfEdgesFile_File, listOfJavaFiles, tempOneJavaFile, fileOneFileTwoWeight);
        temp.setListOfJavaFiles(listOfJavaFiles);
        temp.setListOfEdgesFile_File(listOfEdgesFile_File);
        System.out.println("Lista plikow:");
        listOfJavaFiles.forEach(x-> System.out.println(x));
        System.out.println("Lista krawedzi plik_plik:");
        listOfEdgesFile_File.forEach(x-> System.out.println(x));
        return fileOneFileTwoWeight;
    }
//    public void setJavaFilesSizeForCircles(ArrayList<JavaFile> listOfJavaFiles)
//    {
//        double max=-1;
//
//        int index=-1;
//        for (int i = 0; i< listOfJavaFiles.size(); i++)
//        {
//            if(listOfJavaFiles.get(i).getSize()>max) {
//                max = listOfJavaFiles.get(i).getSize();
//                index = i;
//            }
//        }
//        listOfJavaFiles.get(index).setSize(100);
//        int val = 0;
//        for (int i = 0; i < listOfJavaFiles.size(); i++) {
//            val = (int) ((listOfJavaFiles.get(i).getSize() * 100) / max);
//            listOfJavaFiles.get(i).setSize(val);
//        }
//
//    }

    public void addAllEdgesToList(ArrayList<EdgeFile_File> listOfEdgesFile_File, ArrayList<JavaFile> listOFJavaFiles, JavaFile fileOne, HashMap<String,HashMap<String, Integer>> fileOneFileTwoWeight ) {

        EdgeFile_File edgeFile_file=new EdgeFile_File();
        JavaFile temp1=new JavaFile();
        JavaFile temp2=new JavaFile();
        for (Map.Entry<String, HashMap<String, Integer>> firstEntry : fileOneFileTwoWeight.entrySet()) {
            String name1=firstEntry.getKey();
            for(JavaFile file1:listOFJavaFiles) {
                if (file1.getJavaFileName().equals(name1)) {
                    temp1 = file1;
                }
            }

            for (Map.Entry<String, Integer> secondEntry : firstEntry.getValue().entrySet()) {
                String name2 = secondEntry.getKey();
                Integer weight = secondEntry.getValue();
                for (JavaFile file2 : listOFJavaFiles) {
                    if (file2.getJavaFileName().equals(name2)) {
                        temp2 = file2;
                    }
                }
                edgeFile_file = new EdgeFile_File(temp1, temp2, weight);
                // System.out.println(edgeFile_file);
                if (!listOfEdgesFile_File.contains(edgeFile_file)) listOfEdgesFile_File.add(edgeFile_file);
            }


        }
    }

    public void addAllFilesToList(ArrayList<JavaFile> listOfJavaFiles) {

        clasesFiles.forEach(file -> {
            int fileWeight = (int) file.length();
            //   weight.add(fileWeight);
            JavaFile javaFile = new JavaFile(file.getName().substring(0, file.getName().lastIndexOf(".java")), fileWeight);
            listOfJavaFiles.add(javaFile);
        });
        //ustawienie wartosci pod kolka
        //setJavaFilesSizeForCircles(listOfJavaFiles);

        // listOfJavaFiles.forEach(f -> System.out.println(f));
    }

    //-------------------------------------------------------------------------------------------------------------------------------------
    // Historyjka 2
    // Połączenia pomiędzy metodami
    public HashMap<String, HashMap<String,Integer>> MethodConnections(AllData temp){

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
                HashMap<String, Integer> methodTwoAndWeight = new HashMap<>();
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
        temp.setListOfMethods(methodsList);
        temp.setListOfEdgesMethod_Method(methodsEdgesList);
        System.out.println("Lista metod: ");
        addMethodsToList(methodsList, methodOneMethodTwoWeight);
        //addMethodsToList(methodsList, methodsWeight);
        System.out.println("Lista krawedzi metoda_metoda: ");
        addMethodsEdgesToList(methodsEdgesList,methodOneMethodTwoWeight,methodsList);

        return methodOneMethodTwoWeight;
    }
    // Historyjka 3
    // Połączenia pomiędzy metodami
    public HashMap<String, HashMap<String,Integer>> ModuleConnections(AllData temp){
        ArrayList<Package> listOfPackages=new ArrayList<>();
        ArrayList<EdgeMethod_Package.EdgePackage_Package> listOfEdgesPackage_Package=new ArrayList<>();
        final ArrayList<Method> listOfMethods=new ArrayList<>();
        ArrayList<EdgeMethod_Package> listOfEdgesMethod_Package=new ArrayList<>();
        // addListOfPackages(listOfPackages);

        clasesFiles.forEach(file -> {

            CompilationUnit cu = null;
            try {
                cu = StaticJavaParser.parse(file);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            boolean isAlreadyAtList=false;
            for(MethodDeclaration md:cu.findAll(MethodDeclaration.class)){
                if(!Objects.equals(md.resolve().getName(), null)) {
                    String nameOfMethod = md.resolve().getName();
                    for (Method m : listOfMethods) {
                        if (m.getMethodName().equals(nameOfMethod)) {
                            isAlreadyAtList = true;
                        }
                    }
                    if (!isAlreadyAtList) {
                        Method method = new Method(nameOfMethod);
                        listOfMethods.add(method);
                    }
                }


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

        //lista metod musi byc juz wczesniej gotowa :O dltego tzreba wywolywac wczesniej metod_metod


        addListOfPackages(listOfPackages,moduleOneModuleTwoWeight);
        addAllEdgesPackage_PackagesToList(listOfPackages,listOfEdgesPackage_Package,moduleOneModuleTwoWeight);
        addAllEdgesMethod_PackagesToList(listOfEdgesMethod_Package,listOfMethods,listOfPackages,methodAndModuleWeight);
        temp.setListOfEdgesMethod_Package(listOfEdgesMethod_Package);
        temp.setListOfPackages(listOfPackages);
        temp.setListOfEdgesPackage_Package(listOfEdgesPackage_Package);
        temp.setListOfMethods(listOfMethods);
        System.out.println("Lista paczek:");
        listOfPackages.forEach(x-> System.out.println(x));
        System.out.println("Lista krawedzi paczka_paczka: ");
        listOfEdgesPackage_Package.forEach(x-> System.out.println(x));
        System.out.println("Lista krawedzi metoda_paczka: ");
        listOfEdgesMethod_Package.forEach(x-> System.out.println(x));

        return moduleOneModuleTwoWeight;
    }
    public  void addAllEdgesMethod_PackagesToList(ArrayList<EdgeMethod_Package> listOfEdgesMethod_Package,ArrayList<Method> listOfMethods,ArrayList<Package> listOfPackages,HashMap<String,HashMap<String,Integer>> methodAndModuleWeight)
    {
        EdgeMethod_Package edgeMethod_package=new EdgeMethod_Package();
        Method temp1=new Method();
        Package temp2=new Package();
        for (Map.Entry<String, HashMap<String, Integer>> firstEntry : methodAndModuleWeight.entrySet()) {
            String name1=firstEntry.getKey();
            for(Package p:listOfPackages){
                if(p.getPackageName().equals(name1)){
                    temp2=p;
                }
            }

            for (Map.Entry<String, Integer> secondEntry : firstEntry.getValue().entrySet()) {
                String name2 = secondEntry.getKey();
                Integer weight=secondEntry.getValue();
                for(Method m:listOfMethods){
                    if(m.getMethodName().equals(name2)){
                        temp1=m;
                    }
                }
                edgeMethod_package=new EdgeMethod_Package(temp1,temp2,weight);
                if(!listOfEdgesMethod_Package.contains(edgeMethod_package)) listOfEdgesMethod_Package.add(edgeMethod_package);

            }
        }
        //  listOfEdgesMethod_Package.forEach(x-> System.out.println(x));

    }
    public void addAllEdgesPackage_PackagesToList(ArrayList<Package> listOfPackages, ArrayList<EdgeMethod_Package.EdgePackage_Package> listOfEdgesPackage_Package, HashMap<String,HashMap<String,Integer>> moduleOneModuleTwoWeight)
    {
        EdgeMethod_Package.EdgePackage_Package edgePackage_package=new EdgeMethod_Package.EdgePackage_Package();
        Package temp1=new Package();
        Package temp2=new Package();

        for (Map.Entry<String, HashMap<String, Integer>> firstEntry : moduleOneModuleTwoWeight.entrySet()) {
            String name1=firstEntry.getKey();
            for (Map.Entry<String, Integer> secondEntry : firstEntry.getValue().entrySet()) {
                String name2 = secondEntry.getKey();
                Integer weight=secondEntry.getValue();
                for(Package p:listOfPackages){
                    if(p.getPackageName().equals(name1)){
                        temp1=p;
                    }
                    if(p.getPackageName().equals(name2)){
                        temp2=p;
                    }
                }
                edgePackage_package=new EdgeMethod_Package.EdgePackage_Package(temp1,temp2,weight);
                if(!listOfEdgesPackage_Package.contains(edgePackage_package)) listOfEdgesPackage_Package.add(edgePackage_package);

            }
        }
        // listOfEdgesPackage_Package.forEach(x-> System.out.println(x));
    }
    public void addListOfPackages(ArrayList<Package> listOfPackages,HashMap<String,HashMap<String,Integer>> hashMapHashMap)
    {
        for (Map.Entry<String, HashMap<String, Integer>> firstEntry : hashMapHashMap.entrySet()) {
            String name1=firstEntry.getKey();
            boolean isAlreadyAtList=false;
            for(Package p:listOfPackages){
                if(p.getPackageName().equals(name1)) isAlreadyAtList=true;
            }
            if(!isAlreadyAtList) listOfPackages.add(new Package(name1,500));

            for (Map.Entry<String, Integer> secondEntry : firstEntry.getValue().entrySet()) {
                String name2 = secondEntry.getKey();
                for(Package p:listOfPackages){
                    if(p.getPackageName().equals(name2)) isAlreadyAtList=true;
                }
                if(!isAlreadyAtList) listOfPackages.add(new Package(name2,500));

            }
        }
        //listOfPackages.forEach(x-> System.out.println(x));

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

    public void addMethodsToList(ArrayList<Method> methodsList, HashMap<String, HashMap<String, Integer>> methodOneMethodTwoWeight) {
        boolean isAlreadyAtList=false;

        for (Map.Entry<String, HashMap<String, Integer>> entry : methodOneMethodTwoWeight.entrySet()) {
            String methodName = entry.getKey();
            Method method = new Method(methodName);
            for(Method m:methodsList){
                if(m.getMethodName().equals(entry.getKey()))
                {
                    isAlreadyAtList=true;
                }
            }
            if(!isAlreadyAtList){
                methodsList.add(method);
            }
            isAlreadyAtList=false;
            Map<String, Integer> temp = entry.getValue();
            for (Map.Entry<String, Integer> entr : temp.entrySet()) {
                String methodName1 = entr.getKey();
                Method method1 = new Method(methodName1);
                for(Method m:methodsList){
                    if(m.getMethodName().equals(entr.getKey()))
                    {
                        isAlreadyAtList=true;
                    }
                }
                if(!isAlreadyAtList) {
                    methodsList.add(method1);
                }
            }
            isAlreadyAtList=false;
        }


        for (Method m : methodsList) {
            System.out.println(m);
        }
    }

    public void addMethodsEdgesToList(ArrayList<EdgeMethod_Method> methodsEdgesList, HashMap<String, HashMap<String, Integer>> methodOneMethodTwoWeight,ArrayList<Method> methodsList) {

        for (Map.Entry<String, HashMap<String, Integer>> entry : methodOneMethodTwoWeight.entrySet()) {
            Method methodFrom = new Method();
            for(int i=0;i<methodsList.size();i++)
            {
                if(methodsList.get(i).getMethodName().equals(entry.getKey()))
                {
                    methodFrom=methodsList.get(i);
                }
            }
            // methodFrom.setMethodName(entry.getKey());
            Map<String, Integer> temp = entry.getValue();
            for (Map.Entry<String, Integer> entr : temp.entrySet()) {
                Method methodTo=new Method();
                int edgeWeight;
                for(int i=0;i<methodsList.size();i++)
                {
                    if(methodsList.get(i).getMethodName().equals(entr.getKey()))
                    {
                        methodTo=methodsList.get(i);
                    }
                }
                //methodTo.setMethodName(entr.getKey());
                edgeWeight=entr.getValue();
                EdgeMethod_Method edge=new EdgeMethod_Method(methodFrom,methodTo,edgeWeight);
                methodsEdgesList.add(edge);
            }

        }
        for(EdgeMethod_Method emm:methodsEdgesList){
            System.out.println(emm);
        }


    }

    // Historyjka 6
    // Połczenia Metoda -> Plik
    // methodAndFile return hashMap<String fileName, Set<String methods>>

    public HashMap<String,Set<String>> MethodFileConnections(){

        clasesFiles.forEach(file -> {
            Set<String> methodsInSpecificFile = new HashSet<>();
            CompilationUnit cu = null;
            try {
                cu = StaticJavaParser.parse(file);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            for (MethodDeclaration md : cu.findAll(MethodDeclaration.class)) {
                HashMap<String, Integer> methodTwoAndWeight = new HashMap<>();
                methodsInSpecificFile.add(md.resolve().getName());
            }
            if(!methodsInSpecificFile.isEmpty()){
                methodAndFile.put(file.getName(), methodsInSpecificFile);
            }
        });
        return methodAndFile;
    }

    public static String getPath() {
        return path;
    }

    public static void setPath(String path) {
        DataGuide.path = path;
    }
}