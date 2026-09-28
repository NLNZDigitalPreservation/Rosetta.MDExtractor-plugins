package com.exlibris.rosetta.repository.plugin.mdExtractor.pdf;

import com.exlibris.rosetta.repository.plugin.mdExtractor.base.AbstractNLNZMDExtractorPluginTest;

import edu.harvard.hul.ois.jhove.NisoImageMetadata;
import edu.harvard.hul.ois.jhove.Property;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.File;
import java.net.URL;
import java.util.LinkedList;
import java.util.Objects;
import java.util.List;

import static org.junit.Assume.assumeTrue;

public class PDFHULMDExtractorPluginTest extends AbstractNLNZMDExtractorPluginTest {
    private static PDFHULMDExtractorPlugin testInstance = new PDFHULMDExtractorPlugin();

    @BeforeClass
    public static void init() {
        testInstance.initParams(initParams);
    }

    @Test
    public void testGetAgent() {
        String agent = testInstance.getAgent();
        assert agent != null;
    }

    @Test
    public void testExtractAll() throws Exception {
        ClassLoader classLoader = getClass().getClassLoader();
        File file = new File(Objects.requireNonNull(classLoader.getResource("images.pdf")).getFile());
        testInstance.extract(file.getAbsolutePath());
        // String a =
        // testInstance.getAttributeByName("PDFMetadata.Pages.Page.Sequence");
        String a = testInstance.getAttributeByName("PDFMetadata.Images.Image.NisoImageMetadata.CompressionScheme");
        assert a != null;
        assert a.equals("6");

        String b = testInstance.getAttributeByName("PDFMetadata.Objects");
        assert b != null;
    }

    @Test
    public void testOriginalExtractAll() throws Exception {
        ClassLoader classLoader = getClass().getClassLoader();
        File file = new File(Objects.requireNonNull(classLoader.getResource("sample.pdf")).getFile());
        testInstance.extract(file.getAbsolutePath());
        // String a =
        // testInstance.getAttributeByName("PDFMetadata.Pages.Page.Sequence");
        LinkedList a = (LinkedList) testInstance.getOriginalAttributeByName("PDFMetadata.Pages");
        assert a != null;
        assert a.size() == 2;

        Object b = testInstance.getOriginalAttributeByName("PDFMetadata.Objects");
        assert b != null;
    }

    @Test
    public void testPagesPageCount() throws Exception {
        ClassLoader classLoader = getClass().getClassLoader();
        File file = new File(Objects.requireNonNull(classLoader.getResource("sample.pdf")).getFile());
        testInstance.extract(file.getAbsolutePath());
        String a = testInstance.getAttributeByName("PDFMetadata.Pages.PageCount");
        assert a != null;
        assert a.equals("2");
    }

    @Override
    public void testGetSupportedAttributeNames() throws Exception {

    }

    @Override
    public void testExtract() throws Exception {

    }

    @Override
    protected String getExpectedFileFormatName() {
        return null;
    }

    @Override
    protected void prepareExpectedValues() {

    }

    @Test
    public void testNegativeObjectsCount() throws Exception {
        ClassLoader classLoader = getClass().getClassLoader();
        File file = new File(Objects.requireNonNull(classLoader.getResource("R1603377.pdf")).getFile());
        testInstance.extract(file.getAbsolutePath());

        // PDFMetadata.Objects
        Object b = testInstance.getAttributeByName("PDFMetadata.PDFMetadata.Objects");
        assert b != null;
        assert b.toString().equals("-1");
    }

    @Test
    public void testExtractImages() throws Exception {
        ClassLoader classLoader = getClass().getClassLoader();
        URL resource = classLoader.getResource("R1603377.pdf");
        assumeTrue("Skipping testExtractImages because fixture file is missing: R1603377.pdf", resource != null);

        File file = new File(resource.toURI());
        testInstance.extract(file.getAbsolutePath());

        // PDFMetadata.Images
        List<Property> b = (List<Property>) testInstance.getOriginalAttributeByName("PDFMetadata.PDFMetadata.Images");
        assert b != null;
        for (Property property : b) {
            assert property.getName().equals("Image");
            System.out.println(property.getName() + " : " + property.getValue());
            List<Property> imageProperties = (List<Property>) property.getValue();
            for (Property imageProperty : imageProperties) {
                System.out.println(imageProperty.getName() + " : " + imageProperty.getValue());
                if (imageProperty.getName().equals("NisoImageMetadata")) {
                    NisoImageMetadata nisoImageProperties = (NisoImageMetadata) imageProperty.getValue();
                    System.out.println(
                            "NisoImageMetadata.MimeType : " + nisoImageProperties.getMimeType());
                }
            }
        }
    }
}
