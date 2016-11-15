package cn.salesuite.saf.mvp;

/**
 * Created by Tony Shen on 16/3/18.
 */
public interface MVPPresenter<V extends MVPView> {

    /**
     * Set or attach the view to this presenter
     */
    public void attachView(V view);

    /**
     * Will be called if the view has been destroyed. Typically this method will be invoked from
     * <code>Activity.detachView()</code> or <code>Fragment.onDestroyView()</code>
     */
    public void detachView(boolean retainInstance);
}
