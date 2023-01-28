import java.io.IOException;
import java.nio.CharBuffer;

/**
 * Corrupted Readable implementation that always throws an IOException.
 */
public class CorruptedReadable implements Readable {
  /**
   * Always throws an IOException.
   *
   * @throws IOException method was called
   */
  @Override
  public int read(CharBuffer cb) throws IOException {
    throw new IOException("IOException thrown");
  }
}
