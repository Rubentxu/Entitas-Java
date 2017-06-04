package ilargia.entitas.codeGeneration;


import ilargia.entitas.codeGeneration.utils.CodeFinder;

import java.util.List;
import java.util.stream.Collectors;

public class PackageResolver {
    List<String> _basePaths;

    public PackageResolver(List<String> basePaths) {
        _basePaths = basePaths;

    }

    public List<Class> getTypes() {
        return _basePaths.stream()
                .flatMap(p-> CodeFinder.findClassRecursive(p).stream())
                .collect(Collectors.toList());

    }
}
