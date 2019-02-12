package com.trivago.features;

import com.trivago.exceptions.CucablePluginException;
import com.trivago.files.FileIO;
import com.trivago.gherkin.GherkinDocumentParser;
import com.trivago.logging.CucableLogger;
import com.trivago.properties.PropertyManager;
import com.trivago.runners.RunnerFileContentRenderer;
import com.trivago.vo.CucableFeature;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class FeatureFileConverterTest {

    @Rule
    public TemporaryFolder testFolder = new TemporaryFolder();

    private FeatureFileConverter featureFileConverter;
    private FileIO fileIO;
    private PropertyManager propertyManager;
    private GherkinDocumentParser gherkinDocumentParser;
    private FeatureFileContentRenderer featureFileContentRenderer;
    private RunnerFileContentRenderer runnerFileContentRenderer;

    @Before
    public void setup() {
        gherkinDocumentParser = mock(GherkinDocumentParser.class);
        featureFileContentRenderer = mock(FeatureFileContentRenderer.class);
        runnerFileContentRenderer = mock(RunnerFileContentRenderer.class);
        fileIO = mock(FileIO.class);
        CucableLogger logger = mock(CucableLogger.class);
        propertyManager = new PropertyManager(logger);

        featureFileConverter = new FeatureFileConverter(
                propertyManager,
                gherkinDocumentParser,
                featureFileContentRenderer,
                runnerFileContentRenderer,
                fileIO,
                logger
        );
    }

    @Test(expected = CucablePluginException.class)
    public void convertEmptyPathListToSingleScenariosAndRunnersTest() throws Exception {
        propertyManager.setParallelizationMode(PropertyManager.ParallelizationMode.SCENARIOS.toString());
        List<CucableFeature> cucableFeatures = new ArrayList<>();
        cucableFeatures.add(new CucableFeature("", null));
        featureFileConverter.generateParallelizableFeatures(cucableFeatures);
    }

    @Test(expected = CucablePluginException.class)
    public void convertWithInvalidFeatureTest() throws Exception {
        String generatedFeatureDir = testFolder.getRoot().getPath().concat("/features/");
        String generatedRunnerDir = testFolder.getRoot().getPath().concat("/runners/");

        propertyManager.setNumberOfTestRuns(1);
        propertyManager.setGeneratedFeatureDirectory(generatedFeatureDir);
        propertyManager.setGeneratedRunnerDirectory(generatedRunnerDir);
        propertyManager.setParallelizationMode(PropertyManager.ParallelizationMode.SCENARIOS.toString());

        when(fileIO.readContentFromFile("TEST_PATH")).thenReturn("TEST_CONTENT");
        when(gherkinDocumentParser.getSingleScenariosFromFeature("TEST_CONTENT", "TEST_PATH", null)).thenThrow(new CucablePluginException(""));

        List<CucableFeature> cucableFeatures = new ArrayList<>();
        cucableFeatures.add(new CucableFeature("FEATURE_FILE.feature", null));
        featureFileConverter.generateParallelizableFeatures(cucableFeatures);
    }

    @Test(expected = CucablePluginException.class)
    public void invalidLineNumberTest() throws Exception {
        List<CucableFeature> cucableFeatures = new ArrayList<>();
        cucableFeatures.add(new CucableFeature("FEATURE_FILE.feature", Collections.singletonList(2)));
        featureFileConverter.generateParallelizableFeatures(cucableFeatures);
    }

//    @Test
//    public void convertToSingleScenariosAndRunnersTest() throws Exception {
//        String generatedFeatureDir = testFolder.getRoot().getPath().concat("/features/");
//        String generatedRunnerDir = testFolder.getRoot().getPath().concat("/runners/");
//
//        propertyManager.setNumberOfTestRuns(1);
//        propertyManager.setGeneratedFeatureDirectory(generatedFeatureDir);
//        propertyManager.setGeneratedRunnerDirectory(generatedRunnerDir);
//        propertyManager.setParallelizationMode("scenarios");
//
//        when(fileIO.readContentFromFile("TEST_PATH")).thenReturn("TEST_CONTENT");
//
//        List<SingleScenario> scenarioList = new ArrayList<>();
//        SingleScenario singleScenario = new SingleScenario("feature", "", "", "featureDescription", "name", "scenarioDescription", new ArrayList<>(), new ArrayList<>());
//        scenarioList.add(singleScenario);
//        when(gherkinDocumentParser.getSingleScenariosFromFeature("TEST_CONTENT", "TEST_PATH", null)).thenReturn(scenarioList);
//
//        String featureFileContent = "test";
//        when(featureFileContentRenderer.getRenderedFeatureFileContent(singleScenario)).thenReturn(featureFileContent);
//
//        when(runnerFileContentRenderer.getRenderedRunnerFileContent(any(FeatureRunner.class))).thenReturn("RUNNER_CONTENT");
//
//        List<CucableFeature> cucableFeatures = new ArrayList<>();
//        cucableFeatures.add(new CucableFeature("FEATURE_FILE.feature", null));
//
//        featureFileConverter.generateParallelizableFeatures(cucableFeatures);
//
//        verify(fileIO, times(2)).writeContentToFile(anyString(), anyString());
//    }

//    @Test
//    public void convertToSingleScenariosAndRunnersWithFeaturesModeTest() throws Exception {
//        String generatedFeatureDir = testFolder.getRoot().getPath().concat("/features/");
//        String generatedRunnerDir = testFolder.getRoot().getPath().concat("/runners/");
//
//        propertyManager.setNumberOfTestRuns(1);
//        propertyManager.setGeneratedFeatureDirectory(generatedFeatureDir);
//        propertyManager.setGeneratedRunnerDirectory(generatedRunnerDir);
//        propertyManager.setParallelizationMode("features");
//
//        when(fileIO.readContentFromFile("TEST_PATH")).thenReturn("TEST_CONTENT");
//
//        List<SingleScenario> scenarioList = new ArrayList<>();
//        SingleScenario singleScenario = new SingleScenario("feature", "", "", "featureDescription", "name", "scenarioDescription", new ArrayList<>(), new ArrayList<>());
//        scenarioList.add(singleScenario);
//        when(gherkinDocumentParser.getSingleScenariosFromFeature("TEST_CONTENT", "TEST_PATH")).thenReturn(scenarioList);
//
//        String featureFileContent = "test";
//        when(featureFileContentRenderer.getRenderedFeatureFileContent(singleScenario)).thenReturn(featureFileContent);
//
//        when(runnerFileContentRenderer.getRenderedRunnerFileContent(any(FeatureRunner.class))).thenReturn("RUNNER_CONTENT");
//
//        List<Path> pathList = new ArrayList<>();
//        Path mockPath = mock(Path.class);
//        Path mockFilePath = mock(Path.class);
//        when(mockFilePath.toString()).thenReturn("FEATURE_FILE.feature");
//        when(mockPath.getFileName()).thenReturn(mockFilePath);
//        when(mockPath.toString()).thenReturn("TEST_PATH");
//        pathList.add(mockPath);
//        featureFileConverter.generateParallelizableFeatures(pathList);
//
//        verify(fileIO, times(2)).writeContentToFile(anyString(), anyString());
//    }
//
//    @Test
//    public void convertToSingleScenariosAndMultiRunnersTest() throws Exception {
//        String generatedFeatureDir = testFolder.getRoot().getPath().concat("/features/");
//        String generatedRunnerDir = testFolder.getRoot().getPath().concat("/runners/");
//
//        propertyManager.setNumberOfTestRuns(1);
//        propertyManager.setDesiredNumberOfRunners(1);
//        propertyManager.setGeneratedFeatureDirectory(generatedFeatureDir);
//        propertyManager.setGeneratedRunnerDirectory(generatedRunnerDir);
//        propertyManager.setParallelizationMode("scenarios");
//
//        when(fileIO.readContentFromFile("TEST_PATH")).thenReturn("TEST_CONTENT");
//
//        List<SingleScenario> scenarioList = new ArrayList<>();
//        SingleScenario singleScenario = new SingleScenario("feature", "", "", "featureDescription", "name", "scenarioDescription", new ArrayList<>(), new ArrayList<>());
//        scenarioList.add(singleScenario);
//        scenarioList.add(singleScenario);
//        when(gherkinDocumentParser.getSingleScenariosFromFeature("TEST_CONTENT", "TEST_PATH")).thenReturn(scenarioList);
//
//        String featureFileContent = "test";
//        when(featureFileContentRenderer.getRenderedFeatureFileContent(singleScenario)).thenReturn(featureFileContent);
//
//        when(runnerFileContentRenderer.getRenderedRunnerFileContent(any(FeatureRunner.class))).thenReturn("RUNNER_CONTENT");
//
//        List<Path> pathList = new ArrayList<>();
//        Path mockPath = mock(Path.class);
//        Path mockFilePath = mock(Path.class);
//        when(mockFilePath.toString()).thenReturn("FEATURE_FILE.feature");
//        when(mockPath.getFileName()).thenReturn(mockFilePath);
//        when(mockPath.toString()).thenReturn("TEST_PATH");
//        pathList.add(mockPath);
//        featureFileConverter.generateParallelizableFeatures(pathList);
//
//        verify(fileIO, times(3)).writeContentToFile(anyString(), anyString());
//    }
//
//    @Test
//    public void convertToSingleScenariosAndMultiRunnersFeaturesModeTest() throws Exception {
//        String generatedFeatureDir = testFolder.getRoot().getPath().concat("/features/");
//        String generatedRunnerDir = testFolder.getRoot().getPath().concat("/runners/");
//
//        propertyManager.setNumberOfTestRuns(1);
//        propertyManager.setDesiredNumberOfRunners(1);
//        propertyManager.setGeneratedFeatureDirectory(generatedFeatureDir);
//        propertyManager.setGeneratedRunnerDirectory(generatedRunnerDir);
//        propertyManager.setParallelizationMode(PropertyManager.ParallelizationMode.FEATURES.toString());
//
//        when(fileIO.readContentFromFile("TEST_PATH")).thenReturn("TEST_CONTENT");
//
//        when(runnerFileContentRenderer.getRenderedRunnerFileContent(any(FeatureRunner.class))).thenReturn("RUNNER_CONTENT");
//
//        List<Path> pathList = new ArrayList<>();
//        Path mockPath = mock(Path.class);
//        Path mockFilePath = mock(Path.class);
//        when(mockFilePath.toString()).thenReturn("FEATURE_FILE.feature");
//        when(mockPath.getFileName()).thenReturn(mockFilePath);
//        when(mockPath.toString()).thenReturn("TEST_PATH");
//        pathList.add(mockPath);
//        featureFileConverter.generateParallelizableFeatures(pathList);
//
//        verify(fileIO, times(2)).writeContentToFile(anyString(), anyString());
//    }
}
