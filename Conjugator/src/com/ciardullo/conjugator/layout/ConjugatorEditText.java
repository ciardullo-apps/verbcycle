package com.ciardullo.conjugator.layout;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.util.AttributeSet;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.ciardullo.conjugator.AppConstants;
import com.ciardullo.conjugator.R;
import com.ciardullo.conjugator.activity.ConjugatorActivity;

public class ConjugatorEditText extends EditText implements AppConstants {

	public static int[] editTextIds = new int[] {
		R.id.editIo,
		R.id.editTu,
		R.id.editLei,
		R.id.editNoi,
		R.id.editVoi,
		R.id.editLoro
	};

	private String conjugated;
	private int editTextId;
	private int passFailImgId;
	private boolean correct;
	
	// Cache bitmaps
	private static Bitmap bitmapSi;
	private static Bitmap bitmapNo;

	private boolean autoAdvance = true;

	public String getConjugated() {
		return conjugated;
	}

	public int getPassFailImgId() {
		return passFailImgId;
	}

	public void setPassFailImgId(int passFailImgId) {
		this.passFailImgId = passFailImgId;
	}

	public void setConjugated(String conjugated) {
		this.conjugated = conjugated;
	}

	public int getEditTextId() {
		return editTextId;
	}

	public void setEditTextId(int editTextId) {
		this.editTextId = editTextId;
	}

	public ConjugatorEditText(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		bitmapSi = BitmapFactory.decodeResource(context.getResources(), R.drawable.si);
		bitmapNo = BitmapFactory.decodeResource(context.getResources(), R.drawable.no);
	}

	public ConjugatorEditText(Context context, AttributeSet attrs) {
		super(context, attrs);
		bitmapSi = BitmapFactory.decodeResource(context.getResources(), R.drawable.si);
		bitmapNo = BitmapFactory.decodeResource(context.getResources(), R.drawable.no);
	}

	public ConjugatorEditText(Context context) {
		super(context);
		bitmapSi = BitmapFactory.decodeResource(context.getResources(), R.drawable.si);
		bitmapNo = BitmapFactory.decodeResource(context.getResources(), R.drawable.no);
	}
	
	public void unhide() {
		autoAdvance = false;
		this.setText(conjugated);
	}

	@Override
	protected void onFocusChanged(boolean focused, int direction,
			Rect previouslyFocusedRect) {
		// Prevent Eclipse error in Resource editor
		if(isInEditMode())
			return;
		if(!focused) {
			// Just lost focus
			String text = getText().toString();
			if(text != null && !"".equals(text)) {
				setCorrect(text.toString().equalsIgnoreCase(conjugated));
				setText(text.toLowerCase());
			}
		}

		super.onFocusChanged(focused, direction, previouslyFocusedRect);
	}

	@Override
	protected void onTextChanged(CharSequence text, int start,
			int lengthBefore, int lengthAfter) {
		// Prevent Eclipse error in Resource editor
		if(isInEditMode())
			return;
		if(text != null && !"".equals(text)) {
			setCorrect(text.toString().equalsIgnoreCase(conjugated));
		}
		if(correct) {
			// This response is correct ...
			if(!gradeTense()) {
				// ...but something else is wrong. Set focus to next wrong EditText
				
			}
		}
		super.onTextChanged(text, start, lengthBefore, lengthAfter);
	}

	/**
	 * Clears the user-entered text and sets the passFail image invisible
	 */
	public void reset() {
		this.setText("");
		correct = false;
		ConjugatorActivity activity = (ConjugatorActivity)this.getContext();
		ImageView img = (ImageView)activity.findViewById(passFailImgId);
		if(img != null) {
			img.setVisibility(INVISIBLE);
		}
		// Disable auto-suggestion
		// TODO Fix problem with auto-suggestion duplicating user-input
		// This setInputType() call disables suggestions
//		setInputType(InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);
	}

	/**
	 * Set passFail attribs
	 * @param b
	 */
	private void setCorrect(boolean b) {
		ConjugatorActivity activity = (ConjugatorActivity)this.getContext();
		ImageView img = (ImageView)activity.findViewById(passFailImgId);
		if(img != null) {
			if(b) {
//				img.setImageResource(R.drawable.si);
				img.setImageBitmap(bitmapSi);
			} else {
//				img.setImageResource(R.drawable.no);
				img.setImageBitmap(bitmapNo);
			}
			img.setVisibility(VISIBLE);
		}
		correct = b;
	}

	/**
	 * Checks correctness of each conjugation for the selected tense
	 * @return
	 */
	private boolean gradeTense() {
		// If all responses are correct, cycle by tense
		boolean b = true;
		if(correct) {
			ConjugatorEditText e = isAllResponsesCorrect();
			if(e == null) {
				// All responses are correct

				if(!autoAdvance) {
					// Don't autoadvance if the user touched the X on the last conjugation
					Toast.makeText(getContext(), R.string.moltoBene, Toast.LENGTH_SHORT).show();
					final Handler handler = new Handler();
					Runnable run = new Runnable() {

						@Override
						public void run() {
							handler.removeCallbacks(this);
							doAutoAdvance();
						}
					};
					handler.postDelayed(run, 2000);
				} else {
					// Cycle by either Verb or Tense, depending on the preference
					Toast.makeText(getContext(), R.string.moltoBene, Toast.LENGTH_SHORT).show();
					doAutoAdvance();
				}
			} else {
				// A response is incorrect, set focus
//				dispatchKeyEvent(new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_DPAD_DOWN));
				// IMPORTANT: selectAll() needed to change focus without
				// duplicating a correct entry that is not in the dictionary
				String s = getText().toString();
				if(s != null && s.indexOf(" ") >= 0) {
					// Two or more words are in this edit text
					// Just choose the last word
					int n = s.indexOf(" ");
					int startPosOfLastWord = 0;
					while(n >= 0) {
						startPosOfLastWord = n;
						n = s.indexOf(" ", n+1);
					}
					setSelection(startPosOfLastWord + 1, s.length());
				} else {
					selectAll();
				}
				
				e.requestFocus();
			}

			// Reset autoAdvance indicator
			autoAdvance = true;
		}

		return b;
	}

	public boolean isCorrect() {
		return correct;
	}

	/**
	 * Iterates each EditText and returns the first that is not correct
	 * @return null if all are correct
	 */
	private ConjugatorEditText isAllResponsesCorrect() {
		ConjugatorActivity activity = (ConjugatorActivity)this.getContext();
		ConjugatorEditText e = null;
		for(int n : editTextIds) {
			e = (ConjugatorEditText)activity.findViewById(n);
			if(!e.isCorrect()) {
				break;
			} else {
				e = null;
			}
		}
		return e;
	}
	
	private void doAutoAdvance() {
		ConjugatorActivity context = (ConjugatorActivity)this.getContext();
		SharedPreferences prefs = PreferenceManager
				.getDefaultSharedPreferences(context);
		String cycleBy = prefs.getString(PREF_CYCLE_BY,
				PREF_CYCLE_BY_TENSE);

		Spinner spinner = null;
		if(PREF_CYCLE_BY_VERB.equals(cycleBy)) {
			spinner = (Spinner)context.findViewById(R.id.listVerb);
		} else {
			spinner = (Spinner)context.findViewById(R.id.listTense);
		}
		int n = spinner.getSelectedItemPosition();
		int maxTenses = spinner.getCount();
		if(n < maxTenses - 1)
			spinner.setSelection(n+1);
		else
			spinner.setSelection(0);
	}
}
