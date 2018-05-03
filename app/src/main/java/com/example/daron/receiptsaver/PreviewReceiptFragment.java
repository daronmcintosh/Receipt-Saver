package com.example.daron.receiptsaver;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.ShareActionProvider;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.dropbox.core.DbxException;
import com.dropbox.core.v2.DbxClientV2;
import com.dropbox.core.v2.files.FileMetadata;
import com.dropbox.core.v2.files.Metadata;
import com.example.daron.receiptsaver.dropbox.DropboxClientFactory;
import com.example.daron.receiptsaver.dropbox.FileThumbnailRequestHandler;
import com.example.daron.receiptsaver.dropbox.PicassoClient;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;

public class PreviewReceiptFragment extends Fragment {
    private final String LOG_TAG = PreviewReceiptFragment.class.getSimpleName();

    private ReceiptDataSource receiptDataSource;
    private Receipt receipt;
    private ImageView receiptImage;
    private Context context;
    private TextView total;
    private ShareActionProvider shareActionProvider;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_preview_receipt, container, false);
        PreferenceManager.setDefaultValues(context, R.xml.preferences, false);
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        String currency = sharedPreferences.getString(SettingsFragment.CURRENCY_KEY, "Dollar");


        Bundle bundle;
        bundle = getArguments();
        int id = (int) bundle.getLong("id");

        DbxClientV2 client = DropboxClientFactory.getClient();
        PicassoClient.init(getContext(), client);

        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();

        receiptDataSource = new ReceiptDataSource(view.getContext());
        receiptDataSource.open();
        receipt = receiptDataSource.getReceipt(id);
        receiptDataSource.close();

        total = (TextView) view.findViewById(R.id.total);
        TextView category = (TextView) view.findViewById(R.id.category);
        TextView date = (TextView) view.findViewById(R.id.date);
        TextView description = (TextView) view.findViewById(R.id.description);
        receiptImage = (ImageView) view.findViewById(R.id.receiptImage);

        setApplicationCurrency(currency);

        category.setText(receipt.getCategory());
        date.setText(receipt.getDate());
        description.setText(receipt.getDescription());

        new FetchMetadata().execute(receipt.getFilename());

        actionBar.setTitle(receipt.getName());
        setHasOptionsMenu(true);

        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        MenuItem shareItem = menu.findItem(R.id.action_share);
        shareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(shareItem);
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT, "Hey I bought " + receipt.getName() + " for " + receipt.getTotal());
        shareActionProvider.setShareIntent(intent);
        if (this.isVisible()) {
            menu.findItem(R.id.action_delete).setVisible(true);
            menu.findItem(R.id.action_share).setVisible(true);
            menu.findItem(R.id.action_add).setVisible(false);
        }
    }

    public void setApplicationCurrency(String currency) {
        DecimalFormat df = new DecimalFormat("#.00");
        if (currency.equals("Dollar")) {
            total.setText(getString(R.string.dollar) + df.format(receipt.getTotal()));
        } else if (currency.equals("Euro")) {
            total.setText(getString(R.string.euro) + df.format(receipt.getTotal()));
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent;
        switch (item.getItemId()) {
            case R.id.action_settings:
                intent = new Intent(getActivity(), SettingsActivity.class);
                startActivity(intent);
                return true;
            case R.id.action_add:
                intent = new Intent(getActivity(), AddReceiptActivity.class);
                startActivity(intent);
                return true;
            case R.id.action_delete:
                new DeletePhoto().execute(receipt.getFilename());
                receiptDataSource.open();
                receiptDataSource.deleteReceipt(receipt);
                receiptDataSource.close();
                intent = new Intent(getActivity(), HomeActivity.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    // Fetch FileMetadata
    private class FetchMetadata extends AsyncTask<String, Void, FileMetadata> {

        private DbxClientV2 dbxClient;
        private FileMetadata metadata;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            this.dbxClient = DropboxClientFactory.getClient();
        }

        @Override
        protected FileMetadata doInBackground(String... filename) {
            try {
                metadata = (FileMetadata) dbxClient.files().getMetadata("/" + filename[0]);
                Log.i(LOG_TAG, "Path of file is " + metadata.getPathLower());
            } catch (DbxException e) {
                e.printStackTrace();
            }
            return metadata;
        }

        @Override
        protected void onPostExecute(FileMetadata fileMetadata) {
            super.onPostExecute(fileMetadata);
            if (fileMetadata != null) {
                Picasso picasso = PicassoClient.getPicasso();
                picasso.load(FileThumbnailRequestHandler.buildPicassoUri(fileMetadata))
                        .into(receiptImage);
            }
        }
    }

    // Delete file from dropbox
    private class DeletePhoto extends AsyncTask<String, Void, Boolean> {

        private DbxClientV2 dbxClient;
        private Metadata metadata;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            this.dbxClient = DropboxClientFactory.getClient();
        }

        @Override
        protected Boolean doInBackground(String... filename) {
            String path = "/" + filename[0];
            try {
                metadata = dbxClient.files().deleteV2(path).getMetadata();
            } catch (DbxException e) {
                e.printStackTrace();
            }
            Boolean deleted = path.equals(metadata.getPathLower());

            if (deleted)
                Log.i(LOG_TAG, "The file " + metadata.getName() + " was deleted");

            return deleted;
        }
    }
}
