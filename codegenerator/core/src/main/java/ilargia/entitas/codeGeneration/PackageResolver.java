package ilargia.entitas.codeGeneration;


import ilargia.entitas.codeGeneration.interfaces.IAppDomain;
import ilargia.entitas.codeGeneration.utils.ClassFinder;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class PackageResolver {
    List<String> _basePaths;

    public PackageResolver(List<String> basePaths) {
        _basePaths = basePaths;

    }

    public List<Class> getTypes() {
        return _basePaths.stream()
                .flatMap(p-> ClassFinder.findRecursive(p).stream())
                .collect(Collectors.toList());

    }
}
