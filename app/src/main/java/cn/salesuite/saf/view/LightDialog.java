/**
 * 
 */
package cn.salesuite.saf.view;

import cn.salesuite.saf.utils.SAFUtils;
import android.app.AlertDialog;
import android.content.Context;

/**
 * Holo Light theme的dialog
 * 用法如下：
 * <pre>
 * <code>
 * LightDialog dialog = LightDialog.create(this,"title","test");
 * dialog.show();
 * </code>
 * </pre>
 * @author Tony Shen
 *
 */
public class LightDialog extends AlertDialog{

	  /**
	   * 创建LightDialog,在android4.0及以上的版本dialog采用Holo Light theme
	   *
	   * @param context
	   * @return light alert dialog
	   */
	  public static LightDialog create(Context context) {
	    LightDialog dialog;
	    if (SAFUtils.isICSOrHigher())
	      dialog = new LightDialog(context, 3);
	    else {
	      dialog = new LightDialog(context);
	      dialog.setInverseBackgroundForced(true);
	    }
	    return dialog;
	  }

	  public static LightDialog create(Context context, String title, String message) {
	    LightDialog dialog = create(context);
	    dialog.setTitle(title);
	    dialog.setMessage(message);
	    return dialog;
	  }

	  public static LightDialog create(Context context, int title, int message) {
	    LightDialog dialog = create(context);
	    dialog.setTitle(title);
	    dialog.setMessage(context.getString(message));
	    return dialog;
	  }

	  public static LightDialog create(Context context, int title, String message) {
	    LightDialog dialog = create(context);
	    dialog.setTitle(title);
	    dialog.setMessage(message);
	    return dialog;
	  }

	  public static LightDialog create(Context context, String title, int message) {
	    return create(context, title, context.getString(message));
	  }

	  protected LightDialog(Context context) {
	    super(context);
	  }

	  /**
	   * @param context
	   * @param theme
	   */
	  protected LightDialog(Context context, int theme) {
	    super(context, theme);
	  }

	  public LightDialog setPositiveButton(int text, OnClickListener listener) {
	    return setPositiveButton(getContext().getString(text), listener);
	  }

	  public LightDialog setPositiveButton(CharSequence text,
	      OnClickListener listener) {
	    setButton(BUTTON_POSITIVE, text, listener);
	    return this;
	  }

	  public LightDialog setPositiveButton(OnClickListener listener) {
	    return setPositiveButton(android.R.string.ok, listener);
	  }

	  public LightDialog setNegativeButton(int text, OnClickListener listener) {
	    return setNegativeButton(getContext().getString(text), listener);
	  }

	  public LightDialog setNegativeButton(CharSequence text,
	      OnClickListener listener) {
	    setButton(BUTTON_NEGATIVE, text, listener);
	    return this;
	  }

	  public LightDialog setNegativeButton(OnClickListener listener) {
	    return setNegativeButton(android.R.string.cancel, listener);
	  }
}
