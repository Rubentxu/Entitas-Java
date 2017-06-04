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
            packages.forEach(pkg -> {
                Map<String, List<File>> mapFiles = CodeGeneratorUtil.readFileComponents(path, pkg);
                mapFiles.forEach((subDir, files) -> {
                    datas.addAll(files.stream()
                            .filter(f -> f.getAbsolutePath().contains(pkg))
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
        return datas;

    }
}
