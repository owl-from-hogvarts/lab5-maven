package net.whitehorizont.apps.organization_collection_manager.lib;


import net.whitehorizont.apps.organization_collection_manager.lib.FieldMetadataExtended.Tag;

public interface ICanFill<Host, WritableHost extends Host> {
  /** 
   * Updates <i>all</i> fields of {@code base} object with values from other object 
   */
  void fill(WritableHost base, Host other);
  /**
   * Updates only fields with specified tag
   */
  void fill(WritableHost base, Host other, Tag tag);
}
