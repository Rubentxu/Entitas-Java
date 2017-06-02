package ilargia.entitas.codeGeneration;


import ilargia.entitas.codeGeneration.data.CodeGenFile;
import ilargia.entitas.codeGeneration.data.CodeGeneratorData;
import ilargia.entitas.codeGeneration.interfaces.ICodeGenFilePostProcessor;
import ilargia.entitas.codeGeneration.interfaces.ICodeGenerator;
import ilargia.entitas.codeGeneration.interfaces.ICodeGeneratorDataProvider;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class CodeGenerator {
    public GeneratorProgress OnProgress;
    boolean _cancel;
    private List<ICodeGeneratorDataProvider> _dataProviders;
    private List<ICodeGenerator> _codeGenerators;
    private List<ICodeGenFilePostProcessor> _postProcessors;

    public CodeGenerator(List<ICodeGeneratorDataProvider> dataProviders,
                         List<ICodeGenerator> codeGenerators,
                         List<ICodeGenFilePostProcessor> postProcessors) {

        _dataProviders = dataProviders.stream()
                .sorted((a, b) -> a.gePriority().compareTo(b.gePriority()))
                .collect(Collectors.toList());
        _codeGenerators = codeGenerators.stream()
                .sorted((a, b) -> a.gePriority().compareTo(b.gePriority()))
                .collect(Collectors.toList());
        _postProcessors = postProcessors.stream()
                .sorted((a, b) -> a.gePriority().compareTo(b.gePriority()))
                .collect(Collectors.toList());
    }

    public List<CodeGenFile> dryRun() {
        return generate(
                "[Dry Run] ",
                _dataProviders.stream().filter(i -> i.runInDryMode()).collect(Collectors.toList()),
                _codeGenerators.stream().filter(i -> i.runInDryMode()).collect(Collectors.toList()),
                _postProcessors.stream().filter(i -> i.runInDryMode()).collect(Collectors.toList())
        );
    }

    public List<CodeGenFile> generate() {
        return generate(
                "",
                _dataProviders,
                _codeGenerators,
                _postProcessors
        );
    }

    List<CodeGenFile> generate(String messagePrefix, List<ICodeGeneratorDataProvider> dataProviders,
                               List<ICodeGenerator> codeGenerators, List<ICodeGenFilePostProcessor> postProcessors) {
        _cancel = false;

        List<CodeGeneratorData> data = new ArrayList<>();

        int total = dataProviders.size() + codeGenerators.size() + postProcessors.size();
        int progress = 0;

        for (ICodeGeneratorDataProvider dataProvider : dataProviders) {
            if (_cancel) {
                return new ArrayList<>();
            }

            progress += 1;
            if (OnProgress != null) {
                OnProgress.exec(messagePrefix + "Creating model", dataProvider.getName(), (float) progress / total);
            }

            data.addAll(dataProvider.getData());
        }

        List<CodeGenFile> files = new ArrayList<>();

        for (ICodeGenerator generator : codeGenerators) {

            if (_cancel) {
                return new ArrayList<>();
            }

            progress += 1;
            if (OnProgress != null) {
                OnProgress.exec(messagePrefix + "Creating files", generator.getName(), (float) progress / total);
            }

            files.addAll(generator.generate(data));
        }


        for (ICodeGenFilePostProcessor postProcessor : postProcessors) {

            if (_cancel) {
                return new ArrayList<>();
            }
            progress += 1;
            if (OnProgress != null) {
                OnProgress.exec(messagePrefix + "Processing files", postProcessor.getName(), (float) progress / total);
            }

            files = postProcessor.postProcess(files);
        }

        return files;
    }

    public void Cancel() {
        _cancel = true;
    }

    @FunctionalInterface
    public interface GeneratorProgress {
        public void exec(String title, String info, float progress);
    }

}
