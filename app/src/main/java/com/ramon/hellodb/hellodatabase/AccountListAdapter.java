package com.ramon.hellodb.hellodatabase;

import android.content.Context;
import android.database.DataSetObserver;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;

/**
 * Created by ramon on 2015/1/4.
 */
public class AccountListAdapter extends BaseAdapter {

    private ArrayList<AccountDb.AccountItem> mAccountList = new ArrayList<>();
    private Context mContext;

    AccountListAdapter(Context ctx) {
        mContext = ctx;
    }

    public void setAccountList(ArrayList<AccountDb.AccountItem> accountList) {
        mAccountList = accountList;
    }

    @Override
    public int getCount() {
        return mAccountList.size();
    }

    @Override
    public Object getItem(int position) {
        return mAccountList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return Long.parseLong(mAccountList.get(position).id);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater vi = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = vi.inflate(R.layout.account_item, null);
        }

        TextView textViewDesc = (TextView) convertView.findViewById(R.id.textViewDesc);
        textViewDesc.setText(mAccountList.get(position).desc);

        TextView textViewUserName = (TextView) convertView.findViewById(R.id.textViewUserName);
        textViewUserName.setText(mAccountList.get(position).userName);

        TextView textViewPassword = (TextView) convertView.findViewById(R.id.textViewPassword);
        textViewPassword.setText("??????");

        return convertView;
    }
}
