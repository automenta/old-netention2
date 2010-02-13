package automenta.netention.swingui;

import automenta.netention.run.RunEMailPanel;
import automenta.netention.Action;
import automenta.netention.Self;
import automenta.netention.io.Async;
import automenta.netention.io.HTML;
import automenta.netention.io.Twitter;
import automenta.netention.node.Detail;
import automenta.netention.swingui.html.HTMLCrawlPanel;
import automenta.netention.swingui.util.SwingWindow;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

public abstract class AddMenu extends JMenu {

    public AddMenu(final Self self) {
        super("Add");
        JMenuItem newDetail = new JMenuItem("Detail...");
        newDetail.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                Detail d = self.newDetail(self.getAgent(), "Unnamed");
                refresh();
            }
        });
        JMenuItem ambientMessages = new JMenuItem("Ambient Messages...");
        ambientMessages.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                self.run(new Action("Loading Twitter Timeline") {

                    public Object call() throws Exception {
                        Twitter t = self.getThe(Twitter.class);
                        if (t != null) {
                            t.getPublicTimeline(self.getMemory());
                        }
                        return t;
                    }
                }, new Async() {

                    public void onFinished(Object result) {
                        refresh();
                    }

                    public void onError(Exception ex) {
                        Logger.getLogger(EQListPanel.class.getName()).log(Level.SEVERE, null, ex);
                    }
                });
            }
        });
        add(newDetail);
        addSeparator();
        add(ambientMessages);
        JMenuItem website = new JMenuItem("Website...");
        website.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                final String website = JOptionPane.showInputDialog("Web URL", "http://transalchemy.blogspot.com");
                if (website != null) {
                    self.run(new Action("Loading " + website) {

                        public Object call() throws Exception {
                            return new HTML(self, website);
                        }
                    }, new Async() {

                        public void onFinished(Object result) {
                            refresh();
                        }

                        public void onError(Exception ex) {
                            Logger.getLogger(EQListPanel.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    });
                }
            }
        });
        add(website);
        JMenuItem websiteCrawl = new JMenuItem("Website Crawl...");
        websiteCrawl.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                new SwingWindow(new HTMLCrawlPanel(self) {

                    @Override
                    protected void finished() {
                        getParent().setVisible(false);
                    }
                }, 400, 400, false);
            }
        });
        add(websiteCrawl);
        JMenuItem botmail = new JMenuItem("BotMail...");
        botmail.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                try {
                    new RunEMailPanel();
                } catch (Exception ex) {
                    Logger.getLogger(EQListPanel.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
        add(botmail);
        addSeparator();
    }

    protected abstract void refresh();
}
