package com.example.test_webview;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.net.http.SslError;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.JsResult;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        String url = "http://beta.html5test.com/";
        String url = "http://192.168.31.131:8080/test.html";
        final WebView webView =  findViewById(R.id.web_view);

        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setAllowContentAccess(true);
        webSettings.setAppCacheEnabled(true);
        webSettings.setDomStorageEnabled(true);
        webSettings.setUseWideViewPort(true);

        webView.loadUrl(url);


        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                view.loadUrl(request.toString());
                return true;
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);

                String url_new = view.getUrl();

                Log.v("onPageStarted","url: "+url_new);

//                addressbar.setText(url_new);
            }
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                Log.v("onPageFinished","url: "+url);

            }
            @Override
            public void onLoadResource(WebView view, String url) {
                super.onLoadResource(view, url);
                Log.v("onLoadResource","url: "+url);
            }
            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                Log.i("onReceivedError", "error code:"  + error);
                super.onReceivedError(view, request, error);
            }

            @Override
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError er) {
                handler.proceed();
            }

            @Override
            public WebResourceResponse shouldInterceptRequest (WebView view, WebResourceRequest request) {
                Log.i("shouldInterceptRequest", "error code:"  + request.toString());

                return super.shouldInterceptRequest(view, request);

//                if (url.contains(".css")) {
//                    return getCssWebResourceResponseFromAsset();
//                } else {
//                    return super.shouldInterceptRequest(view, url);
//                }
            }
        });

        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);

                Log.v("onProgressChanged","Progress: "+newProgress);
            };

            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
                Log.d("onReceivedTitle", "TITLE=" + title);
//                txtTitle.setText("ReceivedTitle:" +title);
            }

            public boolean onJsAlert(WebView view, String url, String message, final JsResult result) {
                Log.i("MainActivity", "onJsAlert url=" + url + ";message=" + message);
                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
                result.confirm();
                return true;
            }
        });

        Button button = findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), "系好安全带!", Toast.LENGTH_SHORT).show();

                String call = "javascript:alertMessage(\"" + "content" + "\")";
                webView.loadUrl(call);
            }
        });

        webView.addJavascriptInterface(this, "android");



    }

    @JavascriptInterface
    public void show(String s){
        Toast.makeText(getApplication(), s, Toast.LENGTH_SHORT).show();
    }

    @JavascriptInterface
    public String getMessage(){
        return "Hello,boy~";
    }
}
