package net.whitehorizont.apps.organization_collection_manager.core.collection;

import net.whitehorizont.apps.organization_collection_manager.lib.ICanValidate;
import net.whitehorizont.apps.organization_collection_manager.lib.IDisplayable;

public interface IElementInfoProvider<Host, T> extends ICanValidate<Host, T>, IDisplayable{}
