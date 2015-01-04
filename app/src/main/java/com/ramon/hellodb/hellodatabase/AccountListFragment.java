package com.ramon.hellodb.hellodatabase;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;


public class AccountListFragment extends Fragment {

    private AccountDb mDb;
    private AccountListAdapter mAdapter;

    final int MENU_ITEM_ADD = 1;
    final int MENU_ITEM_EDIT = 2;
    final int MENU_ITEM_DEL = 3;

    static Fragment newInstance(AccountDb db) {
        AccountListFragment fragment = new AccountListFragment();
        fragment.setDb(db);
        return fragment;
    }

    private void setDb(AccountDb db) {
        mDb = db;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_account_list, container, false);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View view, ContextMenu.ContextMenuInfo menuInfo) {
        if (view.getId()==R.id.listViewAccount) {
            AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
            AccountDb.AccountItem item = (AccountDb.AccountItem)((ListView)view).getItemAtPosition(info.position);
            menu.setHeaderTitle("Account Option - " + item.desc);
            menu.add(0, MENU_ITEM_ADD, 0, "Add");
            menu.add(0, MENU_ITEM_EDIT, 0, "Edit");
            menu.add(0, MENU_ITEM_DEL, 0, "Delete");
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item){
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)item.getMenuInfo();
        switch(item.getItemId()){
            case MENU_ITEM_ADD:
                onAddAccount();
                break;
            case MENU_ITEM_EDIT:
                onEditAccount(info.position);
                break;
            case MENU_ITEM_DEL:
                onDelAccount(info.position);
                break;
        }
        return true;
    }

    @Override
    public void onStart() {
        super.onStart();

        mAdapter = new AccountListAdapter(getActivity());
        mAdapter.setAccountList(mDb.getAccount());

        final ListView listViewAccount = (ListView) getActivity().findViewById(R.id.listViewAccount);
        listViewAccount.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();
        registerForContextMenu(listViewAccount);

        listViewAccount.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                AccountDb.AccountItem item = (AccountDb.AccountItem)listViewAccount.getItemAtPosition(position);
                AlertDialog.Builder dlg = new AlertDialog.Builder(getActivity());
                View convertView = getActivity().getLayoutInflater().inflate(R.layout.account_dialog, null);
                TextView textViewUserName = (TextView) convertView.findViewById(R.id.textViewUserName);
                textViewUserName.setText(item.userName);
                TextView textViewPassword = (TextView) convertView.findViewById(R.id.textViewPassword);
                textViewPassword.setText(item.password);
                dlg.setView(convertView);
                dlg.setTitle(item.desc);
                dlg.show();
            }
        });

    }

    private void onEditAccount(int position) {
        ListView listViewAccount = (ListView) getActivity().findViewById(R.id.listViewAccount);
        final AccountDb.AccountItem item = (AccountDb.AccountItem)listViewAccount.getItemAtPosition(position);
        AlertDialog.Builder dlg = new AlertDialog.Builder(getActivity());
        final View convertView = getActivity().getLayoutInflater().inflate(R.layout.account_dialog_edit, null);
        final TextView textViewDesc = (TextView) convertView.findViewById(R.id.editTextDesc);
        textViewDesc.setText(item.desc);
        final TextView textViewUserName = (TextView) convertView.findViewById(R.id.editTextUserName);
        textViewUserName.setText(item.userName);
        final TextView textViewPassword = (TextView) convertView.findViewById(R.id.editTextPassword);
        textViewPassword.setText(item.password);
        dlg.setView(convertView);

        dlg.setTitle("Edit Account - " + item.desc);
        dlg.setPositiveButton("Save", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String newDesc = textViewDesc.getText().toString();
                String newUserName = textViewUserName.getText().toString();
                String newPassword = textViewPassword.getText().toString();
                if (!newDesc.isEmpty() && !newUserName.isEmpty() && !newPassword.isEmpty()) {
                    item.desc = newDesc;
                    item.userName = newUserName;
                    item.password = newPassword;
                    saveChangesToDb(item);
                    dialog.dismiss();
                }
            }
        });
        dlg.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        dlg.show();
    }

    private void onDelAccount(int position) {
        ListView listViewAccount = (ListView) getActivity().findViewById(R.id.listViewAccount);
        AccountDb.AccountItem item = (AccountDb.AccountItem)listViewAccount.getItemAtPosition(position);
        mDb.delete(item.id);
        mAdapter.setAccountList(mDb.getAccount());
        mAdapter.notifyDataSetChanged();
    }

    private void onAddAccount() {

    }

    private void saveChangesToDb(final AccountDb.AccountItem item) {
        mDb.update(item.id, item.desc, item.userName, item.password);
        mAdapter.notifyDataSetChanged();
    }
}
