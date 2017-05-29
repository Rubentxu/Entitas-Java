package ilargia.entitas.fixtures;


import ilargia.entitas.codeGeneration.data.SourceDataFile;
import org.jboss.forge.roaster.Roaster;
import org.jboss.forge.roaster.model.source.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;


public class FixtureProvider  {

    private List<SourceDataFile> sourceDataFiles;
    private Set<String> contextNames;
    private String componentsDirectory;

    public FixtureProvider(String componentsDirectory) throws IOException {
        this.componentsDirectory = componentsDirectory;
        contextNames = new HashSet<>();
        sourceDataFiles = new ArrayList<>();

    }

    public static Map<String, List<File>> readFileComponents(String pathname) {
        Map<String, List<File>> recursiveList = new HashMap(){{put("",new ArrayList<>());}};
        File d = new File(pathname);

        if (d.isDirectory()) {
            for (File listFile : d.listFiles()) {
                if (listFile.isDirectory()) {
                    List<File> listSubDir = Arrays.asList(listFile.listFiles());
                    if(listSubDir.size() > 0) {
                        Path path = Paths.get(listSubDir.get(0).getAbsolutePath());
                        String subDir = path.getName(path.getNameCount() - 2).toString();
                        recursiveList.put(subDir, listSubDir );
                    }

                } else {
                    recursiveList.get("").add(listFile);
                }
            }

        }
        return recursiveList;

    }



    public List<SourceDataFile> getSourceDataFiles() {
        if (sourceDataFiles == null || sourceDataFiles.size() == 0) {
            Map<String, List<File>> mapFiles = readFileComponents(componentsDirectory);
            mapFiles.forEach((subDir, files) -> {
                sourceDataFiles.addAll(files.stream()
                        .map((file) -> {
                            try {
                                return Roaster.parse(JavaClassSource.class, file);
                            } catch (FileNotFoundException e) {
                                return null;
                            }
                        }).filter((source) -> source != null)
                       // .filter((source) -> source.getInterfaces().toString().matches(".*\\bIComponent\\b.*"))
                        .map((source) -> createSourceDataFile(source, subDir))
                        .filter(info -> info != null)
                        .collect(Collectors.toList()));
            });
        }
        return sourceDataFiles;

    }



    public SourceDataFile createSourceDataFile(JavaClassSource component, String subDir) {
            return new SourceDataFile(component.getName(), componentsDirectory, subDir, component);

    }


}
