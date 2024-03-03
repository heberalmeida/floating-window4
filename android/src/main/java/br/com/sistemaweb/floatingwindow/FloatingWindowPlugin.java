package br.com.sistemaweb.floatingwindow;

import com.getcapacitor.JSObject;
import com.getcapacitor.Plugin;
import com.getcapacitor.PluginCall;
import com.getcapacitor.PluginMethod;
import com.getcapacitor.annotation.CapacitorPlugin;
import com.getcapacitor.NativePlugin;

@NativePlugin(name = "FloatingWindow")
public class FloatingWindowPlugin extends Plugin {

    private FloatingWindow implementation = new FloatingWindow();

    @PluginMethod
    public void showFloatingWindow(PluginCall call) {
        String url = call.getString("url", "");
        implementation.showFloatingWindow(getContext(), url);
        call.resolve();
    }

    @PluginMethod
    public void closeFloatingWindow(PluginCall call) {
        implementation.closeFloatingWindow();
        call.resolve();
    }
}