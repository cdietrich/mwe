/*
Generated with Xtext
*/
package org.eclipse.emf.mwe.di;

import org.eclipse.xtext.ui.common.service.DefaultUIModule;

/**
 * GENERATED! Manual modification goes to MWEUIModule
 */
public abstract class AbstractMWEUiModule extends DefaultUIModule {
	
	
	public Class<? extends org.eclipse.xtext.ui.common.editor.contentassist.IProposalProvider> bindIProposalProvider() {
		return org.eclipse.emf.mwe.di.GenMWEProposalProvider.class;
	}
	
	
	
}