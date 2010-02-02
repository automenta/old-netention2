package automenta.netention.gwtdepr.ui.detail;

import java.util.List;
import java.util.Map;

import automenta.netention.gwtdepr.NetworkService;
import automenta.netention.gwtdepr.NetworkServiceAsync;
import automenta.netention.gwtdepr.data.DetailData;
import automenta.netention.gwtdepr.data.PatternData;
import automenta.netention.gwtdepr.data.PatternDataEx;
import automenta.netention.value.Property;
import automenta.netention.value.PropertyValue;
import automenta.netention.value.Value;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.MenuBar;
import com.google.gwt.user.client.ui.MenuItem;

abstract public class NodeEditMenu extends MenuBar {

	final NetworkServiceAsync netService = GWT.create(NetworkService.class);
	private MenuBar patternMenu;
	private DetailData node;
	private List<String> inheritedPatterns;

	abstract public static class PropertySubMenu extends MenuBar {
	
		public PropertySubMenu(DetailData node, final PatternDataEx patternData, Map<String, PatternDataEx> patterns) {
			super(true);
			
			int itemsAdded = 0;

			for (String ext : patternData.getExtends()) {
				PatternDataEx extPattern = patterns.get(ext);
				MenuBar subMenu = new PropertySubMenu(node, extPattern, patterns) {
					@Override protected void onPatternRemoved(PatternDataEx patternData) {
						PropertySubMenu.this.onPatternRemoved(patternData);
					}
					@Override protected void onPropertyAdded(Property propData) {
						PropertySubMenu.this.onPropertyAdded(propData);						
					}					
				};
				
				addItem(extPattern.getName(), subMenu);
				itemsAdded++;
			}

			if (itemsAdded > 0)
				addSeparator();

			itemsAdded = 0;
			for (String dp : patternData.getDefProperties()) {
				if (!canSupportAnotherProperty(dp, patternData, node))
					continue;

				final Property propData = patternData.getDefinedPropertyData().get(dp); 
				String propName = propData.getName();

				addItem(propName, new Command() {
					@Override public void execute() {
						onPropertyAdded(propData);
					}

				});
				itemsAdded++;
			}

			//menu items for removing defined types
			if (node.getPatterns().contains(patternData.getID())) {
				if (itemsAdded > 0) {
					addSeparator();
				}

				addItem("Remove " + patternData.getName(), new Command() {
					@Override public void execute() {
						onPatternRemoved(patternData);
					}
				});
			}


		}

		abstract protected void onPatternRemoved(PatternDataEx patternData);
		abstract protected void onPropertyAdded(Property propData);
		
		
	}
	
	public NodeEditMenu() {
		super();

		addStyleName("PropertiesMenu");
	}

	public static boolean canSupportAnotherProperty(String property, PatternDataEx patternData, DetailData node) {
		Property dpd = patternData.getDefinedPropertyData().get(property);
		int maxCardinality = dpd.getCardinalityMax();
		if (maxCardinality == -1)
			return true;

		int numberDefined = node.getNumPropertiesDefined(property);
		
		if (numberDefined < maxCardinality)
			return true;
		return false;
	}

	public void refresh() {
		updateMenu(getNode());
	}

	public void updateMenu(final DetailData node) {
		clearItems();

		this.node = node;

		patternMenu = new MenuBar(true);
		addItem("Is a...", patternMenu).addStyleName("PropertiesMenuItem");

		addSeparator();

		netService.getInheritedPatterns(node.getPatterns(), new AsyncCallback<List<String>>() {
			@Override public void onSuccess(final List<String> inheritedPatterns) {
				NodeEditMenu.this.inheritedPatterns = inheritedPatterns;

				netService.getPatternDataEx(inheritedPatterns.toArray(new String[0]), new AsyncCallback<Map<String,PatternDataEx>>() {

					@Override public void onSuccess(Map<String,PatternDataEx> patterns) {
						
						for (String pattern : node.getPatterns()) {
							
							final PatternDataEx patternData = patterns.get(pattern);

							final MenuBar propertyMenu = new PropertySubMenu(node, patternData, patterns) {
								@Override protected void onPropertyAdded(Property propData) {
									addProperty(propData);									
								}
								@Override protected void onPatternRemoved(PatternDataEx patternData) {
									removePattern(patternData);									
								}																
							};


							MenuItem m = addItem(new MenuItem(patternData.getName(), propertyMenu));
							m.addStyleName("PropertiesMenuItem");

						}


					}

					@Override public void onFailure(Throwable caught) {		Window.alert(caught.toString());		}			
				});

				netService.getPatterns(null, new AsyncCallback<List<PatternData>>() {

					@Override public void onSuccess(List<PatternData> result) {
						for (final PatternData p : result) {
							if (!hasInheritedPattern(node, p.getID())) {
								patternMenu.addItem(p.getName(), new Command() {
									@Override public void execute() {
										addPattern(p);
									}
								});
							}
						}
					}

					@Override public void onFailure(Throwable caught) {		}			
				});

			}
			@Override public void onFailure(Throwable caught) {		Window.alert(caught.toString());		}			
		});
		




	}




	protected void addPattern(PatternData p) {
		getNode().getPatterns().add(p.getID());
		refresh();
	}						

	protected void removePattern(PatternData p) {
		getNode().getPatterns().remove(p.getID());
		//refresh();
		refreshProperties();
	}						

	private void addProperty(Property prop) {
		synchronized (getNode().getProperties()) {
			getNode().getProperties().add(prop.newDefaultValue());
		}
		refreshProperties();	
	}							

	/** causes a refresh() indirectly */
	abstract protected void refreshProperties();

	public DetailData getNode() {
		return node;
	}

	protected static boolean hasDefinedPattern(DetailData pd, String p) {
		for (String s : pd.getPatterns()) {
			if (s.equals(p))
				return true;
		}
		return false;
	}
	protected boolean hasInheritedPattern(DetailData pd, String p) {
		return inheritedPatterns.contains(p);
	}

}
