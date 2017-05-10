package com.ilargia.games.entitas.codeGeneration.codeGenerator;


import com.ilargia.games.entitas.codeGeneration.interfaces.ICodeGenFilePostProcessor;
import com.ilargia.games.entitas.codeGeneration.interfaces.ICodeGenerator;
import com.ilargia.games.entitas.codeGeneration.interfaces.ICodeGeneratorDataProvider;
import com.ilargia.games.entitas.codeGeneration.CodeGenFile;
import com.ilargia.games.entitas.codeGeneration.CodeGeneratorData;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class CodeGenerator {
    public interface GeneratorProgress {
        public void exec(String title, String info, float progress);
    }

    public GeneratorProgress OnProgress;

    private List<ICodeGeneratorDataProvider> _dataProviders;
    private List<ICodeGenerator> _codeGenerators;
    private List<ICodeGenFilePostProcessor> _postProcessors;

    boolean _cancel;

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

        List<CodeGeneratorData> data = new ArrayList<CodeGeneratorData>();

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

        List files = new ArrayList<CodeGenFile>();

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

}
