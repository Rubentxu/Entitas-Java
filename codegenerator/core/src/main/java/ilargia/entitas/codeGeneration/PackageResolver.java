package ilargia.entitas.codeGeneration;


import ilargia.entitas.codeGeneration.interfaces.IAppDomain;

import java.util.List;

public class PackageResolver {

    private IAppDomain _appDomain;
    List<String> _basePaths;


    public PackageResolver(IAppDomain appDomain, List<String> basePaths) {
        _appDomain = appDomain;
        _appDomain.AssemblyResolve += onAssemblyResolve;
        _basePaths = basePaths;

    }

    public void Load(string path) {
        _logger.Debug("AppDomain load: " + path);
        var assembly = _appDomain.Load(path);
        _assemblies.Add(assembly);
    }

    Assembly onAssemblyResolve(object sender, ResolveEventArgs args) {
        Assembly assembly = null;
        try {
            _logger.Debug("  - Loading: " + args.Name);
            assembly = Assembly.LoadFrom(args.Name);
        } catch(Exception) {
            var name = new AssemblyName(args.Name).Name;
            if (!name.EndsWith(".dll", StringComparison.Ordinal)) {
                name += ".dll";
            }

            var path = resolvePath(name);
            if (path != null) {
                assembly = Assembly.LoadFrom(path);
            }
        }

        return assembly;
    }

    String resolvePath(String packageName) {
        for (String basePath : _basePaths) {
            var path = basePath + Path.DirectorySeparatorChar + assemblyName;
            if (File.Exists(path)) {
                _logger.Debug("    - Resolved: " + path);
                return path;
            }
        }        
        return null;
    }

    public List<Class> getTypes() {
        List<Class<?>> types = new ArrrayList<>();
        foreach (var assembly in _assemblies) {
            try {
                types.AddRange(assembly.GetTypes());
            } catch(ReflectionTypeLoadException ex) {
                types.AddRange(ex.Types.Where(type => type != null));
            }
        }

        return types.ToArray();
    }
}
