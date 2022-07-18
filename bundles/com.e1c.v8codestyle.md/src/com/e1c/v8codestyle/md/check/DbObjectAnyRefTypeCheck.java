/*******************************************************************************
 * Copyright (C) 2022, 1C-Soft LLC and others.
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     1C-Soft LLC - initial API and implementation
 *******************************************************************************/
package com.e1c.v8codestyle.md.check;

import static com._1c.g5.v8.dt.mcore.McorePackage.Literals.TYPE_DESCRIPTION;
import static com._1c.g5.v8.dt.mcore.McorePackage.Literals.TYPE_DESCRIPTION__TYPES;
import static com._1c.g5.v8.dt.metadata.mdclass.MdClassPackage.Literals.BASIC_DB_OBJECT;

import java.util.List;
import java.util.Objects;
import java.util.Set;

import org.eclipse.core.runtime.IProgressMonitor;

import com._1c.g5.v8.dt.mcore.TypeDescription;
import com._1c.g5.v8.dt.mcore.TypeItem;
import com._1c.g5.v8.dt.mcore.util.McoreUtil;
import com._1c.g5.v8.dt.platform.IEObjectTypeNames;
import com.e1c.g5.v8.dt.check.CheckComplexity;
import com.e1c.g5.v8.dt.check.ICheckParameters;
import com.e1c.g5.v8.dt.check.components.BasicCheck;
import com.e1c.g5.v8.dt.check.settings.IssueSeverity;
import com.e1c.g5.v8.dt.check.settings.IssueType;
import com.e1c.v8codestyle.check.StandardCheckExtension;
import com.e1c.v8codestyle.internal.md.CorePlugin;

/**
 * The check that the db attribute has any ref type.
 *
 * @author Artem Iliukhin
 *
 */
public final class DbObjectAnyRefTypeCheck
    extends BasicCheck
{

    private static final String CHECK_ID = "db-object-anyref-type"; //$NON-NLS-1$

   //@formatter:off
    private static final Set<String> REF_TYPES = Set.of(
        IEObjectTypeNames.EXCHANGE_PLAN_REF,
        IEObjectTypeNames.DOCUMENT_REF,
        IEObjectTypeNames.BUSINESS_PROCESS_REF,
        IEObjectTypeNames.BUSINESS_PROCESS_ROUTE_POINT_REF,
        IEObjectTypeNames.CHART_OF_CALCULATION_TYPES_REF,
        IEObjectTypeNames.TASK_REF,
        IEObjectTypeNames.CHART_OF_CHARACTERISTIC_TYPES_REF,
        IEObjectTypeNames.CHART_OF_ACCOUNTS_REF,
        IEObjectTypeNames.CATALOG_REF,
        IEObjectTypeNames.ANY_REF);
  //@formatter:on

    @Override
    public String getCheckId()
    {
        return CHECK_ID;
    }

    @Override
    protected void configureCheck(CheckConfigurer builder)
    {
        builder.title(Messages.DbObjectAnyRefCheck_Title)
            .description(Messages.DbObjectAnyRefCheck_Description)
            .complexity(CheckComplexity.NORMAL)
            .severity(IssueSeverity.MAJOR)
            .issueType(IssueType.PERFORMANCE)
            .extension(new StandardCheckExtension(getCheckId(), CorePlugin.PLUGIN_ID))
            .topObject(BASIC_DB_OBJECT)
            .containment(TYPE_DESCRIPTION)
            .features(TYPE_DESCRIPTION__TYPES);
    }

    @Override
    protected void check(Object object, ResultAcceptor resultAceptor, ICheckParameters parameters,
        IProgressMonitor monitor)
    {
        List<TypeItem> types = ((TypeDescription)object).getTypes();
        for (TypeItem typeItem : types)
        {
            String typeItemName = McoreUtil.getTypeName(typeItem);
            if (!Objects.isNull(typeItemName) && REF_TYPES.contains(typeItemName))
            {
                resultAceptor.addIssue(Messages.DbObjectAnyRefCheck_AnyRef, TYPE_DESCRIPTION__TYPES,
                    types.indexOf(typeItem));
                return;
            }
        }
    }
}
