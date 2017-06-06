package ilargia.entitas.codeGeneration.plugins.dataProviders;

import ilargia.entitas.codeGeneration.interfaces.IAppDomain;
import ilargia.entitas.codeGeneration.plugins.dataProviders.components.ComponentData;
import ilargia.entitas.codeGeneration.utils.CodeGeneratorUtil;
import org.jboss.forge.roaster.Roaster;
import org.jboss.forge.roaster.model.source.JavaClassSource;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ProviderUtils {

    public static List<ComponentData> getComponentDatas(IAppDomain appDomain, List<String> packages) {
        List<ComponentData> datas = new ArrayList<>();
        appDomain.getSrcDirs().forEach(path -> {
            packages.forEach(p -> {
                String pkg= "";
                if(System.getProperty("os.name").toLowerCase().contains("win")) {
                    pkg = p.replace(".", "\\");
                } else {
                    pkg = p.replace(".", "/");
                }
                String finalPkg = pkg;
                Map<String, List<File>> mapFiles = CodeGeneratorUtil.readFileComponents(path, finalPkg);
                mapFiles.forEach((subDir, files) -> {

                    datas.addAll(files.stream()
                            .filter(f -> {
                                return f.getAbsolutePath().contains(finalPkg);
                            })
                            .map((file) -> {
                                try {
                                    return Roaster.parse(JavaClassSource.class, file);
                                } catch (FileNotFoundException e) {
                                    return null;
                                }
                            }).filter((source) -> source != null)
                            .map((source) -> new ComponentData(source, subDir))
                            .filter(info -> info != null)
                            .collect(Collectors.toList()));
                });
            });
        });
        return datas.stream().sorted((a, b)-> a.getSource().getName().compareTo(b.getSource().getName())).collect(Collectors.toList());

    }
}
