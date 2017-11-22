package smimram.nlab;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebSettings;
import android.webkit.WebViewClient;
import android.widget.SearchView;

public class nLab extends AppCompatActivity {
    public static final String ACTION_LOAD_URL = "nlab.load_url";

    private class MyWebviewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
            String url = request.getUrl().toString();

            if (request.isRedirect())
                // By default redirect launches a browser
                view.loadUrl(url);
            else {
                Intent intent = new Intent(nLab.this, nLab.class);
                intent.setAction(ACTION_LOAD_URL);
                intent.putExtra("url", url);
                startActivity(intent);
                //TODO: new activity when users clicks on a link
                //super.shouldOverrideUrlLoading(view, request);
                //view.loadUrl(request.getUrl().toString());
            }
            return true;
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            // Adapt the webpage
            //view.loadUrl("javascript:document.getElementById(\"pageName\").innerHTML = \"\";void(0);");
            view.loadUrl("javascript:document.getElementsByClassName(\"rightHandSide\")[0].innerHTML = \"\";void(0);");
            view.loadUrl("javascript:document.getElementsByClassName(\"navigation\")[0].innerHTML = \"\";void(0);");
            view.loadUrl("javascript:document.getElementsByClassName(\"navigation\")[1].innerHTML = \"\";void(0);");
            view.loadUrl("javascript:document.getElementById(\"contents\").innerHTML = \"\";void(0);");
            view.loadUrl("javascript:document.getElementsByClassName(\"maruku_toc\")[0].innerHTML = \"\";void(0);");
            view.loadUrl("javascript:document.getElementById(\"footer\").innerHTML = \"\";void(0);");
        }
    }

    private void loadUrl(String url) {
        WebView myWebView = (WebView) findViewById(R.id.webview);
        myWebView.loadUrl(url);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_n_lab);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Show back button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        WebView myWebView = (WebView) findViewById(R.id.webview);
        WebSettings webSettings = myWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        myWebView.setWebViewClient(new MyWebviewClient());
        //myWebView.loadUrl("https://ncatlab.org/");
        loadUrl("https://ncatlab.org/nlab/show/bicategory");

        handleIntent(getIntent());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_n_lab, menu);
        // Associate searchable configuration with the SearchView
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.search).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        handleIntent(intent);
    }

    private void handleIntent(Intent intent) {
        String action = intent.getAction();
        if (Intent.ACTION_SEARCH.equals(action)) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            Log.d("search", query);
            WebView myWebView = (WebView) findViewById(R.id.webview);
            myWebView.loadUrl("https://ncatlab.org/nlab/search?query="+query);
        }
        else if(ACTION_LOAD_URL.equals(action)) {
            String url = intent.getStringExtra("url");
            loadUrl(url);
        }
    }
}
