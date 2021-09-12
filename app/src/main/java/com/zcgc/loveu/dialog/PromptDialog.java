package com.zcgc.loveu.dialog;


import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.zcgc.loveu.R;

public class PromptDialog extends Dialog {

    public PromptDialog(@NonNull Context context) {
        super(context);
    }

    public PromptDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
    }

    protected PromptDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    public static class Builder {
        private Context context;
        private String title;
        private String content;
        private String confirmStr;
        private String cancelStr;
        private onDialogButtonClickListener onDialogButtonClickListener;
        private TextView mTitleView, mContentView, mSubmitView, mCancelView;

        public Builder(Context context) {
            this.context = context;
        }

        public Builder setTitle(String title) {
            this.title = title;
            return this;
        }

        public Builder setContent(String content) {
            this.content = content;
            return this;
        }

        public Builder setConfirmStr(String confirmStr) {
            this.confirmStr = confirmStr;
            return this;
        }

        public Builder setCancelStr(String cancelStr) {
            this.cancelStr = cancelStr;
            return this;
        }

        public interface onDialogButtonClickListener {
            void onConfirm();

            void onCancel();
        }

        public Builder setOnDialogButtonClickListener(onDialogButtonClickListener onDialogButtonClickListener) {
            this.onDialogButtonClickListener = onDialogButtonClickListener;
            return this;
        }

        public PromptDialog build() {
            View view = LayoutInflater.from(context).inflate(R.layout.dialog_commom, null);
            PromptDialog promptDialog = new PromptDialog(context, R.style.PromptDialog);
            mTitleView = view.findViewById(R.id.title);
            mContentView = view.findViewById(R.id.content);
            mSubmitView = view.findViewById(R.id.submit);
            mCancelView = view.findViewById(R.id.cancel);
            if (title != null) {
                mTitleView.setText(title);
            }
            if (content != null) {
                mContentView.setText(content);
            }
            if (confirmStr != null) {
                mSubmitView.setText(confirmStr);
            }
            if (cancelStr != null) {
                mCancelView.setText(cancelStr);
            }
            mCancelView.setOnClickListener(v -> {
                if (onDialogButtonClickListener != null) {
                    onDialogButtonClickListener.onCancel();
                }
            });
            mSubmitView.setOnClickListener(v -> {
                if (onDialogButtonClickListener != null) {
                    onDialogButtonClickListener.onConfirm();
                }
            });
            promptDialog.setContentView(view);
            return promptDialog;
        }
    }
}
