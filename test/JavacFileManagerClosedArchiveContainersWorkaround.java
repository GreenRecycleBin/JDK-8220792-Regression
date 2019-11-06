package test;

import jdk.javadoc.doclet.DocletEnvironment;
import jdk.javadoc.doclet.StandardDoclet;

import javax.tools.ToolProvider;
import java.io.File;
import java.io.IOException;
import java.nio.file.ClosedFileSystemException;
import java.util.List;

class JavacFileManagerClosedArchiveContainersWorkaround {
  static DocletEnvironment docletEnvironment;

  public static void main(String[] args) throws IOException {
    var documentationTool = ToolProvider.getSystemDocumentationTool();

    try (var fileManager = documentationTool.getStandardFileManager(null, null, null)) {
      var sources =
          fileManager.getJavaFileObjectsFromFiles(
              List.of(new File("test/JavacFileManagerClosedArchiveContainers.java")));

      List<String> options =
          List.of(
              "--show-members",
              "private",
              "--show-types",
              "private",
              "--show-packages",
              "all",
              "--show-module-contents",
              "all",
              "-quiet");

      var task =
          documentationTool.getTask(null, fileManager, null, DocletImpl.class, options, sources);

      if (!task.call()) {
        throw new RuntimeException(" this is unexpected.");
      }

      var elementUtils = docletEnvironment.getElementUtils();

      try {
        var typeElement = elementUtils.getTypeElement("test.Test");

        System.out.println(" exits normally.");
      } catch (ClosedFileSystemException e) {
        System.out.println(" this is unexpected.");

        e.printStackTrace();
      }
    }
  }

  public static class DocletImpl extends StandardDoclet {
    @Override
    public boolean run(DocletEnvironment environment) {
      docletEnvironment = environment;

      return true;
    }
  }
}
