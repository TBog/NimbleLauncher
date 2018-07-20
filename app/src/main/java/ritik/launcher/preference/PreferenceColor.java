package ritik.launcher.preference;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.preference.DialogPreference;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.Button;



import ritik.launcher.R;
import ritik.launcher.UiTweaks;

import com.jaredrummler.android.colorpicker.ColorPickerDialogListener;
import com.jaredrummler.android.colorpicker.ColorPickerView;
import com.jaredrummler.android.colorpicker.ColorPickerDialog;
import com.jaredrummler.android.colorpicker.ColorPickerView;



public class PreferenceColor extends DialogPreference implements ColorPickerView.OnColorChangedListener {
	//private ColorPickerPalette palette;
	private ColorPickerView colorPickerView;

	private ColorPickerDialog colorPickerDialog;

	
	private int selectedColor;

	
	
	
	public PreferenceColor(Context context) {
		this(context, null);
	}
	
	public PreferenceColor(Context context, AttributeSet attrs) {
		super(context, attrs);
		
		this.setDialogLayoutResource(R.layout.pref_color);

		
		// Optianlly override default color value with value from preference XML
		this.selectedColor = Color.parseColor(UiTweaks.COLOR_DEFAULT);
		if(attrs != null) {
			String value = attrs.getAttributeValue("http://schemas.android.com/apk/res/android", "defaultValue");
			if(value != null) {
				this.selectedColor = Color.parseColor(value);
			}
		}
	}
	
//	protected void drawPalette() {
//		if(this.palette != null) {
//			this.palette.drawPalette(UiTweaks.COLOR_LIST, this.selectedColor);
//		}
//	}
	


	@Override
	public void onColorChanged(int color) {
		Log.d("Ritik", "onColorSelected: color is "+color);
		if(color != this.selectedColor) {
			if(!this.callChangeListener(color)) {
				return;
			}

			this.selectedColor = color;
			this.persistString(String.format("#%08X", this.selectedColor));

			// Redraw palette to show checkmark on newly selected color before dismissing

			//this.drawPalette();
		}

		// Close the dialog
		this.getDialog().dismiss();

	}
	//	public PreferenceColor(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
//		super(context, attrs, defStyleAttr, defStyleRes);
//	}




	@Override
	protected View onCreateDialogView() {
		// Create layout from bound resource
		Log.d("Ritik", "onCreateDialogView: ");
		final View view = super.onCreateDialogView();
		
		// Configure the color picker
		//this.palette = (ColorPickerPalette) view.findViewById(R.id.colorPicker);
		//this.palette.init(ColorPickerDialog.SIZE_SMALL, 4, this);

		this.colorPickerView=(ColorPickerView)view.findViewById(R.id.colorPicker1);
		this.colorPickerView.setAlphaSliderVisible(true);

		colorPickerView.setMinimumHeight(colorPickerView.getWidth());


		
//		// Reconfigure color picker based on the available space
//		view.getViewTreeObserver().addOnGlobalLayoutListener(new OnGlobalLayoutListener() {
//			private boolean ignoreNextUpdate = false;
//
//			public void onGlobalLayout() {
//				if(this.ignoreNextUpdate) {
//					this.ignoreNextUpdate = false;
//					return;
//				}
//
//				// Calculate number of swatches to display
//				//int swatchSize = PreferenceColor.this.palette.getResources().getDimensionPixelSize(R.dimen.color_swatch_small);
//				//PreferenceColor.this.palette.init(ColorPickerDialog.SIZE_SMALL, (view.getWidth() - (swatchSize * 2 / 3)) / swatchSize, PreferenceColor.this);
//
//
//				// Cause redraw and (by extension) also a layout recalculation
//				this.ignoreNextUpdate = true;
//				//PreferenceColor.this.drawPalette();
//			}
//		});
		
		// Bind click events from the custom color values
		Button button1 = (Button) view.findViewById(R.id.colorTransparentDark);
		button1.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				PreferenceColor.this.onColorChanged(0xAA000000);
			}
		});
		Button button2 = (Button) view.findViewById(R.id.colorTransparentWhite);
		button2.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				PreferenceColor.this.onColorChanged(0xAAFFFFFF);
			}
		});
		Button button3 = (Button) view.findViewById(R.id.colorTransparent);
		button3.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				PreferenceColor.this.onColorChanged(0x00000000);
			}
		});
		Log.d("Ritik", "onCreateDialogView: 2");
		return view;
	}
	
	@Override
	protected void onBindDialogView(View view) {
		Log.d("Ritik", "onBindDialogView: ");
		android.util.Log.i("PreferenceColor", "View Width:  " + view.getWidth() + " | " + view.getMeasuredWidth());
		// Set selected color value based on the actual color value currently used
		// (but fall back to default from XML)
		this.selectedColor = Color.parseColor(
				this.getPersistedString(String.format("#%08X", this.selectedColor))
		);
		
		//this.drawPalette();
		super.onBindDialogView(view);
	}

	@Override
	public void onClick(DialogInterface dialog, int which) {
		if(which == DialogInterface.BUTTON_POSITIVE) {
			// do your stuff to handle positive button
			onColorChanged(colorPickerView.getColor());
		}else if(which == DialogInterface.BUTTON_NEGATIVE){
			// do your stuff to handle negative button
		}
	}
}
