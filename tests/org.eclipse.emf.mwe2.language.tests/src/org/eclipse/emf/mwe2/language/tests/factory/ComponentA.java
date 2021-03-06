/*******************************************************************************
 * Copyright (c) 2008,2010 itemis AG (http://www.itemis.eu) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 *******************************************************************************/
package org.eclipse.emf.mwe2.language.tests.factory;

import java.util.List;

import com.google.common.collect.Lists;

public class ComponentA {
	private ComponentA x;
	private List<String> y = Lists.newArrayList();
	private String z;
	
	public ComponentA getX() {
		return x;
	}
	public void setX(ComponentA x) {
		this.x = x;
	}
	
	public List<String> getY() {
		return y;
	}
	
	public void addY(String s) {
		this.y.add(s);
	}
	
	public void setZ(String z) {
		this.z = z;
	}
	
	public String getZ() {
		return z;
	}
	
}
