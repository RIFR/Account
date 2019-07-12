package se.lexicon.account.component.mirror;

import com.google.common.collect.Lists;
import se.lexicon.account.component.entity.AccountBalanceEntity;
import com.so4it.datasource.core.MirroredObjectDefinition;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class AccountMirrorObjectDefinitionFactory {

    private static final List<MirroredObjectDefinition<?>> MIRRORED_OBJECT_DEFINITIONS =
            Collections.unmodifiableList(Lists.<MirroredObjectDefinition<?>>newArrayList(
                    MirroredObjectDefinition.create(AccountBalanceEntity.class).loadDocumentsRouted(true)
            ));

    public Collection<MirroredObjectDefinition<?>> getMirroredObjectDefinitions() {
        return MIRRORED_OBJECT_DEFINITIONS;
    }
}
