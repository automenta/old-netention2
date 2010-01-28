package automenta.netention.swingui.detail;

import automenta.netention.api.Detail;
import automenta.netention.api.Pattern;
import automenta.netention.api.Schema;

import automenta.netention.gwtdepr.data.DetailData;
import automenta.netention.gwtdepr.data.PatternData;
import automenta.netention.gwtdepr.data.PatternDataEx;
import automenta.netention.api.value.Property;
import java.awt.MenuBar;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

abstract public class DetailEditMenu extends JMenuBar {

    private MenuBar patternMenu;
    private List<String> inheritedPatterns;
    private final Schema schema;
    private final Detail detail;

    abstract public class PropertySubMenu extends JMenu {

        public PropertySubMenu(final Pattern pattern) {
            super(pattern.getName());

            int itemsAdded = 0;

            for (String ext : pattern.getExtends()) {
                Pattern extPattern = schema.getPattern(ext);

                JMenu subMenu = new PropertySubMenu(extPattern) {

                    @Override protected void onPatternRemoved(PatternDataEx patternData) {
                        PropertySubMenu.this.onPatternRemoved(patternData);
                    }

                    @Override protected void onPropertyAdded(Property propData) {
                        PropertySubMenu.this.onPropertyAdded(propData);
                    }
                };


                add(subMenu);
                itemsAdded++;
            }

            if (itemsAdded > 0) {
                addSeparator();
            }

            itemsAdded = 0;
            for (String dp : pattern.getDefinedProperties()) {
                if (!canSupportAnotherProperty(dp, pattern, detail)) {
                    continue;
                }

                final Property propData = schema.getProperty(dp);
                String propName = propData.getName();

                add(new JMenuItem(propName));
//				add(propName, new Command() {
//					@Override public void execute() {
//						onPropertyAdded(propData);
//					}
//				});

                itemsAdded++;
            }

            //menu items for removing defined types
            if (detail.getPatterns().contains(pattern.getID())) {
                if (itemsAdded > 0) {
                    addSeparator();
                }

                add(new JMenuItem("Remove " + pattern.getName()));
//				addItem("Remove " + patternData.getName(), new Command() {
//					@Override public void execute() {
//						onPatternRemoved(patternData);
//					}
//				});
            }


        }

        abstract protected void onPatternRemoved(PatternDataEx patternData);

        abstract protected void onPropertyAdded(Property propData);
    }

    public DetailEditMenu(Schema s, Detail d) {
        super();
        this.schema = s;
        this.detail = d;
        refresh();
    }

    protected void refresh() {
        removeAll();

        PatternMenu patternMenu = new PatternMenu(schema, detail);
        add(patternMenu);

        for (String pattern : detail.getPatterns()) {

            final Pattern patternData = schema.getPattern(pattern);

            final JMenu propertyMenu = new PropertySubMenu(patternData) {

                @Override protected void onPropertyAdded(Property propData) {
                    addProperty(propData);
                }

                @Override protected void onPatternRemoved(PatternDataEx patternData) {
                    removePattern(patternData);
                }
            };

            add(propertyMenu);
            
            //JMenuItem m = new JMenuItem(patternData.getName(), propertyMenu);

        }
        
        updateUI();

        //patternMenu = new MenuBar(true);
        //addItem("Is a...", patternMenu).addStyleName("PropertiesMenuItem");

        //addSeparator();


//		netService.getInheritedPatterns(node.getPatterns(), new AsyncCallback<List<String>>() {
//			@Override public void onSuccess(final List<String> inheritedPatterns) {
//				DetailEditMenu.this.inheritedPatterns = inheritedPatterns;
//
//				netService.getPatternDataEx(inheritedPatterns.toArray(new String[0]), new AsyncCallback<Map<String,PatternDataEx>>() {
//
//					@Override public void onSuccess(Map<String,PatternDataEx> patterns) {
//
//						for (String pattern : node.getPatterns()) {
//
//							final PatternDataEx patternData = patterns.get(pattern);
//
//							final JMenu propertyMenu = new PropertySubMenu(node, patternData, patterns) {
//								@Override protected void onPropertyAdded(Property propData) {
//									addProperty(propData);
//								}
//								@Override protected void onPatternRemoved(PatternDataEx patternData) {
//									removePattern(patternData);
//								}
//							};
//
//
//							JMenuItem m = add(new JMenuItem(patternData.getName(), propertyMenu));
//							m.addStyleName("PropertiesMenuItem");
//
//						}
//
//
//					}
//
//					@Override public void onFailure(Throwable caught) {		Window.alert(caught.toString());		}
//				});
//
//				netService.getPatterns(null, new AsyncCallback<List<PatternData>>() {
//
//					@Override public void onSuccess(List<PatternData> result) {
//						for (final PatternData p : result) {
//							if (!hasInheritedPattern(node, p.getID())) {
//								patternMenu.addItem(p.getName(), new Command() {
//									@Override public void execute() {
//										addPattern(p);
//									}
//								});
//							}
//						}
//					}
//
//					@Override public void onFailure(Throwable caught) {		}
//				});
//
//			}
//			@Override public void onFailure(Throwable caught) {		Window.alert(caught.toString());		}
//		});


    }

    public boolean canSupportAnotherProperty(String property, Pattern patternData, Detail node) {
        Property dpd = schema.getProperty(property);

        int maxCardinality = dpd.getCardinalityMax();
        if (maxCardinality == -1) {
            return true;
        }

        int numberDefined = node.getNumPropertiesDefined(property);

        if (numberDefined < maxCardinality) {
            return true;
        }
        return false;
    }

    protected void addPattern(PatternData p) {
        detail.getPatterns().add(p.getID());
        refresh();
    }

    protected void removePattern(PatternData p) {
        detail.getPatterns().remove(p.getID());
        //refresh();
        refreshProperties();
    }

    private void addProperty(Property prop) {
        synchronized (detail.getProperties()) {
            detail.getProperties().add(prop.newDefaultValue());
        }
        refreshProperties();
    }

    /** causes a refresh() indirectly */
    abstract protected void refreshProperties();

    protected static boolean hasDefinedPattern(DetailData pd, String p) {
        for (String s : pd.getPatterns()) {
            if (s.equals(p)) {
                return true;
            }
        }
        return false;
    }

    protected boolean hasInheritedPattern(DetailData pd, String p) {
        return inheritedPatterns.contains(p);
    }
}
