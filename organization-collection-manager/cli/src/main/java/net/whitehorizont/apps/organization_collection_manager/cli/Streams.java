package net.whitehorizont.apps.organization_collection_manager.cli;

import java.io.InputStream;
import java.io.PrintStream;

public class Streams {
  public final InputStream in;
  public final PrintStream out;
  public final PrintStream err;

  public Streams(InputStream in, PrintStream out, PrintStream err) {
    this.in = in;
    this.out = out;
    this.err = err;
  }

}
