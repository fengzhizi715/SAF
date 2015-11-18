/**
 * 
 */
package cn.salesuite.saf.http.rest;

import java.io.Closeable;
import java.io.Flushable;
import java.io.IOException;

/**
 * @author Tony Shen
 *
 */
public abstract class CloseOperation<V> extends Operation<V>{

	private final Closeable closeable;

    private final boolean ignoreCloseExceptions;

    protected CloseOperation(final Closeable closeable,
        final boolean ignoreCloseExceptions) {
      this.closeable = closeable;
      this.ignoreCloseExceptions = ignoreCloseExceptions;
    }

    @Override
    protected void done() throws IOException {
      if (closeable instanceof Flushable)
        ((Flushable) closeable).flush();
      if (ignoreCloseExceptions)
        try {
          closeable.close();
        } catch (IOException e) {
          // Ignored
        }
      else
        closeable.close();
    }
}
