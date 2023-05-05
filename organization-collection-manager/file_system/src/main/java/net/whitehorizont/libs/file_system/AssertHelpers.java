package net.whitehorizont.libs.file_system;


// package private
public class AssertHelpers {
  private static final String DEVELOPER_GITHUB_USERNAME = "owl-from-hogvarts";
  private static final String GITHUB_DOMAIN_NAME = "github.com";
  
  public static String getAssertMessageFor(String fileName, String method) {
    return "This should never happen (" + fileName + ":" + method + ")\n" 
    + "This is error in program!\n" 
    + "Please, reach out developer ("
    + getDeveloperContactInformation() + ")";
  }

  private static String getDeveloperContactInformation() {
    return GITHUB_DOMAIN_NAME + "/" + DEVELOPER_GITHUB_USERNAME;
  }
}
