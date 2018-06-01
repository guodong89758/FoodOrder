package android.support.v7.widget.helper;

/**
 * @author guodong
 * @datetime 2017/6/20 15:02.
 */

public class CustomItemTouchHelper extends ItemTouchHelper {
    /**
     * Creates an ItemTouchHelper that will work with the given Callback.
     * <p>
     * You can attach ItemTouchHelper to a RecyclerView via
     * {@link #attachToRecyclerView(RecyclerView)}. Upon attaching, it will add an item decoration,
     * an onItemTouchListener and a Child attach / detach listener to the RecyclerView.
     *
     * @param callback The Callback which controls the behavior of this touch helper.
     */
    public CustomItemTouchHelper(Callback callback) {
        super(callback);
    }

    public Callback getCallback() {
        return mCallback;
    }

    public int getActionState() {
        return mActionState;
    }

}
