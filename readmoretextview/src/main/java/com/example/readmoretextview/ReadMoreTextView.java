package com.example.readmoretextview;

import android.support.annotation.Nullable;
import android.text.Html;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * ------------------------------------------
 * Created by Ishank on 20-06-2018.
 * ------------------------------------------
 */
public class ReadMoreTextView {
    private String TAG = "ReadMoreTextView";
    private String expandText = "Read More";
    private String collapseText = "Read Less";
    private String colorCode = "";
    private int collapseSize = 3;
    private int maximumLine = 3;


    private TextView textView;
    private ImageView imageView;

    public ReadMoreTextView() {

    }

    public ReadMoreTextView(TextView tv, ImageView iv, int maxLine, String expandText, String collapseText, String readMoreColorCode, boolean isReaded) {
        this.expandText = expandText;
        this.collapseText = collapseText;
        this.collapseSize = maxLine;
        this.colorCode = readMoreColorCode;
        makeTextViewResizable(tv, iv, maxLine, expandText, true);
    }


    public String getExpandText() {
        return expandText;
    }

    public void setExpandText(String expandText) {
        this.expandText = expandText;
    }

    public String getCollapseText() {
        return collapseText;
    }

    public void setCollapseText(String collapseText) {
        this.collapseText = collapseText;
    }

    public String getColorCode() {
        return colorCode;
    }

    public void setColorCode(String colorCode) {
        this.colorCode = colorCode;
    }

    public int getMaximumLine() {
        return maximumLine;
    }

    public void setMaximumLine(int maximumLine) {
        this.maximumLine = maximumLine;
        this.collapseSize = maximumLine;
    }

    public TextView getTextView() {
        return textView;
    }

    public void setTextView(TextView textView) {
        this.textView = textView;
    }

    public ImageView getImageView() {
        return imageView;
    }

    public void setImageView(ImageView imageView) {
        this.imageView = imageView;
    }

    public void setReadMore() {
        if (textView == null) {
            Log.e(TAG, "Textview must not be null");
            return;
        }
        makeTextViewResizable(textView, imageView,maximumLine, expandText, true);
    }

    public void makeTextViewResizable(final TextView tv, @Nullable final ImageView iv, final int maxLine, final String expandText, final boolean readMore) {

        if (tv.getTag() == null) {
            tv.setTag(tv.getText());
        }
        ViewTreeObserver treeObserver = tv.getViewTreeObserver();
        treeObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {

            @SuppressWarnings("deprecation")
            @Override
            public void onGlobalLayout() {

                ViewTreeObserver obs = tv.getViewTreeObserver();
                obs.removeGlobalOnLayoutListener(this);
                if (maxLine == 0) {
                    int lineEndIndex = tv.getLayout().getLineEnd(0);
                    String text = tv.getText().subSequence(0, lineEndIndex - expandText.length() + 1) + " " + expandText;
                    tv.setText(text);
                    tv.setMovementMethod(LinkMovementMethod.getInstance());
                    tv.setText(
                            addClickablePartTextViewResizable(Html.fromHtml(tv.getText().toString().replace("\n", "<br>")),  tv,iv, maxLine, expandText,
                                    readMore), TextView.BufferType.SPANNABLE);
                } else if (maxLine > 0 && tv.getLineCount() >= maxLine) {
                    int lineEndIndex = tv.getLayout().getLineEnd(maxLine - 1);
                    String text = tv.getText().subSequence(0, lineEndIndex - expandText.length() + 1) + " " + expandText;
                    tv.setText(text);
                    tv.setMovementMethod(LinkMovementMethod.getInstance());
                    tv.setText(
                            addClickablePartTextViewResizable(Html.fromHtml(tv.getText().toString().replace("\n", "<br>")), tv,iv, maxLine, expandText,
                                    readMore), TextView.BufferType.SPANNABLE);
                } else {
                    int lineEndIndex = tv.getLayout().getLineEnd(tv.getLayout().getLineCount() - 1);
                    String text = tv.getText().subSequence(0, lineEndIndex) + " " + expandText;
                    tv.setText(text);
                    tv.setMovementMethod(LinkMovementMethod.getInstance());
                    tv.setText(
                            addClickablePartTextViewResizable(Html.fromHtml(tv.getText().toString().replace("\n", "<br>")), tv,iv, lineEndIndex, expandText,
                                    readMore), TextView.BufferType.SPANNABLE);
                }
            }
        });

    }

    private SpannableStringBuilder addClickablePartTextViewResizable(final Spanned strSpanned, final TextView tv, final ImageView iv,
                                                                     final int maxLine, final String spanableText, final boolean viewMore) {
        String str = strSpanned.toString();
        SpannableStringBuilder ssb = new SpannableStringBuilder(strSpanned);

        if (str.contains(spanableText)) {


            ssb.setSpan(new ReadMoreSpan(true, colorCode) {
                @Override
                public void onClick(View widget) {
                    if (viewMore) {
                        tv.setLayoutParams(tv.getLayoutParams());
                        tv.setText(tv.getTag().toString(), TextView.BufferType.SPANNABLE);
                        iv.setImageResource(R.drawable.oval_green);
                        tv.invalidate();
                        iv.invalidate();
                        makeTextViewResizable(tv,iv, -1, collapseText, false);
                    } else {
                        tv.setLayoutParams(tv.getLayoutParams());
                        iv.setLayoutParams(tv.getLayoutParams());
                        tv.setText(tv.getTag().toString(), TextView.BufferType.SPANNABLE);
                        iv.setImageResource(R.drawable.oval_timeline_grey);
                        tv.invalidate();
                        iv.invalidate();
                        // makeTextViewResizable(tv, 3,expandText, true);
                        makeTextViewResizable(tv,iv, collapseSize, expandText, true);
                    }
                }
            }, str.indexOf(spanableText), str.indexOf(spanableText) + spanableText.length(), 0);

        }
        return ssb;

    }
}
