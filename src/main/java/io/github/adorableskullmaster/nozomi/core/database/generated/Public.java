/*
 * This file is generated by jOOQ.
 */
package io.github.adorableskullmaster.nozomi.core.database.generated;


import io.github.adorableskullmaster.nozomi.core.database.generated.tables.*;
import org.jooq.Catalog;
import org.jooq.Table;
import org.jooq.impl.SchemaImpl;

import javax.annotation.Generated;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


/**
 * This class is generated by jOOQ.
 */
@Generated(
    value = {
        "http://www.jooq.org",
        "jOOQ version:3.11.9"
    },
    comments = "This class is generated by jOOQ"
)
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class Public extends SchemaImpl {

    private static final long serialVersionUID = -453281519;

    /**
     * The reference instance of <code>public</code>
     */
    public static final Public PUBLIC = new Public();

    /**
     * The table <code>public.applicantmodule</code>.
     */
    public final Applicantmodule APPLICANTMODULE = io.github.adorableskullmaster.nozomi.core.database.generated.tables.Applicantmodule.APPLICANTMODULE;

    /**
     * The table <code>public.applicants</code>.
     */
    public final Applicants APPLICANTS = io.github.adorableskullmaster.nozomi.core.database.generated.tables.Applicants.APPLICANTS;

    /**
     * The table <code>public.bankmodule</code>.
     */
    public final Bankmodule BANKMODULE = io.github.adorableskullmaster.nozomi.core.database.generated.tables.Bankmodule.BANKMODULE;

    /**
     * The table <code>public.guildconfig</code>.
     */
    public final Guildconfig GUILDCONFIG = io.github.adorableskullmaster.nozomi.core.database.generated.tables.Guildconfig.GUILDCONFIG;

    /**
     * The table <code>public.textsmodule</code>.
     */
    public final Textsmodule TEXTSMODULE = io.github.adorableskullmaster.nozomi.core.database.generated.tables.Textsmodule.TEXTSMODULE;

    /**
     * The table <code>public.vacmodemodule</code>.
     */
    public final Vacmodemodule VACMODEMODULE = io.github.adorableskullmaster.nozomi.core.database.generated.tables.Vacmodemodule.VACMODEMODULE;

    /**
     * The table <code>public.warmodule</code>.
     */
    public final Warmodule WARMODULE = io.github.adorableskullmaster.nozomi.core.database.generated.tables.Warmodule.WARMODULE;

    /**
     * The table <code>public.wars</code>.
     */
    public final Wars WARS = io.github.adorableskullmaster.nozomi.core.database.generated.tables.Wars.WARS;

    /**
     * No further instances allowed
     */
    private Public() {
        super("public", null);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public Catalog getCatalog() {
        return DefaultCatalog.DEFAULT_CATALOG;
    }

    @Override
    public final List<Table<?>> getTables() {
        List result = new ArrayList();
        result.addAll(getTables0());
        return result;
    }

    private final List<Table<?>> getTables0() {
        return Arrays.<Table<?>>asList(
            Applicantmodule.APPLICANTMODULE,
            Applicants.APPLICANTS,
            Bankmodule.BANKMODULE,
            Guildconfig.GUILDCONFIG,
            Textsmodule.TEXTSMODULE,
            Vacmodemodule.VACMODEMODULE,
            Warmodule.WARMODULE,
            Wars.WARS);
    }
}
