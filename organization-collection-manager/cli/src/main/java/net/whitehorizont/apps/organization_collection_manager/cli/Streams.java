package net.whitehorizont.apps.organization_collection_manager.cli;

import java.io.InputStream;
import java.io.OutputStream;

public class Streams {
  public final InputStream in;
  public final OutputStream out;
  public final OutputStream err;

  public Streams(InputStream in, OutputStream out, OutputStream err) {
    this.in = in;
    this.out = out;
    this.err = err;
  }

}
