package Data;
import Common.CommonUtils;
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
    private static String path="src/main/java";
    private JavaSymbolSolver javaSymbolSolver;
    private TypeSolver typeSolver;
    private TypeSolver reflectionTypeSolver;
    private CombinedTypeSolver combinedTypeSolver;
    private File mainFile;
    private AllData allData;
    private static Set<File> classesFiles;
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

    public static HashMap<String, HashMap<String, Integer>> getMethodOneMethodTwoWeight() {
        return methodOneMethodTwoWeight;
    }

    public static HashMap<String, HashMap<String, Integer>> getModuleOneModuleTwoWeight() {
        return moduleOneModuleTwoWeight;
    }

    public static List<String> getClassesNames() {
        return classesNames;
    }

    public void findModuleDependencies(AllData allData) throws IOException, ClassNotFoundException, NoSuchFieldException {

        this.allData = allData;
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
        combinedTypeSolver.add(new JarTypeSolver("jar/java-util-1.8.0.jar"));
        combinedTypeSolver.add(new JarTypeSolver("jar/spring-core-5.2.2.RELEASE.jar"));
        combinedTypeSolver.add(new JarTypeSolver("jar/spring-web-5.2.2.RELEASE.jar"));
        combinedTypeSolver.add(new JarTypeSolver("jar/guava-28.2-jre.jar"));

        this.javaSymbolSolver = new JavaSymbolSolver(combinedTypeSolver);
        StaticJavaParser.getConfiguration().setSymbolResolver(javaSymbolSolver);

        classesFiles = new HashSet<>();
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

            checkDirectory(file, classesFiles);
        });


        ArrayList<File> tempFile = new ArrayList<>();
        classesFiles.forEach(file -> {
            classesNames.add(file.getName().substring(0, file.getName().lastIndexOf(".java")));
        });


    }
    //----------------------------------------------------------------------------------------------------------------------------
    // Historyjka 1
    // Połączenia pomiędzy plikami File_File


    public  HashMap<String, Integer> getFilesWeight() {
        return filesWeight;
    }

    public  HashMap<String, HashMap<String, Integer>> getFileOneFileTwoWeight() {
        return fileOneFileTwoWeight;
    }

    public HashMap<String, HashMap<String,Integer>> FilesConnections(){
        //stworzenie listy plików;
        final ArrayList<JavaFile> listOfJavaFiles = new ArrayList<JavaFile>();
        addAllFilesToList(listOfJavaFiles);


        ArrayList<EdgeFile_File> listOfEdgesFile_File = new ArrayList<EdgeFile_File>();
        // final JavaFile tempTwoJavaFile = new JavaFile();
        final JavaFile tempOneJavaFile = new JavaFile();

        classesFiles.forEach(file -> {
            int value = 0;

            for (JavaFile f : listOfJavaFiles) {
                if (file.getName().substring(0, file.getName().lastIndexOf(".java")).equals(f.getJavaFileName())) {
                    tempOneJavaFile.setJavaFileName(f.getJavaFileName());
                    tempOneJavaFile.setSize(f.getSize());
                    try {
                        tempOneJavaFile.setId(file.getCanonicalFile().toString());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
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
                else{
                    continue;
                }
                fileOneFileTwoWeight.put(file.getName().substring(0, file.getName().lastIndexOf(".java")), fileTwoAndWeight);
            }


        });
//        System.out.println(fileOneFileTwoWeight);
//        System.out.println();
        addAllEdgesToList(listOfEdgesFile_File, listOfJavaFiles, tempOneJavaFile, fileOneFileTwoWeight);
        this.allData.setListOfJavaFiles(listOfJavaFiles);
        this.allData.setListOfEdgesFile_File(listOfEdgesFile_File);
        //System.out.println("Lista plikow:");
        //listOfJavaFiles.forEach(x-> System.out.println(x));
        //System.out.println("Lista krawedzi plik_plik:");
        //listOfEdgesFile_File.forEach(x-> System.out.println(x));
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

        classesFiles.forEach(file -> {
            int fileWeight = (int) file.length();
            //   weight.add(fileWeight);
            JavaFile javaFile = null;
            try {
                javaFile = new JavaFile(file.getCanonicalPath().toString(), file.getName().substring(0, file.getName().lastIndexOf(".java")), fileWeight);
            } catch (IOException e) {
                e.printStackTrace();
            }
            listOfJavaFiles.add(javaFile);
        });
        //ustawienie wartosci pod kolka
        //setJavaFilesSizeForCircles(listOfJavaFiles);

        // listOfJavaFiles.forEach(f -> System.out.println(f));
    }

    //-------------------------------------------------------------------------------------------------------------------------------------
    // Historyjka 2
    // Połączenia pomiędzy metodami
    public HashMap<String, HashMap<String,Integer>> MethodConnections(){

        ArrayList<Method> methodsList = new ArrayList<>();
        ArrayList<EdgeMethod_Method> methodsEdgesList = new ArrayList<>();
        HashMap<String,String> tempListOfMethod=new HashMap<>();
        classesFiles.forEach(file -> {

            CompilationUnit cu = null;
            try {
                cu = StaticJavaParser.parse(file);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            for (MethodDeclaration md : cu.findAll(MethodDeclaration.class)) {
                tempListOfMethod.put(md.resolve().getName(),file.getPath());
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
        methodsList=addMethodsToList(methodsList, methodOneMethodTwoWeight,tempListOfMethod);
        addMethodsEdgesToList(methodsEdgesList,methodOneMethodTwoWeight,methodsList);
        // Mapa methodsWeight zwraca metode i ilosc jej wywołan czyli wagę wezła
        // Zwraca HashMap<String metoda1,<String metoda 2,Integer waga_krawędzi)
        this.allData.setListOfMethods(methodsList);
        this.allData.setListOfEdgesMethod_Method(methodsEdgesList);
        //System.out.println("Lista metod: ");

        //addMethodsToList(methodsList, methodsWeight);
        //System.out.println("Lista krawedzi metoda_metoda: ");


        return methodOneMethodTwoWeight;
    }

    public AllData getAllData() {
        return allData;
    }

    // Historyjka 3
    // Połączenia pomiędzy metodami
    public HashMap<String, HashMap<String,Integer>> ModuleConnections(){
        ArrayList<Package> listOfPackages=new ArrayList<>();
        ArrayList<EdgeMethod_Package.EdgePackage_Package> listOfEdgesPackage_Package=new ArrayList<>();
        final ArrayList<Method> listOfMethods=new ArrayList<>();
        ArrayList<EdgeMethod_Package> listOfEdgesMethod_Package=new ArrayList<>();
        HashMap<String,String> tempPackagesList = new HashMap<>();

        // addListOfPackages(listOfPackages);
        int iter=0;


        classesFiles.forEach(file -> {

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

                    //String nameOfClass=md.resolve().getClassName();
                    String pathToClass= null;
                    pathToClass = file.getPath();
                    String pathToMethod=pathToClass+"\\"+nameOfMethod;
                    System.out.println("Name of method: "+nameOfMethod);
                    System.out.println("Path to class: "+pathToClass);
                    System.out.println("Path to method: "+pathToMethod);

                    for (Method m : listOfMethods) {
                        if (m.getMethodName().equals(nameOfMethod)) {
                            isAlreadyAtList = true;
                        }
                    }
                    if (!isAlreadyAtList) {
                        Method method = new Method(pathToMethod, nameOfMethod);
                        listOfMethods.add(method);
                    }
                }
            }
            HashMap<String,Integer> moduleTwoAndWeight = new HashMap<>();
            HashMap<String,Integer> methodTwoAndWeight = new HashMap<>();
            HashMap<String,Integer> methodTwoAndWeightSecond = new HashMap<>();
            for(MethodDeclaration md : cu.findAll(MethodDeclaration.class)){
                for(MethodCallExpr mce : md.findAll(MethodCallExpr.class)){
                    tempPackagesList.put(md.resolve().getPackageName(), file.getParent());
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


        addListOfPackages(listOfPackages,moduleOneModuleTwoWeight, tempPackagesList);
        addAllEdgesPackage_PackagesToList(listOfPackages,listOfEdgesPackage_Package,moduleOneModuleTwoWeight);
        addAllEdgesMethod_PackagesToList(listOfEdgesMethod_Package,listOfMethods,listOfPackages,methodAndModuleWeight);
        this.allData.setListOfEdgesMethod_Package(listOfEdgesMethod_Package);
        this.allData.setListOfPackages(listOfPackages);
        this.allData.setListOfEdgesPackage_Package(listOfEdgesPackage_Package);
        this.allData.setListOfMethods(listOfMethods);
//        System.out.println("Lista paczek:");
//        listOfPackages.forEach(x-> System.out.println(x));
//        System.out.println("Lista krawedzi paczka_paczka: ");
//        listOfEdgesPackage_Package.forEach(x-> System.out.println(x));
//        System.out.println("Lista krawedzi metoda_paczka: ");
//        listOfEdgesMethod_Package.forEach(x-> System.out.println(x));

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
    public void addListOfPackages(ArrayList<Package> listOfPackages,HashMap<String,HashMap<String,Integer>> hashMapHashMap,
                                  HashMap<String, String> idMap)
    {
        for (Map.Entry<String, HashMap<String, Integer>> firstEntry : hashMapHashMap.entrySet()) {
            String name1=firstEntry.getKey();
            boolean isAlreadyAtList=false;
            for(Package p:listOfPackages){
                if(p.getPackageName().equals(name1)) isAlreadyAtList=true;
            }
            if(!isAlreadyAtList) listOfPackages.add(new Package(idMap.get(name1),name1,500));

            for (Map.Entry<String, Integer> secondEntry : firstEntry.getValue().entrySet()) {
                String name2 = secondEntry.getKey();
                for(Package p:listOfPackages){
                    if(p.getPackageName().equals(name2)) isAlreadyAtList=true;
                }
                if(!isAlreadyAtList) listOfPackages.add(new Package(idMap.get(name2), name2,500));

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

    public ArrayList<Method> addMethodsToList(ArrayList<Method> methodsList, HashMap<String, HashMap<String, Integer>> methodOneMethodTwoWeight,HashMap<String,String> tempListOfMethods) {
        boolean isAlreadyAtList=false;

        for (Map.Entry<String, HashMap<String, Integer>> entry : methodOneMethodTwoWeight.entrySet()) {
            String methodName = entry.getKey();
            String pathToMethod=tempListOfMethods.get(methodName);
            Method method = new Method(pathToMethod, methodName);
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
                pathToMethod=tempListOfMethods.get(methodName1);
                Method method1 = new Method(pathToMethod, methodName1);
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
           // System.out.println(m);
        }
        return methodsList;
    }

    public void addMethodsEdgesToList(ArrayList<EdgeMethod_Method> methodsEdgesList, HashMap<String, HashMap<String, Integer>> methodOneMethodTwoWeight,ArrayList<Method> methodsList) {

//        for (Map.Entry<String, HashMap<String, Integer>> entry : methodOneMethodTwoWeight.entrySet()) {
//            Method methodFrom = new Method();
//            for(int i=0;i<methodsList.size();i++)
//            {
//                if(methodsList.get(i).getMethodName().equals(entry.getKey()))
//                {
//                    methodFrom=methodsList.get(i);
//                }
//            }
//            // methodFrom.setMethodName(entry.getKey());
//            Map<String, Integer> temp = entry.getValue();
//            for (Map.Entry<String, Integer> entr : temp.entrySet()) {
//                Method methodTo=new Method();
//                int edgeWeight;
//                for(int i=0;i<methodsList.size();i++)
//                {
//                    if(methodsList.get(i).getMethodName().equals(entr.getKey()))
//                    {
//                        methodTo=methodsList.get(i);
//                    }
//                }
//                //methodTo.setMethodName(entr.getKey());
//                edgeWeight=entr.getValue();
//                EdgeMethod_Method edge=new EdgeMethod_Method(methodFrom,methodTo,edgeWeight);
//                methodsEdgesList.add(edge);
//            }
//
//        }
//        for(EdgeMethod_Method emm:methodsEdgesList){
//           // System.out.println(emm);
//        }
        EdgeMethod_Method edgeMethod_method=new EdgeMethod_Method();
        Method temp1=new Method();
        Method temp2=new Method();
        for (Map.Entry<String, HashMap<String, Integer>> firstEntry : methodOneMethodTwoWeight.entrySet()) {
            String name1=firstEntry.getKey();
            for(Method method:methodsList) {
                if (method.getMethodName().equals(name1)) {
                    temp1 = method;
                    temp1.setId(method.getId());
                }
            }

            for (Map.Entry<String, Integer> secondEntry : firstEntry.getValue().entrySet()) {
                String name2 = secondEntry.getKey();
                Integer weight = secondEntry.getValue();
                for (Method method : methodsList) {
                    if (method.getMethodName().equals(name2)) {
                        temp2 = method;
                        temp2.setId(method.getId());
                    }
                }
                edgeMethod_method = new EdgeMethod_Method(temp1, temp2, weight);
                // System.out.println(edgeFile_file);
                if (!methodsEdgesList.contains(edgeMethod_method)) methodsEdgesList.add(edgeMethod_method);
            }


        }

    }

    // Historyjka 6
    // Połczenia Metoda -> Plik
    // methodAndFile return hashMap<String fileName, Set<String methods>>

    public HashMap<String,Set<String>> methodFileConnections(){

        ArrayList<JavaFile> listOfJavaFiles = new ArrayList<>();
        ArrayList<Method> listOfDefinedMethods = new ArrayList<>();
        ArrayList<EdgeMethod_File> listOfMethodFileEdges = new ArrayList<>();
        addAllFilesToList(listOfJavaFiles);

        classesFiles.forEach(file -> {
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
                methodAndFile.put(file.getName().substring(0, file.getName().lastIndexOf(".java")), methodsInSpecificFile);
            }
        });

        createListOfDefinedMethods(listOfDefinedMethods,methodAndFile);
        createMethodFileEdges(listOfJavaFiles,listOfDefinedMethods,listOfMethodFileEdges,methodAndFile);
        return methodAndFile;
    }

    public void createListOfDefinedMethods(ArrayList<Method> listOfDefinedMethods, HashMap<String, Set<String>> methodAndFile){

        boolean isAlreadyAtList=false;

        for (Map.Entry<String,Set<String>> entry : methodAndFile.entrySet()) {
            Set<String> tmp = entry.getValue();

            for(String methodName: tmp){
                Method method = new Method(CommonUtils.randomId(), methodName,1);
                listOfDefinedMethods.add(method);
                //System.out.println(listOfDefinedMethods);
            }
        }
        //System.out.println(listOfDefinedMethods);

    }

    public void createMethodFileEdges(ArrayList<JavaFile> listOfJavaFiles, ArrayList<Method> listOfMethods, ArrayList<EdgeMethod_File> edgeMethodFiles, HashMap<String, Set<String>> methodAndFile) {
        Method method = new Method();
        JavaFile javaFile = new JavaFile();
        EdgeMethod_File emf = new EdgeMethod_File();
        //System.out.println("pliki: ");
        for (Map.Entry<String, Set<String>> entry : methodAndFile.entrySet()) {

           // System.out.println(entry.getKey());

            for (JavaFile jf : listOfJavaFiles) {
                if (jf.getJavaFileName().equals(entry.getKey())) {
                    javaFile = jf;
                }

            }
               // for (String ms : listOfMethods) {

                    //System.out.println(ms);
                    for (Method m : listOfMethods) {
                        //System.out.println(m.getMethodName());
                        if (m.getMethodName().equals(entry.getValue())) {
                            method = m;
                        }
                    }
                    emf = new EdgeMethod_File(method, javaFile);

                    if (!edgeMethodFiles.contains(emf)) {
                        edgeMethodFiles.add(emf);
                    }





        }
        for (EdgeMethod_File em : edgeMethodFiles) {
            System.out.println(em.toString());
        }
        allData.getListOfEdgesMethod_File().addAll(edgeMethodFiles);
    }

    public static String getPath() {
        return path;
    }

    public static void setPath(String path) {
        DataGuide.path = path;
    }

}
