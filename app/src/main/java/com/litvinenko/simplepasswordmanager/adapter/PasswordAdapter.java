package com.litvinenko.simplepasswordmanager.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.method.PasswordTransformationMethod;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.litvinenko.simplepasswordmanager.model.Password;
import com.litvinenko.simplepasswordmanager.R;

import java.util.List;

public class PasswordAdapter extends RecyclerView.Adapter<PasswordAdapter.ViewHolder> {

    private List<Password> mPasswords;
    private LayoutInflater mInflater;
    private IOnPasswordActionsListener mListener;

    // Interface implemented in MainActivity
    public interface IOnPasswordActionsListener {
        void onCopyToClipboardClicked(String password);

        void onPasswordFinishedEditing(int adapterPosition, String password);

        void onDeleteClicked(int adapterPosition);
    }

    // Provide a reference to the views for each data item
    static class ViewHolder extends RecyclerView.ViewHolder {

        TextView mLogin;
        EditText mPassword;
        ImageButton mShow;
        ImageButton mCopy;
        ImageButton mDelete;

        ViewHolder(View view) {
            super(view);
            mLogin = view.findViewById(R.id.tvTitle);
            mPassword = view.findViewById(R.id.etPassword);
            mShow = view.findViewById(R.id.ibShow);
            mCopy = view.findViewById(R.id.ibCopy);
            mDelete = view.findViewById(R.id.ibDelete);
        }
    }

    public PasswordAdapter(Context mContext, List<Password> mPasswords, IOnPasswordActionsListener mListener) {
        this.mInflater = LayoutInflater.from(mContext);
        this.mPasswords = mPasswords;
        this.mListener = mListener;
    }

    @NonNull
    @Override
    public PasswordAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent,
                                                         int viewType) {
        // Create a new view
        View view = mInflater.inflate(R.layout.rv_item_password, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        holder.mLogin.setText(mPasswords.get(position).getLogin());
        holder.mPassword.setText(mPasswords.get(position).getPassword());
        holder.mPassword.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView view, int actionId, KeyEvent event) {
                // Track User actions - when finished editing password pass data to MainActivity to save updates
                if (actionId == EditorInfo.IME_ACTION_DONE || actionId == EditorInfo.IME_ACTION_NEXT) {
                    mListener.onPasswordFinishedEditing(holder.getAdapterPosition(),
                            String.valueOf(view.getText()));
                    return true;
                }
                return false;
            }
        });

        holder.mShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Show password or hide it
                if (holder.mPassword.getTransformationMethod() == null)
                    holder.mPassword.setTransformationMethod(new PasswordTransformationMethod());
                else holder.mPassword.setTransformationMethod(null);
            }
        });
        holder.mCopy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onCopyToClipboardClicked(String.valueOf(holder.mPassword.getText()));
            }
        });
        holder.mDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onDeleteClicked(holder.getAdapterPosition());
            }
        });
    }

    @Override
    public int getItemCount() {
        return mPasswords.size();
    }
}
