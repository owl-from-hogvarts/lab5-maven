package net.whitehorizont.apps.organization_collection_manager.core.storage.collection_adapter;

public class ElementXml<Body> {
  final String key;
  final Body body;
  ElementXml(String key, Body body) {
    this.key = key;
    this.body = body;
  }

}