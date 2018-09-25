package com.job.darasalecturer.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.webkit.WebView;
import android.widget.ProgressBar;

import com.job.darasalecturer.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FaqActivity extends AppCompatActivity {

    @BindView(R.id.faq_toolbar)
    Toolbar faqToolbar;
    @BindView(R.id.faq_progressBar)
    ProgressBar faqProgressBar;
    @BindView(R.id.faq_webView)
    WebView faqWebView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_faq);
        ButterKnife.bind(this);
    }
}
