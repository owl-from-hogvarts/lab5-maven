package net.whitehorizont.apps.organization_collection_manager.core.storage.collection_adapter;

import net.whitehorizont.apps.organization_collection_manager.lib.IDisplayable;

interface IntegrityAlgorithm extends IDisplayable {
  byte[] apply(byte[] data);
}
