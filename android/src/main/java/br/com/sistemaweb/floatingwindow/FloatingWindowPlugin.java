package br.com.heber.coords;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.Toast;

import com.getcapacitor.JSObject;
import com.getcapacitor.Plugin;
import com.getcapacitor.PluginCall;
import com.getcapacitor.PluginMethod;
import com.getcapacitor.annotation.CapacitorPlugin;
import com.getcapacitor.NativePlugin;

@NativePlugin(name = "FloatingWindow")
public class FloatingWindowPlugin extends Plugin {

    private WindowManager windowManager;
    private View floatingView;
    private WebView webView;

    private static final int OVERLAY_PERMISSION_REQ_CODE = 1000;

    @Override
    public void load() {
        super.load();
        windowManager = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
    }

    @PluginMethod
    public void showFloatingWindow(PluginCall call) {
        saveCall(call);
        if (floatingView == null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !Settings.canDrawOverlays(getContext())) {
                Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:" + getContext().getPackageName()));
                startActivityForResult(call, intent, OVERLAY_PERMISSION_REQ_CODE);
            } else {
                createFloatingWindow(call);
            }
        } else {
            call.resolve();
        }
    }

    private void createFloatingWindow(PluginCall call) {
        getActivity().runOnUiThread(() -> {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            floatingView = inflater.inflate(R.layout.floating_window_layout, null);

            webView = floatingView.findViewById(R.id.webView);
            webView.setWebViewClient(new WebViewClient());
            webView.setWebChromeClient(new WebChromeClient());
            WebSettings webSettings = webView.getSettings();
            webSettings.setJavaScriptEnabled(true);

            final  String url = call.getString("url", "");
            webView.loadUrl(url);

            WindowManager.LayoutParams params = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                Build.VERSION.SDK_INT >= Build.VERSION_CODES.O ? WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY : WindowManager.LayoutParams.TYPE_PHONE,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT
            );

            params.gravity = Gravity.TOP | Gravity.START;
            params.x = 0;
            params.y = 100;

            windowManager.addView(floatingView, params);

            Button closeButton = floatingView.findViewById(R.id.closeButton);
            closeButton.setOnClickListener(v -> closeFloatingWindow(call));

            floatingView.setOnTouchListener(new View.OnTouchListener() {
                private int initialX;
                private int initialY;
                private float initialTouchX;
                private float initialTouchY;

                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    switch (event.getAction()) {
                        case MotionEvent.ACTION_DOWN:
                            initialX = params.x;
                            initialY = params.y;
                            initialTouchX = event.getRawX();
                            initialTouchY = event.getRawY();
                            return true;
                        case MotionEvent.ACTION_MOVE:
                            params.x = initialX + (int) (event.getRawX() - initialTouchX);
                            params.y = initialY + (int) (event.getRawY() - initialTouchY);
                            windowManager.updateViewLayout(floatingView, params);
                            return true;
                    }
                    return false;
                }
            });
        });
        call.resolve();
    }

    @PluginMethod
    public void closeFloatingWindow(PluginCall call) {
        getActivity().runOnUiThread(() -> {
            if (floatingView != null) {
                windowManager.removeView(floatingView);
                floatingView = null;
            }
        });
        call.resolve();
    }

    @Override
    protected void handleOnActivityResult(int requestCode, int resultCode, Intent data) {
        super.handleOnActivityResult(requestCode, resultCode, data);
        PluginCall call = getSavedCall();
        if (call == null) return;

        if (requestCode == OVERLAY_PERMISSION_REQ_CODE) {
            if (Settings.canDrawOverlays(getContext())) {
                createFloatingWindow(call);
            } else {
                call.reject("Overlay permission is required.");
            }
        }
    }
}
