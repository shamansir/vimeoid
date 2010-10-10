package net.londatiga.android;

import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Action item, displayed as menu with icon and text.
 * 
 * @author Lorensius. W. L. T
 *
 */
public class QActionItem {
    
    public interface QActionClickListener {
        public void onClick(View v, QActionItem item);
    }
    
	private Drawable icon;
	private String title;
	private QActionClickListener listener;
	private View view;
	
	/**
	 * Constructor
	 */
	public QActionItem() {}
	
	/**
	 * Constructor
	 * 
	 * @param icon {@link Drawable} action icon
	 */
	public QActionItem(Drawable icon) {
		this.icon = icon;
	}
	
	/**
	 * Set action title
	 * 
	 * @param title action title
	 */
	public void setTitle(String title) {
		this.title = title;
	}
	
	/**
	 * Get action title
	 * 
	 * @return action title
	 */
	public String getTitle() {
		return this.title;
	}
	
	/**
	 * Set action icon
	 * 
	 * @param icon {@link Drawable} action icon
	 */
	public void setIcon(Drawable icon) {
		this.icon = icon;
	}
	
	/**
	 * Get action icon
	 * @return  {@link Drawable} action icon
	 */
	public Drawable getIcon() {
		return this.icon;
	}
	
	/**
	 * Set on click listener
	 * 
	 * @param listener on click listener {@link View.OnClickListener}
	 */
	public void setOnClickListener(QActionClickListener listener) {
		this.listener = listener;
	}
	
	/**
	 * Get on click listener
	 * 
	 * @return on click listener {@link View.OnClickListener}
	 */
	public QActionClickListener getListener() {
		return this.listener;
	}

    protected void setView(View view) {
        this.view = view;
    }
    
    public View getView() {
        return this.view;
    }
    
    public void invalidate() {
        inject(view);
        view.invalidate();
    }
    
    protected void inject(View parent) {
        
        ImageView img           = (ImageView) parent.findViewById(R.id.icon);
        TextView text           = (TextView) parent.findViewById(R.id.title);
        
        if (icon != null) {
            img.setImageDrawable(icon);
        }
        
        if (title != null) {            
            text.setText(title);
        }
        
        if (listener != null) {
            parent.setOnClickListener(new OnClickListener() {                
                @Override public void onClick(View v) {
                    listener.onClick(v, QActionItem.this);
                }
            });
        }
        
    }
	
}