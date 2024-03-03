package br.com.sistemaweb.floatingwindow;

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
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;

public class FloatingWindow {

    private WindowManager windowManager;
    private View floatingView;
    private Button closeButton;
    private WebView webView;

    private static final int OVERLAY_PERMISSION_REQ_CODE = 1000;

    public void showFloatingWindow(Context context, String url) {
        windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        if (floatingView == null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !Settings.canDrawOverlays(context)) {
                Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                        Uri.parse("package:" + context.getPackageName()));
                if (context instanceof Activity) {
                    ((Activity) context).startActivityForResult(intent, OVERLAY_PERMISSION_REQ_CODE);
                }
            }

            LayoutInflater inflater = LayoutInflater.from(context);
            floatingView = inflater.inflate(R.layout.floating_window_layout, null);

            webView = floatingView.findViewById(R.id.webView);
            webView.setWebViewClient(new WebViewClient());
            webView.setWebChromeClient(new WebChromeClient()); // Adicionando o WebChromeClient
            webView.getSettings().setJavaScriptEnabled(true);
            webView.loadUrl(url);

            WindowManager.LayoutParams params = new WindowManager.LayoutParams(
                    WindowManager.LayoutParams.WRAP_CONTENT,
                    WindowManager.LayoutParams.WRAP_CONTENT,
                    Build.VERSION.SDK_INT >= Build.VERSION_CODES.O ?
                            WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY :
                            WindowManager.LayoutParams.TYPE_TOAST,
                    WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                    PixelFormat.TRANSLUCENT
            );
            params.gravity = Gravity.CENTER;
            windowManager.addView(floatingView, params);

            closeButton = floatingView.findViewById(R.id.closeButton);
            closeButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    closeFloatingWindow();
                }
            });

            Button moveButton = floatingView.findViewById(R.id.moveButton);
            moveButton.setOnTouchListener(new View.OnTouchListener() {
                private int initialX;
                private int initialY;
                private float initialTouchX;
                private float initialTouchY;
                private boolean isMoving = false;

                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    WindowManager.LayoutParams params = (WindowManager.LayoutParams) floatingView.getLayoutParams();
                    switch (event.getAction()) {
                        case MotionEvent.ACTION_DOWN:
                            initialX = params.x;
                            initialY = params.y;
                            initialTouchX = event.getRawX();
                            initialTouchY = event.getRawY();
                            isMoving = true;
                            return true;
                        case MotionEvent.ACTION_MOVE:
                            if (isMoving) {
                                params.x = initialX + (int) (event.getRawX() - initialTouchX);
                                params.y = initialY + (int) (event.getRawY() - initialTouchY);
                                windowManager.updateViewLayout(floatingView, params);
                            }
                            return true;
                        case MotionEvent.ACTION_UP:
                            isMoving = false;
                            return true;
                    }
                    return false;
                }
            });
        }
    }

    public void closeFloatingWindow() {
        if (floatingView != null) {
            windowManager.removeView(floatingView);
            floatingView = null;
        }
    }
}
