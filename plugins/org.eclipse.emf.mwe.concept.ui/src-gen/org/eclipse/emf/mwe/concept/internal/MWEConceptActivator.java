
/*
 * generated by Xtext
 */
package org.eclipse.emf.mwe.concept.internal;

import org.apache.log4j.Logger;
import org.eclipse.xtext.ui.common.service.UIPluginModule;

import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

import com.google.inject.Guice;
import com.google.inject.Injector;

import java.util.Map;
import java.util.HashMap;

/**
 * Generated
 */
public class MWEConceptActivator extends AbstractUIPlugin {

	private Map<String,Injector> injectors = new HashMap<String,Injector>();
	private static MWEConceptActivator INSTANCE;

	public Injector getInjector(String languageName) {
		return injectors.get(languageName);
	}
	
	@Override
	public void start(BundleContext context) throws Exception {
		super.start(context);
		INSTANCE = this;
		try {
			
			injectors.put("org.eclipse.emf.mwe.concept.MWEConcept", Guice.createInjector(
				new org.eclipse.emf.mwe.concept.MWEConceptUiModule(),
				createUIPluginModule()
			));
			
		} catch (Exception e) {
			Logger.getLogger(getClass()).error(e.getMessage(), e);
			throw e;
		}
	}
	
	public static MWEConceptActivator getInstance() {
		return INSTANCE;
	}
	
	protected UIPluginModule createUIPluginModule() {
		return new UIPluginModule(this);
	}
	
}