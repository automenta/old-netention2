package automenta.netention.webui;

import automenta.netention.example.ExampleNetwork;
import nextapp.echo.app.ApplicationInstance;
import nextapp.echo.webcontainer.WebContainerServlet;

public class MainServlet extends WebContainerServlet {

    public ApplicationInstance newApplicationInstance() {
        return new MainApp(new ExampleNetwork());
    }
}
