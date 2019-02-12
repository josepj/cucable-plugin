package com.trivago.files;

import com.trivago.exceptions.filesystem.PathCreationException;
import com.trivago.properties.PropertyManager;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class FileSystemManagerTest {
    @Rule
    public TemporaryFolder testFolder = new TemporaryFolder();
    private PropertyManager propertyManager;
    private FileSystemManager fileSystemManager;

    @Before
    public void setup() {
        propertyManager = mock(PropertyManager.class);
        fileSystemManager = new FileSystemManager(propertyManager);
    }

    @Test(expected = PathCreationException.class)
    public void prepareGeneratedFeatureAndRunnerDirsMissingFeatureDirTest() throws Exception {
        when(propertyManager.getGeneratedFeatureDirectory()).thenReturn("");
        fileSystemManager.prepareGeneratedFeatureAndRunnerDirectories();
    }

    @Test(expected = PathCreationException.class)
    public void prepareGeneratedFeatureAndRunnerDirsMissingRunnerDirTest() throws Exception {
        String featurePath = testFolder.getRoot().getPath().concat("/featureDir");
        when(propertyManager.getGeneratedFeatureDirectory()).thenReturn(featurePath);
        when(propertyManager.getGeneratedRunnerDirectory()).thenReturn("");
        fileSystemManager.prepareGeneratedFeatureAndRunnerDirectories();
    }

    @Test
    public void prepareGeneratedFeatureAndRunnerDirsTest() throws Exception {
        String featurePath = testFolder.getRoot().getPath().concat("/featureDir");
        String runnerPath = testFolder.getRoot().getPath().concat("/runnerDir");
        when(propertyManager.getGeneratedFeatureDirectory()).thenReturn(featurePath);
        when(propertyManager.getGeneratedRunnerDirectory()).thenReturn(runnerPath);
        fileSystemManager.prepareGeneratedFeatureAndRunnerDirectories();
    }
//
//    @Test(expected = CucablePluginException.class)
//    public void invalidSourceFeaturesTest() throws Exception {
//        when(propertyManager.getSourceFeatures()).thenReturn(new ArrayList<>());
//        fileSystemManager.getFeatureFilePaths();
//    }
//
//    @Test(expected = NullPointerException.class)
//    public void getFeatureFilePathsInvalidSourceFeaturesTest() throws Exception {
//        fileSystemManager.getFeatureFilePaths();
//    }
//
//    @Test
//    public void getFeatureFilePathsEmptySourceFeaturesTest() throws Exception {
//        String sourceFeatures = testFolder.getRoot().getPath();
//        when(propertyManager.getSourceFeatures()).thenReturn(Collections.emptyList());
//        List<Path> featureFilePaths = fileSystemManager.getFeatureFilePaths();
//        assertThat(featureFilePaths, is(notNullValue()));
//    }
//
//    @Test(expected = CucablePluginException.class)
//    public void notAFeatureFileOrDirectoryTest() throws Exception {
//        String sourceFeatures = testFolder.getRoot().getPath() + "notExisting";
//        when(propertyManager.getSourceFeatures()).thenReturn(Collections.emptyList());
//        List<Path> featureFilePaths = fileSystemManager.getFeatureFilePaths();
//    }
//
//    @Test
//    public void singleFeatureTest() throws Exception {
//        String sourceFeature = testFolder.getRoot().getPath() + "/myFeature.feature";
//        PrintStream ps = new PrintStream(sourceFeature);
//        when(propertyManager.getSourceFeatures()).thenReturn(Collections.emptyList());
//        List<Path> featureFilePaths = fileSystemManager.getFeatureFilePaths();
//        assertThat(featureFilePaths.size(), is(1));
//        assertThat(featureFilePaths.get(0).toString(), is(sourceFeature));
//    }
}
