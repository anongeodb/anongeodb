package it.unibz.krdb.obda.owlrefplatform.owlapi3;


import it.unibz.krdb.obda.model.OBDAModel;
import it.unibz.krdb.obda.owlrefplatform.core.QuestPreferences;
import it.unibz.krdb.obda.owlrefplatform.core.mappingprocessing.TMappingExclusionConfig;
import it.unibz.krdb.sql.ImplicitDBConstraintsReader;
import org.semanticweb.owlapi.reasoner.NullReasonerProgressMonitor;
import org.semanticweb.owlapi.reasoner.ReasonerProgressMonitor;
import org.semanticweb.owlapi.reasoner.SimpleConfiguration;


@SuppressWarnings("serial")
public class QuestOWLConfiguration extends SimpleConfiguration {

    private final TMappingExclusionConfig excludeFromTMappings;

    private final ImplicitDBConstraintsReader userConstraints;

    private final boolean queryingAnnotationsInOntology;

    private final boolean sameAsInMappings;

    private final OBDAModel obdaModel;

    private final QuestPreferences preferences;

    public TMappingExclusionConfig getExcludeFromTMappings() {
        return excludeFromTMappings;
    }

    public ImplicitDBConstraintsReader getUserConstraints() {
        return userConstraints;
    }

    public boolean isQueryingAnnotationsInOntology() {
        return queryingAnnotationsInOntology;
    }

    public boolean isSameAsInMappings() {
        return sameAsInMappings;
    }

    public OBDAModel getObdaModel() {
        return obdaModel;
    }

    public QuestPreferences getPreferences() {
        return preferences;
    }

    private QuestOWLConfiguration(Builder builder) {
        super(builder.progressMonitor);

        if (builder.excludeFromTMappings == null)
            excludeFromTMappings = TMappingExclusionConfig.empty();
        else
            excludeFromTMappings = builder.excludeFromTMappings;

        if (builder.preferences == null)
            preferences = new QuestPreferences();
        else
            preferences = builder.preferences;

        userConstraints = builder.userConstraints;
        obdaModel = builder.obdaModel;
        queryingAnnotationsInOntology = builder.queryingAnnotationsInOntology;
        sameAsInMappings = builder.sameAsMappings;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {
        private TMappingExclusionConfig excludeFromTMappings;
        private ImplicitDBConstraintsReader userConstraints;
        private OBDAModel obdaModel;
        private boolean queryingAnnotationsInOntology = false;
        private boolean sameAsMappings = false;

        private QuestPreferences preferences;
        private ReasonerProgressMonitor progressMonitor = new NullReasonerProgressMonitor();

        private Builder() {
        }

        public Builder tMappingExclusionConfig(TMappingExclusionConfig val) {
            excludeFromTMappings = val;
            return this;
        }

        public Builder dbConstraintsReader(ImplicitDBConstraintsReader val) {
            userConstraints = val;
            return this;
        }

        public Builder obdaModel(OBDAModel obdaModel) {
            this.obdaModel = obdaModel;
            return this;
        }

        public Builder queryingAnnotationsInOntology(boolean queryingAnnotationsInOntology) {
            this.queryingAnnotationsInOntology = queryingAnnotationsInOntology;
            return this;
        }

        public Builder sameAsMappings(boolean sameAsMappings) {
            this.sameAsMappings = sameAsMappings;
            return this;
        }

        public Builder preferences(QuestPreferences preferences) {
            this.preferences = preferences;
            return this;
        }

        public Builder progressMonitor(ReasonerProgressMonitor progressMonitor){
            this.progressMonitor = progressMonitor;
            return this;
        }

        public QuestOWLConfiguration build() {
            return new QuestOWLConfiguration(this);
        }
    }
}
