package dev.colbster937.sdsh;

import java.io.FileNotFoundException;
import java.lang.management.ManagementFactory;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public final class StartDotSH {
  private static final Path EXEC = Paths.get(
    "start." + (
      !System.getProperty("os.name").toLowerCase().contains("win")
        ? "sh"
        : "bat"
    )
  ).toAbsolutePath().normalize();

  public static void main(String[] args) throws Exception {
    if (Files.exists(EXEC)) {
      EXEC.toFile().setExecutable(true);

      final ProcessBuilder pb = new ProcessBuilder(EXEC.toString());

      pb.directory(Paths.get("").toAbsolutePath().toFile());
      pb.inheritIO();

      pb.environment().put(
        "SDSH_JVM_ARGS",
        String.join(" ", ManagementFactory.getRuntimeMXBean().getInputArguments())
      );

      pb.environment().put(
        "SDSH_PROGRAM_ARGS",
        String.join(" ", args)
      );

      System.exit(pb.start().waitFor());
    } else {
      throw new FileNotFoundException(EXEC.toString());
    }
  }
}
