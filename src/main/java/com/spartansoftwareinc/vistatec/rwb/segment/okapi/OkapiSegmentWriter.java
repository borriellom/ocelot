package com.spartansoftwareinc.vistatec.rwb.segment.okapi;

import com.spartansoftwareinc.vistatec.rwb.its.Provenance;
import com.spartansoftwareinc.vistatec.rwb.segment.Segment;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.util.List;
import java.util.Properties;
import net.sf.okapi.common.Event;
import net.sf.okapi.common.LocaleId;
import net.sf.okapi.common.annotation.GenericAnnotation;
import net.sf.okapi.common.annotation.GenericAnnotationType;
import net.sf.okapi.common.annotation.ITSProvenanceAnnotations;
import net.sf.okapi.common.encoder.EncoderManager;
import net.sf.okapi.common.filters.IFilter;
import net.sf.okapi.common.resource.DocumentPart;
import net.sf.okapi.common.resource.EndSubfilter;
import net.sf.okapi.common.resource.Ending;
import net.sf.okapi.common.resource.ITextUnit;
import net.sf.okapi.common.resource.StartDocument;
import net.sf.okapi.common.resource.StartGroup;
import net.sf.okapi.common.resource.StartSubDocument;
import net.sf.okapi.common.resource.StartSubfilter;
import net.sf.okapi.common.skeleton.ISkeletonWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Superclass for using Okapi's ISkeletonWriter interface to write out the
 * workbench segments as different file formats.
 */
public abstract class OkapiSegmentWriter {
    private Logger LOG = LoggerFactory.getLogger(OkapiSegmentWriter.class);

    public abstract void updateEvent(Segment seg);

    public ITSProvenanceAnnotations addRWProvenance(Segment seg) {
        Properties p = new Properties();
        File rwDir = new File(System.getProperty("user.home"), ".reviewersWorkbench");
        File provFile = new File(rwDir, "provenance.properties");
        if (provFile.exists()) {
            try {
                p.load(new FileInputStream(provFile));
            } catch (IOException ex) {
                LOG.warn("Problems with loading provenance properties file", ex);
            }
        }

        ITSProvenanceAnnotations provAnns = new ITSProvenanceAnnotations();
        for (Provenance prov : seg.getProv()) {
            String revPerson = prov.getRevPerson();
            String revOrg = prov.getRevOrg();
            String provRef = prov.getProvRef();
            GenericAnnotation ga = new GenericAnnotation(GenericAnnotationType.PROV,
                    GenericAnnotationType.PROV_PERSON, prov.getPerson(),
                    GenericAnnotationType.PROV_ORG, prov.getOrg(),
                    GenericAnnotationType.PROV_TOOL, prov.getTool(),
                    GenericAnnotationType.PROV_REVPERSON, revPerson,
                    GenericAnnotationType.PROV_REVORG, revOrg,
                    GenericAnnotationType.PROV_REVTOOL, prov.getRevTool(),
                    GenericAnnotationType.PROV_PROVREF, provRef);
            provAnns.add(ga);

            // Check for existing RW annotation.
            if (p.getProperty("revPerson").equals(prov.getRevPerson())
                    && p.getProperty("revOrganization").equals(prov.getRevOrg())
                    && p.getProperty("externalReference").equals(prov.getProvRef())) {
                seg.setAddedRWProvenance(true);
            }
        }

        if (!seg.addedRWProvenance()) {
            GenericAnnotation provGA = new GenericAnnotation(GenericAnnotationType.PROV,
                    GenericAnnotationType.PROV_REVPERSON, p.getProperty("revPerson"),
                    GenericAnnotationType.PROV_REVORG, p.getProperty("revOrganization"),
                    GenericAnnotationType.PROV_PROVREF, p.getProperty("externalReference"));
            provAnns.add(provGA);
            seg.addProvenance(new Provenance(provGA));
            seg.setAddedRWProvenance(true);
        }

        return provAnns;
    }

    public void saveEvents(IFilter filter, List<Event> events, String output, LocaleId locId) throws UnsupportedEncodingException, FileNotFoundException, IOException {
        StringBuilder tmp = new StringBuilder();
        ISkeletonWriter skelWriter = filter.createSkeletonWriter();
        EncoderManager encoderManager = filter.getEncoderManager();
        for (Event event : events) {
            switch (event.getEventType()) {
                case START_DOCUMENT:
                    tmp.append(skelWriter.processStartDocument(locId, "UTF-8", null, encoderManager,
                            (StartDocument) event.getResource()));
                    break;
                case END_DOCUMENT:
                    tmp.append(skelWriter.processEndDocument((Ending) event.getResource()));
                    break;
                case START_SUBDOCUMENT:
                    tmp.append(skelWriter.processStartSubDocument((StartSubDocument) event
                            .getResource()));
                    break;
                case END_SUBDOCUMENT:
                    tmp.append(skelWriter.processEndSubDocument((Ending) event.getResource()));
                    break;
                case TEXT_UNIT:
                    ITextUnit tu = event.getTextUnit();
                    tmp.append(skelWriter.processTextUnit(tu));
                    break;
                case DOCUMENT_PART:
                    DocumentPart dp = (DocumentPart) event.getResource();
                    tmp.append(skelWriter.processDocumentPart(dp));
                    break;
                case START_GROUP:
                    StartGroup startGroup = (StartGroup) event.getResource();
                    tmp.append(skelWriter.processStartGroup(startGroup));
                    break;
                case END_GROUP:
                    tmp.append(skelWriter.processEndGroup((Ending) event.getResource()));
                    break;
                case START_SUBFILTER:
                    StartSubfilter startSubfilter = (StartSubfilter) event.getResource();
                    tmp.append(skelWriter.processStartSubfilter(startSubfilter));
                    break;
                case END_SUBFILTER:
                    tmp.append(skelWriter.processEndSubfilter((EndSubfilter) event.getResource()));
                    break;
            }
        }
        skelWriter.close();
        Writer outputFile = new BufferedWriter(
                new OutputStreamWriter(new FileOutputStream(output), "UTF-8"));
        outputFile.write(tmp.toString());
        outputFile.flush();
        outputFile.close();
    }
}