/**
 * 
 */
package cn.salesuite.saf.http.rest;

import java.io.IOException;
import java.util.concurrent.Callable;

/**
 * @author Tony Shen
 *
 */
public abstract class Operation<V> implements Callable<V> {

	/**
     * 回调运行的操作
     *
     * @return result
     * @throws RestException
     * @throws IOException
     */
    protected abstract V run() throws RestException, IOException;

    /**
     * 回调完成后的操作，主要是关闭一些流
     *
     * @throws IOException
     */
    protected abstract void done() throws IOException;

    public V call() throws RestException {
      boolean thrown = false;
      try {
        return run();
      } catch (RestException e) {
        thrown = true;
        throw e;
      } catch (IOException e) {
        thrown = true;
        throw new RestException(e);
      } finally {
        try {
          done();
        } catch (IOException e) {
          if (!thrown)
            throw new RestException(e);
        }
      }
    }
}
