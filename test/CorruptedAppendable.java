import java.io.IOException;

/**
 * Corrupted Appendable implementation that always throws an IOException.
 */
public class CorruptedAppendable implements Appendable {
  /**
   * Always throws an IOException.
   *
   * @throws IOException method was called
   */
  @Override
  public Appendable append(CharSequence csq) throws IOException {
    throw new IOException("IOException thrown");
  }

  /**
   * Always throws an IOException.
   *
   * @throws IOException method was called
   */
  @Override
  public Appendable append(CharSequence csq, int start, int end) throws IOException {
    throw new IOException("IOException thrown");
  }

  /**
   * Always throws an IOException.
   *
   * @throws IOException method was called
   */
  @Override
  public Appendable append(char c) throws IOException {
    throw new IOException("IOException thrown");
  }
}

