/**
 * 
 */
package cn.salesuite.saf.inject;

/**
 * 注解的异常类
 * @author Tony Shen
 *
 */
public class InjectException extends RuntimeException{

	private static final long serialVersionUID = -5298989560573894243L;

    public InjectException(String message) {
        super(message);
    }

    public InjectException(Throwable throwable) {
        super(throwable);
    }

    public InjectException(String message, Throwable throwable) {
        super(message, throwable);
    }
}
